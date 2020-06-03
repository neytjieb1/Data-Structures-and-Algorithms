/*
    Change opCount to test different number of operations and m to and change the order of the B+-tree

    Uncomment the output statements --> they are commented out to not have to wait for output to finish
*/

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;

public class CMain
{

    public static int level = 0;

    /* look for keys that should be in the tree but they were not found and output that node to see search is wrong or keyTall etc*/
    public static void findNode(BPTreeNode<Integer, Integer> node, Integer key){
        BPTreeNode ptr = node.getLeftMostLeaf(node);


        while(ptr != null && ((BPTreeLeafNode) ptr).searchNode(key) == null){
            ptr = ptr.rightSibling;
        }

        if(ptr != null){
            System.out.println("printing node for key=" + key);
            ptr.printAllKeys();
            System.out.println();
            System.out.println();

            ptr.printKeys();
            System.out.println();
            System.out.println();
        }
    }

    /* show any empty nodes */
    public static void showEmptyNodes(BPTreeNode<Integer, Integer> node){
        level++;

        if (node != null) {
            boolean showChildren = false;

            if(node.getKeyCount() == 0){
                System.out.print("Level " + level + " ");
                node.printKeys();
                System.out.println("first key contents: " + node.getKey(0));
                System.out.println();

                showChildren = true;
            }

            // If this node is not a leaf, then
            // print all the subtrees rooted with this node.
            if (!node.isLeaf())
            {	BPTreeInnerNode inner = (BPTreeInnerNode<Integer, Integer>)node;
                for (int j = 0; j < (node.m); j++)
                {
                    //System.out.println("j=" + j);/////////////////////////////////////////////
                    if(showChildren && inner.references[j] != null){
                        System.out.println("j=" + j);
                        ((BPTreeNode<Integer, Integer>) inner.references[j]).printKeys();
                        System.out.println();
                    }

                    showEmptyNodes((BPTreeNode<Integer, Integer>)inner.references[j]);
                }
            }
        }

        level--;
    }

    /*checking property: key(i) < key(i+1) */
    public static void checkKeyPositioning(BPTreeNode<Integer, Integer> node){
        if(node != null){
            //check keys go in ascending order
            for(int i = 0; i < node.getKeyCount()-1; i++){
                if(node.getKey(i) >= node.getKey(i+1)){
                    node.printKeys();
                    System.out.println();
                    System.out.println();
                    break;
                }
            }

            if(!node.isLeaf()){
                //check that the first key of the child is equal to the parent key if the child is a leaf
                if(((BPTreeInnerNode) node).getChild(0).isLeaf()){
                    for(int i = 0; i < node.getKeyCount(); i++){
                        if(((BPTreeInnerNode) node).getChild(i+1).getKey(0) != node.getKey(i)){
                            System.out.println("child leftmost key does not match the seperator key");
                            node.printKeys();
                            System.out.println();
                            ((BPTreeInnerNode) node).getChild(i+1).printKeys();
                            System.out.println();
                        }
                    }
                }
                else{
                    for(int i = 0; i < node.getKeyCount(); i++){
                        if(((BPTreeInnerNode) node).getChild(i+1).getKey(0) == node.getKey(i)){
                            System.out.println("child leftmost key does match the seperator key when it should not");
                            node.printKeys();
                            System.out.println();
                            ((BPTreeInnerNode) node).getChild(i+1).printKeys();
                            System.out.println();
                        }
                    }
                }

                for(int i = 0; i <= node.getKeyCount(); i++){
                    checkKeyPositioning(((BPTreeInnerNode) node).getChild(i));
                }
            }
        }
    }

    /*checking property: all nodes connected in the linked list are actually in the tree --> this would not work if you search using the linked list rather than using the
    child references to find a node*/
    public static void checkLinkedListNodesInTree(BPTreeNode<Integer, Integer> node){
        BPTreeNode ptr = node.getLeftMostLeaf(node);

        while(ptr != null){
            //check that the node is in the tree properly --> i.e has a parent and is not just an old connection that was not broken
            Integer key = (Integer) ptr.getKey(0);
            BPTreeNode n = node.search(key, node);//this returns the leaf where the key would be found in (if it is there at all)

            if(n == null){
                System.out.println("n not found when it should of been: ");
                n.printKeys();
                System.out.println();
                System.out.println();
            }

            ptr = ptr.rightSibling;
        }
    }

    public static void checkBalanced(BPTreeNode<Integer, Integer> node){
        level++;
        if (node != null) {
            // If this node is not a leaf, then
            // print all the subtrees rooted with this node.
            if (!node.isLeaf())
            {	BPTreeInnerNode inner = (BPTreeInnerNode<Integer, Integer>)node;
                for (int j = 0; j < (node.m); j++)
                {
                    ////System.out.println("j=" + j);/////////////////////////////////////////////
                    checkBalanced((BPTreeNode<Integer, Integer>)inner.references[j]);
                }
            }
            else if(level != 5){
                System.out.println("leaf node is on the wrong level of the tree");
                node.printKeys();
                System.out.println();
                System.out.println();
            }
        }
        level--;
    }

    public static void debug(BPTree<Integer, Integer> tree, List<Integer> inTree, int opCount){
        //output the tree after
        System.out.println();
        /*System.out.println("//output the tree after");
        tree.print();

        System.out.println();
        System.out.println("-- Showing EMPTY NODES --");
        showEmptyNodes(tree.root);
        System.out.println("-- DONE --");

        System.out.println();
        System.out.println("----- CHECKING THAT ALL VALUES IN \"inTree\" are still in the tree (perhaps they are and key count is too small OR reference was set to NULL)");

        for(int x : inTree){
            if(inTree.contains(x) && tree.search(x) == null){
                System.out.println("x=" + x + " should of been found but it wasn't");
                findNode(tree.root, x);

                tree.print();
            }
        }

        System.out.println("-- DONE --");*/

        /*System.out.println();
        System.out.println("Checking the positioning of the keys");
        checkKeyPositioning(tree.root);

        System.out.println("-- DONE --");*/
/*
        System.out.println();
        System.out.println("Checking that the link list only contains valid nodes");
        checkLinkedListNodesInTree(tree.root);
        System.out.println("-- DONE --");

        System.out.println();
        System.out.println("Checking that all leaves are on the same level");
        checkBalanced(tree.root);
        System.out.println("-- DONE --");

        System.out.println("opCount = " + opCount);

        //remove all of the keys

        for(int i = 0; i < 1000; i++){
            tree.delete(i);
        }

        tree.print();*/
    }

    public static void main(String[] args) throws Exception
    {
        PrintStream out = new PrintStream(new FileOutputStream("/home/jo/IdeaProjects/Ass3/outputB.txt"));
        System.setOut(out);

        int opCount = 20000;
        if (args.length > 0) { //number of operations to do
            opCount = Integer.parseInt(args[0]);
        }

        int m = 5;      //also 6
        BPTree<Integer, Integer> tree = new BPTree<Integer, Integer>(m);
        java.util.Random rand = new java.util.Random(69420);
        Random r = new Random(1551587);
        List<Integer> inTree = new ArrayList<Integer>();

        for (int i = 0; i < opCount; i++) {

            java.util.Random actionPicker = new java.util.Random(i);
            int action = actionPicker.nextInt(20);
            System.out.println(i + " " + action);
            int value = rand.nextInt(opCount/14);
            if (action <= 1) { //0-1
                //search
                Integer result = (Integer) tree.search(value);
                /*
				if (result != null)
					System.out.println("Found key with value " + result);
				else
                    System.out.println("Key not found!"); */

                if(i == opCount-1){
                    System.out.println("Last operation: search for value = " + value);
                }
            } else if (action <= 6) { //2-6
                //insert
                if (i==11113 && action == 2) {
                    System.out.println('x');
                }

                int x = r.nextInt(5000);
                tree.insert(x, r.nextInt(5000));

                if(!inTree.contains(x)){
                    inTree.add(x);
                }

                //tree.print();

                if(i == opCount-1){
                    System.out.println("Last operation: insert x = " + x);
                }
            } else if (action <= 11) { //7-11
                int x = r.nextInt(5000);

                tree.delete(x);

                inTree.remove(Integer.valueOf(x));
                //tree.print();
            } else if (action <= 12) { //12
                Object[] values = tree.values();

				/*if(values != null){
					for(int a = 0; a < values.length; a++){
						System.out.println("Value " + values[a]);
					}
				}
				else{
					System.out.println("Index is empty!");
				}*/
            } else if (action <= 13) { //13
				/*Object[] keys = tree.keys();

				if(keys != null){
					/*for(int a = 0; a < keys.length; a++){
						System.out.println("Key " + keys[a]);
					}
				}
				else{
					System.out.println("Index is empty!");
				}*/
            }
            //checks --> add
            //debug(tree, inTree, opCount);
        }//for

        // debug(tree, inTree, opCount);

        tree.print();
    }
}