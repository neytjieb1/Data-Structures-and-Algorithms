/*
Name and Surname: B Nortier
Student Number: 17091820
*/

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

   public ThreadedAVLNode(T val, int bf, boolean thr) {
      this.data = val;
      this.balanceFactor = bf;
      this.left = null;
      this.right = null;
      this.hasThread = thr;
   }

   public ThreadedAVLNode(T val) {
      this(val, 0, false);
   }

   public ThreadedAVLNode() {
      this(null,0,false);
   }



}
