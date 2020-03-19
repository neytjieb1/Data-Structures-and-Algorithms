/**
 * Name: Berne' Nortier
 * Student Number: 17091820
 */

public class Main {

    public static void insertAll(SplayTree<Integer> tree, Integer[] list) {
	for (int k = 0; k < list.length; k++) {
		tree.insert(list[k]);
	}
    }

    public static void main(String[] args) 
    {
        Integer[] list = new Integer[]{50, 25, 100, 5, 110, 115, 75, 42, 101, 112};

	SplayTree<Integer> tree = new SplayTree<Integer>();
	insertAll(tree, list);
	System.out.println(tree.contains(50));
	System.out.println(tree.contains(125));
/*
 Expected Output:
	true
	false
	*/



	SplayTree<Integer> stree = new SplayTree<Integer>();
	insertAll(stree, list);
	stree.printTree(true);

	stree.access(110, SplayTree.SplayType.SPLAY);
	stree.printTree(true);
	stree.access(42, SplayTree.SplayType.SPLAY);
	stree.printTree(true);
/*

 Expected Output:
	50 [L: 25]  [R: 100]
	25 [L: 5]  [R: 42]
	5 [L: null]  [R: null]
	42 [L: null]  [R: null]
	100 [L: 75]  [R: 110]
	75 [L: null]  [R: null]
	110 [L: 101]  [R: 115]
	101 [L: null]  [R: null]
	115 [L: 112]  [R: null]
	112 [L: null]  [R: null]

	110 [L: 100]  [R: 115]
	100 [L: 50]  [R: 101]
	50 [L: 25]  [R: 75]
	25 [L: 5]  [R: 42]
	5 [L: null]  [R: null]
	42 [L: null]  [R: null]
	75 [L: null]  [R: null]
	101 [L: null]  [R: null]
	115 [L: 112]  [R: null]
	112 [L: null]  [R: null]

	42 [L: 25]  [R: 100]
	25 [L: 5]  [R: null]
	5 [L: null]  [R: null]
	100 [L: 50]  [R: 110]
	50 [L: null]  [R: 75]
	75 [L: null]  [R: null]
	110 [L: 101]  [R: 115]
	101 [L: null]  [R: null]
	115 [L: 112]  [R: null]
	112 [L: null]  [R: null]
*/




	SplayTree<Integer> sstree = new SplayTree<Integer>();
	insertAll(sstree, list);
	System.out.println("START SEMISPLAY");
	sstree.printTree(true);
	sstree.access(110, SplayTree.SplayType.SEMISPLAY);
	sstree.printTree(true);
	sstree.access(42, SplayTree.SplayType.SEMISPLAY);
	sstree.printTree(true);
/*
 Expected Output:
	50 [L: 25]  [R: 100]
	25 [L: 5]  [R: 42]
	5 [L: null]  [R: null]
	42 [L: null]  [R: null]
	100 [L: 75]  [R: 110]
	75 [L: null]  [R: null]
	110 [L: 101]  [R: 115]
	101 [L: null]  [R: null]
	115 [L: 112]  [R: null]
	112 [L: null]  [R: null]

	100 [L: 50]  [R: 110]
	50 [L: 25]  [R: 75]
	25 [L: 5]  [R: 42]
	5 [L: null]  [R: null]
	42 [L: null]  [R: null]
	75 [L: null]  [R: null]
	110 [L: 101]  [R: 115]
	101 [L: null]  [R: null]
	115 [L: 112]  [R: null]
	112 [L: null]  [R: null]

	42 [L: 25]  [R: 100]
	25 [L: 5]  [R: null]
	5 [L: null]  [R: null]
	100 [L: 50]  [R: 110]
	50 [L: null]  [R: 75]
	75 [L: null]  [R: null]
	110 [L: 101]  [R: 115]
	101 [L: null]  [R: null]
	115 [L: 112]  [R: null]
	112 [L: null]  [R: null]*/



    }
	
}
