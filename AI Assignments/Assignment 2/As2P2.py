import heapq
import math


def euclidean_distance(node1, node2):
    x1, y1 = node1
    x2, y2 = node2
    return math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)


def astar_search(graph, start, goal):
    priority_queue = [(0, start)]
    came_from = {start: None}
    cost_so_far = {start: 0}

    while priority_queue:
        _, current_node = heapq.heappop(priority_queue)

        if current_node == goal:
            break

        for neighbor, edge_cost in graph[current_node][1]:
            new_cost = cost_so_far[current_node] + edge_cost
            if neighbor not in cost_so_far or new_cost < cost_so_far[neighbor]:
                cost_so_far[neighbor] = new_cost
                priority = new_cost + euclidean_distance(
                    graph[neighbor][0], graph[goal][0]
                )
                heapq.heappush(priority_queue, (priority, neighbor))
                came_from[neighbor] = current_node

    path = reconstruct_path(came_from, start, goal)
    return path


def reconstruct_path(came_from, start, goal):
    current_node = goal
    path = []

    while current_node != start:
        path.append(current_node)
        current_node = came_from[current_node]

    path.append(start)
    return path[::-1]


def create_graph():
    graph = {}

    num_vertices = int(input("Enter the number of vertices: "))

    for _ in range(num_vertices):
        node_input = input("Enter a vertex (label, x, y): ").split()
        label, x, y = node_input
        graph[label] = [(int(x), int(y)), []]

    while True:
        edge = input("Enter an edge (endpoint 1, endpoint 2, cost): ").split()
        endpoint1, endpoint2, cost = edge

        while endpoint1 not in graph or endpoint2 not in graph:
            print("Invalid vertices. Please enter a valid edge.")

            edge = input("Enter an edge (endpoint 1, endpoint 2, cost): ").split()
            endpoint1, endpoint2, cost = edge

        graph[endpoint1][1].append((endpoint2, int(cost)))
        graph[endpoint2][1].append((endpoint1, int(cost)))

        more_edges = input("Do you want to enter more edges? (y/n): ").lower()
        if more_edges != "y":
            break

    return graph


# Example usage:
graph = create_graph()

start_node = input("\nEnter the start node: ")
goal_node = input("Enter the goal node: ")

path = astar_search(graph, start_node, goal_node)

print("\nA* Search Sequence:")
print(" -> ".join(path))
