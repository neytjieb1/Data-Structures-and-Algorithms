import java.util.Random;

public class Main {
    public static void printArrOfValues(BPTree<Integer, Integer> tree) {
        Object[] arr = tree.values();
        System.out.println("Size=" + arr.length);
        /*for (int i = 0; i < arr.length; i++) {
            System.out.print((Integer) arr[i] + " ");
        }
        System.out.println(" ");*/
    }

    public static void main(String[] args) {
        //General tree
        BPTree<Integer, Integer> tree = new BPTree<Integer, Integer>(4); // A B+ Tree with order 4
        Integer[] keys = {20, 10, 30, 50, 40, 60, 90, 70, 80, 100};
        Integer[] values = {5000, 1000, 9000, 8000, 2000, 70000, 3000, 6000, 4000, 4500};
        for (int i = 0; i < keys.length; i++) {
            System.out.println("\ni=" + (i) + " Insert: " + keys[i] + ", " + values[i]);
            tree.insert(keys[i], values[i]);
            printArrOfValues(tree);
            //tree.print();

        }

        //printArrOfValues(tree);

        Random rd = new Random();
        rd.setSeed(123456987);
        int numInserts = 20;
        for (int i = 0; i < numInserts; i++) {
            int key = Math.abs(rd.nextInt()) % 100 + 1;
            int val = Math.abs(rd.nextInt()) % 10000 + 1;
            tree.insert(key, val);
            System.out.println("\ni=" + (i+10) + " Insert: " + key + ", " + val);
            printArrOfValues(tree);
            //tree.print();
        }


	/*


	tree.insert(20, 5000);
		tree.print();
	tree.insert(10, 1000);
		tree.print();
	tree.insert(30, 9000);
		tree.print();
	tree.insert(50, 8000);
		tree.print();
	tree.insert(40, 2000);
		tree.print();
	tree.insert(60, 7000);
		tree.print();
	tree.insert(90, 3000);
		tree.print();
	tree.insert(70, 6000);
		tree.print();
	tree.insert(80, 4000);
	tree.print();





	System.out.println("Structure of the constructed tree is: ");
	tree.print();

	Integer value = 70; // Delete leaf also internal
	tree.delete(value);
	System.out.println("Structure of the tree after delete of: " + value);
	tree.print();

	value = 60; // Delete leaf
	tree.delete(value);
	System.out.println("Structure of the tree after delete of: " + value);
	tree.print();

	value = 50; // Delete leaf, underflow, borrow left
	tree.delete(value);
	System.out.println("Structure of the tree after delete of: " + value);
	tree.print();

	value = 40; // Delete leaf, underflow, borrow right
	tree.delete(value);
	System.out.println("Structure of the tree after delete of: " + value);
	tree.print();

	System.out.println("Search the tree for 80: ");
    	Integer result = (Integer)tree.search(80);
	if (result != null)
		System.out.println("Found key with value " + result);
	else
		System.out.println("Key not found!");

	System.out.println("Search the tree for 100: ");
    	result = (Integer)tree.search(100);
	if (result != null)
		System.out.println("Found key with value " + result);
	else
		System.out.println("Key not found!");

	System.out.println("Search the tree for 40: ");
	result = (Integer)tree.search(40);
	if (result != null)
		System.out.println("Found key with value " + result);
	else
		System.out.println("Key not found!");

	// DB student table indexes
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
	result = (Integer)pktree.search(studentid);
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
	result = (Integer)sktree.search(surname);
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
	Structure of the constucted tree is:
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