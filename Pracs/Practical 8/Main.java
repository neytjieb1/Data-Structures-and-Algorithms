/**
* Name: Berne' Nortier
* Student Number: 17091820
*/

public class Main {

	public static void main(String[] args) {

		// Case 1
		Vertex vertexA = new Vertex("A");
		Vertex vertexB = new Vertex("B");
		Vertex vertexC = new Vertex("C");
		Vertex vertexD = new Vertex("D");
		Vertex vertexE = new Vertex("E");

		vertexA.addNeighbour(new Edge(vertexA, vertexC, 10));
		vertexA.addNeighbour(new Edge(vertexA, vertexB, 17));
		vertexC.addNeighbour(new Edge(vertexC, vertexB, 5));
		vertexC.addNeighbour(new Edge(vertexC, vertexD, 9));
		vertexC.addNeighbour(new Edge(vertexC, vertexE, 11));
		vertexB.addNeighbour(new Edge(vertexB, vertexD, 1));
		vertexD.addNeighbour(new Edge(vertexD, vertexE, 6));

		Graph graph = new Graph();
		graph.addVertex(vertexA);
		graph.addVertex(vertexB);
		graph.addVertex(vertexC);
		graph.addVertex(vertexD);
		graph.addVertex(vertexE);

		Vertex startVertex = vertexA;
		Vertex endVertex = vertexE;

		graph.constructMatrix();

		System.out.println("Minimum distance from " + startVertex.getName() + " to " +
				endVertex.getName() + " : " + graph.getShortestPathDistance(startVertex, endVertex));
		System.out.println("Shortest Path from " + startVertex.getName() + " to " +
				endVertex.getName() + " : " + graph.getShortestPath(startVertex, endVertex));

		/* Expected output
		Minimum distance from A to E : 21.0
		Shortest Path from A to E : [A, C, E]
		*/

		// Case 2
		graph.reset();
		startVertex = vertexB;
		endVertex = vertexC;

		System.out.println("Minimum distance from " + startVertex.getName() + " to " + endVertex.getName() + " : " +
				graph.getShortestPathDistance(startVertex, endVertex));
		System.out.println("Shortest Path from " + startVertex.getName() + " to " + endVertex.getName() + " : " +
				graph.getShortestPath(startVertex, endVertex));

		/* Expected output
		Minimum distance from B to C : Infinity
		Shortest Path from B to C : []
		*/

		// Case 3
		graph.reset();
		startVertex = vertexA;
		endVertex = vertexA;

		System.out.println("Minimum distance from " + startVertex.getName() + " to " + endVertex.getName() + " : " +
				graph.getShortestPathDistance(startVertex, endVertex));
		System.out.println("Shortest Path from " + startVertex.getName() + " to " + endVertex.getName() + " : " +
				graph.getShortestPath(startVertex, endVertex));

		/* Expected output
		Minimum distance from A to A : 0.0
		Shortest Path from A to A : [A]
		*/


	}
}