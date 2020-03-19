/**
 * Name: B Nortier
 * Student Number: 17091820
 */

import java.util.Random;

public class Main {

    public static void searchList(OrganisingList orglist, Integer key)
    {
        ListNode result = orglist.searchNode(key);
        if (result != null)
            System.out.println("Found node " + result.toString());
        else
            System.out.println("Node with key " + key + " not found");
    }

    public static void printList(OrganisingList orglist)
    {
        ListNode node = orglist.root;
        if (node == null)
        {
            System.out.println("List is empty");
        }
        else
        {
            System.out.println("Organising List Content:");

            while (node != null)
            {
                System.out.println(node.toString());
                node = node.next;
            }
        }
    }

    public static void printKeys(OrganisingList orglist)
    {
        ListNode node = orglist.root;
        if (node == null)
        {
            System.out.println("List is empty");
        }
        else
        {
            System.out.print(node.key);
            while (node.next != null)
            {
                node = node.next;
                System.out.print(", " + node.key);
            }
            System.out.println();

        }
    }

    public static void testList(OrganisingList mylist) {
        //Multiple Values
        Random rd = new Random();
        int length = 10;
        Integer arr[] = new Integer[length];
        for (int i = 0; i < length; i++) {
            arr[i] = (rd.nextInt()%50);
            System.out.println("Inserting " + arr[i]);
            if (mylist.contains(arr[i])) {
                System.out.println("Duplicate");
                mylist.insert(arr[i]);
            }
            else {
                mylist.insert(arr[i]);
                printKeys(mylist);
            }
        }
        System.out.println('\n');

        //Searching existing values
        for (int i = 0; i < 5; i++) {
            int index = Math.abs(rd.nextInt()%9);
            //System.out.println("Looking for " + arr[index]);
            searchList(mylist,arr[index]);
            printKeys(mylist);
            //System.out.println('\n');
        }

        //Search First Node
        System.out.println("\nSearching first element");
        searchList(mylist, mylist.root.key);
        printKeys(mylist);
        //System.out.println('\n');

        //Searching Last Node
        System.out.println("\nSearching last element");
        searchList(mylist, mylist.findTail().key);
        printKeys(mylist);
        System.out.println('\n');

        //Searching strange values
        for (int i = 0; i < 5; i++) {
            int val = rd.nextInt()%100;
            //System.out.println("Looking for" + val);
            searchList(mylist, val);
            printKeys(mylist);
        }

        //Deletion: Nonexistent
        System.out.println("\nDeleting " + 957 + "\nBefore:");
        printKeys(mylist);
        mylist.delete(957);
        System.out.println("After:");
        printKeys(mylist);
        //Deletion: first
        System.out.println("\nDeleting first element \nBefore:");
        printKeys(mylist);
        mylist.delete(mylist.root.key);
        System.out.println("After:");
        printKeys(mylist);
        //Deletion: last
        System.out.println("\nDeleting last element \nBefore:");
        printKeys(mylist);
        mylist.delete(mylist.findTail().key);
        System.out.println("After:");
        printKeys(mylist);
        //Deletion:Other
        for (int i = 0; i < length; i++) {
            System.out.println("\nDeleting " + arr[i]+ "\nBefore:");
            printKeys(mylist);
            mylist.delete(arr[i]);
            System.out.println("After:");
            printKeys(mylist);
        }
    }

    public static void main(String[] args)
    {
        OrganisingList mvflist = new MoveToFrontOrganisingList();
        System.out.println("=========================================================\nTesting Move-To-Front\n=========================================================");
        testList(mvflist);

        OrganisingList tlist = new TransposeOrganisingList();
        System.out.println("\n=========================================================\nTesting Transpose\n=========================================================");
        testList(tlist);



    }
}