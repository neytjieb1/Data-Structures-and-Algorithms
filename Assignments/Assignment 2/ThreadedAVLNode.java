/*
Name and Surname: 
Student Number: 
*/

import javax.swing.plaf.TableHeaderUI;

public class ThreadedAVLNode<T extends Comparable<? super T>>
{
   /*
   TODO: You must implement a node class which would be appropriate to use with your trees.
   
   You may add methods and data fields. You may NOT alter the given class name or data fields.
   */
   
   protected T data;                      // stored data
   protected int balanceFactor;           // balance factor (follow the definition in the textbook and slides exactly)
   protected ThreadedAVLNode<T> left;     // right child
   protected ThreadedAVLNode<T> right;    // left child
   protected boolean hasThread;           // flag indicating whether the right pointer is a thread

   public ThreadedAVLNode(T val, int bf) {
      this.data = val;
      this.balanceFactor = bf;
      this.left = null;
      this.right = null;
      this.hasThread = false;
   }

   public ThreadedAVLNode(T val) {
      this(val, 0);
   }

   public ThreadedAVLNode() {
      this(null,0);
   }

   public void printNodeInfo() {
      if (this!=null) {
         System.out.println("Data: " + data);
         System.out.println("BF: " + balanceFactor);
         System.out.println("Right : " + right);
         System.out.println("Left: " + left);
         System.out.println("Thread: " + hasThread);
      }
      else System.out.println("Node is empty");
      System.out.println(" ");
   }



}
