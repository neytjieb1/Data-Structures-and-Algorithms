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


        if (testIndex == tempNode.keyTally) {
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
        //get delNode
        BPTreeLeafNode<TKey, TValue> delNode = (BPTreeLeafNode<TKey, TValue>) searchNode(key);
        if (delNode == null) {
            return this;
        }

        //delete from Node
        int i = delNode.getIndexOf(key);
        for (int k = i; k < delNode.keyTally; k++) {
            delNode.keys[k] = delNode.keys[k + 1];
            delNode.values[k] = delNode.values[k + 1];
        }
        delNode.keyTally--;

        //Check for underflow
        BPTreeNode<TKey, TValue> node = delNode;
        while (true) {
            BPTreeInnerNode<TKey, TValue> parent = (BPTreeInnerNode<TKey, TValue>) node.parentNode;
            BPTreeNode<TKey, TValue> sibling;
            //if no underflow
            if (node.keyTally >= Math.ceil(m / 2) - 1) {
                return this;
            }
            //get index (consider parent case as well
            if (!node.equals(this)) { //added in later
                for (i = 0; parent.getChild(i) != node; i++) ;
            } else {
                return node;
            }
            //has shareable leftSib
            if (i >= 1 && parent.getChild(i - 1) != null && parent.getChild(i - 1).keyTally > Math.ceil(m / 2) - 1) {
                sibling = parent.getChild(i - 1);
                if (node.isLeaf()) {
                    TKey Key = (TKey) sibling.keys[sibling.keyTally - 1];
                    TValue val = (TValue) ((BPTreeLeafNode<TKey, TValue>) sibling).values[sibling.keyTally - 1];
                    for (int j = 0; j < node.keyTally + 1; j++) {
                        TKey tempKey = (TKey) node.keys[j];
                        TValue tempVal = (TValue) ((BPTreeLeafNode<TKey, TValue>) node).values[j];
                        node.keys[j] = Key;
                        ((BPTreeLeafNode<TKey, TValue>) node).values[j] = val;
                        Key = tempKey;
                        val = tempVal;
                    }
                    sibling.keys[sibling.keyTally - 1] = null;
                    ((BPTreeLeafNode<TKey, TValue>) sibling).values[sibling.keyTally - 1] = null;
                    sibling.keyTally--;
                    node.keyTally++;
                    //update index
                    parent.keys[i - 1] = node.keys[0];

                } else {//node not a leaf
                    //insert key into node
                    TKey Key = (TKey) parent.keys[i - 1];
                    for (int j = 0; j < node.keyTally + 1; j++) {
                        TKey tempKey = (TKey) node.keys[j];
                        node.keys[j] = Key;
                        Key = tempKey;
                    }
                    node.keyTally++;
                    //insert references into node
                    BPTreeNode<TKey, TValue> ref = ((BPTreeInnerNode<TKey, TValue>) sibling).getChild(sibling.keyTally);
                    for (int j = 0; j < node.keyTally + 1; j++) {
                        BPTreeNode<TKey, TValue> tempRef = ((BPTreeInnerNode<TKey, TValue>) node).getChild(j);
                        ((BPTreeInnerNode<TKey, TValue>) node).references[j] = ref;
                        ref = tempRef;
                    }
                    //update parent
                    parent.keys[i - 1] = sibling.keys[sibling.keyTally - 1];
                    //update sibling
                    sibling.keys[sibling.keyTally - 1] = null;
                    ((BPTreeInnerNode<TKey, TValue>) sibling).references[sibling.keyTally] = null;
                    sibling.keyTally--;
                }
                return this;
                /*
                (redistribute between node, sibling and parent (ie take from top and then move up from fullSib)
                move child reference over)
                 */
            }
            //has shareable rightSib
            else if (parent.getChild(i + 1) != null && parent.getChild(i + 1).keyTally > Math.ceil(m / 2) - 1) { //&& i + 1 < parent.getChild(i+1).keyTally
                sibling = parent.getChild(i + 1);
                if (node.isLeaf()) {
                    //insert into left
                    node.keys[node.keyTally] = sibling.keys[0];
                    ((BPTreeLeafNode<TKey, TValue>) node).values[node.keyTally] = ((BPTreeLeafNode<TKey, TValue>) sibling).values[0];
                    node.keyTally++;
                    //remove from right
                    for (int j = 0; j < sibling.keyTally + 1; j++) {
                        sibling.keys[j] = sibling.keys[j + 1];
                        ((BPTreeLeafNode<TKey, TValue>) sibling).values[j] = ((BPTreeLeafNode<TKey, TValue>) sibling).values[j + 1];
                    }
                    //(redistribute between node & sibling)
                    parent.keys[i] = sibling.keys[0];
                } else { //node is NOT leaf
                    //insert into node
                    node.keys[node.keyTally] = (TKey) parent.keys[i];
                    node.keyTally++;
                    ((BPTreeInnerNode<TKey, TValue>) node).setChild(node.keyTally, ((BPTreeInnerNode<TKey, TValue>) sibling).getChild(0));
                    //update parent
                    parent.keys[i] = sibling.keys[0];
                    //update Sibling
                    for (int j = 0; j < sibling.keyTally; j++) {
                        sibling.keys[j] = sibling.keys[j + 1];
                        ((BPTreeInnerNode<TKey, TValue>) sibling).references[j] = ((BPTreeInnerNode<TKey, TValue>) sibling).references[j + 1];
                        //((BPTreeInnerNode<TKey, TValue>) sibling).setChild(i, ((BPTreeInnerNode<TKey, TValue>) sibling).getChild(i + 1));
                    }
                    ((BPTreeInnerNode<TKey, TValue>) sibling).setChild(sibling.keyTally, null);
                    /* redistribute between node, sibling and parent (ie take from top and then move up from fullSib)
                    move child reference over */
                }
                sibling.keyTally--;
                return this;
            }
            //parent is root + child ! leaf
            else if (parent == this && !node.isLeaf()) { //what about if parent is root && node is a leaf?
                if (parent.keyTally == 1) {
                    BPTreeInnerNode<TKey, TValue> newRoot = new BPTreeInnerNode<>(this.m);
                    if (i == 0) {
                        sibling = parent.getChild(1);
                        for (int j = 0; j < node.keyTally; j++) {
                            newRoot.keys[j] = node.keys[j];
                            newRoot.references[j] = ((BPTreeInnerNode<TKey, TValue>) node).references[j]; // node might still be leaf?
                        }
                        newRoot.keyTally += node.keyTally;
                        if (newRoot.keyTally == 0)
                            newRoot.references[0] = ((BPTreeInnerNode<TKey, TValue>) node).references[0];
                        insertIntoNewRoot(sibling, sibling, (BPTreeInnerNode<TKey, TValue>) newRoot);
                        //newRoot.references[newRoot.keyTally] = ((BPTreeInnerNode<TKey, TValue>) node).references[sibling.keyTally];
                    } else {
                        sibling = parent.getChild(0);
                        for (int j = 0; j < sibling.keyTally; j++) {
                            newRoot.keys[j] = sibling.keys[j];
                            newRoot.references[j] = ((BPTreeInnerNode<TKey, TValue>) sibling).references[j];
                        }
                        newRoot.keyTally += sibling.keyTally;
                        newRoot.references[newRoot.keyTally] = ((BPTreeInnerNode<TKey, TValue>) sibling).references[sibling.keyTally];
                        insertIntoNewRoot(node, sibling, (BPTreeInnerNode<TKey, TValue>) newRoot);
                        if (((BPTreeInnerNode<TKey, TValue>) node).references[node.keyTally] == null) {
                            newRoot.references[newRoot.keyTally] = ((BPTreeInnerNode<TKey, TValue>) node).references[node.keyTally - 1];
                        } else {
                            newRoot.references[newRoot.keyTally] = ((BPTreeInnerNode<TKey, TValue>) node).references[node.keyTally];
                        }
                        /* merge node, sib and parent*/
                    }
                    newRoot.resetParentNodes();
                    return newRoot;
                } else {
                    merge(node, i);
                    this.resetParentNodes();
                    return this;
                }
            }
            //parent is root + child is leaf
            else if (parent.equals(this) && node.isLeaf()) {
                if (parent.keyTally == 1) {
                    BPTreeLeafNode<TKey, TValue> newRoot = new BPTreeLeafNode<>(this.m);
                    if (i == 0) {
                        sibling = parent.getChild(1);
                        for (int j = 0; j < node.keyTally; j++) {
                            newRoot.keys[j] = node.keys[j];
                            newRoot.values[j] = ((BPTreeLeafNode<TKey, TValue>) node).values[j];
                        }
                        newRoot.keyTally += node.keyTally;
                        for (int j = 0; j < sibling.keyTally; j++) {
                            newRoot.keys[newRoot.keyTally + j] = sibling.keys[j];
                            newRoot.values[newRoot.keyTally + j] = ((BPTreeLeafNode<TKey, TValue>) sibling).values[j];
                        }
                        newRoot.keyTally += sibling.keyTally;
                    } else {
                        sibling = parent.getChild(0);
                        for (int j = 0; j < sibling.keyTally; j++) {
                            newRoot.keys[j] = sibling.keys[j];
                            newRoot.values[j] = ((BPTreeLeafNode<TKey, TValue>) sibling).values[j];
                        }
                        newRoot.keyTally += sibling.keyTally;
                        for (int j = 0; j < node.keyTally; j++) {
                            newRoot.keys[newRoot.keyTally + j] = node.keys[j];
                            newRoot.values[newRoot.keyTally + j] = ((BPTreeLeafNode<TKey, TValue>) node).values[j];
                        }
                        newRoot.keyTally += node.keyTally;
                    }
                    newRoot.resetParentNodes();
                    return newRoot;
                } else {
                    merge(node, i);
                    this.resetParentNodes();
                    return this;
                }
            } else {
                merge(node, i);
                this.resetParentNodes();
                node = node.parentNode;
            }

        }

    }

    private void insertIntoNewRoot(BPTreeNode<TKey, TValue> node, BPTreeNode<TKey, TValue> sibling, BPTreeInnerNode<TKey, TValue> newRoot) {
        newRoot.keys[newRoot.keyTally] = this.keys[0];
        newRoot.keyTally++;
        for (int j = 0; j < node.keyTally; j++) {
            newRoot.keys[newRoot.keyTally + j] = node.keys[j];
            newRoot.references[newRoot.keyTally + j] = ((BPTreeInnerNode<TKey, TValue>) node).references[j];
        }
        newRoot.keyTally += node.keyTally;
        newRoot.references[newRoot.keyTally] = ((BPTreeInnerNode<TKey, TValue>) node).references[node.keyTally];

    }

    public void merge(BPTreeNode<TKey, TValue> node, int i) {
        BPTreeInnerNode<TKey, TValue> parent = (BPTreeInnerNode<TKey, TValue>) node.parentNode;
        BPTreeNode<TKey, TValue> sibling;
        if (node.isLeaf()) {    /*merge sibling and node remove keyvalue and reference from parent node)*/
            if (i >= 1 && parent.getChild(i - 1) != null) { //has a left sibling
                //insert node into sibling
                sibling = parent.getChild(i - 1);
                insertValsLeafNode(sibling, node);
                //update parent
                updateParent(i, (BPTreeInnerNode<TKey, TValue>) parent);
            } else { // if (i + 1 < parent.keyTally && parent.getChild(i + 1) != null)
                sibling = parent.getChild(i + 1);
                //insert sibling into node and then shift around
                insertValsLeafNode(node, sibling);
                node.rightSibling = sibling.rightSibling;
                parent.setChild(i + 1, node);
                //update parent
                updateParent(i, (BPTreeInnerNode<TKey, TValue>) parent);
            } /*else {
                //anything here?
            }*/
        } else { //node not a leaf
            if (i >= 1 && parent.getChild(i - 1) != null) {//has a left sibling
                //insert keyValue from parent
                sibling = parent.getChild(i - 1);
                sibling.keys[sibling.keyTally] = parent.keys[i - 1];
                sibling.keyTally++;
                //move references of node
                if (((BPTreeInnerNode<TKey, TValue>) node).getChild(0) == null) {
                    for (int j = 0; j < node.keyTally + 1; j++) {
                        ((BPTreeInnerNode<TKey, TValue>) node).references[j] = ((BPTreeInnerNode<TKey, TValue>) node).references[j + 1];
                    }
                }
                //insert references && keyValues
                insertRefInnerNode(node, sibling);
                //update parent
                if (parent.isLeaf()) {
                    updateParentLinkages(i, parent);
                }
                for (int j = i - 1; j < parent.keyTally; j++) {
                    parent.keys[j] = parent.keys[j + 1];
                }
                for (int j = i; j < parent.keyTally; j++) {
                    parent.references[j] = parent.references[j + 1];
                }
                if (i == parent.keyTally) parent.keys[parent.keyTally - 1] = null;
                parent.references[parent.keyTally] = null;
                parent.keyTally--;
                //update index set
                if (i - 1 == parent.keyTally) {
                    parent.keys[i - 1] = parent.getChild(i - 1).keys[0];
                } else if (i != 0) {
                    if (parent.keyTally != 1) {
                        parent.keys[i] = parent.getChild(i + 1).keys[0];
                    }
                }


            } else if (parent.getChild(i + 1) != null) {  //i + 1 < parent.keyTally &&
                sibling = parent.getChild(i + 1);
                //insert keys into node
                node.keys[node.keyTally] = parent.keys[i];
                node.keyTally++;
                //move references of node
                if (((BPTreeInnerNode<TKey, TValue>) node).references[0] == null) {
                    for (int j = 0; j < node.keyTally + 1; j++) {
                        ((BPTreeInnerNode<TKey, TValue>) node).references[j] = ((BPTreeInnerNode<TKey, TValue>) node).references[j + 1];
                    }
                }
                //move references & keyValues over
                insertRefInnerNode(sibling, node);
                ((BPTreeInnerNode<TKey, TValue>) node).references[node.keyTally] = ((BPTreeInnerNode<TKey, TValue>) sibling).references[sibling.keyTally];

                //switch nodes
                parent.setChild(i + 1, node);

                //update parent
                updateParent(i, (BPTreeInnerNode<TKey, TValue>) parent);
                /* merge sibling and node and take element from root*/
            }
        }
    }

    private void insertValsLeafNode(BPTreeNode<TKey, TValue> node, BPTreeNode<TKey, TValue> sibling) {
        for (int j = 0; j < sibling.keyTally; j++) {
            node.keys[node.keyTally + j] = sibling.keys[j];
            ((BPTreeLeafNode<TKey, TValue>) node).values[node.keyTally + j] = ((BPTreeLeafNode<TKey, TValue>) sibling).values[j];
        }
        node.keyTally += sibling.keyTally;
    }

    private void insertRefInnerNode(BPTreeNode<TKey, TValue> node, BPTreeNode<TKey, TValue> sibling) {
        for (int j = 0; j < node.keyTally; j++) {
            sibling.keys[sibling.keyTally + j] = node.keys[j];
            ((BPTreeInnerNode<TKey, TValue>) sibling).references[sibling.keyTally + j] = ((BPTreeInnerNode<TKey, TValue>) node).references[j];
        }
        sibling.keyTally += node.keyTally;
        ((BPTreeInnerNode<TKey, TValue>) sibling).references[sibling.keyTally] = ((BPTreeInnerNode<TKey, TValue>) node).references[node.keyTally];
    }

    private void updateParentLinkages(int i, BPTreeInnerNode<TKey, TValue> parent) {
        //update linkages //will be overwriting i
        BPTreeNode<TKey, TValue> newNode;
        if (i == 0) {
            newNode = parent.getChild(i + 1);
            newNode.leftSibling = parent.getChild(i).leftSibling;
            if (newNode.rightSibling != null) {
                newNode.rightSibling.leftSibling = parent.getChild(i).leftSibling;
            }
        } else if (i == parent.keyTally) {
            newNode = parent.getChild(i - 1);
            newNode.rightSibling = parent.getChild(i).rightSibling;
            if (parent.getChild(i).rightSibling != null) {
                parent.getChild(i).rightSibling = newNode;
            }
        } else {
            newNode = parent.getChild(i);
            if (newNode.leftSibling != null) newNode.leftSibling.rightSibling = newNode.rightSibling;
            if (newNode.rightSibling != null) newNode.rightSibling.leftSibling = newNode.leftSibling;
        }
        //different linking in planning
    }

    private void updateParent(int i, BPTreeInnerNode<TKey, TValue> parent) {
        if (parent.getChild(i).isLeaf()) {
            updateParentLinkages(i, parent);
        }
        for (int j = i; j < parent.keyTally; j++) {
            parent.keys[j] = parent.keys[j + 1];
            parent.references[j] = parent.references[j + 1];
        }
        if (i == parent.keyTally) {
            parent.keys[parent.keyTally - 1] = null;
        }
        parent.references[parent.keyTally] = null;
        parent.keyTally--;

        //update index set
        if (i - 1 == parent.keyTally) {
            parent.keys[i - 1] = parent.getChild(i - 1).keys[0];
        } else if (i != 0) {
            if (parent.keyTally != 1 && parent.getChild(i+1)!=null) { //added in second &&
                parent.keys[i] = parent.getChild(i + 1).keys[0];
            }
            /*else {
                parent.keys[0] = parent.getChild(1).keys[0];
            }*/
        }

        /*if (parent.getChild(0).isLeaf()) { // too much. Shouldn update all indices
            for (int j = 0; j < parent.keyTally; j++) {
                parent.keys[j] = parent.getChild(j + 1).keys[0];
            }
        }*/
    }

    protected void resetParentNodes() {
        if (this.isLeaf()) {
            return;
        } else if (((BPTreeInnerNode<TKey, TValue>) this).getChild(0).isLeaf()) {
            for (int i = 0; i < this.keyTally + 1; i++) {
                ((BPTreeInnerNode<TKey, TValue>) this).getChild(i).setParent(this);
            }
        } else {
            for (int i = 0; i < this.keyTally+1; i++) {
                ((BPTreeInnerNode<TKey, TValue>) this).getChild(i).setParent(this);
                ((BPTreeInnerNode<TKey, TValue>) this).getChild(i).resetParentNodes();
            }
        }
    }

    /*public BPTreeNode<TKey, TValue> deleteFirstTry(TKey key) {
        // Your code goes here

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
        if (delNode.keyTally >= Math.ceil(m/2)-1) {
            return this;
        } else {
*/
    /*            int parentIndex;
            for (parentIndex = 0; parent.getChild(parentIndex) != node; parentIndex++);*/
    /*
            if (delNode.parentNode.parentNode != null) {
                i = ((BPTreeInnerNode<TKey,TValue>)(delNode.parentNode.parentNode)).findIndexOf(key);
            }
            else {
                i= -1;
            }
            if (delNode.leftSibling != null && delNode.leftSibling.keyTally > (Math.ceil(m/2)-1)) {
                shareLeafs(((BPTreeLeafNode<TKey, TValue>) delNode.leftSibling), delNode);
            } else if (delNode.rightSibling != null && delNode.rightSibling.keyTally > (Math.ceil(m/2)-1)) {
                shareLeafs(delNode, ((BPTreeLeafNode<TKey, TValue>) delNode.rightSibling));
            } else {
                mergeLeafs(delNode, null);
            }
        }

        //4. Check for underflow
        BPTreeInnerNode<TKey, TValue> tempParent = (BPTreeInnerNode<TKey, TValue>) delNode.parentNode;
        while (tempParent!=null && tempParent.keyTally < Math.ceil(m/2)-1) {
            //do magic stuff here
            BPTreeNode<TKey, TValue> lSibling, rSibling;
            if (i==-1) {
                BPTreeLeafNode<TKey,TValue> newRoot = new BPTreeLeafNode<>(this.m);
                BPTreeLeafNode<TKey, TValue> child = (BPTreeLeafNode<TKey, TValue>) (((BPTreeInnerNode<TKey,TValue>)this).getChild(0));
                while(child.keys[i+1]!=null) {
                    newRoot.insertNonFulLeafNode((TKey) child.keys[i+1], (TValue)child.values[i+1]);
                    i++;
                }
                return newRoot;
            }
            else if (i!=0) { //has a left sibling
                lSibling = ((BPTreeInnerNode<TKey, TValue>)(tempParent.parentNode)).getChild(i-1);
                if (lSibling!=null && lSibling.keyTally > Math.ceil(m/2)-1) {
                    shareInners((BPTreeInnerNode<TKey, TValue>) lSibling, tempParent);
                }
            }
            //now check right or merge
            rSibling = ((BPTreeInnerNode<TKey, TValue>)(tempParent.parentNode)).getChild(i+1);
            if (rSibling!=null && rSibling.keyTally > Math.ceil(m/2)-1) {
                shareInners(tempParent, (BPTreeInnerNode<TKey, TValue>) rSibling);
            }
            else if (i!=0){ //merge with left sibling
                lSibling = ((BPTreeInnerNode<TKey, TValue>)(tempParent.parentNode)).getChild(i-1);
                mergeInners( (BPTreeInnerNode<TKey, TValue>) lSibling, tempParent, null);
            }
            else {
                mergeInners(null, tempParent, (BPTreeInnerNode<TKey, TValue>) rSibling);
            }
            //throw new ArithmeticException("Supposed to do magic stuff here");
            tempParent = (BPTreeInnerNode<TKey, TValue>) tempParent.parentNode;
        }

        return this;

    }

    private void shareLeafs(BPTreeLeafNode<TKey, TValue> lNode, BPTreeLeafNode<TKey, TValue> rNode) {
        //index where to insert into parent
        int j;
        if (rNode.keyTally==0) {
            j = ((BPTreeInnerNode<TKey, TValue>) lNode.parentNode).findIndexOf((TKey) lNode.keys[lNode.keyTally-1]) ;
        }
        else {
            j = ((BPTreeInnerNode<TKey, TValue>) lNode.parentNode).findIndexOf((TKey) rNode.keys[0]) - 1;
        }

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
            lNode.parentNode.keys[j] = lNode.keys[lNode.keyTally - 1];
            //insert to right
            rNode.insertNonFulLeafNode((TKey) lNode.keys[lNode.keyTally - 1], (TValue) lNode.values[lNode.keyTally - 1]);
            //remove last value in left
            lNode.keyTally--;
            lNode.keys[lNode.keyTally] = null;
            lNode.values[lNode.keyTally] = null;
        }
        //no need to update links since only moved values
    }

    private void shareInners(BPTreeInnerNode<TKey, TValue> lNode, BPTreeInnerNode<TKey,TValue> rNode) {
        //index where to insert into parent
        int j;    //will always have a parent
        if (rNode.parentNode == null) {
            j = -1;
        }
        else if (rNode.keyTally==0) {
            j = ((BPTreeInnerNode<TKey, TValue>) lNode.parentNode).findIndexOf((TKey) lNode.keys[lNode.keyTally-1]) ;
        }
        else {
            j = ((BPTreeInnerNode<TKey, TValue>) lNode.parentNode).findIndexOf((TKey) rNode.keys[0]) - 1;
        }
        //share from right -> left
        if (lNode.keyTally < rNode.keyTally) {
            //into left
            lNode.insertNonFullInnerNode((TKey)rNode.keys[0], lNode.keyTally);
            lNode.keyTally++;
            lNode.references[keyTally] = rNode.references[0];
            //delete from right
            for (int i = 0; i < rNode.keyTally; i++) {
                rNode.keys[i] = rNode.keys[i+1];
                rNode.references[i] = rNode.references[i+1];
            }
            rNode.references[rNode.keyTally] = rNode.references[rNode.keyTally+1];
            rNode.references[keyTally+1]=null;
            rNode.keys[rNode.keyTally-1]=null;
            rNode.keyTally--;
            //update parent index
            if (j!=-1) {
                lNode.parentNode.keys[j] = rNode.keys[0];
            }
        }
        //share from left to right
        else {          // NB NB will never have equality
            //update parent
            if (j!=-1) {
                lNode.parentNode.keys[j] = lNode.keys[lNode.keyTally - 1];
            }
            //insert into right
            rNode.insertNonFullInnerNode((TKey) lNode.keys[lNode.keyTally-1], 0);
            rNode.keyTally++;
            for (int i = 0; i < rNode.keyTally; i++) {
                rNode.references[i+1] = rNode.references[i];
            }
            rNode.references[0] = lNode.references[lNode.keyTally];
            rNode.keys[0] = rNode.getChild(1).keys[0];
            //remove last value in left
            lNode.keys[lNode.keyTally-1] = null;
            lNode.references[lNode.keyTally] = null;
            lNode.keyTally--;

        }
    }

    private void mergeLeafs(BPTreeLeafNode<TKey, TValue> node, BPTreeLeafNode<TKey, TValue> sibling) {
        BPTreeInnerNode<TKey, TValue> parent = (BPTreeInnerNode<TKey, TValue>) node.parentNode;
        if (node.leftSibling == null) {
            if (sibling==null) {
                sibling = ((BPTreeLeafNode<TKey, TValue>) node.rightSibling);
            }
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
            int i;
            if (node.keyTally==0) {
                i = parent.findIndexOf((TKey) node.leftSibling.keys[node.leftSibling.keyTally-1]);
            }
            else {
                i = parent.findIndexOf((TKey) node.keys[0]) - 1;
            }
            if (sibling==null) {
                sibling = ((BPTreeLeafNode<TKey, TValue>) node.leftSibling);
            }
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
            */
    /* ERROR HERE
    if (i==0) {
                for (int j = i; j < parent.keyTally; j++) {  //changed from i+1 to i
                    parent.keys[j] = parent.keys[j + 1];
                    parent.references[j] = parent.references[j + 1];
                }
            }
            else */
    /*{
                for (int j = i+1; j < parent.keyTally; j++) {  //changed from i+1 to i
                    parent.keys[j] = parent.keys[j + 1];
                    parent.references[j] = parent.references[j + 1];
                }
            }
            parent.references[parent.keyTally] = null;
            */
    /*if (parent.keyTally>1) {

                parent.keyTally--;
                parent.keys[parent.keyTally] = null;

            if (i >= 0 && parent.keyTally>1 && parent.keyTally!=i) {
                parent.keys[i] = parent.getChild(i + 1).keys[0];
            }
            if (i==0 && parent.keyTally==1) {
                parent.keys[i] = parent.getChild((i+1)).keys[0];
            }
        }
    }

    private void mergeInners(BPTreeInnerNode<TKey,TValue> leftSibling, BPTreeInnerNode<TKey, TValue> node, BPTreeInnerNode<TKey, TValue> rightSibling) {
        BPTreeInnerNode<TKey, TValue> parent = (BPTreeInnerNode<TKey, TValue>) node.parentNode;
        if (leftSibling==null) {
            //merge nodes
            for (int i = 0; i < rightSibling.keyTally; i++) {
                node.keys[node.keyTally+i] = rightSibling.keys[i];
                node.references[i+1] = rightSibling.references[i];
            }
            //add extra node
            node.keys[node.keyTally] = rightSibling.getChild(rightSibling.keyTally).keys[0];
            node.keyTally+= rightSibling.keyTally + 1;
            node.references[node.keyTally] = rightSibling.references[rightSibling.keyTally];
            //now in parent
            for (int j = 1; j < parent.keyTally; j++) {
                parent.keys[j] = parent.keys[j + 1];
            }
            for (int j = 1+1; j < parent.keyTally; j++) {
                parent.references[j] = parent.references[j + 1];
            }
            parent.keyTally--;
            parent.keys[parent.keyTally] = null;
            parent.references[parent.keyTally + 1] = null;
            if (parent.getChild(1)!=null) {
                parent.keys[0] = parent.getChild(1).keys[0];
            }
        }
        else {
            int i;
            if (node.keyTally==0) {
                i = parent.findIndexOf((TKey) leftSibling.keys[leftSibling.keyTally-1])-1;
            }
            else {
                i = parent.findIndexOf((TKey) node.keys[0]) - 1;
            }
            //merge nodes
            for (int j = 0; j < node.keyTally; j++) {
                leftSibling.keys[leftSibling.keyTally + j] = node.keys[j];
                leftSibling.references[j+1] = node.references[j];
            }
            //add extra node
            leftSibling.keys[leftSibling.keyTally] = node.getChild(node.keyTally).keys[0];
            leftSibling.keyTally += node.keyTally + 1;
            leftSibling.references[leftSibling.keyTally] = node.references[node.keyTally];
            //now in parent
            for (int j = i + 1; j < parent.keyTally; j++) {
                parent.keys[j] = parent.keys[j + 1];
            }
            for (int j = i+2; j<parent.keyTally; j++) {
                parent.references[j] = parent.references[j+1];
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
*/


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