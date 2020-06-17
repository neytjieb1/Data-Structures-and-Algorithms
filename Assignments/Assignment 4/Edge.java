/**
 * Name: Berne' Nortier
 * Student Number: 17091820
 */

public class Edge {

    private Vertex startVertex;
    private Vertex endVertex;
    private Integer weight;

    public Edge(Vertex startVertex, Vertex endVertex, Integer weight) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.weight = weight;
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(Vertex startVertex) {
        this.startVertex = startVertex;
    }

    public Vertex getEndVertex() {
        return endVertex;
    }

    public void setEndVertex(Vertex endVertex) {
        this.endVertex = endVertex;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}