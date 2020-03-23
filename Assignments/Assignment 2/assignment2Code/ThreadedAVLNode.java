/*
Name and Surname: 
Student Number: 
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
}
