/*
Name and Surname: 
Student Number: 
*/
import java.util.Random;

public class Tester
{
   static Random rd = new Random();
   static int numChecks = 25;

   public static boolean isValidAVL(ThreadedAVLNode<Integer> node) {
      if (node!=null) {
         if (Math.abs(node.balanceFactor)==2) return false;
         isValidAVL(node.left);
         isValidAVL(node.right);
      }
      return true;
   }

   public static void checkDelete(int seed, ThreadedAVLTree<Integer> t) {
      ThreadedAVLTree t2 = t.clone();
      int size = t.getNumberOfNodes();
      System.out.println("\nDelete all nodes from root");
      int i;
      for (i = 0; i < size; i++) {
         //System.out.println("Try: " + t.getRoot().data);
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
             //System.out.println("Deleted: " + val);
             //System.out.println("Size: " + t2.getNumberOfNodes());
             //System.out.println("Valid: " + isValidAVL(t2.getRoot()));*
             //t2.myOwnPreOrder(t2.getRoot());
             //System.out.println("");
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


   public static void main(String[] args) throws Exception
   {
      rd.setSeed(1234567890);
      //Testing Insert
      ThreadedAVLTree<Integer> t1 = new ThreadedAVLTree<Integer>();
      int numDuples = 0;
      for (int i = 0; i < numChecks; i++) {
         int val = Math.abs(rd.nextInt()%numChecks);
         //System.out.print(val + " ");
         if (!t1.insert(val)) {
            numDuples++;
         }
      }
      System.out.println("INSERT");
      System.out.println("Numchecks: " + numChecks);
      System.out.println("NumDuples: " + numDuples);
      System.out.println("NumNodes: " + t1.getNumberOfNodes());
      System.out.println("Valid: " + isValidAVL(t1.getRoot()));

      //TESTING DELETE
      ThreadedAVLTree<Integer> t2 = new ThreadedAVLTree<Integer>();
      Integer arr[] = {15,10,30,5,20,11,40,12};
      for (int i = 0; i < 8; i++) {
         t2.insert(arr[i]);
      }

      System.out.println("\nTree 1");
/*      System.out.println("First: " + t1.findLeftestNode(t1.getRoot()).data);
      System.out.println("Height " + t1.getHeight());
      System.out.println("#Nodes " + t1.getNumberOfNodes());*/
      t1.myOwnPreOrder(t1.getRoot());
      System.out.println(" ");
      ThreadedAVLTree<Integer> t1_c = t1.clone();
      t1_c.myOwnPreOrder(t1_c.getRoot());


      System.out.println("\nTree 2");
/*      System.out.println("First: " + t2.findLeftestNode(t2.getRoot()).data);
      System.out.println("Height " + t2.getHeight());
      System.out.println("#Nodes " + t2.getNumberOfNodes());*/
      System.out.println("Before insertion");
      t2.myOwnPreOrder(t2.getRoot());
      System.out.println(" ");
      ThreadedAVLTree<Integer> t2_c = t2.clone();
      t2_c.myOwnPreOrder(t2_c.getRoot());
      //checkDelete(123456, t2);

      t2.insert(-20);
      System.out.println("\nAfter Insertion");
      t2.myOwnPreOrder(t2.getRoot());
      System.out.println(" ");
      t2_c.myOwnPreOrder(t2_c.getRoot());


/*
      BinaryTreePrinter p = new BinaryTreePrinter();
      p.printNode(t1.getRoot());
*/

      /*System.out.println("DELETE");
      System.out.println("Tree 1");
      //t1.myOwnPreOrder(t1.getRoot());
      t1.delete(441);
      checkDelete(123456,t1);*/



   }
}
