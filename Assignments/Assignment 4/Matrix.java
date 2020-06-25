public class Matrix {
    int V;
    int E;
    double data[][];
    int intdata[][];
    Vertex next[][];
    private Vertex[] verticesList;

    public Matrix(int numV, int numE, Vertex[] vList) {
        this.V = numV;
        this.E = numE;
        this.verticesList = vList;
        constructMatrix(this.V);
    }

    public int[][] getData() {
        return this.intdata;
    }

    public void constructMatrix(int size) {
        this.data = new double[size][size];
        this.next = new Vertex[size][size];
        this.intdata = new int[size][size];

        //initialise to infinity
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i==j) {
                    data[i][j] = 0.0;
                }
                else {
                    data[i][j] = Double.POSITIVE_INFINITY;
                }
                next[i][j] = null;
            }
        }

        //create matrix
        for (Vertex vertex : this.verticesList) {
            Edge[] edges = vertex.getAdjacenciesList();
            for (Edge e : edges) {
                if (e!=null) {
                    int start = findVertexIndex(e.getStartVertex().getName());
                    int end = findVertexIndex(e.getEndVertex().getName());
                    data[start][end] = Double.valueOf(e.getWeight());
                    data[end][start] = Double.valueOf(e.getWeight());       //since undirected
                    next[start][end] = e.getEndVertex();
                    next[end][start] = e.getStartVertex();
                }
            }
        }

        //Shortest Path
        for (int k = 0; k < V; k++) {           //pick all vertices as source one by one
            for (int i = 0; i < V; i++) {       //pick all vertices as destination one by one
                for (int j = 0; j < V; j++) {
                    if (data[i][k] + data[k][j] < data[i][j]) { //if on shortest path then update value
                        data[i][j] = data[i][k] + data[k][j];
                        next[i][j] = next[i][k];
                        //next[j][i] = next[i][k];
                    }
                }
            }
        }
        //Construct intdata
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                /*if (data[i][j].equals(Double.POSITIVE_INFINITY)) {
                    intdata[i][j] = Integer.MAX_VALUE;
                }
                else*/ {
                    intdata[i][j] = (int)data[i][j];
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
        System.out.print('\t');
        for (Vertex v : this.verticesList) {
            System.out.print(v.getName() + "\t");
        }
        System.out.println(" ");

        for (int i = 0; i < this.V; i++) {
            System.out.print(this.verticesList[i].getName() + "\t");
            for (int j = 0; j < this.V; j++) {
                if (this.data[i][j]==(Double.MAX_VALUE)) {
                    System.out.print("x.x\t");
                } else {
                    System.out.print(this.data[i][j] + "\t");
                }
            }
            System.out.println('\n');
        }
    }

    public void printNext() {
        System.out.print('\t');
        for (Vertex v : this.verticesList) {
            System.out.print(v.getName() + "\t");
        }
        System.out.println("");

        for (int i = 0; i < this.V; i++) {
            System.out.print(this.verticesList[i].getName() + "\t");
            for (int j = 0; j < this.V; j++) {
                if (this.next[i][j]==null) {
                    System.out.print("-\t");
                } else {
                    System.out.print(this.next[i][j].getName() + "\t");
                }
            }
            System.out.println('\n');
        }
    }


}
