/*
Name and Surname: 
Student Number: 
*/

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

public class Tester {
    static Random rd = new Random();
    static int numChecks = 20;
    static int rdSeedVal = 1234567890;

    public static boolean isValidAVL(ThreadedAVLNode<Integer> node) {
        if (node != null) {
            if (Math.abs(node.balanceFactor) == 2) return false;
            isValidAVL(node.left);
            if (node.hasThread) return true;
            else isValidAVL(node.right);
        }
        return true;
    }

    public static void testDelete(ThreadedAVLTree<Integer> t) {
        BinaryTreePrinter p = new BinaryTreePrinter();
        p.printNode(t.getRoot());
        ThreadedAVLTree t2 = t.clone();
        int size = t.getNumberOfNodes();

        System.out.println("\nDelete elements from root");
        int i;
        for (i = 0; i < size; i++) {
            System.out.println("i=" + i + " deleteVal=" + t.getRoot().data + " size=" + t.getNumberOfNodes());
            t.delete(t.getRoot().data);
            p.printNode(t.getRoot());
            if (!isValidAVL(t.getRoot())) {
                throw new ArithmeticException("i: " + i + " tree now invalid. Root = " + t.getRoot());
            }
            if (t.getNumberOfNodes()+i != size-1) {
                throw new ArithmeticException("From Root-- i: " + i + " size=" + t.getNumberOfNodes() + "; should be " + (size-1-i) );
            }
        }
        System.out.println("After " + i + " checks. Size = " + t.getNumberOfNodes());


        System.out.println("\n\nDelete randomly until empty");
        boolean deleted;
        i = 0;
        while (t2.getRoot() != null) {
            int val = Math.abs(rd.nextInt()) % (numChecks*numChecks);
            deleted = t2.delete(val);
            if (deleted) {
                i++;
                if (!isValidAVL(t2.getRoot())) {
                    throw new ArithmeticException("Invalid tree after deleting " + val);
                }
                if (t2.getNumberOfNodes() != size-i) {
                    throw new ArithmeticException("Random-- i: " + i + "size: " + t2.getNumberOfNodes() + " but should be " + (size-i) );
                }
            }
        }
        System.out.println("After " + i + " checks. Size = " + t2.getNumberOfNodes());
    }

    public static void testInsert(ThreadedAVLTree<Integer> t2, Integer[] arr) {
        BinaryTreePrinter p = new BinaryTreePrinter();

        for (int i = 0; i < arr.length; i++) {
            t2.insert(arr[i]);
        }
        System.out.println("INSERT ACCORDING TO ARRAY");
        System.out.println("NumNodes: " + t2.getNumberOfNodes());
        System.out.println("Valid: " + isValidAVL(t2.getRoot()));
        //p.printNode(t2.getRoot());


    }

    public static void testInsert(ThreadedAVLTree<Integer> t1) {
        BinaryTreePrinter p = new BinaryTreePrinter();
        int numDuples = 0;
        for (int i = 0; i < numChecks; i++) {
            int val = Math.abs(rd.nextInt() % numChecks);
            if (!t1.insert(val)) {
                numDuples++;
            }
            else {
                System.out.println("Insert: " + val);
                p.printNode(t1.getRoot());
            }
        }
        System.out.println("\nINSERT Randomly");
        System.out.println("Numchecks: " + numChecks);
        System.out.println("NumDuples: " + numDuples);
        System.out.println("NumNodes: " + t1.getNumberOfNodes());
        System.out.println("Valid: " + isValidAVL(t1.getRoot()));
        p.printNode(t1.getRoot());
    }

    public static void testClone(ThreadedAVLTree<Integer> tOriginal) {
        BinaryTreePrinter p = new BinaryTreePrinter();
        System.out.println("CLONE");
        ThreadedAVLTree<Integer> tclone = tOriginal.clone();

        System.out.println("\nOriginal before Insert");
        p.printNode(tOriginal.getRoot());
        System.out.println(tOriginal.preorder());

        System.out.println("\nClone before Insert");
        System.out.println(tclone.preorder());
        p.printNode(tclone.getRoot());

        tOriginal.insert(22);

        System.out.println("\nOriginal after insert of 22");
        p.printNode(tOriginal.getRoot());
        System.out.println(tOriginal.preorder());

        System.out.println("\nClone after Insert");
        System.out.println(tclone.preorder());
        p.printNode(tclone.getRoot());
    }

    public static void testInOrder(ThreadedAVLTree<Integer> tree) {
        tree.myOwnInorder(tree.getRoot());
        System.out.println("\n" + tree.inorder());
        System.out.println(tree.inorderDetailed());
        System.out.println("");
    }

    public static void testPreOrder(ThreadedAVLTree<Integer> tree) {
        tree.myOwnPreOrder(tree.getRoot());
        System.out.println("\n" + tree.preorder());
        System.out.println(tree.preorderDetailed());
        System.out.println("");
    }

    public static void main(String[] args) throws Exception {
        PrintStream out = new PrintStream(new FileOutputStream("/home/jo/IdeaProjects/Assignment 2/outputB.txt"));
        System.setOut(out);
        rd.setSeed(rdSeedVal);
        //INSERT
        ThreadedAVLTree<Integer> t1 = new ThreadedAVLTree<Integer>();
        ThreadedAVLTree<Integer> t2 = new ThreadedAVLTree<Integer>();
        Integer arr[] = {15, 10, 30, 5, 20, 11, 40, 12};
        testInsert(t1, arr);

        for (int i = 0; i < 1; i++) {
            t2.clearTree();
            testInsert(t2);
        }

        //TEST CLONE
        testClone(t1);
        testClone(t2);

        //Test Threaded Inorder
        System.out.println("TEST INORDER");
        testInOrder(t1);
        testInOrder(t2);

        //Test Threaded PreOrder
        System.out.println("TEST PREORDER");
        testPreOrder(t1);
        testPreOrder(t2);

        //Test Delete
        System.out.println("\nTEST DELETE");
        testDelete(t1);         //randomly generated case
        testDelete(t2);         //Easy self-setup case
    }
}
