#include <iostream>
#include <vector>
#include <stack>
#include <map>
#include <unordered_set>
#include <algorithm>
#include <limits>

using namespace std;

vector<char> alphabet;

int terminalCount = 0;

vector<char> terminals;

vector<vector<int>> followpos;

map<char, vector<int>> stateMap;

map<char, vector<pair<char, char>>> transitionMap;

vector<char> states;

vector<char> finalStates;

struct TreeNode
{
    char value;
    int p;
    bool nullable;
    vector<int> firstpos;
    vector<int> lastpos;
    TreeNode *left;
    TreeNode *right;

    TreeNode(char val) : value(val), p(-1), left(nullptr), right(nullptr), nullable(false)
    {
        if (val != '|' && val != '.' && val != '*' && val != 'E')
        {
            p = terminalCount--;
        }
        else if (val == 'E' || val == '*')
        {
            nullable = true;
        }
    }
};

string convertToModifiedRegex(string &regex)
{
    string result;
    stack<char> operators;

    string temp = "";
    string temp1 = "";
    for (char c : regex)
    {
        switch (c)
        {
        case '[':
            operators.push(c);
            temp1 = temp1 + "(";
            break;

        case ']':
            operators.pop();
            temp1 = temp1 + ")";
            temp = temp + temp1;
            temp1 = "";
            break;

        case '-':
            if (!operators.empty())
            {
                operators.push(c);
            }
            else
            {
                temp = temp + string(1, c);
            }
            break;

        default:
            if (!operators.empty() && operators.top() == '[')
            {
                operators.push(c);
                if (temp1 != "(")
                {
                    temp1 = temp1 + "|" + string(1, c);
                }
                else
                {
                    temp1 = temp1 + string(1, c);
                }
            }
            else if (!operators.empty() && operators.top() == '-')
            {
                operators.pop();
                char t = operators.top();
                for (char i = t + 1; i <= c; i++)
                {
                    temp1 = temp1 + "|" + string(1, i);
                }
                operators.pop();
            }
            else
            {
                temp = temp + string(1, c);
            }
            break;
        }
    }

    regex = temp;
    regex = "(" + regex + ").$";

    for (char c : regex)
    {
        switch (c)
        {
        case '(':
            operators.push(c);
            break;

        case ')':
            while (!operators.empty() && operators.top() != '(')
            {
                result += operators.top();
                operators.pop();
            }
            operators.pop(); // Pop '('
            break;

        case '*':
            result += c;
            break;

        case '.':
            while (!operators.empty() && operators.top() == '*')
            {
                result += operators.top();
                operators.pop();
            }
            operators.push(c);
            break;

        case '|':
            while (!operators.empty() && (operators.top() == '*' || operators.top() == '.'))
            {
                result += operators.top();
                operators.pop();
            }
            operators.push(c);
            break;

        default:
            if (c != 'E')
            {
                terminalCount++;
            }
            result += c;
            break;
        }
    }

    while (!operators.empty())
    {
        result += operators.top();
        operators.pop();
    }

    reverse(result.begin(), result.end());

    return result;
}

TreeNode *createSyntacticTree(const string &modifiedRegex, size_t &index)
{
    if (index >= modifiedRegex.size())
    {
        return nullptr;
    }

    char c = modifiedRegex[index++];
    TreeNode *newNode = new TreeNode(c);

    if (c != '|' && c != '*' && c != '.' && c != 'E')
    {
        newNode->firstpos.push_back(newNode->p);
        newNode->lastpos.push_back(newNode->p);
    }
    else if (c == '|')
    {
        newNode->right = createSyntacticTree(modifiedRegex, index);
        newNode->left = createSyntacticTree(modifiedRegex, index);
        newNode->nullable = (newNode->left->nullable || newNode->right->nullable);
        newNode->firstpos.insert(newNode->firstpos.end(), newNode->left->firstpos.begin(), newNode->left->firstpos.end());
        newNode->firstpos.insert(newNode->firstpos.end(), newNode->right->firstpos.begin(), newNode->right->firstpos.end());
        newNode->lastpos.insert(newNode->lastpos.end(), newNode->left->lastpos.begin(), newNode->left->lastpos.end());
        newNode->lastpos.insert(newNode->lastpos.end(), newNode->right->lastpos.begin(), newNode->right->lastpos.end());
    }
    else if (c == '*')
    {
        newNode->left = createSyntacticTree(modifiedRegex, index);
        newNode->firstpos = newNode->left->firstpos;
        newNode->lastpos = newNode->left->lastpos;
    }
    else if (c == '.')
    {
        newNode->right = createSyntacticTree(modifiedRegex, index);
        newNode->left = createSyntacticTree(modifiedRegex, index);
        newNode->nullable = (newNode->left->nullable && newNode->right->nullable);
        newNode->firstpos = newNode->left->firstpos;
        newNode->lastpos = newNode->right->lastpos;
        if (newNode->left->nullable)
        {
            newNode->firstpos.insert(newNode->firstpos.end(), newNode->right->firstpos.begin(), newNode->right->firstpos.end());
        }
        if (newNode->right->nullable)
        {
            newNode->lastpos.insert(newNode->lastpos.begin(), newNode->left->lastpos.begin(), newNode->left->lastpos.end());
        }
    }

    return newNode;
}

void calculateFollowpos(TreeNode *root)
{
    if (root == nullptr || (root->left == nullptr && root->right == nullptr) || root->value == 'E')
    {
        return;
    }

    calculateFollowpos(root->left);
    calculateFollowpos(root->right);

    if (root->value == '*')
    {
        if (root->left->value != 'E')
        {
            sort(root->left->firstpos.begin(), root->left->firstpos.end());
            for (int p : root->left->lastpos)
            {
                sort(followpos[p].begin(), followpos[p].end());
                vector<int> temp;
                set_union(followpos[p].begin(), followpos[p].end(), root->left->firstpos.begin(), root->left->firstpos.end(), back_inserter(temp));
                followpos[p] = temp;
            }
        }
    }
    else if (root->value == '.')
    {
        if (root->left->value != 'E' && root->right->value != 'E')
        {
            sort(root->right->firstpos.begin(), root->right->firstpos.end());
            for (int p : root->left->lastpos)
            {
                sort(followpos[p].begin(), followpos[p].end());
                vector<int> temp;
                set_union(followpos[p].begin(), followpos[p].end(), root->right->firstpos.begin(), root->right->firstpos.end(), back_inserter(temp));
                followpos[p] = temp;
            }
        }
    }

    return;
}

void collectTerminals(TreeNode *root)
{
    if (root)
    {
        if (root->p != -1)
        {
            terminals[root->p] = root->value;
        }

        collectTerminals(root->left);
        collectTerminals(root->right);
    }

    return;
}

map<char, vector<int>> calculateTerminalPositions(const string &modifiedRegex)
{
    map<char, vector<int>> terminalPositions;

    unordered_set<char> temp;
    for (char c : modifiedRegex)
    {
        if (c != 'E' && c != '|' && c != '.' && c != '*' && c != '$')
        {
            if (temp.find(c) == temp.end())
            {
                temp.insert(c);
                alphabet.push_back(c);
            }
        }
    }
    sort(alphabet.begin(), alphabet.end());

    for (char c : alphabet)
    {
        vector<int> temp1;
        for (int i = 1; i < terminals.size(); i++)
        {
            if (terminals[i] == c)
            {
                temp1.push_back(i);
            }
        }
        sort(temp1.begin(), temp1.end());
        terminalPositions[c] = temp1;
    }

    return terminalPositions;
}

void constructDFA(TreeNode *root, map<char, vector<int>> terminalPositions)
{
    char currState = 'A';
    char lastState = 'A';

    stateMap[currState] = root->firstpos;
    do
    {
        states.push_back(currState);
        sort(stateMap[currState].begin(), stateMap[currState].end());
        for (char c : alphabet)
        {
            vector<int> temp;
            set_intersection(stateMap[currState].begin(), stateMap[currState].end(), terminalPositions[c].begin(), terminalPositions[c].end(), back_inserter(temp));
            vector<int> setOfPositions;
            for (int i : temp)
            {
                vector<int> temp1;
                set_union(setOfPositions.begin(), setOfPositions.end(), followpos[i].begin(), followpos[i].end(), back_inserter(temp1));
                sort(temp1.begin(), temp1.end());
                setOfPositions = temp1;
            }

            auto it = find_if(stateMap.begin(), stateMap.end(), [setOfPositions](const auto &pair)
                              { return pair.second == setOfPositions; });
            if (it != stateMap.end())
            {
                transitionMap[currState].push_back(make_pair(c, it->first));
            }
            else
            {
                stateMap[++lastState] = setOfPositions;
                transitionMap[currState].push_back(make_pair(c, lastState));
            }
        }
        currState++;
    } while (currState <= lastState);

    for (char state : states)
    {
        auto it = find(stateMap[state].begin(), stateMap[state].end(), root->right->p);

        if (it != stateMap[state].end())
        {
            finalStates.push_back(state);
        }
    }

    cout << "\n=============== DFA ===============\n";
    cout << "\n  Q";
    for (char c : alphabet)
    {
        cout << " | " << c;
    }
    cout << " | Set of Positions" << endl;
    string separator((4 * alphabet.size()) + 23, '=');
    cout << separator << endl;
    for (char state : states)
    {
        auto it = find(finalStates.begin(), finalStates.end(), state);
        if (it != finalStates.end())
        {
            if (state == 'A')
            {
                cout << "->" << state << "*| ";
            }
            else
            {
                cout << "  " << state << "*| ";
            }
        }
        else
        {
            if (state == 'A')
            {
                cout << "->" << state << " | ";
            }
            else
            {
                cout << "  " << state << " | ";
            }
        }
        for (const auto &pair : transitionMap[state])
        {
            cout << pair.second << " | ";
        }
        if (stateMap[state].size())
        {
            cout << "[";
            for (int p : stateMap[state])
            {
                cout << p << " ";
            }
            cout << "\b]" << endl;
        }
        else
        {
            cout << "[]" << endl;
        }
    }
    cout << "\n";

    return;
}

void validateInput(string &input)
{
    char currState = 'A';
    for (char c : input)
    {
        auto it = find(alphabet.begin(), alphabet.end(), c);
        if (it != alphabet.end())
        {
            auto it1 = find_if(transitionMap[currState].begin(), transitionMap[currState].end(), [c](const auto &pair)
                               { return pair.first == c; });
            currState = it1->second;
        }
        else
        {
            cout << "The string " << input << " will be rejected\n";
            return;
        }
    }

    auto it2 = find(finalStates.begin(), finalStates.end(), currState);
    if (it2 != finalStates.end())
    {
        cout << "The string " << input << " will be accepted\n";
    }
    else
    {
        cout << "The string " << input << " will be rejected\n";
    }

    return;
}

void regexToDFA(string &regex)
{
    string modifiedRegex = convertToModifiedRegex(regex);

    terminals.resize(terminalCount + 1);
    followpos.resize(terminalCount + 1);

    size_t index = 0;
    TreeNode *root = createSyntacticTree(modifiedRegex, index);

    calculateFollowpos(root);

    collectTerminals(root);

    map<char, vector<int>> terminalPositions = calculateTerminalPositions(modifiedRegex);

    constructDFA(root, terminalPositions);

    return;
}

int main()
{
    cout << "\nEnter a regular expression: ";
    string regex;
    getline(cin, regex);

    regexToDFA(regex);

    char continueInput;
    do
    {
        cout << "\nEnter a string: ";
        string input;
        getline(cin, input);

        validateInput(input);

        cout << "\nDo you want to enter another string? (y/n): ";
        cin >> continueInput;

        cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    } while (continueInput == 'y' || continueInput == 'Y');

    return 0;
}
