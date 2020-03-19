/**
 * Name: B Nortier
 * Student Number: 17091820
 */

abstract class OrganisingList {

    public ListNode root = null;

    public OrganisingList() {

    }

    /**
     * Calculate the data field of all nodes in the list using the recursive functions.
     */
    public void calculateData() {
        if (root != null) {
            dataRec(root);
        }
    }

    ////// You may not change any code above this line //////

    ////// Implement the functions below this line     //////


    //Recursive functions

    public static int sumRec(ListNode node) {
        if (node.next == null) {
            node.data = node.key;
            return node.data;
        }
        else {
            node.data = sumRec(node.next) + node.key;
            return node.data;
        }
    }

    public static int dataRec(ListNode node) {
        if (node.next == null) {
            node.data = node.key;
            return node.data;
        }
        else {
            node.data = (node.key*sumRec(node)) - dataRec(node.next);
            return node.data;
        }
    }


    //Organising List functions

    /**
     * Retrieve the node with the specified key, move the node as required and recalculate all data fields.
     * @return The node with its data value before it has been moved, otherwise 'null' if the key does not exist.
     * Implement only in specific subclass!
     */
    public abstract ListNode searchNode(Integer key);

    /**
     * Check if a key is contained in the list
     * @return true if the key is in the list, otherwise false
     */
    public boolean contains(Integer key) {
        //empty
        if (root == null) {
            return false;
        }
        else {
            ListNode temp = root;
            while (temp!=null && temp.key != key) {
                temp = temp.next;
            }
            if (temp==null) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    public ListNode findTail() {
        ListNode tail = root;
        while (tail.next != null) {
            tail = tail.next;
        }
        return tail;
    }

    public void insert(Integer key) {
        //Duplicates
        if (contains(key)) {
            return;
        }
        //Create Node
        ListNode newNode = new ListNode(key);
        newNode.next = null;
        //First
        if (root == null) {
            root = newNode;
        }
        //Middle or last
        else {
            ListNode tail = findTail();
            tail.next = newNode;
        }
        //Update all
        calculateData();

    }

    /**
     * Delete the node with the given key.
     * calculateData() should be called after deletion.
     * If the key does not exist, do nothing.
     */
    public void delete(Integer key) {
        //Duplicate
        if (!contains(key)) {
            return;
        }
        //Create node
        ListNode curr = root;
        ListNode prev = null;

        //First
        if (root.key == key) {
            //only element
            if (root.next == null) {
                root = null;
            }
            else {
                root = root.next;
            }
        }
        //Middle or Last
        else {
            while (curr.next != null && curr.key!=key) {
                prev = curr;
                curr = curr.next;
            }
            if (curr!=null) {
                prev.next = curr.next;
            }
        }
        //Recalculate
        calculateData();

    }

    public ListNode findNode(Integer key) {
        if (!contains(key)) {
            return null;
        }
        else {
            //will only be called if exists
            ListNode findNode = root;
            while (findNode.key != key) {
                findNode = findNode.next;
            }
            return findNode;
        }
    }

    public void Swap(ListNode prev, ListNode curr) {
        ListNode temp = prev.next;
        temp.next = curr.next;
        curr.next = temp;
        prev.next = curr;

    }

}