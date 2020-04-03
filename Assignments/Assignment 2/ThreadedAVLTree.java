/*
Name and Surname: 
Student Number: 
*/

import java.util.LinkedList;
import java.util.Queue;

public class ThreadedAVLTree<T extends Comparable<? super T>>
{
   /*
   TODO: You must complete each of the methods in this class to create your own threaded AVL tree.
   Note that the AVL tree is single-threaded, as described in the textbook and slides.
   
   You may add any additional methods or data fields that you might need to accomplish your task.
   You may NOT alter the given class name, data fields, or method signatures.
   */
   
   private ThreadedAVLNode<T> root;       // the root node of the tree

   //Constructors
   public ThreadedAVLTree() {
      /*
      The default constructor
      */
      root = null;
   }
   public ThreadedAVLTree(ThreadedAVLTree<T> other) {
      /*
      The copy constructor
      */
      root = other.clone().root;
   }


   public ThreadedAVLTree<T> clone() {
      ThreadedAVLTree<T> clTree = new ThreadedAVLTree<T>();
      clTree.root = clone(root);
      return clTree;
   }
   private ThreadedAVLNode<T> clone(ThreadedAVLNode node) {
      if (node==null) {
         return null;
      }
      else {
         ThreadedAVLNode<T> newNode = new ThreadedAVLNode<T>((T)node.data, node.balanceFactor, node.hasThread);
         newNode.left = clone(node.left);
         newNode.right = clone(node.right);
         return newNode;
      }
   }


   public ThreadedAVLNode<T> getRoot()
   {
      return root;
   }
   
   public int getNumberOfNodes() {
      /*
      This method should count and return the number of nodes currently in the tree.
      */
     if (root==null) {
        return 0;
     }
     return getNumberOfNodes(root);
   }
   private int getNumberOfNodes(ThreadedAVLNode<T> n) {
      if (n!=null) {
         return 1 + getNumberOfNodes(n.left) + getNumberOfNodes(n.right);
      }
      return 0;
   }
   
   public int getHeight() {
      /*
      This method should return the height of the tree. The height of an empty tree is 0; the
      height of a tree with nothing but the root is 1.
      */
      return getHeight(root);
   }
   private int getHeight(ThreadedAVLNode<T> node) {
      if (node==null) {
         return 0;
      }
      int lh = getHeight(node.left);
      int rh=0;
      if (!node.hasThread) {
         rh = getHeight(node.right);
      }

      return 1 + Math.max(lh,rh);
   }

   public boolean insert(T element) {
      //Step 1
      if (contains(element)) {
         return false;
      }

      //Step 2: insertion
      ThreadedAVLNode<T> p = root;
      ThreadedAVLNode<T> prev = null;
      //find place to insert
      while (p != null) {
         prev = p;
         if (element.compareTo(p.data) < 0) {
            p = p.left;
         } else {
            p = p.right;
         }
      }
      //insert node
      ThreadedAVLNode<T> newNode = new ThreadedAVLNode<T>(element);
      if (root==null) {       //at root
         root = newNode;
         return true;
      }
      else if (element.compareTo(prev.data) < 0) {    //somewhere else
         prev.left = newNode;
      } else {
         prev.right = newNode;
      }

/*      ThreadedAVLNode<T> rGP = findFirstRGreatGP(newNode);
      if (rGP!=null) {
         newNode.hasThread = true;
         newNode.right = rGP;
      }*/


      //Step 3: update BF's
      if (newNode.equals(prev.left)) {
         prev.balanceFactor--;
      }
      else {
         prev.balanceFactor++;
      }
      while (prev!=root && Math.abs(prev.balanceFactor)!=2) {
         newNode = prev;
         prev = getParent(prev.data);
         if (newNode.balanceFactor==0) {
            calculateUpdatedBF(newNode);
            break;
            //??
         }
         if (newNode.equals(prev.left)) {
            prev.balanceFactor--;
         }
         else {
            prev.balanceFactor++;
         }
      }

      //Step 4: Rebalance tree
      p = getParent(prev.data);  //should be null if prev == root
      boolean leftChild = false;
      if (p!=null) {
         leftChild = p.left.equals((prev));
      }
      if (Math.abs(prev.balanceFactor) == 2) {
         prev = rebalInsert(prev);
         if (p==null) {
            root = prev;
         }
         else if (leftChild) {
            p.left = prev;
         }
         else {
            p.right = prev;
         }
      }

      return true;

   }
   public ThreadedAVLNode<T> rebalInsert(ThreadedAVLNode<T> node) {
      if (node.balanceFactor==2 && node.right.balanceFactor==1 || node.balanceFactor==-2 && node.left.balanceFactor==-1) {
         node = homoRotate(node);
      }
      else {
         node = heteroRotate(node);
      }
      calculateUpdatedBF(node);
      return node;

   }


   public boolean delete(T element) {
      //1. Contains Element
      if (!this.contains(element) || root==null) {
         return false;
      }

      //2. Delete by Copy
      ThreadedAVLNode<T> newParent = getParent(element);
      ThreadedAVLNode<T> originalParent = delByCopy(element);


      //3.1 Tree now empty
      if (root==null) {
         return true;
      }

      //3.2 root element was deleted, tree non-empty
      if (originalParent==null) {
         calculateUpdatedBF(root);
         if (Math.abs(root.balanceFactor)==2) {
            root = rebalDelete(root);
         }
         return true;
      }

      //3.3 deleted in middle, rebalance
      while (originalParent!=null) {
         calculateUpdatedBF(originalParent);

         if (Math.abs(originalParent.balanceFactor)==2) {
            ThreadedAVLNode<T> GP = getParent(originalParent.data);
            if (GP==null) {   //now at root
               root = rebalDelete(root);
               return true;
            }
            boolean leftChild = originalParent.equals(GP.left);

            originalParent = rebalDelete(originalParent);

            if (GP==null) root = originalParent;
            else if (leftChild) GP.left = originalParent;
            else GP.right = originalParent;
         }

         originalParent = getParent(originalParent.data);
      }

      return true;

   }
   private ThreadedAVLNode<T> delByCopy(T el) {
      //already know it exists
      ThreadedAVLNode<T> node=root;
      ThreadedAVLNode<T> p = root;
      ThreadedAVLNode<T> prev = null;

      while (p!=null && !p.data.equals(el)) { //finding node in tree
         prev = p;
         if (el.compareTo(p.data) < 0) {
            p = p.left;
         }
         else {
            p = p.right;
         }
      }

      node = p;

      ThreadedAVLNode<T> originalParent = null;
      if (p!=null && p.data.equals(el)) {    //unneccessary condition since will always find node
         if (node.right == null) {
            originalParent = prev;
            node = node.left;
         }
         else if (node.left == null) {
            originalParent = prev;
            node = node.right;
         }
         else {
            //find rightmost node
            ThreadedAVLNode<T> temp = node.left;
            ThreadedAVLNode<T> previous = node;
            while (temp.right!=null) {
               previous = temp;
               temp = temp.right;
            }
            originalParent = getParent(temp.data);
            node.data = temp.data;


            if (previous.data.equals(node.data)) {
               previous.left = temp.left;
            }
            else {
               previous.right = temp.left;
            }
         }

         if (p.data.equals(root.data)) {
            root = node;
         }
         else if (p.equals(prev.left)) {
            prev.left = node;
         }
         else {
            prev.right = node;
         }
      }
      calculateUpdatedBF(originalParent);
      calculateUpdatedBF(node);
      return originalParent;
   }
   private ThreadedAVLNode<T> rebalDelete(ThreadedAVLNode<T> node) {
      if ( (node.balanceFactor==2 && (node.right.balanceFactor==1 || node.right.balanceFactor==0)) ||
           (node.balanceFactor==-2&& (node.left.balanceFactor==-1 || node.left.balanceFactor==0 )) ) {
         node = homoRotate(node);
      }
      else {
         node = heteroRotate(node);
      }
      calculateUpdatedBF(node);
      return node;
   }

   //Helpers
   public ThreadedAVLNode<T> getParent(T el) {
      ThreadedAVLNode<T> Ch = root;
      ThreadedAVLNode<T> P = null;
      while (Ch!=null) {
         if(el.equals(Ch.data)) {
            return P;
         }
         else if (el.compareTo(Ch.data) < 0) {
            P = Ch;
            Ch = Ch.left;
         }
         else {
            P = Ch;
            Ch = Ch.right;
         }
      }
      //throw new ArithmeticException("Insertion obviously didn't work since we can't find element which was supposed to exist");
      return null;
   }
   public ThreadedAVLNode<T> homoRotate(ThreadedAVLNode<T> GP) {
      if (GP.balanceFactor==2) {
         GP = leftRot(GP);
      }
      else {
         GP =rightRot(GP);
      }
      return GP;
   }
   public ThreadedAVLNode<T> heteroRotate(ThreadedAVLNode<T> GP) {
      if (GP.balanceFactor == 2) {
         GP.right = rightRot(GP.right);
         GP = leftRot(GP);
      }
      else {
         GP.left = leftRot(GP.left);
         GP = rightRot(GP);
      }
      return GP;
   }
   public ThreadedAVLNode<T> rightRot(ThreadedAVLNode<T> P) {
      ThreadedAVLNode<T> Ch = P.left;
      P.left = Ch.right;
      Ch.right = P;
      calculateUpdatedBF(Ch);
      calculateUpdatedBF(Ch.right);
      return Ch;
   }
   public ThreadedAVLNode<T> leftRot(ThreadedAVLNode<T> P) {
      ThreadedAVLNode<T> Ch = P.right;
      P.right = Ch.left;
      Ch.left = P;
      calculateUpdatedBF(Ch);
      calculateUpdatedBF(Ch.left);
      return Ch;
   }
   public void calculateUpdatedBF(ThreadedAVLNode <T> node) {
      if (node==null) return;
      node.balanceFactor = getHeight(node.right) - getHeight(node.left);
   }
   public boolean contains(T element) {
      return findNode(element) != null;
   }

   public ThreadedAVLNode<T> findFirstRGreatGP(ThreadedAVLNode<T> node) {
      ThreadedAVLNode<T> parent = getParent(node.data);
      if (parent==null) return null;
      else if (node.equals(parent.left)) return parent;
      else return findFirstRGreatGP(parent);
   }

   public ThreadedAVLNode<T> findLeftestNode(ThreadedAVLNode<T> p) {
      if (p==null) return null;
      while (p.left != null) p = p.left;
      return p;
   }

   public String inorder() {
      /*
      This method must return a string representation of the elements in the tree visited during an
      inorder traversal. This method must not be recursive. Instead, the threads must be utilised
      to perform a depth-first inorder traversal.
      
      Note that there are no spaces in the string, and the elements are comma-separated. Note that
      no comma appears at the end of the string.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      The following string must be returned:
      
      A,B,C,D,E,F
      */
      
      return "";
   }
   public String inorderDetailed() {
      /*
      This method must return a string representation of the elements in the tree visited during an
      inorder traversal. This method must not be recursive. Instead, the threads must be utilised
      to perform a depth-first inorder traversal.
      
      Note that there are no spaces in the string, and the elements are comma-separated.
      Additionally, whenever a thread is followed during the traversal, a pipe symbol should be
      printed instead of a comma. Note that no comma or pipe symbol appears at the end of the
      string. Also note that if multiple threads are followed directly after one another, multiple
      pipe symbols will be printed next to each other.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      In this tree, there is a thread linking the right branch of node A to node B, a thread
      linking the right branch of node B to node C, and a thread linking the right branch of node D
      to node E. The following string must therefore be returned:
      
      A|B|C,D|E,F
      */
      
      return "";
   }
   public String preorder() {
      /*
      This method must return a string representation of the elements in the tree visited during a
      preorder traversal. This method must not be recursive. Instead, the threads must be utilised
      to perform a depth-first preorder traversal. See the last paragraph on page 240 of the
      textbook for more detail on this procedure.
      
      Note that there are no spaces in the string, and the elements are comma-separated. Note that
      no comma appears at the end of the string.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      The following string must be returned:
      
      C,B,A,E,D,F
      */
      
      return "";
   }
   public String preorderDetailed() {
      /*
      This method must return a string representation of the elements in the tree visited during a
      preorder traversal. This method must not be recursive. Instead, the threads must be utilised
      to perform a depth-first preorder traversal. See the last paragraph on page 240 of the
      textbook for more detail on this procedure.
      
      Note that there are no spaces in the string, and the elements are comma-separated.
      Additionally, whenever a thread is followed during the traversal, a pipe symbol should be
      printed instead of a comma. Note that no comma or pipe symbol appears at the end of the
      string. Also note that if multiple threads are followed directly after one another, multiple
      pipe symbols will be printed next to each other.
      
      If the tree looks as follows:
      
          C
         / \
        B   E
       /   / \
      A   D   F
      
      In this tree, there is a thread linking the right branch of node A to node B, a thread
      linking the right branch of node B to node C, and a thread linking the right branch of node D
      to node E. The following string must therefore be returned:
      
      C,B,A||E,D|F
      
      Note that two pipe symbols are printed between A and E, because the thread linking the right
      child of node A to node B is followed, B is not printed because it has already been visited,
      and the thread linking the right child of node B to node C is followed.
      */
      
      return "";
   }


   public void myOwnInorder(ThreadedAVLNode<T> n) {
      if (n!=null) {
         myOwnInorder(n.left);
         System.out.println(n.data + "\tbf: " + n.balanceFactor);
         myOwnInorder(n.right);
      }
   }
   public void myOwnPreOrder(ThreadedAVLNode<T> n) {
      if (n!=null) {
         System.out.println(n.data + "\tbf: " + n.balanceFactor);
         myOwnPreOrder(n.left);
         myOwnPreOrder(n.right);
      }
   }

   public ThreadedAVLNode<T> findNode(T element) {
      ThreadedAVLNode<T> p = root;
      while (p!=null) {
         if (element.equals(p.data)) {
            return p;
         }
         else  if (element.compareTo(p.data) < 0) {
            p = p.left;
         }
         else {
            p = p.right;
         }
      }
      return null;

   }


}
