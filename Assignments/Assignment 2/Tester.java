/*
Name and Surname: B Nortier
Student Number: 17091820
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

        for (int i = 0; i < arr.length; i++) {
            t2.insert(arr[i]);
        }
        System.out.println("INSERT ACCORDING TO ARRAY");
        System.out.println("NumNodes: " + t2.getNumberOfNodes());
        System.out.println("Valid: " + isValidAVL(t2.getRoot()));


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

    public static void testClone(ThreadedAVLTree<Integer> tOriginal, boolean copyConstructorTest, boolean verbose) {
        ThreadedAVLTree<Integer> tClone = null;
        if (copyConstructorTest) {
            tClone = new ThreadedAVLTree<>(tOriginal);
        }
        else {
            tClone = tOriginal.clone();
        }

        if (verbose) System.out.println("\nTEST CLONE");

        if (verbose) System.out.println("Random inserts");
        Random rd = new Random();
        rd.setSeed(rdSeedVal);
        for (int i = 0; i < numChecks*2; i++) {
            int val = Math.abs(rd.nextInt())%(100);
            if (verbose) System.out.println(i + ": Value to insert: " + val);
            if (tOriginal.preorder().equals(tClone.preorder())==false) {
                throw new ArithmeticException("The two trees should be equal before an insertion, but aren't");
            }

            if (tOriginal.contains(val)) {
                if (verbose) System.out.println("DUPLICATE");
                continue;
            }

            tOriginal.insert(val);
            if (tOriginal.preorder().equals(tClone.preorder()) ) {
                throw new ArithmeticException("Trees should be equal. Only inserted "+val+" into original");
            }
            if (tClone.contains(val)) {
                throw new ArithmeticException("tClone should NOT contain " + val);
            }

            tClone.insert(val);
            if (tOriginal.preorder().equals(tClone.preorder())==false) {
                throw new ArithmeticException("The two trees should be equal after inserting " + val + " into both.");
            }


        }


        System.out.println("Random deletes");
        int i = 0;
        while (tOriginal.getNumberOfNodes()!=0) {
            BinaryTreePrinter p = new BinaryTreePrinter();
            int val = Math.abs(rd.nextInt())%(100);
            while (!tOriginal.contains(val)) {
                val = Math.abs(rd.nextInt())%(100);
            }
            if (verbose) {
                System.out.println(i + ": Value to delete: " + val);
                p.printNode(tOriginal.getRoot());
                p.printNode(tClone.getRoot());
            }

            if (tOriginal.preorder().equals(tClone.preorder())==false) {
                throw new ArithmeticException("The two trees should be equal before an insertion, but aren't");
            }

            tOriginal.delete(val);
            if (tOriginal.preorder().equals(tClone.preorder()) ) {
                throw new ArithmeticException("Trees should be equal. Only deleted "+val+" from original");
            }
            if (!tClone.contains(val)) {
                throw new ArithmeticException("tClone should contain " + val);
            }
            if (verbose) {
                System.out.println("after deletion from tOriginal");
                p.printNode(tOriginal.getRoot());
                p.printNode(tClone.getRoot());
            }
            tClone.delete(val);
            if (tOriginal.preorder().equals(tClone.preorder())==false) {
                throw new ArithmeticException("The two trees should be equal after deleting " + val + " from both.");
            }
            if (verbose) {
                System.out.println("after deletion from clone. should be equal");
                p.printNode(tOriginal.getRoot());
                p.printNode(tClone.getRoot());
            }

            i++;
        }

    }

    public static boolean testCopyConstructor(ThreadedAVLTree<Integer> tOriginal, boolean verbose) {
        if (verbose) {
            testClone(tOriginal, true, true);
        }
        else {
            try {
                testClone(tOriginal, true, true);
            }
            catch (ArithmeticException ex) {
                System.out.println(ex);
                return false;
            }
        }
        return true;
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

        ThreadedAVLTree<Integer> t3 = new ThreadedAVLTree<Integer>();
        ThreadedAVLTree<Integer> t3_clone = t3.clone();

        Integer arr_cl[] = {10};
        for (int i = 0; i < arr_cl.length; i++) {
            t3.insert(arr_cl[i]);
            t3.preorder();
            t3_clone.preorder();
        }
        
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

        //Test Threaded Inorder
        System.out.println("TEST INORDER");
        testInOrder(t1);
        testInOrder(t2);

        //Test Threaded PreOrder
        System.out.println("TEST PREORDER");
        testPreOrder(t1);
        testPreOrder(t2);

        //TEST CLONE
        testClone(t1, false, true);
        testClone(t2, false, true);
        //TEST COPY-CONSTRUCTOR
        if (testCopyConstructor(t1, false)) {
            System.out.println("Copy constructor works for t1");
        }
        else {
            testCopyConstructor(t1, true);
        }
        if (testCopyConstructor(t2, false)) {
            System.out.println("Copy constructor works for t2");
        }
        else {
            testCopyConstructor(t2, true);
        }


        //Test Delete
        System.out.println("\nTEST DELETE");
        testDelete(t1);         //randomly generated case
        testDelete(t2);         //Easy self-setup case
    }
}
