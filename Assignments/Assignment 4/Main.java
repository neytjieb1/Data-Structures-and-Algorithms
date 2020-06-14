import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Name and Surname: BL Nortier
 * Student/staff Number: u17091820
 */


public class Main {

    public static int numInserts = 100;
    public static int numChecks = 100;
    public static int seed = 123456;

    public static void main(String[] args) throws IOException {
        /*PrintStream out = new PrintStream(new FileOutputStream("/home/jo/IdeaProjects/Practical 8/outputB.txt"));
        System.setOut(out);*/
        System.out.println("Initialise graph");
        Graph g = new Graph("/home/jo/IdeaProjects/Ass4/graphs/graph.txt");
        g.printAdjacencyM();

        System.out.println("Cloning graph1");
        Graph clone = g.clone();
        clone.printAdjacencyM();


        /*//Quick Test of changeLabel
        String[] currNames = {"A", "B", "E"};
        String[] newNames = {"a","b","e"};
        for (int i = 0; i < newNames.length; i++) {
            if (g.changeLabel(currNames[i], newNames[i])) {
                System.out.println("Success: " + currNames[i] + " " + newNames[i]);
            }
            else {
                System.out.println("Failure for " + currNames[i] + " " + newNames[i]);
            }
        }
        */

        /*//Test Clone after a change to the matrix
        System.out.println("Changed Labels");
        g.printAdjacencyM();
        System.out.println("Original graph as cloned before changes");
        clone.printAdjacencyM();
        */

        /*System.out.println("Reconstruct: Graph 2");
        g.reconstructGraph("/home/jo/IdeaProjects/Ass4/graphs/graph2.txt");
        g.printAdjacencyM();

        System.out.println("Between B and D: " + g.numEdges("B", "D")); //invalid input

        System.out.println("Cloning graph 2");
        clone = g.clone();
        clone.printAdjacencyM();*/


        System.out.println("Between B and D: " + g.numEdges("B", "D")); //invalid input

/*        System.out.println("\nTesting getDegree Quickly");
        System.out.println("A: " + g.getDegree("A"));
        System.out.println("B: " + g.getDegree("B"));
        System.out.println("C: " + g.getDegree("C"));
        System.out.println("D: " + g.getDegree("D"));*/


        System.out.println("\nTesting Num Edges Quickly");
        System.out.println("Between B and D: " + g.numEdges("B", "D")); //invalid input
        System.out.println("Between A and E: " + g.numEdges("A", "E"));
        System.out.println("Between E and A: " + g.numEdges("E", "A"));

        // DFS
        Integer[] = g.depthFirstTraversal("A");



    }
}