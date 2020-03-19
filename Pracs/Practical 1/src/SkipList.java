// Name: Berne' Nortier
// Student number: 17091820

import java.util.Random;

@SuppressWarnings("unchecked")
public class SkipList<T extends Comparable<? super T>>
{

    public int maxLevel;
    public SkipListNode<T>[] root;
    private int[] powers;
    private Random rd = new Random();

    SkipList(int i)
    {
        maxLevel = i;
        root = new SkipListNode[maxLevel];
        powers = new int[maxLevel];
        for (int j = 0; j < i; j++)
            root[j] = null;
        choosePowers();
        rd.setSeed(1230456789);
    }

    SkipList()
    {
        this(4);
    }

    public void choosePowers()
    {
        powers[maxLevel-1] = (2 << (maxLevel-1)) - 1;
        for (int i = maxLevel - 2, j = 0; i >= 0; i--, j++)
            powers[i] = powers[i+1] - (2 << j);
    }

    public int chooseLevel()
    {
        int i, r = Math.abs(rd.nextInt()) % powers[maxLevel-1] + 1;
        for (i = 1; i < maxLevel; i++)
        {
            if(r < powers[i])
                return i-1;
        }
        return i-1;
    }

    ////// You may not change any code above this line //////

    ////// Implement the functions below this line //////
//================================================================================================================
    public boolean isEmpty()
    {
        return root[0] == null;
    }

    public void insert(T key)
    {
        SkipListNode<T>[] curr = new SkipListNode[maxLevel];
        SkipListNode<T>[] prev = new SkipListNode[maxLevel];
        SkipListNode<T> newNode;
        int lvl;

        curr[maxLevel - 1] = root[maxLevel - 1];            //why -1? because top one guaranteed to be empty?
        prev[maxLevel - 1] = null;

        for (lvl = maxLevel - 1; lvl >= 0; lvl--) {         //going down the levels
            //skip ahead
            while (curr[lvl] != null && curr[lvl].key.compareTo(key) < 0) {         //Notie. curr at level pointing to a key less than object's key
                prev[lvl] = curr[lvl];
                curr[lvl] = curr[lvl].next[lvl];
            }
            //if found
            if (curr[lvl] != null && curr[lvl].key.equals(key))                 //why do we check null?
                return;                                                         // Don't include duplicate values
            //still not base level --> not found yet
            if (lvl > 0)
                //go down one level and still at start
                if (prev[lvl] == null) {
                    curr[lvl - 1] = root[lvl - 1];
                    prev[lvl - 1] = null;
                }
                else {
                    //go down one level but not back to start
                    curr[lvl - 1] = prev[lvl].next[lvl - 1];
                    prev[lvl - 1] = prev[lvl];
                }
        }

        //place now found. Choose some random level for insertion
        lvl = chooseLevel();
        //initialise newNode with data
        newNode = new SkipListNode<T>(key, lvl + 1);
        //at each level
        for (int i = 0; i <= lvl; i++) {
            newNode.next[i] = curr[i];                                  //link to next
            if (prev[i] == null)                                        //link back  (if first)
                root[i] = newNode;
            else                                                        //link back
                prev[i].next[i] = newNode;
        }
    }

    private SkipListNode<T> findAlreadyFoundNode(T key)
    {
        SkipListNode<T> temp = root[0];
        while (temp.key != key) {
            temp = temp.next[0];
        }
        return temp;
    }

    public boolean delete(T key) {
        //exist in list?
        T found = search(key);

        //if yes
        if (found != null) {

            //find Node
            SkipListNode<T> delNode = findAlreadyFoundNode(key);
            int delNodeLevel = delNode.next.length;

            //Print info
            //printNodeInfo(delNode);

            //Prev & Ptr
            SkipListNode<T>[] prev = new SkipListNode[delNodeLevel];
            SkipListNode<T> ptr = root[delNodeLevel - 1];
            //boolean first = false;


            //make list of predecessors
            for (int i = delNodeLevel - 1; i >= 0; i--) {
                //top level down
                //System.out.println("Level " + (i) + "\tFirst:" + (root[i]==delNode) + "\tKey: " + delNode.key) ;
                ptr = root[i];
                //first element?
                if (root[i] == delNode) {
                    //first = true;
                    prev[i] = root[i];
                }
                //middle or last element
                else {

                    while (ptr.next[i] != delNode) {
                        ptr = ptr.next[i];
                    }
                    prev[i] = ptr;
                    //System.out.println("Ptr: " + ptr.key + "\tNext at level " + i + ":" + ptr.next[i].key);
                    //ptr = root[i];
                }
            }
            //printPrevInfo(prev);

            //Predecessors found
            for (int i = 0; i < delNodeLevel; i++) {
                if (root[i] == delNode) {
                    root[i] = delNode.next[i];
                } else {
                    prev[i].next[i] = delNode.next[i];
                }
            }

            //Done
            return true;
        }

        //value does not exist in list
        else {
            return false;
        }
    }

    public T first()
    {
        return root[0].key;
    }

    public T last()
    {
        SkipListNode<T> temp = root[0];
        while (temp.next[0] != null) {
            temp = temp.next[0];
        }
        return temp.key;
    }

    public T search(T key) {
        int lvl;
        SkipListNode<T> curr;
        SkipListNode<T> prev;
        for (lvl = maxLevel - 1; lvl >= 0 && root[lvl] == null; lvl--) ;
        prev = curr = root[lvl];
        while (true) {
            if (key.equals(curr.key))
                return curr.key;
            else if (key.compareTo(curr.key) < 0) {
                if (lvl == 0)
                    return null;
                else if (curr == root[lvl])
                    curr = root[--lvl];
                else
                    curr = prev.next[--lvl];
            } else {
                prev = curr;
                if (curr.next[lvl] != null)
                    curr = curr.next[lvl];
                else {
                    for (lvl--; lvl >= 0 && curr.next[lvl] == null; lvl--) ;
                    if (lvl >= 0)
                        curr = curr.next[lvl];
                    else
                        return null;
                }
            }
        }
    }

}