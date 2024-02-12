#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <sstream>
#include <algorithm>

using namespace std;

// Function to process input string and store in map
void processInput(const string &input, map<string, vector<string>> &productionMap, vector<string> &nonterminals)
{
    istringstream iss(input);
    string key;
    vector<string> values;

    // Extracting the key
    iss >> key;
    nonterminals.push_back(key);

    // Extracting the values
    string word;
    while (iss >> word)
    {
        if (word == "->" || word == "|")
        {
            continue;
        }
        values.push_back(word);
    }

    // Sorting the values vector
    sort(values.begin(), values.end());

    // Storing the key-value pair in the map
    productionMap[key] = values;
}

void remove_direct(map<string, vector<string>> &productionMap, string &nonterminal, vector<string> &nonterminals)
{
    vector<string> alphas, betas;
    for (const auto &str : productionMap[nonterminal])
    {
        if (str.find(nonterminal) == 0)
        {
            if ((str.substr(nonterminal.length())).empty())
            {
                cout << "Given grammar cannot be made free from left recursion"
                     << "\n\n";
                exit(0);
            }
            alphas.push_back(str.substr(nonterminal.length()));
        }
        else
        {
            betas.push_back(str);
        }
    }
    if (!alphas.empty())
    {
        productionMap[nonterminal].clear();
        for (auto &str : betas)
        {
            if (str == "ε")
            {
                productionMap[nonterminal].push_back(nonterminal + "'");
            }
            else
            {
                productionMap[nonterminal].push_back(str + nonterminal + "'");
            }
        }
        sort(productionMap[nonterminal].begin(), productionMap[nonterminal].end());
        productionMap[nonterminal + "'"] = {};
        for (auto &str : alphas)
        {
            productionMap[nonterminal + "'"].push_back(str + nonterminal + "'");
        }
        productionMap[nonterminal + "'"].push_back("ε");
        sort(productionMap[nonterminal + "'"].begin(), productionMap[nonterminal + "'"].end());

        nonterminals.push_back(nonterminal + "'");
        sort(nonterminals.begin(), nonterminals.end());
    }
}

void remove_indirect(map<string, vector<string>> &productionMap, vector<string> &nonterminals)
{
    for (int i = 0; i < nonterminals.size(); i++)
    {
        for (int j = 0; j < i; j++)
        {
            for (auto str : productionMap[nonterminals[i]])
            {
                if (str.find(nonterminals[j]) == 0)
                {
                    string temp = str.substr(nonterminals[j].length());
                    for (auto str1 : productionMap[nonterminals[j]])
                    {
                        if (temp.empty())
                        {
                            if (find(productionMap[nonterminals[i]].begin(), productionMap[nonterminals[i]].end(), str1) == productionMap[nonterminals[i]].end())
                            {
                                productionMap[nonterminals[i]].push_back(str1);
                            }
                        }
                        else
                        {
                            if (str1 == "ε")
                            {
                                productionMap[nonterminals[i]].push_back(temp);
                            }
                            else
                            {
                                productionMap[nonterminals[i]].push_back(str1 + temp);
                            }
                        }
                    }
                    productionMap[nonterminals[i]].erase(remove(productionMap[nonterminals[i]].begin(), productionMap[nonterminals[i]].end(), str), productionMap[nonterminals[i]].end());
                }
            }
        }
        sort(productionMap[nonterminals[i]].begin(), productionMap[nonterminals[i]].end());
        remove_direct(productionMap, nonterminals[i], nonterminals);
    }
}

int main()
{
    map<string, vector<string>> productionMap; // Map to store key-value pairs
    string input;
    vector<string> nonterminals;

    // Loop until the user presses enter twice
    cout << "\nEnter the production rules of the grammar (press enter twice to exit): \n";
    while (true)
    {
        getline(cin, input);

        // If the input is empty, exit the loop
        if (input.empty())
        {
            break;
        }

        // Process the input and store in the map
        processInput(input, productionMap, nonterminals);
    }
    sort(nonterminals.begin(), nonterminals.end());

    remove_indirect(productionMap, nonterminals);

    // Print the stored key-value pairs
    cout << "Grammar after removing left recursion:" << endl;
    for (const auto &pair : productionMap)
    {
        cout << pair.first << " -> ";
        for (int i = 0; i < pair.second.size() - 1; i++)
        {
            cout << pair.second[i] << " | ";
        }
        cout << pair.second[pair.second.size() - 1] << endl;
    }
    cout << endl;

    return 0;
}
