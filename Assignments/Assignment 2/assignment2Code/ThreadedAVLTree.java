/*
Name and Surname: 
Student Number: 
*/

public class ThreadedAVLTree<T extends Comparable<? super T>>
{
   /*
   TODO: You must complete each of the methods in this class to create your own threaded AVL tree.
   Note that the AVL tree is single-threaded, as described in the textbook and slides.
   
   You may add any additional methods or data fields that you might need to accomplish your task.
   You may NOT alter the given class name, data fields, or method signatures.
   */
   
   private ThreadedAVLNode<T> root;       // the root node of the tree
   
   public ThreadedAVLTree()
   {
      /*
      The default constructor
      */
   }
   
   public ThreadedAVLTree(ThreadedAVLTree<T> other)
   {
      /*
      The copy constructor
      */
   }
   
   public ThreadedAVLTree<T> clone()
   {
      /*
      The clone method should return a copy/clone of this tree.
      */
      
      return null;
   }
   
   public ThreadedAVLNode<T> getRoot()
   {
      /*
      Return the root of the tree.
      */
      
      return root;
   }
   
   public int getNumberOfNodes()
   {
      /*
      This method should count and return the number of nodes currently in the tree.
      */
      
      return 0;
   }
   
   public int getHeight()
   {
      /*
      This method should return the height of the tree. The height of an empty tree is 0; the
      height of a tree with nothing but the root is 1.
      */
      
      return 0;
   }
   
   public boolean insert(T element)
   {
      /*
      The element passed in as a parameter should be inserted into the tree. Duplicate values are
      not allowed in the tree. Threads must be updated accordingly, as necessary. If the insertion
      operation is successful, the method should return true. If the insertion operation is
      unsuccessful for any reason, the method should return false.
      
      NB: Do not throw any exceptions.
      */
      
      return false;
   }
   
   public boolean delete(T element)
   {
      /*
      The element passed in as a parameter should be deleted from this tree. Threads must be
      updated accordingly, as necessary. If the deletion operation was successful, the method
      should return true. If the deletion operation is unsuccessful for any reason (e.g. the
      requested element is not found in the tree), the method should return false.
      
      NB: Do not throw any exceptions.
      */
      
      return false;
   }
   
   public boolean contains(T element)
   {
      /*
      This method searches for the element passed in as a parameter. If the element is found, true
      should be returned. If the element is not in the tree, the method should return false.
      */
      
      return false;
   }
   
   public String inorder()
   {
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
   
   public String inorderDetailed()
   {
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
   
   public String preorder()
   {
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
   
   public String preorderDetailed()
   {
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
}
