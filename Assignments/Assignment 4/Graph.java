/**
 * Name and Surname: BL Nortier
 * Student/staff Number: 17091820
 */

import java.io.*;

/**
 * 1. You may not change the signatures of any of the given methods.  You may
 * however add any additional methods and/or fields which you may require to aid
 * you in the completion of this assignment.
 * <p>
 * 2. You will have to design and implement your own Graph class in terms of
 * graph representation.
 * <p>
 * 3. You may add any additional classes and/or methods to your assignment.
 */
public class Graph {
    private Integer numV;
    private Integer numE = 0;
    private Vertex[] verticesList;
    private String[] vertexNames;
    public Matrix adjacency;
    private Integer finalWeight = Integer.MIN_VALUE;

    /**
     * The constructor receives the name of the file from which a graph
     * is read and constructed.
     */

    public Graph(String f) throws IOException {

        String[][] lines = readFile(f);
        this.verticesList = new Vertex[this.numV];
        this.vertexNames = new String[this.numV];
        if (lines != null) {
            constructGraph(lines);
            this.adjacency = new Matrix(numV, numE, verticesList);
        }

    }

    private Graph(int numV, int numE, Vertex[] vList, String vNames[], Matrix adj) {
        this.numV = numV;
        this.numE = numE;
        this.verticesList = vList;
        this.vertexNames = vNames;
        this.adjacency = adj;
    }

    /**
     * @param lines Given the lines read from the file as 2D array, construct all vertices and all edges
     */
    public void constructGraph(String[][] lines) {
        int insertedVs = 0;
        //create and initialise alphabetic list
        String[] sorted = new String[this.numV];
        for (int i = 0; i < this.numE && insertedVs < this.numV; i++) {
            if (contains(lines[i][0], sorted) == -1) {
                sorted[insertedVs++] = lines[i][0];
            }
            if (contains(lines[i][1], sorted) == -1) {
                sorted[insertedVs++] = lines[i][1];
            }
        }
        sorted = insertionSort(sorted);

        //construct graph
        insertedVs = 0;
        for (int i = 0; i < this.numV; i++) {
            this.vertexNames[insertedVs] = sorted[insertedVs];
            this.verticesList[insertedVs] = new Vertex(sorted[insertedVs++]);
        }

        for (int i = 0; i < this.numE; i++) {
            //add edge
            Vertex start = this.verticesList[contains(lines[i][0], this.vertexNames)];
            Vertex end = this.verticesList[contains(lines[i][1], this.vertexNames)];
            Integer weight = Integer.valueOf(lines[i][2]);
            start.addNeighbour(new Edge(start, end, weight));
            //end.addNeighbour(new Edge(end,start, weight)); //since undirected  //NO! CANNOT EDGE
        }
    }

    /**
     * @param name Check if the vertex is already contained in the list. If yes, return the index at which it is stored.
     *             If not, return -1 indicating that it's not in the list
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
            this.numV = Integer.valueOf(br.readLine());
            String st;
            String[][] lines = new String[10000][3];
            i = 0;
            while ((st = br.readLine()) != null && st != "") {
                String[] temp = st.split(",");
                for (int j = 0; j < 3; j++) {
                    lines[i][j] = temp[j];
                }
                this.numE++;
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
            this.numE = -1;
            return null;
        }
    }


    /**
     * The clone method should return a deep copy/clone
     * of this graph.
     */
    public Graph clone() {
        int numV = this.numV;
        int numE = this.numE;
        Vertex[] vList = new Vertex[verticesList.length];
        String[] vNames = new String[vertexNames.length];
        int[][] data = this.adjacency.getData();

        int insertedVs = 0;
        for (int start = 0; start < vNames.length; start++) {
            for (int end = 0; end < vNames.length; end++) {
                //no important infor here
                if (data[start][end] == Integer.MAX_VALUE) continue;
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
                    Vertex startV = vList[contains(this.vertexNames[start], vNames)];
                    Vertex endV = vList[contains(this.vertexNames[end], vNames)];
                    Integer w = data[start][end];
                    startV.addNeighbour(new Edge(startV, endV, w));
                    endV.addNeighbour(new Edge(endV, startV, w)); //Not necessary since data is symmetric
                    //Otherwise adding Integer
                }
            }
        }

        Matrix adj = new Matrix(numV, numE, vList);
        Graph clone = new Graph(numV, numE, vList, vNames, adj);

        return clone;
    }

    /**
     * This method should discard the current graph and construct a new
     * graph contained in the file named "fileName". Return true if reconstruction
     * was successful.
     */
    public boolean reconstructGraph(String fileName) throws IOException {
        //initialise all to null
        this.adjacency = null;
        this.numV = 0;
        this.numE = 0;
        this.verticesList = null;
        this.vertexNames = null;

        //start over
        String[][] lines = readFile(fileName);
        this.verticesList = new Vertex[this.numV]; //overwrites
        this.vertexNames = new String[this.numV];
        if (lines != null) {
            constructGraph(lines);
            this.adjacency = new Matrix(numV, numE, verticesList);
            return true;
        } else {
            return false;
        }

    }

    /**
     * This method returns the number of direct edges between u and v vertices.
     * If there are no direct edges, return 0.
     * In there is 1 or more direct edges, return the number of edges.
     */
    public int numEdges(String u, String v) {
        int numDirectE = 0;
        //check start
        int index = this.contains(u, vertexNames);
        if (index != -1) {
            Vertex vertexU = this.verticesList[this.contains(u, vertexNames)];
            Edge[] adjList = vertexU.getAdjacenciesList();
            for (int i = 0; i < vertexU.getAdjacenciesCount(); i++) {
                Edge e = adjList[i];
                if (e != null && e.getEndVertex().getName().equals(v)) {
                    numDirectE++;
                }
            }
        }
        //check end
        index = this.contains(v, vertexNames);
        if (index != -1) {
            Vertex vertexV = this.verticesList[this.contains(v, vertexNames)];
            Edge[] adjList = vertexV.getAdjacenciesList();
            for (int i = 0; i < vertexV.getAdjacenciesCount(); i++) {
                Edge e = adjList[i];
                if (e != null && e.getEndVertex().getName().equals(v)) {
                    numDirectE++;
                }
            }
        }
        return numDirectE;
    }

    /**
     * This method returns the degree of vertex u.
     */
    public int getDegree(String u) {
        /*int indexRow = contains(u, this.vertexNames);
        if (indexRow == -1) {
            throw new ArithmeticException(u + " not in vList");
        }

        Vertex v = this.verticesList[indexRow];
        return v.getAdjacenciesCount();*/
        int deg = 0;
        for (int i = 0; i < this.vertexNames.length; i++) {
            if (u.equals(this.vertexNames[i])) continue;
            else {
                Edge e = findEdgeBetween(u, this.vertexNames[i]);
                if (e!=null) deg++;
            }
        }
        return deg;
        /*int[][] data = this.adjacency.getData();
        int indexRow = contains(u, this.vertexNames);
        if (indexRow==-1) {
            throw new ArithmeticException(u + " not in vList");
        }
        int deg = 0;
        for (int j = 0; j < this.numV; j++) {
            if (data[indexRow][j]!=Double.POSITIVE_INFINITY && data[indexRow][j]!=(0)) {
                deg++;
            }
        }
        return deg;*/
    }

    private Edge findEdgeBetween(String one, String two) {
        Vertex v = this.verticesList[contains(one, this.vertexNames)];
        Vertex u = this.verticesList[contains(two, this.vertexNames)];
        //one side
        Edge[] adj = v.getAdjacenciesList();
        for (int i = 0; i < v.getAdjacenciesCount(); i++) {
            if (adj[i].getEndVertex().getName().equals(u.getName())) {
                return adj[i];
            }
        }
        //second side
        adj = u.getAdjacenciesList();
        for (int i = 0; i < u.getAdjacenciesCount(); i++) {
            if (adj[i].getEndVertex().getName().equals(v.getName())) {
                return adj[i];
            }
        }
        return null;
    }

    /**
     * Change the label of the vertex v to newLabel.  The method returns true
     * if the label change was successful, and false otherwise.
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
     * This method returns a depth first traversal of the graph,
     * starting with node v. When there is choice between vertices,
     * choose in alphabetical order.
     * <p>
     * The list must be separated by commas containing no additional
     * white space.
     */
    Integer[] numDFSV;

    public String depthFirstTraversal(String v) {
        //order vertices alphabetically
        numDFSV = new Integer[this.vertexNames.length];

        //first traverse according to v and then
        String out = "";
        out += DFS(this.verticesList[contains(v, vertexNames)], out);
        //traverse according to while
        int i;
        while ((i = numVisZero()) != -1) {
            out += DFS(this.verticesList[contains(this.vertexNames[i], vertexNames)], out);
        }
        return out.substring(0, out.length() - 1);
    }

    private String DFS(Vertex v, String out) {
        out += v.getName() + " ";
        numDFSV[contains(v.getName(), vertexNames)] = 0;
        Edge[] adjList = v.getAdjacenciesList();
        //loop through every vertex
        for (int i = 0; i < this.vertexNames.length; i++) {
            String currV = this.vertexNames[i];
            for (int j = 0; j < v.getAdjacenciesCount(); j++) {
                if (numDFSV[contains(adjList[j].getEndVertex().getName(), this.vertexNames)] == null && adjList[j].getEndVertex().getName().equals(currV)) {
                    out = DFS(adjList[j].getEndVertex(), out);
                }
            }
        }
        return out;
    }

    private int numVisZero() {
        for (int i = 0; i < numDFSV.length; i++) {
            if (numDFSV[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private String[] insertionSort(String[] vNames) {
        String[] sortedNames = vNames;
        for (int i = 1, j; i < sortedNames.length; i++) {
            String temp = sortedNames[i];
            for (j = i; j > 0 && temp.compareTo(sortedNames[j - 1]) < 0; j--) {
                sortedNames[j] = sortedNames[j - 1];
            }
            sortedNames[j] = temp;
        }
        return sortedNames;
    }

    //Helpers
    public void printAdjacencyM() {
        adjacency.printAdjacencyM();
    }

    public void printNextM() {
        adjacency.printNext();
    }

    public String[] getVertexNames() {
        return this.vertexNames;
    }

    ////// You must finish all this for Task 1 //////


    ////// You must start here for Task 2 //////


    /**
     * This method returns a list of all vertices with odd degrees.
     * The vertices should be sorted alphabetically. If there are no
     * vertices with odd degrees, return an empty string.
     * <p>
     * The list must be separated by commas containing no additional
     * white space.
     */
    public String getOdd() {
        String out = "";
        for (String v : this.vertexNames) {
            if (getDegree(v) % 2 == 1) {
                out += v + ",";
            }
        }
        if (out.length() > 0) {
            out = out.substring(0, out.length() - 1);
        }
        return out;
    }

    /**
     * This method should return the shortest path between two vertices.
     * Inputs are the vertex labels, as read from the input file.
     * <p>
     * The returned string should be the vertex labels, starting with u and
     * ending with v. The list must be separated by commas containing no additional
     * white space.
     * <p>
     * Assumption: both vertices are present in the graph, and a path between
     * them exists.
     */
    public String getPath(String u, String v) {
        String out = u;
        int start = contains(u, this.vertexNames);
        int end = contains(v, this.vertexNames);
        //no need to check if not present
        Vertex temp = this.adjacency.next[start][end];
        if (temp == null) temp = this.adjacency.next[end][start];
        out += "," + temp.getName();
/*        if (this.adjacency.next[start][end]!=null) {
            out += "," + this.adjacency.next[start][end].getName();
        }
        else {
            out += "," + this.adjacency.next[end][start].getName();
        }*/

        while (!temp.getName().equals(v)) {
            start = contains(temp.getName(), this.vertexNames);
            temp = this.adjacency.next[start][end];
            if (temp == null) temp = this.adjacency.next[end][start];
            out += "," + this.adjacency.next[start][end].getName();
        }

        return out;
    }

    public Integer getShortestDistance(String u, String v) {
        int start = contains(u, this.vertexNames);
        int end = contains(v, this.vertexNames);
        return this.adjacency.getData()[start][end];
    }


    /**
     * This method should return the cost of the optimal Chinese Postman
     * route determined by your algorithm.
     */
    public int getChinesePostmanCost() {
        if (this.finalWeight.equals(Integer.MIN_VALUE)) {
            this.getChinesePostmanGraph();
        }
        return this.finalWeight;
    }

    /**
     * This method should return your graph with the extra edges as constructed
     * during the optimal Chinese postman route calculation.
     */
    public Graph getChinesePostmanGraph() {
        String[] oddEdges = getOdd().split(",");
        String oddE = String.join("", oddEdges);
        if (oddEdges.equals("")) {
            return this;
        } else {
            Graph clone = this.clone();
            //get total weight (since I've constructed digraph with two directions every time)
            this.finalWeight = getTotalGraphWeight();
            //create array of pairings
            int n_P_n = factorial(oddEdges.length);
            String[][] weightsAndPairings = new String[n_P_n][oddEdges.length / 2 + 1];
            int minIndex = pairUpVertices(weightsAndPairings, oddEdges);
            String[] addEdges = weightsAndPairings[minIndex];
            //add edges
            for (int i = 0; i < addEdges.length - 1; i++) { //to exclude the weight in last column
                String[] addOns = getPath(addEdges[i].substring(0, 1), addEdges[i].substring(1, 2)).split(",");
                for (int k = 0; k < addOns.length - 1; k++) { //so can do pairs
                    Vertex start = clone.verticesList[contains(addOns[k], clone.vertexNames)];
                    Vertex end = clone.verticesList[contains(addOns[k + 1], clone.vertexNames)];
                    Integer weight = findEdgeWeight(start, end);
                    start.addNeighbour(new Edge(start, end, weight));
                    System.out.println("adding " + start.getName() + "-" + end.getName() );
                }
            }
            System.out.println("Start weight= " + finalWeight);
            System.out.println("Add weight = " + Integer.valueOf(Integer.valueOf(weightsAndPairings[minIndex][oddEdges.length / 2]))) ;
            this.finalWeight += Integer.valueOf(Integer.valueOf(weightsAndPairings[minIndex][oddEdges.length / 2]));
            System.out.println("New weight: " + finalWeight);
            clone.adjacency = new Matrix(clone.numV, clone.numE, clone.verticesList);
            return clone;
        }
    }

    private Integer findEdgeWeight(Vertex u, Vertex v) {
        //one side
        Edge[] adj = v.getAdjacenciesList();
        for (int i = 0; i < v.getAdjacenciesCount(); i++) {
            if (adj[i].getEndVertex().getName().equals(u.getName())) {  //changed v.getName() to u.getName()
                return adj[i].getWeight();
            }
        }
        //second side
        adj = u.getAdjacenciesList();
        for (int i = 0; i < u.getAdjacenciesCount(); i++) {
            if (adj[i].getEndVertex().getName().equals(v.getName())) {  //changed u.getName() to v.getName()
                return adj[i].getWeight();
            }
        }
        return getShortestDistance(u.getName(), v.getName());
    }

    private int pairUpVertices(String[][] weightsAndPairings, String[] oddEdges) {
        //int n_P_n = factorial(oddEdges.length);
        //String[][] weightsAndPairings = new String[n_P_n][oddEdges.length/2+1];
        permute(weightsAndPairings, String.join("", oddEdges), "");
        int minWeightIndex = 0;
        for (int i = 0; i < count; i++) {
            String[] bits = weightsAndPairings[i][0].split("(?<=\\G.{2})");
            Integer weight = 0;
            for (int j = 0; j < bits.length; j++) {
                weightsAndPairings[i][j] = bits[j];
                weight += getShortestDistance(bits[j].substring(0, 1), bits[j].substring(1, 2));
            }
            weightsAndPairings[i][bits.length] = String.valueOf(weight);
            if (Integer.valueOf(weightsAndPairings[i][bits.length]) < Integer.valueOf(weightsAndPairings[minWeightIndex][bits.length])) {
                //check alphabetically? No, since I order my vertices alphabetically at the start
                minWeightIndex = i;
            }
        }
        //return weightsAndPairings;
        return minWeightIndex;
    }

    Integer count = 0;

    public void permute(String[][] weightsAndPairings, String str, String ans) {
        // If string is empty
        if (str.length() == 0) {
            weightsAndPairings[count++][0] = ans;
            return;
        }

        for (int i = 0; i < str.length(); i++) {

            // ith character of str
            char ch = str.charAt(i);

            // Rest of the string after excluding
            // the ith character
            String ros = str.substring(0, i) +
                    str.substring(i + 1);

            // Recursive call
            permute(weightsAndPairings, ros, ans + ch);
        }
    }

    private int factorial(int n) {
        int fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    public int getTotalGraphWeight() {
        int cost = 0;
        for (int i = 0; i < this.vertexNames.length; i++) {
            Vertex curr = this.verticesList[i];
            Edge[] adj = curr.getAdjacenciesList();
            for (int j = 0; j < curr.getAdjacenciesCount(); j++) {
                cost += adj[j].getWeight();
            }
        }
        return cost;

    }


    ////// You must finish all this for Task 2 //////


    ////// You must start here for Task 3 //////

    /**
     * This method should return the circular optimal Chinese postman path from v
     * back to v. If there are vertices with odd degrees, return "not available".
     * Otherwise, return a list of vertices starting and ending in v.
     * When there are alternative paths, choose the next vertex in alphabetical order.
     * <p>
     * The list must be separated by commas containing no additional
     * white space.
     */
    public String getChinesePostmanRoute(String v) {
        if (getOdd().equals("")) {
            return "not available";
        }
        return null;

    }
}