//Berne' Nortier
//17091820

import java.util.Queue;
import java.util.LinkedList;

@SuppressWarnings("unchecked")
public class BST<T extends Comparable<? super T>> {

    protected BSTNode<T> root = null;
    protected static int count = 0;

    public BST()
    {
    }

    public void clear()
    {
        root = null;
    }

    public String inorder(BSTNode<T> node)
    {
        boolean verbose = true;
        if (node != null) {
            String result = "";
            result += inorder(node.left);
            if (verbose) {
                result += node.toString()+"\n";
            } else {
                result += node.element.toString() + " ";
            }
            result += inorder(node.right);
            return result;
        }
        return "";
    }

    ////// You may not change any code above this line //////


    ////// Implement the functions below this line //////

    public boolean isEmpty()
    {
        return root == null;
    }

    public BSTNode<T> mirror()
    {   //Depth-First Traversal

        //Create mirrorTree
        BST<T> mTree = new BST<T>();

        //if empty
        if (isEmpty()) {
            return null;
        }
        else {
            mTree.root = new BSTNode<T>(root.element);
            mTree.root.left = new BSTNode<T>(root.right.element);
            mTree.root.right = new BSTNode<T>(root.left.element);
        }
        //Using preorder
        mirrorPreOrder(mTree.root, root);
        //return
        return mTree.root;

    }

    private void mirrorPreOrder(BSTNode<T> mNode, BSTNode<T> trueNode) {
        if (trueNode!=null) {
            //add p to new tree
            if (trueNode.right != null)
                mNode.left = new BSTNode<T>(trueNode.right.element);
            else mNode.left = null;

            if (trueNode.left != null)
                mNode.right = new BSTNode<T>(trueNode.left.element);
            else mNode.right = null;

            //Recursion
            mirrorPreOrder(mNode.right, trueNode.left);
            mirrorPreOrder(mNode.left, trueNode.right);
        }
    }

    public BSTNode<T> clone()
    {   //Breadth-First Traversal

        //Create cloneTree
        BST<T> clTree = new BST<T>();

        //if empty
        if (isEmpty() ) {
            return null;
        }
        else {
            clTree.root = new BSTNode<T>(root.element);
            clTree.root.right = new BSTNode<T>(root.right.element);
            clTree.root.left = new BSTNode<T>(root.left.element);
        }

        BSTNode<T> p = root;
        BSTNode<T> clTemp = clTree.root;
        Queue<BSTNode<T>> trueQ = new LinkedList<BSTNode<T>>();
        Queue<BSTNode<T>> cloneQ = new LinkedList<BSTNode<T>>();

        if (p!=null) {
            trueQ.add(p);
            cloneQ.add(clTemp);

            while (!trueQ.isEmpty()) {
                p = trueQ.remove();
                clTemp = cloneQ.remove();

                //add p to new tree
                if (p.right != null)
                    clTemp.right = new BSTNode<T>(p.right.element);
                else clTemp.right = null;
                if (p.left != null)
                    clTemp.left = new BSTNode<T>(p.left.element);
                else clTemp.left = null;

                if (p.left != null) {
                    trueQ.add(p.left);
                    cloneQ.add(clTemp.left);
                }
                if (p.right != null) {
                    trueQ.add(p.right);
                    cloneQ.add(clTemp.right);
                }
            }
         }
        return clTree.root;
    }

    public void printPreorder()
    {
        printPreorder(root);
        System.out.print("\n");
    }

    protected void printPreorder(BSTNode<T> p) {
        if (p!= null) {
            System.out.print(p.element + " ");
            printPreorder(p.left);
            printPreorder(p.right);
        }
    }

    public void printPostorder()
    {
        printPostorder(root);
        System.out.print("\n");
    }

    protected void printPostorder(BSTNode<T> p) {
        if (p != null) {
            printPostorder(p.left);
            printPostorder(p.right);
            System.out.print(p.element + " ");
        }
    }

    public void insert(T element)
    {
        BSTNode<T> p = root;
        BSTNode<T> prev = null;
        while (p!=null) {
            prev = p;
            if (element.compareTo(p.element)<0) {
                p = p.left;
            }
            else {
                p = p.right;
            }
        }
        if (root == null) {
            root = new BSTNode<T>(element);
        }
        else if (element.compareTo(prev.element) < 0) {
            prev.left = new BSTNode<T>(element);
        }
        else {
            prev.right = new BSTNode<T>(element);
        }

    }

    public boolean deleteByMerge(T element)
    {
        BSTNode<T> temp, node, p = root;
        BSTNode<T> prev = null;

        //find element
        while (p!=null && !p.element.equals(element)) {
            prev = p;
            if (element.compareTo(p.element) < 0)
                p = p.left;
            else
                p = p.right;
        }

        node = p;
        if (p!=null && element.equals(p.element)) {
            if (node.right == null)
                node = node.left;
            else if (node.left == null)
                node = node.right;
            else {
                temp = node.left;
                while (temp.right != null) {
                    temp = temp.right;
                }
                temp.right = node.right;
                node = node.left;
            }
            if (p==root)
                root = node;
            else if (prev.left == p)
                prev.left = node;
            else
                prev.right = node;
            return true;
        }

        else if (root != null)
            return false;
        else
            return false;
    }

    public boolean deleteByCopy(T element) {
        BSTNode<T> node, p = root;
        BSTNode<T> prev = null;

        while (p!=null && !p.element.equals(element)) {
            prev = p;
            if (element.compareTo(p.element) < 0)
                p = p.left;
            else p = p.right;
        }

        node = p;

        if (p != null && element.equals(p.element)) {
            if (node.right == null)
                node = node.left;
            else if (node.left == null)
                node = node.right;
            else {
                BSTNode<T> temp = node.left;
                BSTNode<T> previous = node;
                while (temp.right != null) {
                    previous = temp;
                    temp = temp.right;
                }
                node.element = temp.element;

                if (previous == node)
                    previous.left = temp.left;
                else
                    previous.right = temp.left;
            }

            if (p == root)
                root = node;
            else if (prev.left == p)
                prev.left = node;
            else
                prev.right = node;
            return true;
        }
        else if (root != null) {
            //Not in tree
            return false;
        }
        else {
            //Empty
            return false;
        }
    }

    public T search(T element)
    {
        //Your code goes here
        BSTNode<T> p = root;
        while (p != null) {
            if (element.equals(p.element))
                return p.element;
            else if (element.compareTo(p.element) <0)
                p = p.left;
            else
                p = p.right;
        }
        return null;
    }


    //Helper functions


}