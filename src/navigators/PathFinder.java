package navigators;

import java.util.ArrayList;
import java.util.Collections;

import entities.Edge;
import entities.Frontier;
import entities.Graph;
import entities.Node;

public class PathFinder {
		
	public static void search(Graph graph, Node source, Node destination, int navigatorType) {
		int[] status = new int[graph.V()];
		Frontier frontier = new Frontier(navigatorType);
		frontier.push(source);
		status[source.getId()] = Constants.VISITED;
		source.setParent(-1);
		source.setDistToGoal(destination);
		// System.out.println("Pushed Vertex " + source.getId() + '(' + source.getX() + ',' + source.getY() + ')');
		int iterations = 0;
		int verticesVisited = 1;
		while (!frontier.isEmpty()) {
			iterations++;
			Node poppedVertex = frontier.pop();
			// System.out.println("Popped Vertex " + poppedVertex.getId() + '(' + poppedVertex.getX() + ',' + poppedVertex.getY() + ')');
			if (poppedVertex.getId() == destination.getId()) {
				System.out.println("Found Node"); 
				int pathLength = printTracebackPath(poppedVertex, graph);
				printStats(graph, frontier, navigatorType, iterations, verticesVisited, pathLength);
				return;
			}
			for(Edge edge: graph.getAdj()[poppedVertex.getId()]) {
				int neighborID = edge.getV() == poppedVertex.getId() ? edge.getW() : edge.getV();				
				if (status[neighborID] == Constants.UNSEEN) {
					Node neighborVertex = graph.getVertices()[neighborID];
					neighborVertex.setParent(poppedVertex.getId());
					neighborVertex.setDistToGoal(destination);
					frontier.push(neighborVertex);
					// System.out.println("Pushed Vertex " + neighborVertex.getId() + '(' + neighborVertex.getX() + ',' + neighborVertex.getY() + ')');
					status[neighborID] = Constants.VISITED;
					verticesVisited++;
				}				
			}
		}
	}

	private static int printTracebackPath(Node poppedVertex, Graph graph) {	
		int pathLength = 0;
		int parentID = poppedVertex.getId();
		ArrayList<String> path = new ArrayList<>();
		System.out.println();
		while (parentID != -1) {
			Node node = graph.getVertices()[parentID];
			String pathVertex = "vertex " + parentID + "(" + node.getX() + "," + node.getY() + ")";
			path.add(pathVertex);			
			System.out.println(node.getX() + " " + node.getY());
			pathLength++;
			parentID = node.getParent();
		}
		Collections.reverse(path);
		System.out.println();
		for (String string : path) {
			System.out.println(string);
		}
		return pathLength;
	}

	private static void printStats(Graph graph, Frontier frontier, int navigatorType, int iterations, int verticesVisited, int pathLength) {
		System.out.println();
		switch (navigatorType) {
		case 0:
			System.out.println("search algorithm = BFS");
			break;
		case 1:
			System.out.println("search algorithm = DFS");
			break;
		case 2:
			System.out.println("search algorithm = GBFS");
			break;
		default:
			break;
		}		
		System.out.println("total iterations = " + iterations);
		System.out.println("max frontier size = " + frontier.getMaxSize());
		System.out.println("vertices visited = " + verticesVisited + "/" + graph.V());
		System.out.println("path length = " + (pathLength-1));
	}
}
