from queue import PriorityQueue


def best_first_search(graph, source, target):
    visited = {node: False for node in graph}
    pq = PriorityQueue()
    pq.put((0, source))
    visited[source] = True

    while not pq.empty():
        u = pq.get()[1]
        print(u, end=" ")
        if u == target:
            break

        for v, c in graph[u]:
            if not visited[v]:
                visited[v] = True
                pq.put((c, v))
    print()


def add_edge(graph, x, y, cost):
    graph[x].append((y, cost))


def create_graph():
    graph = {}

    while True:
        edge = input("Enter an edge (source, destination, cost): ").split()

        if edge[0] not in graph:
            graph[edge[0]] = []
        if edge[1] not in graph:
            graph[edge[1]] = []

        add_edge(graph, edge[0], edge[1], int(edge[2]))

        more_edges = input("Do you want to enter more edges? (y/n): ").lower()
        if more_edges != "y":
            break

    return graph


# Define the graph
graph = create_graph()

source = input("Enter the source vertex: ")
target = input("Enter the target vertex: ")

print("Search Sequence:")
best_first_search(graph, source, target)
