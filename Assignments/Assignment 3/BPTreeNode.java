import java.awt.*;

/**
 * A B+ tree generic node
 * Abstract class with common methods and data. Each kind of node implements this class.
 *
 * @param <TKey>   the data type of the key
 * @param <TValue> the data type of the value
 */
abstract class BPTreeNode<TKey extends Comparable<TKey>, TValue> {

    protected Object[] keys;
    protected int keyTally;
    protected int m;
    protected BPTreeNode<TKey, TValue> parentNode;
    protected BPTreeNode<TKey, TValue> leftSibling;
    protected BPTreeNode<TKey, TValue> rightSibling;
    protected static int level = 0;


    protected BPTreeNode() {
        this.keyTally = 0;
        this.parentNode = null;
        this.leftSibling = null;
        this.rightSibling = null;
    }

    public int getKeyCount() {
        return this.keyTally;
    }

    @SuppressWarnings("unchecked")
    public TKey getKey(int index) {
        return (TKey) this.keys[index];
    }

    public void setKey(int index, TKey key) {
        this.keys[index] = key;
    }

    public BPTreeNode<TKey, TValue> getParent() {
        return this.parentNode;
    }

    public void setParent(BPTreeNode<TKey, TValue> parent) {
        this.parentNode = parent;
    }

    public abstract boolean isLeaf();

    // A utility function to give a string representation of this node
    public String toString() {
        String out = "[";
        for (int i = 1; i <= (this.keyTally - 1); i++)
            out += keys[i - 1] + ",";
        out += keys[keyTally - 1] + "] ";
        return out;
    }


    /**
     * Print all nodes in a subtree rooted with this node
     */
    @SuppressWarnings("unchecked")
    public void print(BPTreeNode<TKey, TValue> node) {
        level++;
        if (node != null) {
            System.out.print("Level " + level + " ");
            node.printKeys();
            System.out.println();

            // If this node is not a leaf, then
            // print all the subtrees rooted with this node.
            if (!node.isLeaf()) {
                BPTreeInnerNode inner = (BPTreeInnerNode<TKey, TValue>) node;
                for (int j = 0; j < (node.m); j++) {
                    this.print((BPTreeNode<TKey, TValue>) inner.references[j]);
                }
            }
        }
        level--;
    }

    /**
     * Print all the keys in this node
     */
    protected void printKeys() {
        System.out.print("[");
        for (int i = 0; i < this.getKeyCount(); i++) {
            System.out.print(" " + this.keys[i]);
        }
        System.out.print("]");
    }


    ////// You may not change any code above this line //////

    ////// Implement the functions below this line //////


    /**
     * Search a key on the B+ tree and return its associated value. If the given key
     * is not found, null should be returned.
     */
    public TValue search(TKey key) {
        BPTreeNode<TKey, TValue> tempNode = this;

        //go down inners
        while (!tempNode.isLeaf()) {
            BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey, TValue>) tempNode;
            tempNode = tempInner.getChild(tempInner.findIndexOf(key));
        }

        //correct leaf found
        BPTreeLeafNode<TKey, TValue> tempLeaf = (BPTreeLeafNode<TKey, TValue>) tempNode;
        //find index of key between right values
        int testIndex = tempLeaf.getIndexOf(key);


        if (testIndex == keyTally) {
            return null;
        } else if (!tempLeaf.keys[testIndex].equals(key)) {
            return null;
        } else {
            return tempLeaf.getValue(testIndex);
        }
    }

    private BPTreeLeafNode<TKey, TValue> searchNode(TKey key) {
        BPTreeNode<TKey, TValue> tempNode = this;

        //go down inners
        while (!tempNode.isLeaf()) {
            BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey, TValue>) tempNode;
            tempNode = tempInner.getChild(tempInner.findIndexOf(key));
        }

        //correct leaf found
        BPTreeLeafNode<TKey, TValue> tempLeaf = (BPTreeLeafNode<TKey, TValue>) tempNode;
        //find index of key between right values
        int testIndex = tempLeaf.getIndexOf(key);

        if (testIndex == tempLeaf.keyTally) {
            return null;
        } else if (!tempLeaf.keys[testIndex].equals(key)) {
            return null;
        } else {
            return tempLeaf;
        }
    }

    /**
     * Insert a new key and its associated value into the B+ tree. The root node of the
     * changed tree should be returned.
     */
    public BPTreeNode<TKey, TValue> insert(TKey key, TValue value) {
        // Your code goes here
        if (this.isLeaf() && this.keyTally < m - 1) {
            this.insertNonFulLeafNode(key, value);
            return this;
        } else if (this.isLeaf() && this.keyTally == (m - 1) && this.parentNode == null) {        //ie it's the root
            this.insertNonFulLeafNode(key, value);
            return splitInsideNode(this);
        } else if (this.isLeaf() && this.keyTally == (m - 1)) {
            //1. Create new child
            BPTreeLeafNode<TKey, TValue> rChild = new BPTreeLeafNode<>(this.m);
            //2. Insert key into node (since has theoretical extra space
            insertNonFulLeafNode(key, value);  //return type?
            //3. redistribute data between this and new rChild
            redistributeData(this, rChild);
            //4. update all the links since in leaf
            updateLinks(this, rChild, this.parentNode);
            //5. add middle value to parent (will check if parent is overfull)
            BPTreeNode<TKey, TValue> newRoot;
            newRoot = addRChildToParent(key, value, this);
            //6. Check Overfull

            //7. return parent node
            return newRoot;
        } else {            //not a leaf
            //cast to inner to find child
            BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey, TValue>) this;
            //insert into child
            BPTreeNode<TKey, TValue> temp = tempInner.getChild(tempInner.findIndexOf(key)).insert(key, value);
            if (temp.parentNode != null) return temp.parentNode;
            else return temp;
        }
    }

    protected void insertNonFullInnerNode(TKey key, int i) {
        BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey, TValue>) this;
        TKey tempKey = (TKey) (this).keys[i];
        Object tempRef = ((BPTreeInnerNode<TKey, TValue>) this).references[i];
        Object ref = null;
        Boolean start = (i == 0);

        if (i == 0) {
            keys[i] = key;
            // ignore references[0]
            key = tempKey;
            ref = null;
            tempKey = (TKey) (this).keys[++i];
            ;
            tempRef = ((BPTreeInnerNode<TKey, TValue>) this).references[i];
        }

        while (tempKey != null && i < keys.length + 1) {
            keys[i] = key;
            ((BPTreeInnerNode<TKey, TValue>) this).references[i] = ref;
            if (ref != null) {
                ((BPTreeInnerNode<TKey, TValue>) this).getChild(i).parentNode = this;
            }
            key = tempKey;
            ref = tempRef;
            tempKey = (TKey) (this).keys[++i];
            ;
            tempRef = ((BPTreeInnerNode<TKey, TValue>) this).references[i];
        }
        ((BPTreeInnerNode<TKey, TValue>) this).references[i] = ref;
        if (ref != null) {
            ((BPTreeInnerNode<TKey, TValue>) this).getChild(i).parentNode = this;
        }
        this.keys[i++] = key;
        //keyTally++;
        if (tempRef != null) {
            ((BPTreeInnerNode<TKey, TValue>) this).references[i] = tempRef;
            ((BPTreeInnerNode<TKey, TValue>) this).getChild(i).parentNode = this;
        }
        if (start) {
            ((BPTreeInnerNode<TKey, TValue>) this).references[i - 1] = ref;
            if (ref != null) {
                ((BPTreeInnerNode<TKey, TValue>) this).getChild(i - 1).parentNode = this;
            }
        }
    }

    protected void insertNonFulLeafNode(TKey key, TValue val) {
        //since isLeaf()
        BPTreeLeafNode<TKey, TValue> templeaf = (BPTreeLeafNode<TKey, TValue>) this;
        int i = templeaf.getIndexOf(key);

        //allow duplicates, so don't check

        TValue tempVal = (TValue) ((BPTreeLeafNode<TKey, TValue>) this).values[i];
        TKey tempKey = (TKey) (this).keys[i];

        do {
            keys[i] = key;
            ((BPTreeLeafNode<TKey, TValue>) this).values[i] = val;

            val = tempVal;
            key = tempKey;

            if (i + 1 < m) {
                tempVal = (TValue) ((BPTreeLeafNode<TKey, TValue>) this).values[++i];
                tempKey = (TKey) this.keys[i];
            }


        } while (tempVal != null && i < keys.length + 1);
        if (i + 1 < m || key != null) {
            keys[i] = key;
            ((BPTreeLeafNode<TKey, TValue>) this).values[i] = val;
        }
        keyTally++;

    }

    protected BPTreeNode<TKey, TValue> splitInsideNode(BPTreeNode<TKey, TValue> node) {
        //happens at root or inside node
        //1. Create rChild
        BPTreeNode<TKey, TValue> rChild;
        if (node.isLeaf()) {
            rChild = new BPTreeLeafNode<>(m);
        } else {
            rChild = new BPTreeInnerNode<>(m);
        }
        //2. Move data over to rChild
        redistributeData(node, rChild);
        //3. create Parent or add to parent
        BPTreeInnerNode<TKey, TValue> parent;
        if (node.parentNode == null) {
            parent = new BPTreeInnerNode<>(this.m);
        } else {
            parent = ((BPTreeInnerNode<TKey, TValue>) node.parentNode);
        }

        //4. Insert index key
        if (node.parentNode == null) {
            parent.keys[0] = rChild.keys[0];
            parent.keyTally++;
            parent.references[0] = node;
            parent.getChild(0).parentNode = parent;
            parent.references[1] = rChild;
            parent.getChild(1).parentNode = parent;
        } else {
            int i = ((BPTreeInnerNode<TKey, TValue>) node.parentNode).findIndexOf((TKey) rChild.keys[0]);
            parent.insertNonFullInnerNode((TKey) rChild.keys[0], i);
            parent.keyTally++;
            if (parent.references[i + 1] != null) {
                parent.references[i] = parent.references[i + 1];
                parent.getChild(i).parentNode = parent;
            }
            parent.references[i + 1] = rChild;  //node.rightSibling already defined
            rChild.parentNode = parent;
        }
        if (!node.isLeaf()) {
            rChild.keys[0] = null;
            rChild.keyTally--;
            for (int i = 0; i <= rChild.keyTally; i++) {
                rChild.setKey(i, (TKey) rChild.keys[i + 1]);
                ((BPTreeInnerNode<TKey, TValue>) rChild).references[i] = ((BPTreeInnerNode<TKey, TValue>) rChild).references[i + 1];
                ((BPTreeInnerNode<TKey, TValue>) rChild).getChild(i).parentNode = rChild;
            }
            rChild.keys[keyTally] = null;
            ((BPTreeInnerNode<TKey, TValue>) rChild).references[keyTally] = null;
        }
        if (node.isLeaf()) {
            updateLinks(node, rChild, parent);
        }

        node.parentNode = parent;
        rChild.parentNode = parent;

        return parent;

    }

    protected void redistributeData(BPTreeNode<TKey, TValue> node, BPTreeNode<TKey, TValue> rChild) {
        int midIndex;
        if (node.keyTally == m) midIndex = Math.floorDiv(node.keyTally, 2);
        else midIndex = Math.floorDiv(node.keyTally - 1, 2);
        int numMoveValues = node.keyTally - (midIndex);

        //CASE: leaf
        if (node.isLeaf()) {
            //move over half of keys & values to rChild
            for (int i = midIndex; i < node.keyTally; i++) {
                rChild.keys[i - midIndex] = node.keys[i];
                ((BPTreeLeafNode<TKey, TValue>) rChild).values[i - midIndex] = ((BPTreeLeafNode<TKey, TValue>) node).values[i];

                node.keys[i] = null;
                ((BPTreeLeafNode<TKey, TValue>) node).values[i] = null;
            }
        }
        //CASE: !leaf
        else {
            for (int i = midIndex; i < node.keyTally; i++) {
                rChild.keys[i - midIndex] = node.keys[i];
                ((BPTreeInnerNode<TKey, TValue>) rChild).references[i - midIndex + 1] = ((BPTreeInnerNode<TKey, TValue>) node).references[i + 1];
                ((BPTreeInnerNode<TKey, TValue>) rChild).getChild(i - midIndex + 1).parentNode = rChild;

                node.keys[i] = null;
                ((BPTreeInnerNode<TKey, TValue>) node).references[i + 1] = null;
            }
            //((BPTreeInnerNode<TKey, TValue>)rChild).references[numMoveValues] = ((BPTreeInnerNode<TKey, TValue>)node).references[node.keyTally];
            //((BPTreeInnerNode<TKey, TValue>)node).references[node.keyTally] = null;
        }
        rChild.keyTally = rChild.keyTally + numMoveValues;
        node.keyTally = node.keyTally - numMoveValues;

    }

    protected void updateLinks(BPTreeNode<TKey, TValue> lChild, BPTreeNode<TKey, TValue> rChild, BPTreeNode<TKey, TValue> parentNode) {
        //must work for leaves and non-leaves
        rChild.rightSibling = lChild.rightSibling;
        if (lChild.rightSibling != null) {
            lChild.rightSibling.leftSibling = rChild;
        }
        lChild.rightSibling = rChild;
        rChild.leftSibling = lChild;
        rChild.parentNode = lChild.parentNode;
    }

    protected BPTreeNode<TKey, TValue> addRChildToParent(TKey key, TValue value, BPTreeNode<TKey, TValue> node) {
        //1. Get index
        int i = ((BPTreeInnerNode<TKey, TValue>) node.parentNode).findIndexOf(key);

        //2. Add
        if (i != parentNode.keyTally) {
            parentNode.insertNonFullInnerNode(key, i);
        }
        ((BPTreeInnerNode<TKey, TValue>) node.parentNode).references[i + 1] = node.rightSibling;  //node.rightSibling already defined
        node.rightSibling.parentNode = node.parentNode;
        ((BPTreeInnerNode<TKey, TValue>) node.parentNode).references[i] = node;
        parentNode.keys[i] = node.rightSibling.keys[0];   //is this necessary?
        parentNode.keyTally++;                                //is this necessary

        //3. Check if parent now overfull
        if (this.parentNode.keyTally == m) {
            BPTreeNode<TKey, TValue> newParent = this.parentNode;
            while (newParent.keyTally == m) {
                newParent = splitInsideNode(newParent);
                //update links of children to parent
                BPTreeNode<TKey, TValue> temp = ((BPTreeInnerNode<TKey, TValue>) newParent).getChild(newParent.keyTally);
                for (int j = 0; j < temp.keyTally + 1; j++) {
                    if (!temp.isLeaf()) {
                        BPTreeNode<TKey, TValue> t = ((BPTreeInnerNode<TKey, TValue>) temp).getChild(j);
                        t.parentNode = temp;
                    }
                }
            }
            return newParent;
        } else return this.parentNode;
    }


    /**
     * Delete a key and its associated value from the B+ tree. The root node of the
     * changed tree should be returned.
     */
    public BPTreeNode<TKey, TValue> delete(TKey key) {
        // Your code goes here
        //At leaf level && do NOT remove separator

        //1. get delNode
        BPTreeLeafNode<TKey, TValue> delNode = searchNode(key);
        if (delNode == null) return this;

        //2. Delete from leaf Node
        int i = delNode.getIndexOf(key);
        for (int j = i; j < delNode.keyTally; j++) {
            delNode.keys[j] = delNode.keys[j + 1];
            delNode.values[j] = delNode.values[j + 1];
        }
        delNode.keyTally--;
        delNode.keys[delNode.keyTally] = null; //used to be last element at position keyTally-1
        delNode.values[delNode.keyTally] = null;

        //3. Cases
        //enough keys left
        if (delNode.keyTally > m / 2 - 1) {
            //I shouldn't be reading delNode to the tree right? Because I only changed attributes
            return this;
        } else {
            if (delNode.leftSibling != null && delNode.leftSibling.keyTally > ((m / 2))) {
                share(((BPTreeLeafNode<TKey, TValue>) delNode.leftSibling), delNode);
            } else if (delNode.rightSibling != null && delNode.rightSibling.keyTally > ((m / 2))) {
                share(delNode, ((BPTreeLeafNode<TKey, TValue>) delNode.rightSibling));
            } else {
                merge(delNode);
            }
        }

        //4. Check for underflow
        BPTreeInnerNode<TKey, TValue> tempParent = (BPTreeInnerNode<TKey, TValue>) delNode.parentNode;
        while (tempParent.keyTally < m / 2 - 1) {
            //do magic stuff here
            throw new ArithmeticException("Supposed to do magic stuff here");
        }

        return this;

    }

    private void share(BPTreeLeafNode<TKey, TValue> lNode, BPTreeLeafNode<TKey, TValue> rNode) {
        //index where to insert into parent
        int j = ((BPTreeInnerNode<TKey, TValue>) lNode.parentNode).findIndexOf((TKey) rNode.keys[0]) - 1;
        //share from right -> left
        if (lNode.keyTally < rNode.keyTally) {
            //into left
            lNode.keys[lNode.keyTally] = rNode.keys[0];
            lNode.values[lNode.keyTally] = rNode.keys[0];
            lNode.keyTally++;
            //from right, shift values backwards
            for (int i = 0; i < rNode.keyTally; i++) {
                rNode.keys[i] = rNode.keys[i + 1];
                rNode.values[i] = rNode.values[i + 1];
            }
            //update right
            rNode.keyTally--;
            rNode.keys[rNode.keyTally] = null;
            rNode.values[rNode.keyTally] = null;
            //update parent index
            lNode.parentNode.keys[j] = rNode.keys[0];
        }
        //share from left to right
        else {          // NB NB will never have equality
            //update parent
            lNode.parentNode.keys[j] = lNode.keys[keyTally - 1];
            //insert to right
            rNode.insertNonFulLeafNode((TKey) lNode.keys[keyTally - 1], (TValue) lNode.values[keyTally - 1]);
            //remove last value in left
            lNode.keyTally--;
            lNode.keys[lNode.keyTally] = null;
            lNode.values[lNode.keyTally] = null;
        }
        //no need to update links since only moved values
    }

    private void merge(BPTreeLeafNode<TKey, TValue> node) {
        BPTreeInnerNode<TKey, TValue> parent = (BPTreeInnerNode<TKey, TValue>) node.parentNode;
        BPTreeLeafNode<TKey, TValue> sibling;
        if (node.leftSibling == null) {
            sibling = ((BPTreeLeafNode<TKey, TValue>) node.rightSibling);
            //merge nodes
            for (int j = 0; j < sibling.keyTally; j++) {
                node.keys[node.keyTally + j] = sibling.keys[j];
                node.values[node.keyTally + j] = sibling.values[j];
            }
            node.keyTally += sibling.keyTally;
            //update links
            node.rightSibling = sibling.rightSibling;
            sibling.rightSibling.leftSibling = node;
            //now in parent;
            //originally said j=2?
            for (int j = 1; j < parent.keyTally; j++) {
                parent.keys[j] = parent.keys[j + 1];
                parent.references[j] = parent.references[j + 1];
            }
            parent.keyTally--;
            parent.keys[parent.keyTally] = null;
            parent.references[parent.keyTally + 1] = null;
            parent.keys[0] = parent.getChild(1).keys[0];
        } else {
            int i = parent.findIndexOf((TKey) node.keys[0]) - 1;
            sibling = ((BPTreeLeafNode<TKey, TValue>) node.leftSibling);
            //merge nodes
            for (int j = 0; j < node.keyTally; j++) {
                sibling.keys[sibling.keyTally + j] = node.keys[j];
                sibling.values[sibling.keyTally + j] = node.values[j];
            }
            sibling.keyTally += node.keyTally;
            //update links
            if (node.rightSibling!=null) node.rightSibling.leftSibling = sibling;
            sibling.rightSibling = node.rightSibling;
            //now in parent
            for (int j = i + 1; j < parent.keyTally; j++) {
                parent.keys[j] = parent.keys[j + 1];
                parent.references[j] = parent.references[j + 1];
            }
            parent.keyTally--;
            parent.references[parent.keyTally + 1] = null;
            parent.keys[parent.keyTally] = null;
            if (i >= 0 && parent.keyTally!=1 && parent.keyTally!=i) {
                parent.keys[i] = parent.getChild(i + 1).keys[0];
            }
            if (i==0 && parent.keyTally==1) {
                parent.keys[i] = parent.getChild((i+1)).keys[0];
            }
        }
    }


    /**
     * Return all associated key values on the B+ tree in ascending key order. An array
     * of the key values should be returned.
     */
    @SuppressWarnings("unchecked")
    public TValue[] values() {
        // Your code goes here
        //1. Go down to smallest
        BPTreeNode<TKey, TValue> tempNode = this;
        while (!tempNode.isLeaf()) {
            BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey, TValue>) tempNode;
            tempNode = tempInner.getChild(0);
        }
        BPTreeLeafNode<TKey, TValue> tempLeaf = (BPTreeLeafNode<TKey, TValue>) tempNode;

        //2. Get Length
        int totalLength = 0;
        while (tempLeaf != null) {
            totalLength += tempLeaf.keyTally;
            tempLeaf = (BPTreeLeafNode<TKey, TValue>) tempLeaf.rightSibling;
        }

        //3. Create Array
        Object[] arrValues = new Object[totalLength];

        //4. Traverse
        tempLeaf = (BPTreeLeafNode<TKey, TValue>) tempNode;            //reinitialise tempLeaf
        int j = 0;
        while (tempLeaf != null) {
            for (int i = 0; i < tempLeaf.keyTally; i++) {
                arrValues[j] = (TValue) tempLeaf.values[i];
                j++;
            }
            tempLeaf = (BPTreeLeafNode<TKey, TValue>) tempLeaf.rightSibling;
        }
        //5. Return
        return (TValue[]) arrValues;

    }

}