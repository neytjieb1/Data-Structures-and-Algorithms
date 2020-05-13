@SuppressWarnings("unchecked")
class BTreeNode<T extends Comparable<T>> {
    boolean leaf;
    int keyTally;
    Comparable<T> keys[];
    BTreeNode<T> references[];
    int m;
    static int level = 0;

    // Constructor for BTreeNode class
    public BTreeNode(int order, boolean leaf1) {
        // Copy the given order and leaf property
        m = order;
        leaf = leaf1;

        // Allocate memory for maximum number of possible keys
        // and child pointers
        keys = new Comparable[2 * m - 1];
        references = new BTreeNode[2 * m];

        // Initialize the number of keys as 0
        keyTally = 0;
    }

    // Function to print all nodes in a subtree rooted with this node
    public void print() {
        level++;
        if (this != null) {
            System.out.print("Level " + level + " ");
            this.printKeys();
            System.out.println();

            // If this node is not a leaf, then
            // print all the subtrees rooted with this node.
            if (!this.leaf) {
                for (int j = 0; j < 2 * m; j++) {
                    if (this.references[j] != null)
                        this.references[j].print();
                }
            }
        }
        level--;
    }

    // A utility function to print all the keys in this node
    private void printKeys() {
        System.out.print("[");
        for (int i = 0; i < this.keyTally; i++) {
            System.out.print(" " + this.keys[i]);
        }
        System.out.print("]");
    }

    // A utility function to give a string representation of this node
    public String toString() {
        String out = "[";
        for (int i = 1; i <= (this.keyTally - 1); i++)
            out += keys[i - 1] + ",";
        out += keys[keyTally - 1] + "] ";
        return out;
    }

    ////// You may not change any code above this line //////

    ////// Implement the functions below this line //////

    // Function to insert the given key in tree rooted with this node
    public BTreeNode<T> insert(T key) {
        // Your code goes here
        if (this.keyTally == this.keys.length) {  //if full: (will only happen with root)
            this.split(-1);
        }
        //continue normally

        //while (this != null) {   //(can actually say while(true)?
        int i = findInfimum(key); //(place where to start insertion is also index of reference)
        if (references[i] == null) {
            insertNonFullNode(i, key);
            //return this;
        } else if (references[i].keyTally == (2 * m - 1)) {  //full next node
            this.split(i);
            if (this.keys[i].compareTo(key) < 0)
                this.references[i + 1].insert(key);
            else
                this.references[i].insert(key);
        } else {//insert into that node
            references[i].insert(key);
            //
        }
        //}
        return this;
    }

    private void split(int index) {

        int midIndex = Math.floorDiv(keys.length - 1, 2);
        if (index == -1) {  //ie. root
            //create nodes
            BTreeNode<T> lChild = new BTreeNode<T>(m, this.leaf);
            BTreeNode<T> rChild = new BTreeNode<T>(m, this.leaf);
            this.leaf = false;

            for (int i = 0; i < midIndex; i++) {            //move values to left child
                lChild.insertNonFullNode(i, (T) this.keys[i]);
                lChild.references[i] = this.references[i];
                this.keys[i] = null;
                this.references[i] = null;
            }
            lChild.references[midIndex] = this.references[midIndex];
            this.references[midIndex] = null;

            for (int i = midIndex + 1; i < this.keyTally; i++) {      //move values to right child
                rChild.insertNonFullNode(i - (midIndex + 1), (T) keys[i]);
                rChild.references[i - (midIndex + 1)] = this.references[i];
                this.keys[i] = null;
                this.references[i] = null;
            }
            rChild.references[rChild.keyTally] = this.references[keyTally];
            this.references[keyTally] = null;

            this.keys[0] = this.keys[midIndex];         //update root
            this.keys[midIndex] = null;
            this.keyTally = 1;
            this.leaf = false;
            this.references[0] = lChild;
            this.references[1] = rChild;

        } else {           //non-root, references[index] is full ; won't ever be full itself
            //create pointer
            BTreeNode<T> refNode = this.references[index];

            //move midValue of references[index] up to this

            this.insertNonFullNode(findInfimum((T) refNode.keys[midIndex]), (T) refNode.keys[midIndex]);

            //create rChild
            BTreeNode<T> rChild = new BTreeNode<T>(m, refNode.leaf);

            //move values to right child
            for (int i = midIndex + 1; i < refNode.keyTally; i++) {
                rChild.insertNonFullNode(i - (midIndex + 1), (T) refNode.keys[i]);
                rChild.references[i - (midIndex + 1)] = refNode.references[i];
                refNode.keys[i] = null;
                refNode.keyTally--;
                refNode.references[i] = null;
            }
            rChild.references[rChild.keyTally] = refNode.references[refNode.keyTally+1];
            refNode.references[refNode.keyTally+1] = null;
            refNode.keys[midIndex] = null;
            refNode.keyTally--;


            //update references of this
            this.references[index] = refNode;
            this.references[index + 1] = rChild;

        }
    }

    private void insertNonFullNode(int i, T val) {
        BTreeNode<T> ref = null;
        Boolean start = (i==0);
        if (this.keys[i] == val)
            return;

        T tempVal = (T) this.keys[i];
        BTreeNode<T> tempRef = references[i];

        //determine if ref should be changed.
        if (i==0)/* && !references[0].keys[0].equals(val))*/ {
            keys[i] = val;
            // ignore references[0]
            val = tempVal;
            ref = null;
            tempVal = (T) keys[++i];
            tempRef = references[i];
        }

        while (tempVal != null && i < keys.length) {
            keys[i] = val;
            references[i] = ref;
            val = tempVal;
            ref = tempRef;
            tempVal = (T) keys[++i];
            tempRef = references[i];
        }
        keys[i++] = val;
        if (tempRef != null) references[i] = tempRef;
        else references[i] = ref;
        if (start) references[i-1] = ref;
        keyTally++;

    }

    private int findInfimum(T key) {
        int i = 0;
        while (this.keys[i] != null && key.compareTo((T) this.keys[i]) > 0) ++i;
        return i;
    }


    // Function to search a key in a subtree rooted with this node
    public BTreeNode<T> search(T key) {
        // Your code goes here
        if (this==null) return null;
        for (int i = 0; i < this.keyTally; i++) {
            if (key==this.keys[i]) return this;
            else if (key.compareTo((T)this.keys[i]) < 0) {
                return references[i].search(key);
            }
            // else key > this.keys[i] so do nothing
        }

        //went all the way through, one remaining case
        if (references[keyTally]==null) return null;
        else return references[keyTally].search(key);

    }

    // Function to traverse all nodes in a subtree rooted with this node
    public void traverse() {
        // Your code goes here
        if (this.leaf) {
            for (int i = 0; i < this.keyTally; i++) {
                System.out.print(" "+this.keys[i]);
            }
        }
        else {
            for (int i = 0; i < this.keyTally; i++) {
                this.references[i].traverse();
                System.out.print(" "+this.keys[i]);
            }
            this.references[keyTally].traverse();
        }
    }
}