/*
Name and Surname: 
Student Number: 
*/
import java.util.Random;

public class Tester 
{
   public static void main(String[] args) throws Exception
   {
      /*
      TODO: Write code to thoroughly test your implementation here.
      Note that this file will be overwritten for marking purposes.
      */

      //Testing Insert
      ThreadedAVLTree<Integer> t1 = new ThreadedAVLTree<Integer>();
      Random rd = new Random();
      rd.setSeed(123456789);
      int numChecks = 20;
      for (int i = 0; i < numChecks; i++) {
         int val = Math.abs(rd.nextInt()%50);
         System.out.print(val + " ");
         t1.insert(val);
      }
      System.out.println("\nPreOrder");
      t1.myOwnPreOrder(t1.getRoot());




   }
}
