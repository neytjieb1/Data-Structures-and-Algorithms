import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

public class Main {


    public static void main(String[] args) throws FileNotFoundException {
//        PrintStream out = new PrintStream(new FileOutputStream("/home/jo/IdeaProjects/Practical 6/outputB.txt"));
//        System.setOut(out);

        BTree<Integer> t = new BTree<Integer>(2); // A B-Tree with order 4 (2*m)

        t.insert(20);
        t.insert(10);
        t.insert(30);
        t.insert(50);
        t.insert(40);
        t.insert(60);
        t.insert(90);
        t.insert(70);
        t.insert(80);


        System.out.println("Search the constucted tree for 80: ");
        BTreeNode result = t.search(80);
        if (result != null)
            System.out.println("Found in node " + result);
        else
            System.out.println("Not found!");

        System.out.println("Search the constucted tree for 100: ");
        result = t.search(100);
        if (result != null)
            System.out.println("Found in node " + result);
        else
            System.out.println("Not found!");

        System.out.println("Traversal of the constucted tree is : ");
        t.traverse();
        System.out.println("Structure of the constucted tree is : ");
        t.print();


        Random rd = new Random();
        rd.setSeed(123456);
        int numChecks = 20;
        int numTestPerCheck = 10;
        BTree<Integer>  tree = new BTree(0);
        for (int i = 0; i < numChecks; i++) {
            System.out.println("At Test " + i);
            tree.root = null;
            tree.m = Math.abs(rd.nextInt() % 7) + 3;
            for (int j = 0; j < numTestPerCheck; j++) {
                int insertVal = Math.abs(rd.nextInt() % 100) + 1;
                tree.insert(insertVal);
            }
            tree.traverse();
            System.out.println("");
        }

	/* Expected Output:
	Search the constucted tree for 80:
	Found in node [70,80,90]
	Search the constucted tree for 100:
	Not found!
	Traversal of the constucted tree is :
	 10 20 30 40 50 60 70 80 90
	Structure of the constucted tree is :
	Level 1 [ 40]
	Level 2 [ 20]
	Level 3 [ 10]
	Level 3 [ 30]
	Level 2 [ 60]
	Level 3 [ 50]
	Level 3 [ 70 80 90]
	*/
    }


}