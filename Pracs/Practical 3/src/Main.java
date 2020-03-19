//Berne' Nortier
//17091820

import java.util.Random;

public class Main
{
    public static void searchElement(BST<Integer> tree, Integer element)
    {
        if (tree.isEmpty())
            System.out.println("Tree is empty");
        else
        {
            Integer result = tree.search(element);
            if (result != null)
                System.out.println("Found element " + result);
            else
                System.out.println("Element " + element + " not found");
        }
    }

    public static void deleteElement(BST<Integer> tree, Integer element, String type)
    {
        if (tree.isEmpty())
            System.out.println("Tree is empty");
        else
        {
            boolean result = false;
            if (type.trim().equals("m"))
                result = tree.deleteByMerge(element);
            else if (type.equals("c"))
                result = tree.deleteByCopy(element);

            if (result)
                System.out.println("\nDeleted element " + element);
            else
                System.out.println("\nElement " + element + " not found for deletion");
        }
    }

    public static void printTree(BST<Integer> tree, int method)
    {
        switch (method)
        {
            case 0: //verbose
                String result;
                //System.out.println();
                System.out.println("Binary Search Tree Content:");
                if (tree.root != null)
                    System.out.println("Root: " + tree.root.element);
                result = tree.inorder(tree.root);
                System.out.println(result);
                break;
            case 1: //preorder
                tree.printPreorder();
                break;
            case 2: //postorder
                tree.printPostorder();
                break;
            case 3: //all 3
                printTree(tree, 0);
                printTree(tree, 1);
                printTree(tree, 2);
                System.out.println(" ");
        }
    }

    public static void testDelete(BST<Integer> tree, BST<Integer> other, String type) {
        //Testing independence of trees
        System.out.println("\nTESTING INDEPENDENCE");
        /*printTree(tree, 1);
        printTree(other, 1);*/


        deleteElement(tree, 8, "m");
        System.out.print("Tree: ");
        printTree(tree, 1);
        System.out.print(type);
        printTree(other, 1);

        System.out.println("\nInserted element 8");
        tree.insert(8);
        System.out.print("Tree: ");
        printTree(tree, 1);
        System.out.print(type);
        printTree(other, 1);

        deleteElement(tree, 8, "c");
        System.out.print("Tree: ");
        printTree(tree, 1);
        System.out.print(type);
        printTree(other, 1);

        System.out.println("\nInserted element 8");
        tree.insert(8);
        System.out.print("Tree: ");
        printTree(tree, 1);
        System.out.print(type);
        printTree(other, 1);

        deleteElement(other, 8, "m");
        System.out.print("Tree: ");
        printTree(tree, 1);
        System.out.print(type);
        printTree(other, 1);

        System.out.println("\nInserted element 8");
        other.insert(8);
        System.out.print("Tree: ");
        printTree(tree, 1);
        System.out.print(type);
        printTree(other, 1);

        deleteElement(other, 8, "c");
        System.out.print("Tree: ");
        printTree(tree, 1);
        System.out.print(type);
        printTree(other, 1);

        System.out.println("\nInserted element 8");
        other.insert(8);
        System.out.print("Tree: ");
        printTree(tree, 1);
        System.out.print(type);
        printTree(other, 1);
    }

    public static void main(String[] args)
    {
        //Practical 3

        BST<Integer> tree = new BST<Integer>();
        System.out.println("Empty: " + tree.isEmpty());

        Random rd = new Random();
        rd.setSeed(1234567890);
        Integer[] arr = {8,5,4,7,39,56,81,-8,25,14,15,12,30,-1,12,12};
        for (int i = 0; i < arr.length; i++) {
            tree.insert(arr[i]);
        }

        //Print
        System.out.println("\nPRINT ORIGINAL");
        printTree(tree, 3);

        //nonexistent element
        deleteElement(tree, 10, "c");
        deleteElement(tree, 10, "m");
        //root element
        deleteElement(tree, 8, "c");
        printTree(tree, 0);
        deleteElement(tree, tree.root.element, "m");
        printTree(tree, 0);
        //bottom
        deleteElement(tree, -8, "c");
        printTree(tree, 0);
        deleteElement(tree, -1, "m");
        printTree(tree, 0);

        //Testing Delete
        System.out.println("TESTING DELETE");
        for (int i = 0; i < arr.length; i++) {
            deleteElement(tree, arr[i], "m");
            printTree(tree,1);
        }

        /////////////////////////////////////////

        //Re-Insert
        for (int i = 0; i < arr.length; i++) {
            tree.insert(arr[i]);
        }

        System.out.println("\nPRINT ORIGINAL");
        printTree(tree, 3);

        BST<Integer> clone = new BST<Integer>();
        clone.root = tree.clone();
        System.out.println("\nPRINT CLONE");
        printTree(clone, 3);


        System.out.print("Tree: ");
        printTree(tree, 2);
        System.out.print("Clone ");
        printTree(clone, 2);

        //Testing independence of trees
        testDelete(tree, clone, "Clone ");


        System.out.println("\nTESTING MIRROR");
        System.out.println("\nPRINT ORIGINAL");
        printTree(tree, 3);

        System.out.println("\nPRINT MIRROR");
        BST<Integer> mirror = new BST<Integer>();
        BST<Integer> dblMirr = new BST<Integer>();
        mirror.root = tree.mirror();
        dblMirr.root = mirror.mirror();
        printTree(tree, 1);
        printTree(mirror, 1);
        printTree(dblMirr, 1);
        printTree(tree, 0);
        printTree(dblMirr, 0);

        //testDelete(tree, mirror, "Mirr");

        /*
        //RANDOM TESTS
        System.out.println("TESTING SEARCH");
        searchElement(tree, 10);
        deleteElement(tree, 8, "m");
        deleteElement(tree, 8, "c");
        searchElement(tree, 8);
        printTree(tree, 0);
        tree.insert(8);
        searchElement(tree, 8);
        deleteElement(tree, 8, "c");
        deleteElement(tree, 8, "m");
        searchElement(tree, 8);
        tree.insert(8);
*/


//        printTree(mirror, 0);

		/* Expected Output:
		8 5 12
		5 12 8
		8 12 5
		12 5 8
		Element 10 not found

		Binary Search Tree Content:
		3 [L: null]  [R: null]
		5 [L: 3]  [R: 7]
		7 [L: null]  [R: null]
		8 [L: 5]  [R: 12]
		10 [L: null]  [R: null]
		12 [L: 10]  [R: 14]
		14 [L: null]  [R: null]

		Found element 10
		Deleted element 8

		Binary Search Tree Content:
		3 [L: null]  [R: null]
		5 [L: 3]  [R: 7]
		7 [L: null]  [R: 12]
		10 [L: null]  [R: null]
		12 [L: 10]  [R: 14]
		14 [L: null]  [R: null]

		Deleted element 8

		Binary Search Tree Content:
		3 [L: null]  [R: null]
		5 [L: 3]  [R: null]
		7 [L: 5]  [R: 12]
		10 [L: null]  [R: null]
		12 [L: 10]  [R: 14]
		14 [L: null]  [R: null]


		Binary Search Tree Content:
		14 [L: null]  [R: null]
		12 [L: 14]  [R: 10]
		10 [L: null]  [R: null]
		7 [L: 12]  [R: null]
		5 [L: 7]  [R: 3]
		3 [L: null]  [R: null]


		*/

    }
}