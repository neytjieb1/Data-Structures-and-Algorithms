/**
* Name: Berne' Nortier
* Student Number: 17091820
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		PrintStream out = new PrintStream(new FileOutputStream("/home/jo/IdeaProjects/Practical 8/outputB.txt"));
		System.setOut(out);

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

		System.out.println("Minimum distance from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPathDistance(startVertex, endVertex));
		System.out.println("Shortest Path from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPath(startVertex, endVertex));

		/* Expected output
		Minimum distance from A to E : 21.0
		Shortest Path from A to E : [A, C, E]
		*/

		// Case 2
		graph.reset();
		startVertex = vertexB;
		endVertex = vertexC;

		System.out.println("Minimum distance from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPathDistance(startVertex, endVertex));
		System.out.println("Shortest Path from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPath(startVertex, endVertex));

		// /* Expected output
		// Minimum distance from B to C : 1.7976931348623157E308 (The Double.MAX_VALUE depends on your platform)
		// Shortest Path from B to C : []
		// */

		Vertex P = new Vertex("P");
		Vertex J = new Vertex("J");
		Vertex N = new Vertex("N");
		Vertex L = new Vertex("L");
		Vertex H = new Vertex("H");
		Vertex Z = new Vertex("Z");
		Vertex D = new Vertex("D");

		P.addNeighbour(new Edge(P, J, 2));
		P.addNeighbour(new Edge(P, N, 13));
		J.addNeighbour(new Edge(J, H, 15));
		H.addNeighbour(new Edge(H, L, 3));
		N.addNeighbour(new Edge(N, L, 9));
		L.addNeighbour(new Edge(L, Z, 10));
		H.addNeighbour(new Edge(H, Z, 11));
		Z.addNeighbour(new Edge(Z, D, 3));

		graph = new Graph();
		graph.addVertex(P);
		graph.addVertex(J);
		graph.addVertex(N);
		graph.addVertex(L);
		graph.addVertex(H);
		graph.addVertex(Z);
		graph.addVertex(D);

		startVertex = P;
		endVertex = D;

		System.out.println("Minimum distance from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPathDistance(startVertex, endVertex));
		System.out.println("Shortest Path from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPath(startVertex, endVertex));

		graph.reset();
		startVertex = P;
		endVertex = P;

		System.out.println("Minimum distance from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPathDistance(startVertex, endVertex));
		System.out.println("Shortest Path from " + startVertex.getName() + " to " + endVertex.getName() + " : " + graph.getShortestPath(startVertex, endVertex));
	}
}