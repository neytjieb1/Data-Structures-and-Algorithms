/**
 * Name: Berne' Nortier
 * Student Number: 17091820
 */

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private List<Vertex> verticesList;
    private List<String> vertexNames;
    private Double dist[][];
    private Vertex next[][];

    public Graph() {
        this.verticesList = new ArrayList<>();
        this.vertexNames = new ArrayList<>();
    }

    public void addVertex(Vertex vertex) {
        this.verticesList.add(vertex);
        this.vertexNames.add(vertex.getName());
    }

    public void reset() {
        for (Vertex vertex : verticesList) {
            vertex.setVisited(false);
            vertex.setPredecessor(null);
            vertex.setDistance(Double.POSITIVE_INFINITY);
        }
    }

    ////// Implement the methods below this line //////

    public List<Vertex> getShortestPath(Vertex sourceVertex, Vertex targetVertex) {
        // Your code here
        constructMatrix();
        int start = findVertexIndex(sourceVertex.getName());
        int end = findVertexIndex(targetVertex.getName());
        //negative cycle
        if (findNegativeCycle()) {
            //empty list
            return null;
        }
        // unreachable
        if (dist[start][end].equals(Double.POSITIVE_INFINITY)) {
            return new ArrayList<>();
        }
        //start == end
        if (start==end && dist[start][end].equals(Double.POSITIVE_INFINITY)) {
            ArrayList<Vertex> singleton = new ArrayList<Vertex>();
            singleton.add(sourceVertex);
            return singleton;
        }

        //reachable, construct path
        return reconstructPath(sourceVertex, targetVertex);

    }

    public List<Vertex> reconstructPath(Vertex sourceVertex, Vertex targetVertex) {
        int start = findVertexIndex(sourceVertex.getName());
        int end = findVertexIndex(targetVertex.getName());
        if (next[start][end] == null) {
            return new ArrayList<>();
        }
        List<Vertex> path = new ArrayList<>();
        path.add(sourceVertex);
        while (sourceVertex != targetVertex) {
            sourceVertex = next[start][end];
            path.add(sourceVertex);
            start = findVertexIndex(sourceVertex.getName());
        }
        return path;
    }

    public double getShortestPathDistance(Vertex sourceVertex, Vertex targetVertex) {
        // Your code here
        constructMatrix();

        int start = findVertexIndex(sourceVertex.getName());
        int end = findVertexIndex(targetVertex.getName());

        if (findNegativeCycle()) {
            return Double.NEGATIVE_INFINITY;
        }
        if (start == end && dist[start][end].equals(Double.POSITIVE_INFINITY)) {
            return 0;
        }
        //will return infinity or value
        return dist[start][end];
    }

    private boolean findNegativeCycle() {
        for (int i = 0; i < this.verticesList.size(); i++) {
            if (this.dist[i][i] < 0) {
                return true;
            }
        }
        return false;
    }

    private int findVertexIndex(String name) {
        for (int i = 0; i < this.verticesList.size(); i++) {
            if (this.verticesList.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public void constructMatrix() {
        int V = this.verticesList.size();
        this.dist = new Double[V][V];
        this.next = new Vertex[V][V];

        //initialise to infinity
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        //create matrix
        for (Vertex vertex : this.verticesList) {
            List<Edge> edges = vertex.getAdjacenciesList();
            for (Edge e : edges) {
                int start = findVertexIndex(e.getStartVertex().getName());
                int end = findVertexIndex(e.getEndVertex().getName());
                dist[start][end] = e.getWeight();
                next[start][end] = e.getEndVertex();
            }
        }


        for (int k = 0; k < V; k++) {           //pick all vertices as source one by one
            for (int i = 0; i < V; i++) {       //pick all vertices as destination one by one
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) { //if on shortest path then update value
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    }

    private void printArr(Double[][] arr) {
        for (Vertex v : this.verticesList) {
            System.out.print(v.getName() + "     ");
        }
        System.out.println(" ");

        for (int i = 0; i < arr.length; i++) {
            System.out.print(this.verticesList.get(i).getName() + " ");
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j].equals(Double.POSITIVE_INFINITY)) {
                    System.out.print("x.x ");
                } else {
                    System.out.print(arr[i][j] + " ");
                }
            }
            System.out.println('\n');
        }
        System.out.println("\n");
    }

}