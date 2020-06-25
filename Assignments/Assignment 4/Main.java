import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Name and Surname: BL Nortier
 * Student/staff Number: u17091820
 */


public class Main {

    private static boolean verbose = false;

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
        /*System.out.println("See how the labels changed");
        g.printAdjacencyM();
        System.out.println("The Original before changing");
        clone.printAdjacencyM();*/

        //reset for later tests
        g.changeLabel("a", "A");
        g.changeLabel("b", "B");
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
        if (numberOddE.length() == 0) {
            System.out.println("No odd edges");
        } else {
            System.out.println("Odd edges as follows: ");
            System.out.println(g.getOdd() + "x");
        }
    }

    public static void testShortestPath(Graph g, int i) {
        switch (i) {
            case 1:
                System.out.println("Shortest Path between A and B");
                System.out.println(g.getPath("A", "B"));
                System.out.println("Length: " + g.getShortestDistance("A", "B"));
                break;
            case 2:
                System.out.println("Shortest Path between A and E");
                System.out.println(g.getPath("A", "E"));
                System.out.println("Length: " + g.getShortestDistance("A", "E"));
                break;
            case 3:
                System.out.println("Shortest Path between A and K");
                System.out.println(g.getPath("A", "K"));
                System.out.println("Length: " + g.getShortestDistance("A", "K"));
                break;
            case 4:
                System.out.println("Shortest Path between A and F");
                System.out.println(g.getPath("A", "F"));
                System.out.println("Length: " + g.getShortestDistance("A", "F"));
                break;
            case 5:
                System.out.println("Shortest Path between A and D");
                System.out.println(g.getPath("A", "D"));
                System.out.println("Length: " + g.getShortestDistance("A", "D"));
                break;
            case 6:
                System.out.println("Shortest Path between C and B");
                System.out.println(g.getPath("C", "B"));
                System.out.println("Length: " + g.getShortestDistance("C", "B"));
                break;
            case 7:
                System.out.println("Shortest Path between D and B");
                System.out.println(g.getPath("D", "B"));
                System.out.println("Length: " + g.getShortestDistance("D", "B"));
                break;
            case 8:
                System.out.println("Shortest Path between F and A");
                System.out.println(g.getPath("F", "A"));
                System.out.println("Length: " + g.getShortestDistance("F", "A"));
                System.out.println("Similarly: A and F");
                System.out.println(g.getPath("A", "F"));
                System.out.println("Length: " + g.getShortestDistance("A", "F"));
                break;
            case 9:
                System.out.println("Shortest Path between adam and peggy");
                System.out.println(g.getPath("adam", "peggy"));
                System.out.println("Length: " + g.getShortestDistance("adam", "peggy"));
                break;
            case 10:
                System.out.println("Shortest Path between Jippo and EggsandToast");
                System.out.println(g.getPath("Jippo", "EggsandToast"));
                System.out.println("Length: " + g.getShortestDistance("Jippo", "EggsandToast"));
                break;
        }
    }

    public static void testEdgePrintings(Graph g) {
        System.out.println("Using edgeList");
        g.printEdgesUsingList();
        System.out.println("Using Adjacencies");
        g.printEdgesUsingAdjacencies();

        System.out.println("\nCLONE GRAPH");
        Graph clone = g.clone();
        System.out.println("Using edgeList");
        clone.printEdgesUsingList();
        System.out.println("Using Adjacencies");
        clone.printEdgesUsingAdjacencies();
    }

    public static void testPostmanPaths(Graph g, int i) {
        System.out.println("\nGETTING POSTMANGRAPH");
        Graph adjustedG = g.getChinesePostmanGraph();
        System.out.println(g.getChinesePostmanCost());
        System.out.println();
        if (i==1) {
            System.out.println(adjustedG.getChinesePostmanRoute("A"));
        }
        else if (i > 8) {
            switch (i) {
                case 9:
                    System.out.println(adjustedG.getChinesePostmanRoute("ben"));
                    break;
                case 10:
                    System.out.println(adjustedG.getChinesePostmanRoute("Easy"));
                    break;
            }
        } else {
            System.out.println(adjustedG.getChinesePostmanRoute("B"));
        }
    }

    public static void main(String[] args) throws IOException {
        /*PrintStream out = new PrintStream(new FileOutputStream("/home/jo/IdeaProjects/Ass4/graphs/outputB.txt"));
        System.setOut(out);*/
        System.out.println("On the road!");
        String[] filenames = {"/home/jo/IdeaProjects/Ass4/graphs/graph.txt",
                "/home/jo/IdeaProjects/Ass4/graphs/graph2.txt",
                "/home/jo/IdeaProjects/Ass4/graphs/graph3.txt",
                "/home/jo/IdeaProjects/Ass4/graphs/graph4.txt",
                "/home/jo/IdeaProjects/Ass4/graphs/graph5.txt",
                "/home/jo/IdeaProjects/Ass4/graphs/graph6.txt",
                "/home/jo/IdeaProjects/Ass4/graphs/graph7.txt",
                "/home/jo/IdeaProjects/Ass4/graphs/graph8.txt",
                "/home/jo/IdeaProjects/Ass4/graphs/graph9.txt",
                "/home/jo/IdeaProjects/Ass4/graphs/graph10.txt"};

        Graph g = new Graph(filenames[0]);

        for (int i = 1; i<2/*i < filenames.length + 1*/; i++) {
            System.out.println("=======NEW SKETCH " + i + "============");
            //g.printAdjacencyM();
            //g.printNextM();
            testShortestPath(g, i);
            //testClone(g);
            theDegree(g);
            oddOrEven(g);
            numberEdges(g);
            deepDive(g);
            testPostmanPaths(g, i);
            //System.out.println(g.getTotalGraphWeight());
            System.out.println("Chinese postman cost: " + g.getChinesePostmanCost());
            if (i != filenames.length) {
                g = newConstructions(g, filenames[i], verbose);
            }
        }
    }
}