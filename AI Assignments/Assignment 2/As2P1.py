def depth_limited_dfs(graph, node, target, depth_limit, visited, current_depth):
    if current_depth > depth_limit:
        return False

    print(node, end=" ")

    visited[node] = True

    if node == target:
        return True

    for neighbor in graph[node]:
        if not visited[neighbor]:
            if depth_limited_dfs(
                graph, neighbor, target, depth_limit, visited, current_depth + 1
            ):
                return True

    visited[node] = False  # Backtrack
    return False


def iterative_deepening_dfs(graph, start, target, max_depth):
    for depth_limit in range(max_depth + 1):
        visited = {node: False for node in graph}
        print(f"\nDepth Limit {depth_limit}:")

        if depth_limited_dfs(graph, start, target, depth_limit, visited, 0):
            return


def add_edge(graph, x, y):
    graph[x].append(y)
    graph[y].append(x)


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
max_depth = int(input("\nEnter the maximum depth for IDDFS: "))

# Run Iterative Deepening DFS
iterative_deepening_dfs(graph, start_node, target_node, max_depth)
