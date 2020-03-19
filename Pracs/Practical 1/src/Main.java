// Name: Berne' Nortier
// Student number: 17091820

import java.util.Random;
import java.util.Arrays;

public class Main
{

    public static void firstKey(SkipList<?> skiplist)
    {
        if (skiplist.isEmpty())
            System.out.println("List is empty");
        else
            System.out.println("First key : " + skiplist.first());
    }

    public static void lastKey(SkipList<?> skiplist)
    {
        if (skiplist.isEmpty())
            System.out.println("List is empty");
        else
            System.out.println("Last key : " + skiplist.last());
    }

    public static void deleteKey(SkipList<Integer> skiplist, Integer key)
    {
        if (skiplist.isEmpty())
            System.out.println("List is empty");
        else
        {
            boolean result = skiplist.delete(key);
            if (result)
                System.out.println("Deleted key " + key);
            else
                System.out.println("Key " + key + " not found for deletion");
        }
    }

    public static void deleteKey(SkipList<Character> skiplist, Character key)
    {
        if (skiplist.isEmpty())
            System.out.println("List is empty");
        else
        {
            boolean result = skiplist.delete(key);
            if (result)
                System.out.println("Deleted key " + key);
            else
                System.out.println("Key " + key + " not found for deletion");
        }
    }

    public static void searchKey(SkipList<Integer> skiplist, Integer key)
    {
        if (skiplist.isEmpty())
            System.out.println("List is empty");
        else
        {
            Integer result = skiplist.search(key);
            if (result != null)
                System.out.println("Found key " + result);
            else
                System.out.println("Key " + key + " not found");
        }
    }

    public static void searchKey(SkipList<Double> skiplist, Double key)
    {
        if (skiplist.isEmpty())
            System.out.println("List is empty");
        else
        {
            Double result = skiplist.search(key);
            if (result != null)
                System.out.println("Found key " + result);
            else
                System.out.println("Key " + key + " not found");
        }
    }

    public static void searchKey(SkipList<Character> skiplist, Character key)
    {
        if (skiplist.isEmpty())
            System.out.println("List is empty");
        else
        {
            Character result = skiplist.search(key);
            if (result != null)
                System.out.println("Found key " + result);
            else
                System.out.println("Key " + key + " not found");
        }
    }


    public static void printList(SkipList<?> skiplist)
    {
        System.out.println();
        System.out.println("Skip List Content:");
        for (int i = 0; i < skiplist.maxLevel; i++)
        {
            SkipListNode<?> node = skiplist.root[i];
            System.out.print("Level " + i + ": ");
            while (node != null)
            {
                System.out.print(node.key + " ");
                node = node.next[i];
            }
            System.out.println();
        }
        System.out.println();
    }



    public static void main(String[] args)
    {
        //GIVEN TESTS
        System.out.println("=============================GIVEN TESTS=============================");
        SkipList<Integer> skiplist = new SkipList<Integer>();
        skiplist.insert(8);
        skiplist.insert(15);
        skiplist.insert(9);
        skiplist.insert(18);
        firstKey(skiplist);
        lastKey(skiplist);
        printList(skiplist);
        searchKey(skiplist, 10);
        skiplist.insert(10);
        deleteKey(skiplist, 18);
        deleteKey(skiplist, 9);
        deleteKey(skiplist, 3);
        printList(skiplist);
        firstKey(skiplist);
        lastKey(skiplist);
        searchKey(skiplist, 10);


        //PRESTON TEST
        System.out.println("\n=============================PRESTON TESTS=============================");
        System.out.println("------------INTEGER SKIP LIST------------");
        SkipList<Integer> iList = new SkipList<Integer>();
        printList(iList);
        searchKey(iList, 18);
        firstKey(iList);
        lastKey(iList);
        System.out.println();
        System.out.println("Inserting 8");
        iList.insert(8);
        System.out.println("Inserting 5");
        iList.insert(5);
        System.out.println("Inserting 12");
        iList.insert(12);
        System.out.println();
        searchKey(iList, 9);
        System.out.println();
        System.out.println("Inserting 18");
        iList.insert(18);
        printList(iList);
        firstKey(iList);
        lastKey(iList);
        System.out.println();
        searchKey(iList, 18);
        System.out.println("\nInserting 3");
        iList.insert(3);
        printList(iList);
        firstKey(iList);
        lastKey(iList);
        printList(iList);
        searchKey(iList, 10);
        System.out.println("\nInserting 10");
        iList.insert(10);
        printList(iList);
        searchKey(iList, 10);

        System.out.println("\n------------DOUBLE SKIP LIST------------");
        SkipList<Double> dList = new SkipList<Double>();
        printList(dList);
        System.out.println("Inserting 18.1");
        dList.insert(18.1);
        printList(dList);
        firstKey(dList);
        lastKey(dList);
        System.out.println("\nInserting 18.13");
        dList.insert(18.13);
        printList(dList);
        firstKey(dList);
        lastKey(dList);
        System.out.println();
        searchKey(dList, 19.0);
        System.out.println();
        searchKey(dList, 18.13);




        //JO-ANNE TESTING
        System.out.println("=============================JO TESTS=============================");
        SkipList<Integer> bList = new SkipList<Integer>(6);
        //Array
        Random rd = new Random();
        rd.setSeed(123456789);
        int arrSize = 20;
        int[] arr = new int[arrSize];
        for (int i = 0; i < arrSize; i++) {
            arr[i] = Math.abs(rd.nextInt()) % 200 - 100 ;               //Values [-100;100]
        }
        System.out.println("Elements for insertion:\n"+Arrays.toString(arr));

        //Populate
        for (int i = 0; i < arr.length; i++) {
            bList.insert(arr[i]);
        }

        firstKey(bList);
        lastKey(bList);
        searchKey(bList, 10);


        printList(bList);
        //Delete
        for (int i = arrSize-1; i >= 0; i--) {
            deleteKey(bList,arr[i]);

            if (i==9) {
                firstKey(bList);
                lastKey(bList);
                searchKey(bList, 10);
                searchKey(bList, arr[5]);
            }

            printList(bList);
        }

        firstKey(bList);
        searchKey(bList, 95);
        lastKey(bList);


        System.out.println("\n------------CHAR SKIP LIST------------");
        SkipList<Character> cList = new SkipList<Character>(3);
        printList(cList);
        System.out.println("Inserting e");
        cList.insert('e');
        printList(cList);
        firstKey(cList);
        lastKey(cList);
        System.out.println("\nInserting m");
        cList.insert('m');
        System.out.println("\nInserting a");
        cList.insert('a');
        System.out.println("\nInserting n");
        cList.insert('n');
        System.out.println("\nInserting u");
        cList.insert('u');

        printList(cList);
        firstKey(cList);
        lastKey(cList);
        System.out.println();
        searchKey(cList, 'e');
        System.out.println();
        searchKey(cList, 'f');
        System.out.println("\nDeleting m");
        deleteKey(cList, 'm');
        System.out.println("\nDeleting z");
        deleteKey(cList,'z');
        System.out.println("\nDeleting u");
        deleteKey(cList, 'u');
        printList(cList);
        firstKey(cList);
        lastKey(cList);
    }
}

causeitsworthit
