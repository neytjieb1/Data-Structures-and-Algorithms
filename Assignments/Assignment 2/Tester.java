/*
Name and Surname: 
Student Number: 
*/

import java.util.Random;

public class Tester {
    static Random rd = new Random();
    static int numChecks = 200;

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
        ThreadedAVLTree t2 = t.clone();
        int size = t.getNumberOfNodes();
        System.out.println("\nDelete elements from root");
        int i;
        for (i = 0; i < size; i++) {
            //System.out.println(i + ": " + t.getRoot().data + " size: " + t.getNumberOfNodes());
            t.delete(t.getRoot().data);
            if (!isValidAVL(t.getRoot())) {
                throw new ArithmeticException("i: " + i + " tree now invalid. Root = " + t.getRoot());
            }
            if (t.getNumberOfNodes()+i != size-1) {
                throw new ArithmeticException("From Root-- i: " + i + "size: " + t.getNumberOfNodes() + " but should be " + (size-1-i) );
            }
        }
        System.out.println("After " + i + " checks. Size = " + t.getNumberOfNodes());

        System.out.println("\n\nDelete randomly until empty");
        boolean deleted;
        i = 0;
        while (t2.getRoot() != null) {
            int val = Math.abs(rd.nextInt()) % (numChecks*2);
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

    public static void testInsert(ThreadedAVLTree<Integer> t1, ThreadedAVLTree<Integer> t2, Integer[] arr) {
        BinaryTreePrinter p = new BinaryTreePrinter();

        for (int i = 0; i < arr.length; i++) {
            t2.insert(arr[i]);
        }
        System.out.println("INSERT ACCORDING TO ARRAY");
        System.out.println("NumNodes: " + t2.getNumberOfNodes());
        System.out.println("Valid: " + isValidAVL(t2.getRoot()));
        //p.printNode(t2.getRoot());

        int numDuples = 0;
        for (int i = 0; i < numChecks; i++) {
            int val = Math.abs(rd.nextInt() % numChecks);
            //System.out.print("i: " + i + " val: " + val);
            //System.out.print(val + " ");
            if (!t1.insert(val)) {
                numDuples++;
            }
            //System.out.println(" valid: " + isValidAVL(t1.getRoot()));
        }
        System.out.println("\nINSERT Randomly");
        //System.out.println("Numchecks: " + numChecks);
        System.out.println("NumDuples: " + numDuples);
        System.out.println("NumNodes: " + t1.getNumberOfNodes());
        System.out.println("Valid: " + isValidAVL(t1.getRoot()));
        //p.printNode(t1.getRoot());


    }

    public static void testClone(ThreadedAVLTree<Integer> tOriginal) {
        System.out.println("CLONE");
        ThreadedAVLTree<Integer> tclone = tOriginal.clone();
        System.out.println("Original before Insert");
        tOriginal.myOwnPreOrder(tOriginal.getRoot());
        System.out.println("Clone before Insert");
        tclone.myOwnPreOrder(tclone.getRoot());
        tOriginal.insert(22);
        System.out.println("Original after insert of 22");
        tOriginal.myOwnPreOrder(tOriginal.getRoot());
        System.out.println("LCone before Insert");
        tclone.myOwnPreOrder(tclone.getRoot());
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
        rd.setSeed(1234567890);
        //INSERT
        ThreadedAVLTree<Integer> t1 = new ThreadedAVLTree<Integer>();
        ThreadedAVLTree<Integer> t2 = new ThreadedAVLTree<Integer>();
        Integer arr[] = {15, 10, 30, 5, 20, 11, 40, 12};
        testInsert(t1, t2, arr);

        //TEST CLONE
//       testClone(t2);

        //Test Threaded Inorder
//       System.out.println("TEST INORDER");
//       testInOrder(t1);
//       testInOrder(t2);

        //Test Threaded PreOrder
       System.out.println("TEST PREORDER");
       //Is my interpretation right?
//       testPreOrder(t1);
//       testPreOrder(t2);

        //Test Delete
        System.out.println("\nTEST DELETE");
//        testDelete(t2);         //Easy self-setup case
        testDelete(t1);         //randomly generated case


    }
}
