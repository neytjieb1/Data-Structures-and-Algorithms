import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;


public class Main {

    public static int rdSeed = 123456789;

    public static void printArrOfValues(BPTree<Integer, Integer> tree) {
        Object[] arr = tree.values();
        System.out.println("Size=" + arr.length);
        for (int i = 0; i < arr.length; i++) {
            System.out.print((Integer) arr[i] + " ");
        }
        System.out.println(" ");
    }

    public static void testInsert(BPTree<Integer, Integer> tree, int numInserts, boolean verbose) {
        Random rd = new Random();
        rd.setSeed(rdSeed);
        for (int i = 0; i < numInserts; i++) {
            int key = Math.abs(rd.nextInt()) % 1000 + 1;
            int val = key * 1;
            tree.insert(key, val);
            System.out.println("\ni=" + (i) + " Insert: " + key + ", " + val);
            if (verbose) {
                tree.print();
                printArrOfValues(tree);
            }
        }
        if (!verbose) {
            tree.print();
        }
    }

    public static void testDelete(BPTree<Integer, Integer> tree, int numDeletes, boolean verbose) {
        Random rd = new Random();
        rd.setSeed(rdSeed);
        for (int i = 0; i < numDeletes; i++) {

            int delVal = Math.abs(rd.nextInt()) % 1000 + 1;
            if (verbose) {
                if (tree.search(delVal) == null) {
                    System.out.println("i: " + i + " Shouldn't change anything when deleting " + delVal);
                } else {
                    System.out.println("i: " + i + " After deletion: " + delVal);
                }
            }
            tree.delete(delVal);
            if (verbose) {
                tree.print();
            }

        }
    }

    public static void testCase1_11(BPTree<Integer, Integer> tree) {
        tree.insert(20, 20);
        tree.insert(10, 10);
        tree.insert(30, 30);
        tree.insert(50, 50);
        tree.insert(40, 40);
        tree.insert(60, 60);
        tree.insert(90, 90);
        tree.insert(70, 70);
        tree.insert(80, 80);
        System.out.println("Structure of the constructed tree is: ");
        tree.print();
        printArrOfValues(tree);

        Integer value = 111;    //non-existent value
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        value = 70; // Delete leaf also internal
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        value = 60; // Delete leaf
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        value = 50; // Delete leaf, underflow, borrow left
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        value = 40; // Delete leaf, underflow, borrow right
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        value = 30; // Delete leaf, underflow, borrow left
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        value = 90; //Delete rightmost, underflow, merge & update index
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        value = 10; //Delete leftmost, underflow, merge & update index
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

//        value = 20; //Delete leftmost, newRoot
//        tree.delete(value);
//        System.out.println("Structure of the tree after delete of: " + value);
//        tree.print();
//        printArrOfValues(tree);

        value = 80;   //Delete rightmost, newRoot
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        value = 80;
        tree.insert(value, value);
        value = 40;
        tree.insert(value, value);
        System.out.println("\nStructure of the tree after insertion of: 80, 40");
        tree.print();
        printArrOfValues(tree);

        value = 80;   //Delete end value from root
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        value = 20;   //Delete first value from root
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        value = 40;   //Delete only value from root
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);
    }

    public static void testCase12_14(BPTree<Integer, Integer> tree) {
        tree.insert(20, 20);
        tree.insert(10, 10);
        tree.insert(30, 30);
        tree.insert(50, 50);
        tree.insert(40, 40);
        tree.insert(60, 60);
        tree.insert(90, 90);
        tree.insert(70, 70);
        tree.insert(80, 80);
        System.out.println("Structure of the constructed tree is: ");
        tree.print();
        printArrOfValues(tree);

        Integer[] arrValues = {10, 30, 50, 80, 90};
        for (int i = 0; i < arrValues.length; i++) {
            tree.delete(arrValues[i]);
        }
        System.out.println("\nStructure of the tree after multiple deletions");
        tree.print();
        printArrOfValues(tree);

/*        Integer value = 20;   //Delete leftmost, underflow, merge, parent==root
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);*/

//        Integer value = 70;   //Delete rightmost, underflow, merge, parent==root
//        tree.delete(value);
//        System.out.println("\nStructure of the tree after delete of: " + value);
//        tree.print();
//        printArrOfValues(tree);

        Integer value = 40;   //Delete leftmost, underflow, merge, parent==root
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);
    }

    public static void testCase15(BPTree<Integer, Integer> tree) {
        Integer arrIn[] = {1,2,3,4,5,6,7,8,9,10,11,12};
        for (int i = 0; i < arrIn.length; i++) {
            tree.insert(arrIn[i], arrIn[i]);
        }
        System.out.println("Structure of the constructed tree is: ");
        tree.print();
        printArrOfValues(tree);

        Integer[] arrOut2 = {1,4,5,6};
        for (int i = 0; i < arrOut2.length; i++) {
            tree.delete(arrOut2[i]);
        }
        System.out.println("\nStructure of the deleted tree is: ");
        tree.print();
        printArrOfValues(tree);

        Integer value = 3;  //case 15, also try 2
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

    }

    public static void testCase16(BPTree<Integer, Integer> tree) {
        Integer arrIn[] = {1,2,3,4,5,6,7,8,9,10,11,12};
        for (int i = 0; i < arrIn.length; i++) {
            tree.insert(arrIn[i], arrIn[i]);
        }
        System.out.println("Structure of the constructed tree is: ");
        tree.print();
        printArrOfValues(tree);

        Integer arrOut[] = {8,10,12,11};
        for (int i = 0; i < arrOut.length; i++) {
            tree.delete(arrOut[i]);
        }
        System.out.println("\nStructure of the deleted tree is: ");
        tree.print();
        printArrOfValues(tree);

        Integer value = 7;  //case 16, also tried deleting 9
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

    }

    public static void testCase17_18(BPTree<Integer,Integer> tree) {
        for (int i = 0; i < 18; i++) {
            tree.insert(i+1, i+1);
        }
        System.out.println("Structure of the constructed tree is: ");
        tree.print();
        printArrOfValues(tree);

        Integer arrOut[] = {7,9,11,12};
        for (int i = 0; i < arrOut.length; i++) {
            tree.delete(arrOut[i]);
        }
        System.out.println("\nStructure of the deleted tree is: ");
        tree.print();
        printArrOfValues(tree);

        Integer value = 10;  //case 17, delete either 8/10 (share from left parent
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        //case 18: additonal deletions to set tree up:
        //share from right parent
/*        tree.delete(1);
        tree.delete(2);
        tree.delete(3);
        System.out.println("\nStructure of the deleted tree is: ");
        tree.print();
        printArrOfValues(tree);

        Integer value = 8;  //case 18, delete either 8/10 (share from right parent
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);*/
    }

    public static void testCase19_20(BPTree<Integer,Integer> tree) {
        for (int i = 0; i < 12; i++) {
            tree.insert(i+1,i+1);
        }
        System.out.println("\nStructure of the constructed tree is: ");
        tree.print();
        printArrOfValues(tree);

        Integer[] arrOut = {1,3,5,7,9,11,2,12};
        for (int i = 0; i < arrOut.length; i++) {
            tree.delete(arrOut[i]);
        }
        System.out.println("\nStructure of the deleted tree is: ");
        tree.print();
        printArrOfValues(tree);

        //case 19: delete from leftmost, parent also underflows
        Integer value = 4;  //either 4/6 (merge parent
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);

        //case 20: delete from leftmost, parent also underflows
        Integer value = 8;  //either 8/10 (merge parent
        tree.delete(value);
        System.out.println("\nStructure of the tree after delete of: " + value);
        tree.print();
        printArrOfValues(tree);
    }

    public static void main(String[] args) throws FileNotFoundException {
         /*PrintStream out = new PrintStream(new FileOutputStream("/home/jo/IdeaProjects/Ass3/outputB.txt"));
        System.setOut(out);*/

        //Random Tree
        /*BPTree<Integer, Integer> tree = new BPTree<Integer, Integer>(4); // A B+ Tree with order 4
        testInsert(tree, 20, true);
        /*BPTree<Integer, Integer> t = new BPTree<Integer, Integer>(4);
        Integer[] arr = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        for (int i = 0; i < arr.length; i++) {
            t.insert(arr[i],arr[i]);
        }
        t.print();
        printArrOfValues(t);
        testDelete(tree, 20, true);*/
        BPTree<Integer, Integer> tree = new BPTree<Integer, Integer>(4); // A B+ Tree with order 4

        //testCase1_11(tree);
        //testCase12_14(tree);
        //testCase15(tree);
        //testCase16(tree);
        //testCase17_18(tree);
        //testCase19_20(tree);

        for (int i = 0; i < 12; i++) {
            tree.insert(i+1,i+1);
        }
        System.out.println("\nStructure of the constructed tree is: ");
        tree.print();
        printArrOfValues(tree);

        Integer[] arrOut = {1,3,5,7,9,11,2,12};
        for (int i = 0; i < arrOut.length; i++) {
            tree.delete(arrOut[i]);
        }
        System.out.println("\nStructure of the deleted tree is: ");
        tree.print();
        printArrOfValues(tree);


















        /*System.out.println("Search the tree for 80: ");
        Integer result = (Integer) tree.search(80);
        if (result != null)
            System.out.println("Found key with value " + result);
        else
            System.out.println("Key not found!");

        System.out.println("Search the tree for 10: ");
        result = (Integer) tree.search(10);
        if (result != null)
            System.out.println("Found key with value " + result);
        else
            System.out.println("Key not found!");

        System.out.println("Search the tree for 20: ");
        result = (Integer) tree.search(20);
        if (result != null)
            System.out.println("Found key with value " + result);
        else
            System.out.println("Key not found!");


        System.out.println("Search the tree for 100: ");
        result = (Integer) tree.search(100);
        if (result != null)
            System.out.println("Found key with value " + result);
        else
            System.out.println("Key not found!");

        System.out.println("Search the tree for 40: ");
        result = (Integer) tree.search(40);
        if (result != null)
            System.out.println("Found key with value " + result);
        else
            System.out.println("Key not found!");*/

        /*// DB student table indexes
        BPTree<Integer, Integer> pktree = new BPTree<Integer, Integer>(4); // A B+ Tree with order 4
        pktree.insert(16230943, 1);
        pktree.insert(17248830, 2);
        pktree.insert(16094340, 3);
        pktree.insert(17012340, 4);

        System.out.println();
        System.out.println("Structure of the constucted index is: ");
        pktree.print();

        Integer studentid = 17248830;
        System.out.println("Search the index tree for student: " + studentid);
        result = (Integer) pktree.search(studentid);
        if (result != null)
            System.out.println("Found key with value " + result);
        else
            System.out.println("Key not found!");

        System.out.println("Return index tree values ordered by student id: ");
        Object[] array = pktree.values();
        if (array != null) {
            for (int i = 0; i < array.length; i++)
                System.out.println("Value " + array[i]);
        } else
            System.out.println("Index empty!");

        BPTree<String, Integer> sktree = new BPTree<String, Integer>(4); // A B+ Tree with order 4
        sktree.insert("Molefe", 1);
        sktree.insert("Muller", 2);
        sktree.insert("Botha", 3);
        sktree.insert("Evans", 4);
        System.out.println();
        System.out.println("Structure of the constucted index is: ");
        sktree.print();

        String surname = "Botha";
        System.out.println("Search the index tree for student: " + surname);
        result = (Integer) sktree.search(surname);
        if (result != null)
            System.out.println("Found key with value " + result);
        else
            System.out.println("Key not found!");

        System.out.println("Return index tree values ordered by surname: ");
        array = sktree.values();
        if (array != null) {
            for (int i = 0; i < array.length; i++)
                System.out.println("Value " + array[i]);
        } else
            System.out.println("Index empty!");*/

	/* Expected Output:
	Structure of the constructed tree is:
	Level 1 [ 30 50 70]
	Level 2 [ 10 20]
	Level 2 [ 30 40]
	Level 2 [ 50 60]
	Level 2 [ 70 80 90]

	Structure of the tree after delete of: 70
	Level 1 [ 30 50 70]
	Level 2 [ 10 20]
	Level 2 [ 30 40]
	Level 2 [ 50 60]
	Level 2 [ 80 90]

	Structure of the tree after delete of: 60
	Level 1 [ 30 50 70]
	Level 2 [ 10 20]
	Level 2 [ 30 40]
	Level 2 [ 50]
	Level 2 [ 80 90]

	Structure of the tree after delete of: 50
	Level 1 [ 30 40 70]
	Level 2 [ 10 20]
	Level 2 [ 30]
	Level 2 [ 40]
	Level 2 [ 80 90]

	Structure of the tree after delete of: 40
	Level 1 [ 30 40 90]
	Level 2 [ 10 20]
	Level 2 [ 30]
	Level 2 [ 80]
	Level 2 [ 90]

	Search the tree for 80:
	Found key with value 4000
	Search the tree for 100:
	Key not found!
	Search the tree for 40:
	Key not found!

	Structure of the constucted index is:
	Level 1 [ 17012340]
	Level 2 [ 16094340 16230943]
	Level 2 [ 17012340 17248830]

	Search the index tree for student: 17248830
	Found key with value 2
	Return index tree values ordered by student id:
	Value 3
	Value 1
	Value 4
	Value 2

	Structure of the constucted index is :
	Level 1 [ Molefe]
	Level 2 [ Botha Evans]
	Level 2 [ Molefe Muller]

	Search the index tree for student: Botha
	Found key with value 3
	Return index tree values ordered by surname:
	Value 3
	Value 4
	Value 1
	Value 2
	*/
    }


}