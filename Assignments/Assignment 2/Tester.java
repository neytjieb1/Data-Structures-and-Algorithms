/*
Name and Surname: 
Student Number: 
*/
import java.util.Random;

public class Tester
{
   static Random rd = new Random();
   static int numChecks = 15;

   public static boolean isValidAVL(ThreadedAVLNode<Integer> node) {
      if (node!=null) {
         if (Math.abs(node.balanceFactor)==2) return false;
         isValidAVL(node.left);
         if (node.hasThread) return true;
         else isValidAVL(node.right);
      }
      return true;
   }

   public static void checkDelete(ThreadedAVLTree<Integer> t) {
      ThreadedAVLTree t2 = t.clone();
      int size = t.getNumberOfNodes();
      System.out.println("\nDelete all nodes from root");
      int i;
      for (i = 0; i < size; i++) {
         t.delete(t.getRoot().data);
         if (!isValidAVL(t.getRoot())) {
              System.out.println("i: " + i + " tree now invalid");
              break;
          }
      }
      System.out.println("After " + i + " checks: " + isValidAVL(t.getRoot()));

      System.out.println("\nDelete randomly until empty");
      boolean deleted;
      i = 0;
      while (t2.getRoot()!=null) {
         int val = Math.abs(rd.nextInt()) % numChecks;
         //System.out.println("\nTry: " + val);
          deleted = t2.delete(val);
          if (deleted) {
             i++;
             if (!isValidAVL(t2.getRoot())) {
                System.out.println("Tree invalid after deleting " + val);
                //t2.myOwnPreOrder(t2.getRoot());
                break;
             }
          }
      }
      System.out.println("After " + i + " checks: " + isValidAVL(t.getRoot()));
   }

   public static void CheckInsert(ThreadedAVLTree<Integer> t1, ThreadedAVLTree<Integer> t2, Integer[] arr) {
       int numDuples = 0;
       for (int i = 0; i < numChecks; i++) {
           int val = Math.abs(rd.nextInt()%numChecks);
           //System.out.print(val + " ");
           if (!t1.insert(val)) {
               numDuples++;
           }
       }
       System.out.println("INSERT Randomly");
       System.out.println("Numchecks: " + numChecks);
       System.out.println("NumDuples: " + numDuples);
       System.out.println("NumNodes: " + t1.getNumberOfNodes());
       System.out.println("Valid: " + isValidAVL(t1.getRoot()));
       BinaryTreePrinter p = new BinaryTreePrinter();
       p.printNode(t1.getRoot());


       for (int i = 0; i < arr.length; i++) {
           t2.insert(arr[i]);
       }
       System.out.println("INSERT ACCORDING TO ARRAY");
       System.out.println("NumNodes: " + t2.getNumberOfNodes());
       System.out.println("Valid: " + isValidAVL(t2.getRoot()));
       p.printNode(t2.getRoot());

   }


   public static void main(String[] args) throws Exception
   {
      rd.setSeed(1234567890);
      //INSERT
       ThreadedAVLTree<Integer> t1 = new ThreadedAVLTree<Integer>();
       ThreadedAVLTree<Integer> t2 = new ThreadedAVLTree<Integer>();
       Integer arr[] = {15,10,30,5,20,11,40,12};
       CheckInsert(t1,t2, arr);



       //TEST CLONE
       System.out.println("CLONE");
       ThreadedAVLTree<Integer> tclone = t2.clone();
       System.out.println("Original before Insert");
       t2.myOwnPreOrder(t2.getRoot());
       System.out.println("Clone before Insert");
       tclone.myOwnPreOrder(tclone.getRoot());
       t2.insert(22);
       System.out.println("Original after insert of 22");
       t2.myOwnPreOrder(t2.getRoot());
       System.out.println("LCone before Insert");
       tclone.myOwnPreOrder(tclone.getRoot());



       /*       System.out.println("DELETE");
       System.out.println("\nTree 2");
       System.out.println("Height " + t2.getHeight());
       System.out.println("#Nodes " + t2.getNumberOfNodes());
       checkDelete(123456, t2);*/

      /*System.out.println("DELETE");
      System.out.println("Tree 1");
      //t1.myOwnPreOrder(t1.getRoot());
      t1.delete(441);
      checkDelete(123456,t1);*/

   }
}
