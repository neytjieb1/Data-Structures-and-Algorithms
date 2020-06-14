public class Matrix {
    int V;
    int E;
    Double data[][];
    Vertex next[][];
    private Vertex[] verticesList;

    public Matrix(int numV, int numE, Vertex[] vList) {
        this.V = numV;
        this.E = numE;
        this.verticesList = vList;
//        this.vertexNames = vNames;
        constructMatrix(this.V);
    }


    public void constructMatrix(int size) {
        this.data = new Double[size][size];
        this.next = new Vertex[size][size];

        //initialise to infinity
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                data[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        //create matrix
        for (Vertex vertex : this.verticesList) {
            Edge[] edges = vertex.getAdjacenciesList();
            for (Edge e : edges) {
                if (e!=null) {
                    int start = findVertexIndex(e.getStartVertex().getName());
                    int end = findVertexIndex(e.getEndVertex().getName());
                    data[start][end] = e.getWeight();
                    data[end][start] = e.getWeight();       //since undirected
                    next[start][end] = e.getEndVertex();
                }
            }
        }
    }


    private int findVertexIndex(String name) {
        for (int i = 0; i < this.verticesList.length; i++) {
            if (this.verticesList[i].getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public void printAdjacencyM() {
        for (Vertex v : this.verticesList) {
            System.out.print(v.getName() + "     ");
        }
        System.out.println(" ");

        for (int i = 0; i < this.V; i++) {
            System.out.print(this.verticesList[i].getName() + " ");
            for (int j = 0; j < this.V; j++) {
                if (this.data[i][j].equals(Double.POSITIVE_INFINITY)) {
                    System.out.print("x.x ");
                } else {
                    System.out.print(this.data[i][j] + " ");
                }
            }
            System.out.println('\n');
        }
        //System.out.println("\n");
    }

}
