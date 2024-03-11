import heapq


def best_first_search(graph, start, goal):
    visited = set()
    priority_queue = [(0, [start])]  # (cost, path) tuple in the priority queue

    while priority_queue:
        _, current_path = heapq.heappop(priority_queue)
        current_node = current_path[-1]

        if current_node not in visited:
            print(f"Visiting node: {current_node}")

            if current_node == goal:
                print(f"Goal reached: {current_node}")
                print("Path:", current_path)
                return True

            visited.add(current_node)

            for neighbor, weight in graph.get(current_node, []):
                if neighbor not in visited:
                    new_path = current_path + [neighbor]
                    heapq.heappush(priority_queue, (weight, new_path))

    print("Goal not reached.")
    return False


def add_edge(graph, source, destination, cost):
    graph[source].append((destination, cost))


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


# Example usage
graph = create_graph()

start_node = input("\nEnter the start node: ")
goal_node = input("Enter the goal node: ")

print("\nBest-First Search Sequence:")
best_first_search(graph, start_node, goal_node)
