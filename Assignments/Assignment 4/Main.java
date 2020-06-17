import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * Name and Surname: BL Nortier
 * Student/staff Number: u17091820
 */


public class Main {

    private static boolean verbose=false;

    public static void testClone(Graph g) {
        System.out.println("Cloning Graph");
        Graph clone = g.clone();

        //Quick Test of changeLabel
        String[] currNames = {"A", "B", "Z"};
        String[] newNames = {"a", "b", "f"};
        for (int i = 0; i < newNames.length; i++) {
            if (g.changeLabel(currNames[i], newNames[i])) {
                System.out.println("Success: " + currNames[i] + " " + newNames[i]);
            } else {
                System.out.println("Failure for " + currNames[i] + " " + newNames[i]);
            }
        }
        //Test Clone after a change to the matrix
        System.out.println("See how the labels changed");
        g.printAdjacencyM();
        System.out.println("The Original before changing");
        clone.printAdjacencyM();
    }

    public static Graph newConstructions(Graph g, String filepath, boolean verbose) throws IOException {
        if (verbose) {
            System.out.println("We reconstruct");
            g.printAdjacencyM();
        }
        g.reconstructGraph(filepath);
        return g;
    }

    public static void theDegree(Graph g) {
        System.out.println("\nFirst, it's getDegree");
        String[] names = g.getVertexNames();
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i] + ": " + g.getDegree(names[i]));
        }
    }

    public static void numberEdges(Graph g) {
        System.out.println("\nand then numEdges (quickly)");
        String[] names = g.getVertexNames();
        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < names.length; j++) {
                int numE = g.numEdges(names[i], names[j]);
                if (numE != 0) {
                    System.out.println("Between " + names[i] + " and " + names[j] + ": " + numE);
                }
            }
        }
    }

    public static void deepDive(Graph g) {
        // DFS
        String[] names = g.getVertexNames();
        System.out.println("\nNow we're trying DFS");
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i] + ": " + g.depthFirstTraversal(names[i]));
        }
    }

    public static void oddOrEven(Graph g) {
        //getOdd
        String numberOddE = g.getOdd();
        if (numberOddE.length()==0) {
            System.out.println("No odd edges");
        }
        else {
            System.out.println("Odd edges as follows: ");
            System.out.println(g.getOdd() + "x");
        }
    }

    public static void testShortestPath(Graph g, int i) {
        switch (i) {
            case 1:
                System.out.println("Shortest Path between B and C");
                System.out.println(g.getPath("B","C"));
                System.out.println("Length: " + g.getShortestDistance("B","C"));
                break;
            case 2:
                System.out.println("Shortest Path between A and E");
                System.out.println(g.getPath("A","E"));
                System.out.println("Length: " + g.getShortestDistance("A","E"));
                break;
            case 3:
                System.out.println("Shortest Path between A and K");
                System.out.println(g.getPath("A","K"));
                System.out.println("Length: " + g.getShortestDistance("A","K"));
                break;
            case 4:
                System.out.println("Shortest Path between A and F");
                System.out.println(g.getPath("A","F"));
                System.out.println("Length: " + g.getShortestDistance("A","F"));
                break;
            case 5:
                System.out.println("Shortest Path between A and D");
                System.out.println(g.getPath("A","D"));
                System.out.println("Length: " + g.getShortestDistance("A","D"));
                break;
            case 6:
                System.out.println("Shortest Path between C and B");
                System.out.println(g.getPath("C","B"));
                System.out.println("Length: " + g.getShortestDistance("C","B"));
                break;
            case 7:
                System.out.println("Shortest Path between D and B");
                System.out.println(g.getPath("D","B"));
                System.out.println("Length: " + g.getShortestDistance("D","B"));
                break;
            case 8:
                System.out.println("Shortest Path between F and A");
                System.out.println(g.getPath("F","A"));
                System.out.println("Length: " + g.getShortestDistance("F","A"));
                System.out.println("Similarly: A and F");
                System.out.println(g.getPath("A","F"));
                System.out.println("Length: " + g.getShortestDistance("A","F"));
                break;
        }
    }

    public static void main(String[] args) throws IOException {
        /*PrintStream out = new PrintStream(new FileOutputStream("/home/jo/IdeaProjects/Ass4/outputB.txt"));
        System.setOut(out);*/
        System.out.println("On the road!");
        String[] filenames = {"/home/jo/IdeaProjects/Ass4/graphs/graph.txt",
                            "/home/jo/IdeaProjects/Ass4/graphs/graph2.txt",
                            "/home/jo/IdeaProjects/Ass4/graphs/graph3.txt",
                            "/home/jo/IdeaProjects/Ass4/graphs/graph4.txt",
                            "/home/jo/IdeaProjects/Ass4/graphs/graph5.txt",
                            "/home/jo/IdeaProjects/Ass4/graphs/graph6.txt",
                            "/home/jo/IdeaProjects/Ass4/graphs/graph7.txt",
                            "/home/jo/IdeaProjects/Ass4/graphs/graph8.txt"};
        Graph g = new Graph(filenames[5]);

        /*for (int i = 1; i < filenames.length+1; i++) {
            System.out.println("=======NEW SKETCH " + i + "============");
            g.printAdjacencyM();
            g.printNextM();
            testShortestPath(g, i);
            testClone(g);
            theDegree(g);
            oddOrEven(g);
            numberEdges(g);
            deepDive(g);
            System.out.println(g.getTotalGraphWeight());
            if (i!=filenames.length) {
                g = newConstructions(g, filenames[i], verbose);
            }
        }*/


        Graph adjustedg = g.getChinesePostmanGraph();
        adjustedg.printAdjacencyM();
        adjustedg.printNextM();
        System.out.println(g.getChinesePostmanCost());


    }
}