/**
 * Name: Berne' Nortier
 * Student Number: 17091820
 */

public class Vertex implements Comparable<Vertex> {

    private String name;
    private Edge[] adjacenciesList;
    private int adjacenciesCount;
    private Integer distance = Integer.MAX_VALUE;
    public int numNeighbours;

    private boolean visited = false;
    private Vertex predecessor;

    public Vertex(String name) {
        this.name = name;
        this.adjacenciesList = new Edge[10000];
        this.adjacenciesCount = 0;
        this.numNeighbours = 0;
    }

/*    public Vertex clone() {
        //name
        Vertex temp = new Vertex(this.name);
        //adjacenciesCount
        temp.adjacenciesCount = this.adjacenciesCount;
        //adjacenciesList
        for (int i = 0; i < this.adjacenciesCount; i++) {
            Edge e = this.adjacenciesList[i];
            temp.adjacenciesList[i] = new Edge(e.getStartVertex(), e.getEndVertex(), e.getWeight());
        }
        //Vertex startVertex, Vertex endVertex, Integer weight



        //distance

        //numNeighbours
    }*/

    public void addNeighbour(Edge edge) {
        this.adjacenciesList[numNeighbours++] = edge;
        this.adjacenciesCount++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAdjacenciesCount() {return adjacenciesCount;}

    public Edge[] getAdjacenciesList() {
        return adjacenciesList;
    }

    public void setAdjacenciesList(Edge[] adjList) {
        this.adjacenciesList = adjList;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Vertex getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Vertex predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(Vertex otherVertex) {
        return Integer.compare(this.distance, otherVertex.getDistance());
    }
}