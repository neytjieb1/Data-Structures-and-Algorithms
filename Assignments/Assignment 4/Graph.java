/**
 * Name and Surname: BL Nortier
 * Student/staff Number: 17091820
 */

import java.io.*;

/**
 1. You may not change the signatures of any of the given methods.  You may
 however add any additional methods and/or fields which you may require to aid
 you in the completion of this assignment.

 2. You will have to design and implement your own Graph class in terms of
 graph representation.

 3. You may add any additional classes and/or methods to your assignment.
 */
public class Graph {
    private Integer numVertices;
    private Integer numEdges = 0;
    private Vertex[] verticesList;
    private String[] vertexNames;
    public Matrix adjacency;

    /**
     The constructor receives the name of the file from which a graph
     is read and constructed.
     */

    public Graph(String f) throws IOException {

        String[][] lines = readFile(f);
        this.verticesList = new Vertex[this.numVertices];
        this.vertexNames = new String[this.numVertices];
        if (lines != null) {
            constructGraph(lines);
            this.adjacency = new Matrix(numVertices, numEdges, verticesList);
        }

    }

    private Graph(int numV, int numE, Vertex[] vList, String vNames[], Matrix adj) {
        this.numVertices = numV;
        this.numEdges = numE;
        this.verticesList = vList;
        this.vertexNames = vNames;
        this.adjacency = adj;
    }

    /**
     * @param lines
     * Given the lines read from the file as 2D array, construct all vertices and all edges
     */
    public void constructGraph(String[][] lines) {
        int insertedVs = 0;
        for (int i = 0; i < this.numEdges; i++) {
            //if vertices not in set yet, add
            if (contains(lines[i][0], this.vertexNames) == -1) {
                this.vertexNames[insertedVs] = lines[i][0];
                this.verticesList[insertedVs++] = new Vertex(lines[i][0]);

            }
            if (contains(lines[i][1], this.vertexNames) == -1) {
                this.vertexNames[insertedVs] = lines[i][1];
                this.verticesList[insertedVs++] = new Vertex(lines[i][1]);
            }
            //add edge
            Vertex start = this.verticesList[contains(lines[i][0], this.vertexNames)];
            Vertex end = this.verticesList[contains(lines[i][1], this.vertexNames)];
            Double weight = Double.valueOf(lines[i][2]);
            start.addNeighbour(new Edge(start, end, weight));
            end.addNeighbour(new Edge(end,start, weight)); //since undirected  //NO! CANNOT EDGE
        }
    }

    /**
     * @param name
     * Check if the vertex is already contained in the list. If yes, return the index at which it is stored.
     * If not, return -1 indicating that it's not in the list
     */
    private int contains(String name, String[] vNames) {
        for (int i = 0; i < vNames.length; i++) {
            if (vNames[i] != null && vNames[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param filename
     * @return given a file, convert the file into array format by removing the delimiters.
     * Also sets numVertices
     * @throws IOException
     */
    public String[][] readFile(String filename) throws IOException {
        int i;
        try {
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            //read first line
            this.numVertices = Integer.valueOf(br.readLine());
            String st;
            String[][] lines = new String[10000][3];
            i = 0;
            while ((st = br.readLine()) != null && st != "") {
                String[] temp = st.split(",");
                for (int j = 0; j < 3; j++) {
                    lines[i][j] = temp[j];
                }
                this.numEdges++;
                i++;
            }
            /*
            for (int j = 0; j < i; j++) {
                for (int k = 0; k < 3; k++) {
                    System.out.print(lines[j][k] + " ");
                }
                System.out.println("");
            }
             */
            return lines;

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. File Not found");
            e.printStackTrace();
            this.numEdges = -1;
            return null;
        }
    }


    /**
     The clone method should return a deep copy/clone
     of this graph.
     */
    public Graph clone() {
        int numV = this.numVertices;
        int numE = this.numEdges;
        Vertex[] vList = new Vertex[verticesList.length];
        String[] vNames = new String[vertexNames.length];
        Double[][] data = this.adjacency.data;

        int insertedVs = 0;
        for (int start = 0; start < vNames.length; start++) {
            for (int end = 0; end < vNames.length; end++) {
                //no important infor here
                if (data[start][end].equals(Double.POSITIVE_INFINITY)) {
                    continue;
                }
                //vertex to be added
                else {
                    //Add both vertices
                    if (contains(this.vertexNames[start], vNames) == -1) {
                        vNames[insertedVs] = this.vertexNames[start];
                        vList[insertedVs++] = new Vertex(this.vertexNames[start]);
                    }
                    if (contains(this.vertexNames[end], vNames) == -1) {
                        vNames[insertedVs] = this.vertexNames[end];
                        vList[insertedVs++] = new Vertex(this.vertexNames[end]);
                    }
                    //add edge between vertices
                    Vertex startV = vList[contains(vNames[start], vNames)];
                    Vertex endV = vList[contains(vNames[end], vNames)];
                    Double w = data[start][end];
                    startV.addNeighbour(new Edge(startV, endV, w));
                    endV.addNeighbour(new Edge(endV,startV,w)); //Not necessary since data is symmetric
                    //Otherwise adding double
                }
            }
        }

        Matrix adj = new Matrix(numV, numE, vList);
        Graph clone = new Graph(numV, numE, vList, vNames, adj);

        return clone;
    }

    /**
     This method should discard the current graph and construct a new
     graph contained in the file named "fileName". Return true if reconstruction
     was successful.
     */
    public boolean reconstructGraph(String fileName) throws IOException {
        //initialise all to null
        this.adjacency = null;
        this.numVertices = 0;
        this.numEdges = 0;
        this.verticesList = null;
        this.vertexNames = null;

        //start over
        String[][] lines = readFile(fileName);
        this.verticesList = new Vertex[this.numVertices]; //overwrites
        this.vertexNames = new String[this.numVertices];
        if (lines != null) {
            constructGraph(lines);
            this.adjacency = new Matrix(numVertices, numEdges, verticesList);
            return true;
        } else {
            return false;
        }

    }

    /**
     This method returns the number of direct edges between u and v vertices.
     If there are no direct edges, return 0.
     In there is 1 or more direct edges, return the number of edges.
     */
    public int numEdges(String u, String v) {
        int numDirectE = 0;
        //check start
        int index = this.contains(u, vertexNames);
        if (index != -1) {
            Vertex vertexU = this.verticesList[this.contains(u, vertexNames)];
            for (int i = 0; i < vertexU.getAdjacenciesCount(); i++) {
                Edge e = vertexU.getAdjacenciesList()[i];
                if (e!=null && e.getEndVertex().getName().equals(v)) {
                    numDirectE++;
                }
            }
        }
        //check end
        index = this.contains(v, vertexNames);
        if (index != -1) {
            Vertex vertexV = this.verticesList[this.contains(v, vertexNames)];
            for (int i = 0; i < vertexV.getAdjacenciesCount(); i++) {
                Edge e = vertexV.getAdjacenciesList()[i];
                if (e!=null && e.getEndVertex().getName().equals(v)) {
                    numDirectE++;
                }
            }
        }
        return numDirectE;
    }

    /**
     This method returns the degree of vertex u.
     */
    public int getDegree(String u) {
        Double[][] data = this.adjacency.data;
        int indexRow = contains(u, this.vertexNames);
        if (indexRow==-1) {
            throw new ArithmeticException(u + " not in vList");
        }
        int deg = 0;
        for (int j = 0; j < this.numVertices; j++) {
            if (!data[indexRow][j].equals(Double.POSITIVE_INFINITY)) {
                deg++;
            }
        }
        return deg;
    }

    /**
     Change the label of the vertex v to newLabel.  The method returns true
     if the label change was successful, and false otherwise.
     */
    public boolean changeLabel(String v, String newLabel) {
        int i = contains(v, this.vertexNames);
        if (i == -1) {
            return false;
        } else {
            Vertex changeFace = this.verticesList[i];
            changeFace.setName(newLabel);
            this.vertexNames[i] = newLabel;
            return true;
        }
    }

    /**
     This method returns a depth first traversal of the graph,
     starting with node v. When there is choice between vertices,
     choose in alphabetical order.

     The list must be separated by commas containing no additional
     white space.
     */
    Integer[] numDFSV;

    public String depthFirstTraversal(String v) {
        //order vertices alphabetically
        String[] sortedVNames = insertionSort(vertexNames);
        numDFSV = new Integer[this.vertexNames.length];

        //first traverse according to v and then
        String out = "";
        out += DFS(this.verticesList[contains(v, vertexNames)], out);
        //traverse according to while
        int i;
        while ((i=numVisZero())!=-1) {
            out += DFS(this.verticesList[contains(sortedVNames[i], vertexNames)], out);
        }
        return out;
    }

    private String DFS(Vertex v, String out) {
        out += v.getName()+ " ";
        numDFSV[contains(v.getName(), vertexNames)]=0;
        for (Edge e: v.getAdjacenciesList()) {
            if (e!=null){
                if (numDFSV[contains(e.getStartVertex().getName(),vertexNames)]==null) {
                    DFS(e.getEndVertex(), out);
                }
            }
        }
        return out;
    }

    private int numVisZero(){
        for (int i = 0; i < numDFSV.length; i++) {
            if (numDFSV[i]==null) {
                return i;
            }
        }
        return -1;
    }

    private String[] insertionSort(String[] vNames) {
        String[] sortedNames = vNames;
        for (int i=1,j; i < sortedNames.length; i++) {
            String temp = sortedNames[i];
            for (j = i; j > 0 && temp.compareTo(sortedNames[j-1])<0 ; j--) {
                sortedNames[j] = sortedNames[j-1];
            }
            sortedNames[j] = temp;
            }
        return sortedNames;
        }

    //Helpers
    public void printAdjacencyM() {
        adjacency.printAdjacencyM();
    }

    public String[] getVertexNames() {
        return this.vertexNames;
    }

    ////// You must finish all this for Task 1 //////


    ////// You must start here for Task 2 //////


    /**
     This method returns a list of all vertices with odd degrees.
     The vertices should be sorted alphabetically. If there are no
     vertices with odd degrees, return an empty string.

     The list must be separated by commas containing no additional
     white space.
     */
    public String getOdd() {

        return "";
    }

    /**
     This method should return the shortest path between two vertices.
     Inputs are the vertex labels, as read from the input file.

     The returned string should be the vertex labels, starting with u and
     ending with v. The list must be separated by commas containing no additional
     white space.

     Assumption: both vertices are present in the graph, and a path between
     them exists.
     */
    public String getPath(String u, String v) {


        return "";
    }

    /**
     This method should return the cost of the optimal Chinese Postman
     route determined by your algorithm.
     */
    public int getChinesePostmanCost() {


        return 0;
    }

    /**
     This method should return your graph with the extra edges as constructed
     during the optimal Chinese postman route calculation.
     */
    public Graph getChinesePostmanGraph() {


        return null;
    }


    ////// You must finish all this for Task  //////


    ////// You must start here for Task 3 //////


    /**
     This method should return the circular optimal Chinese postman path from v
     back to v. If there are vertices with odd degrees, return "not available".
     Otherwise, return a list of vertices starting and ending in v.
     When there are alternative paths, choose the next vertex in alphabetical order.

     The list must be separated by commas containing no additional
     white space.
     */
    public String getChinesePostmanRoute(String v) {


        return "not available";
    }

}
