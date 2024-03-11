from collections import deque


def bfs(graph, start, goal):
    if start == goal:
        return f"Goal node {goal} found."

    visited = set()
    queue = deque([[start]])

    while queue:
        path = queue.popleft()
        node = path[-1]

        if node not in visited:
            print(f"Visiting node: {node}")  # Print the current node
            visited.add(node)

            if node == goal:
                return f"Goal reached: {node}\nPath: {' -> '.join(path)}"

            neighbors = graph[node]

            for neighbor in neighbors:
                new_path = list(path)
                new_path.append(neighbor)
                queue.append(new_path)

    return "Goal node not reachable from the start node."


def dfs(graph, start, goal):
    visited = set()
    stack = [[start]]

    while stack:
        path = stack.pop()
        node = path[-1]

        if node not in visited:
            print(f"Visiting node: {node}")  # Print the current node
            visited.add(node)

            if node == goal:
                return f"Goal reached: {node}\nPath: {' -> '.join(path)}"

            neighbors = graph[node]
            # Push unvisited neighbors onto the stack starting from the leftmost child
            for neighbor in reversed(neighbors):
                if neighbor not in visited:
                    new_path = list(path)
                    new_path.append(neighbor)
                    stack.append(new_path)

    return "Goal node not reachable from the start node."


def add_edge(graph, u, v):
    graph[u].append(v)
    graph[v].append(u)


def create_graph():
    graph = {}

    while True:
        edge = input("Enter an edge (endpoint 1, endpoint 2): ").split()

        if edge[0] not in graph:
            graph[edge[0]] = []
        if edge[1] not in graph:
            graph[edge[1]] = []

        add_edge(graph, edge[0], edge[1])

        more_edges = input("Do you want to enter more edges? (y/n): ").lower()
        if more_edges != "y":
            break

    return graph


# Define the graph
graph = create_graph()

start_node = input("\nEnter the start node: ")
target_node = input("Enter the target node: ")

# Test BFS
print("\nBreadth-First Search Sequence:")
print(bfs(graph, start_node, target_node))

# Test DFS
print("\nDepth-First Search Sequence:")
print(dfs(graph, start_node, target_node))
