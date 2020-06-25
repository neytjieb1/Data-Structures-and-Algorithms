/**
 * Name and Surname: BL Nortier
 * Student/staff Number: 17091820
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    private Integer numV = 0;
    private Integer numE = 0;
    private Vertex[] verticesList = null;
    private String[] vertexNames = null;
    private Edge[] edgeList = null;
    public Matrix adjacency = null;
    private Integer finalPostmanWeight = Integer.MIN_VALUE;

    //naughty global variables
    Integer count = 0;
    Integer[] numDFSV = null;

    /**
     * The constructor receives the name of the file from which a graph
     * is read and constructed.
     */

    public Graph(String f) {
        String[][] lines = readFile(f);

        if (lines != null) {
            this.verticesList = new Vertex[this.numV];
            this.vertexNames = new String[this.numV];
            this.edgeList = new Edge[(numE * (numE+1))]; // #edges in complete graph, which is max it will ever take
            constructGraph(lines);
            this.adjacency = new Matrix(numV, numE, verticesList);
        }
        //everything will be empty

    }

    private Graph(int numV, int numE, Vertex[] vList, String vNames[], Edge[] eList, Matrix adj, int finWeight) {
        this.numV = numV;
        this.numE = numE;
        this.verticesList = vList;
        this.vertexNames = vNames;
        this.edgeList = eList;
        this.adjacency = adj;
        this.finalPostmanWeight = finWeight;
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
        int insertedEs = 0;
        for (int i = 0; i < this.numE; i++) {
            //add edge
            Vertex start = this.verticesList[contains(lines[i][0], this.vertexNames)];
            Vertex end = this.verticesList[contains(lines[i][1], this.vertexNames)];
            Integer weight = Integer.valueOf(lines[i][2]);
            edgeList[insertedEs] = new Edge(start, end, weight);
            start.addNeighbour(edgeList[insertedEs++]);

        }
    }

    /**
     * @param name Check if the vertex is already contained in the list. If yes, return the index at which it is stored.
     *             If not, return -1 indicating that it's not in the list
     */
    private int contains(String name, String[] vNames) {
        for (int i = 0; i < vNames.length; i++) {
            String tempName;
            if (vNames[i]!=null) tempName = vNames[i].strip();
            else tempName = null;
            if (tempName != null && (tempName.equals(name) || vNames[i].equals(name))) {
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
    public String[][] readFile(String filename) {
        try {
            int i;
            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            //read first line
            String st = br.readLine();
            if (st == null || st.equals("")) {
                return null;
            }
            this.numV = Integer.valueOf(st);
            String[][] lines = new String[10000][3];
            i = 0;
            while ((st = br.readLine()) != null /* && !st.equals("")*/) {
                if (!st.equals("")) {
                    String[] temp = st.split(",");
                    for (int j = 0; j < 3; j++) {
                        lines[i][j] = temp[j];
                    }
                    this.numE++;
                    i++;
                }
            }
            return lines;

        } catch (Exception e) {
            return null;
        }
    }


    /**
     * The clone method should return a deep copy/clone
     * of this graph.
     */
    public Graph clone() {
        //naughty global variables
        int numV = this.numV;
        int numE = this.numE;
        Vertex[] vList = new Vertex[verticesList.length];
        String[] vNames = new String[vertexNames.length];
        Edge[] eList = new Edge[this.edgeList.length];
        Matrix adj = null;
        int finWeightClone = this.finalPostmanWeight;

        for (int i = 0; i < this.verticesList.length; i++) {
            vNames[i] = this.vertexNames[i];
            vList[i] = new Vertex(this.verticesList[i].getName());
        }
        int insertedEs = 0;
        for (int i = 0; i < this.vertexNames.length; i++) {
            Vertex oldV = this.verticesList[i];
            for (int j = 0; j < oldV.getAdjacenciesCount(); j++) {
                Edge oldE = oldV.getAdjacenciesList()[j];
                Vertex start = vList[contains(oldE.getStartVertex().getName(), vNames)];
                Vertex end = vList[contains(oldE.getEndVertex().getName(), vNames)];
                Integer weight = oldE.getWeight();
                Edge newE = new Edge(start, end, weight);
                vList[i].addNeighbour(newE);
                eList[insertedEs++] = newE;
            }
        }
        adj = new Matrix(numV, numE, vList);
        Graph clone = new Graph(numV, numE, vList, vNames, eList, adj, finWeightClone);

        return clone;
    }

    /**
     * This method should discard the current graph and construct a new
     * graph contained in the file named "fileName". Return true if reconstruction
     * was successful.
     */
    public boolean reconstructGraph(String fileName) {
        //start over
        this.numE = 0;
        this.numV = 0;

        //initialise bad ones to null
        this.count = 0;
        this.numDFSV = null;
        this.verticesList = null;
        this.vertexNames = null;
        this.edgeList = null;
        this.adjacency = null;
        this.finalPostmanWeight = Integer.MIN_VALUE;

        String[][] lines = readFile(fileName);
        if (lines == null) {
            return false;
        }

        //initialise good ones to null
        this.verticesList = new Vertex[this.numV]; //overwrites
        this.vertexNames = new String[this.numV];
        this.edgeList = new Edge[(numE * (numE - 1)) / 2];

        constructGraph(lines);
        this.adjacency = new Matrix(numV, numE, verticesList);
        return true;

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
        int deg = 0;
        for (int i = 0; i < numE; i++) {
            Edge e = this.edgeList[i];
            if (e.getStartVertex().getName().equals(u.strip()) || e.getEndVertex().getName().equals(u.strip())) {
                deg++;
            }
            if (e.getEndVertex().getName().equals(u.strip()) && e.getStartVertex().getName().equals(u.strip())) {
                deg++; //ie self-edge
            }
        }
        return deg;
    }

    private Edge findEdgeBetween(String one, String two, boolean checkTraversed) {
        Vertex v = this.verticesList[contains(one, this.vertexNames)];
        Vertex u = this.verticesList[contains(two, this.vertexNames)];
        //one side
        Edge[] adj = v.getAdjacenciesList();
        for (int i = 0; i < v.getAdjacenciesCount(); i++) {
            if (adj[i].getEndVertex().getName().equals(u.getName())) {
                if (checkTraversed) {
                    if (adj[i].traversed) continue;
                    else return adj[i];
                } else return adj[i];
            }
        }
        //second side
        adj = u.getAdjacenciesList();
        for (int i = 0; i < u.getAdjacenciesCount(); i++) {
            if (adj[i].getEndVertex().getName().equals(v.getName())) {
                if (checkTraversed) {
                    if (adj[i].traversed) continue;
                    else return adj[i];
                } else return adj[i];
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


    public String depthFirstTraversal(String v) {
        //order vertices alphabetically
        numDFSV = new Integer[this.vertexNames.length];

        //first traverse according to v and then
        String out = "";
        out += DFS(this.verticesList[contains(v, vertexNames)], out);
        //traverse according to rest
        int i;
        while ((i = numVisZero()) != -1) {
            out += DFS(this.verticesList[contains(this.vertexNames[i], vertexNames)], out);
        }
        return out.substring(0, out.length() - 1);
    }

    private String DFS(Vertex v, String out) {
        out += v.getName() + ",";
        numDFSV[contains(v.getName(), this.vertexNames)] = 0;
        for (int i = 0; i < this.vertexNames.length; i++) {
            if (numVisZero() == -1) return out;
            for (int j = 0; j < this.vertexNames.length; j++) {
                if (numDFSV[contains(this.vertexNames[j], this.vertexNames)] == null && findEdgeBetween(v.getName(), this.vertexNames[j], true) != null) {
                    out = DFS(this.verticesList[j], out);
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
        //find Max
        int max = sortedNames[0].length();
        for (int i = 0; i < sortedNames.length ; i++) {
            if (sortedNames[i].length() > max) max = sortedNames[i].length();
        }
        //add spaces to the rest
        for (int i = 0; i < sortedNames.length; i++) {
            if (sortedNames[i].length()< max) {
                for (int j = sortedNames[i].length(); j < max; j++) {
                    sortedNames[i] = sortedNames[i] + " ";
                }
            }
        }
        return sortedNames;
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
        String out = u.strip();
        int start = contains(u, this.vertexNames);
        int end = contains(v, this.vertexNames);
        //no need to check if not present
        Vertex temp = this.adjacency.next[start][end];
        if (temp == null) temp = this.adjacency.next[end][start];
        out += "," + temp.getName();

        while (!temp.getName().equals(v.strip())) {
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
        if (this.finalPostmanWeight.equals(Integer.MIN_VALUE)) {
            this.finalPostmanWeight = this.getTotalGraphWeight();
        }
        return this.finalPostmanWeight;
    }

    /**
     * This method should return your graph with the extra edges as constructed
     * during the optimal Chinese postman route calculation.
     */
    public Graph getChinesePostmanGraph() {
        int len = this.vertexNames[0].length();
        String[] oddEdges = getOdd().split(",");
        //String oddE = String.join("", oddEdges);
        if (oddEdges[0].equals("")) {
            return this.clone();
        } else {
            Graph clone = this.clone();
            //get total weight (since I've constructed digraph with two directions every time)
            this.finalPostmanWeight = getTotalGraphWeight();
            //create array of pairings
            int n_P_n = factorial(oddEdges.length);
            String[][] weightsAndPairings = new String[n_P_n * n_P_n][oddEdges.length / 2 + 1];
            int minIndex = pairUpVertices(weightsAndPairings, oddEdges);
            String[] addEdges = weightsAndPairings[minIndex];
            //add edges
            for (int i = 0; i < addEdges.length - 1; i++) { //to exclude the weight in last column
                String[] addOns = getPath(addEdges[i].substring(0, len), addEdges[i].substring(len, 2 * len)).split(",");
                for (int k = 0; k < addOns.length - 1; k++) { //so can do pairs
                    Vertex start = clone.verticesList[contains(addOns[k], clone.vertexNames)];
                    Vertex end = clone.verticesList[contains(addOns[k + 1], clone.vertexNames)];
                    Integer weight = findEdgeWeight(start, end);
                    clone.edgeList[clone.numE] = new Edge(start, end, weight);
                    start.addNeighbour(clone.edgeList[clone.numE++]);
                    System.out.println("adding " + start.getName() + "-" + end.getName());
                }
            }
            //recalculate adjacency matrix ?
            System.out.println("Start weight= " + finalPostmanWeight);
            System.out.println("Add weight = " + Integer.valueOf(Integer.valueOf(weightsAndPairings[minIndex][oddEdges.length / 2])));
            this.finalPostmanWeight += Integer.valueOf(Integer.valueOf(weightsAndPairings[minIndex][oddEdges.length / 2]));
            clone.finalPostmanWeight = this.finalPostmanWeight;
            System.out.println("New weight: " + clone.finalPostmanWeight);
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
        int len = this.vertexNames[0].length();
        permute(weightsAndPairings, String.join("", oddEdges), "");
        int minWeightIndex = 0;
        for (int i = 0; i < count; i++) {
            String regex = "(?<=\\G.{" + len * 2 + "})"; //"(?<=\\G.{2})"
            String[] bits = weightsAndPairings[i][0].split(regex);
            Integer weight = 0;
            for (int j = 0; j < bits.length; j++) {
                weightsAndPairings[i][j] = bits[j];
                weight += getShortestDistance(bits[j].substring(0, len), bits[j].substring(len, len * 2));
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


    public void permute(String[][] weightsAndPairings, String str, String ans) {
        int len = this.vertexNames[0].length();
        // If string is empty
        if (str.length() == 0) {
            weightsAndPairings[count++][0] = ans;
            return;
        }

        for (int i = 0; i < str.length() / len; i++) {

            // ith character of str
            String chip = str.substring(len * i, len * i + len);

            // Rest of the string after excluding
            // the ith character
            String ros = str.substring(0, i * len) +
                    str.substring(i * len + len);

            // Recursive call
            permute(weightsAndPairings, ros, ans + chip);
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
     * The list must be separated by commas containing no additional
     * white space.
     */
    public String getChinesePostmanRoute(String v) {
        if (!getOdd().equals("")) {
            return "not available";
        }
        //initialise all edges to untraversed
        for (int i = 0; i < this.numE; i++) {
            this.edgeList[i].traversed = false;
        }

        Vertex start = this.verticesList[contains(v, this.vertexNames)];
        String out = v;

        //For all adjacent vertices
        for (int i = 0; i < this.verticesList.length; i++) {
            Edge e = findEdgeBetween(start.getName(), this.vertexNames[i], true);
            if (e == null) continue;
                // If edge u-v is a valid next edge
            else if (isValidNextEdge(start, this.verticesList[i])) {
                out += "," + this.verticesList[i].getName();
                // This edge is used so remove it now
                e.traversed = true;

                out = getChinesePostmanRoute(this.verticesList[i], out);
            }
        }
        return out;
    }

    private String getChinesePostmanRoute(Vertex u, String out) {
        // Recur for all the vertices adjacent to this vertex
        for (int i = 0; i < this.verticesList.length; i++) {
            Edge e = findEdgeBetween(u.getName(), this.getVertexNames()[i], true);
            if (e == null || e.traversed) continue;
                // If edge u-v is a valid next edge
            else if (isValidNextEdge(u, this.verticesList[i])) {
                out += "," + this.verticesList[i].getName();
                // This edge is used so remove it now
                e.traversed = true;
                out = getChinesePostmanRoute(this.verticesList[i], out);
            }
        }
        return out;
    }

    private boolean isValidNextEdge(Vertex u, Vertex v) {
        //valid if
        // 1) If v is the only adjacent vertex of u
        // ie size of adjacent vertex list is 1
        int count = 0;
        for (int i = 0; i < this.numV; i++) {
            Edge e = findEdgeBetween(u.getName(), this.verticesList[i].getName(), true);
            if (e != null && !e.traversed) count++;
            else continue;
        }
        if (count == 1) {
            return true;
        }

        //Determine if Bridge
        //a) count of vertices reachable from u

        numDFSV = new Integer[this.vertexNames.length];
        String out = DFS(this.verticesList[contains(u.getName(), vertexNames)], "");
        int count1 = out.length();
        Edge e = findEdgeBetween(u.getName(), v.getName(), true);

        //b) Remove edge (u, v) and count again
        e.traversed = true;
        numDFSV = new Integer[this.vertexNames.length];
        out = DFS(this.verticesList[contains(u.getName(), vertexNames)], "");
        int count2 = out.length();
        // 2.c) Add the edge back to the graph
        e.traversed = false;
        //did it change?
        return (count1 > count2) ? false : true;
    }



    //Helpers
    public void printEdgesUsingList() {
        for (int i = 0; i < numE; i++) {
            System.out.println(edgeList[i].getStartVertex() + "-" + edgeList[i].getEndVertex() + " " + edgeList[i].getWeight());
        }
    }

    public void printEdgesUsingAdjacencies() {
        for (int i = 0; i < this.verticesList.length; i++) {
            Edge[] eList = this.verticesList[i].getAdjacenciesList();
            for (int j = 0; j < this.verticesList[i].getAdjacenciesCount(); j++) {
                System.out.println(eList[j].getStartVertex() + "-" + eList[j].getEndVertex() + " " + eList[j].getWeight());
            }
        }
    }

    public void printAdjacencyM() {
        adjacency.printAdjacencyM();
    }

    public void printNextM() {
        adjacency.printNext();
    }

    public String[] getVertexNames() {
        String[] strippedNames = new String[this.vertexNames.length];
        for (int i = 0; i < strippedNames.length; i++) {
            strippedNames[i] = this.vertexNames[i].strip();
        }
        return strippedNames;
    }

}