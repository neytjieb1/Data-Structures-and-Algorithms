import java.util.zip.CheckedOutputStream;

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
        /*BPTreeNode<TKey, TValue> tempNode = this;

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
        }*/
        BPTreeLeafNode<TKey, TValue> tempNode = searchNode(key);
        if (tempNode == null) {
            return null;
        } else {
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
    }

    public BPTreeLeafNode<TKey, TValue> searchNode(TKey key) {
        BPTreeNode<TKey, TValue> tempNode = this;

        //go down inners
        boolean found = false;
        while (!tempNode.isLeaf()) {
            BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey, TValue>) tempNode;
            for (int i = 0; i < tempInner.keyTally; i++) {
                if (tempInner.keys[i].equals(key)) {
                    if (tempInner.getChild(i + 1).keys[0].equals(key)) {
                        tempNode = tempInner.getChild(i + 1);
                        found = true;
                    }
                }
            }
            if (!found) {
                tempNode = tempInner.getChild(tempInner.findIndexOf(key));
            }
            found = false;
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
        if (this.searchNode(key) != null) {
            return this;
        }
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

            //7. reset ParentNodes
            newRoot.resetParentNodes();
            //8. return parent node
            return newRoot;
        } else {            //not a leaf
            //cast to inner to find child
            BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey, TValue>) this;
            //insert into child
            BPTreeNode<TKey, TValue> temp = tempInner.getChild(tempInner.findIndexOf(key)).insert(key, value);
            if (temp.parentNode != null) {
                temp.parentNode.resetParentNodes();
                return temp.parentNode;
            } else {
                temp.resetParentNodes();
                return temp;
            }
        }
    }

    protected void insertNonFullInnerNode(TKey key, int i) {
        BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey, TValue>) this;
        int j = tempInner.keyTally;
        if (tempInner.keyTally + 1 == this.m) {
            for (j = tempInner.keyTally; j >= i; --j) {
                if (j != 0) tempInner.keys[j] = tempInner.keys[j - 1];
                tempInner.references[j + 1] = tempInner.references[j];
            }
        } else {
            for (j = tempInner.keyTally; j >= i; --j) {
                tempInner.keys[j + 1] = tempInner.keys[j];
                tempInner.references[j + 1] = tempInner.references[j];
            }
        }
        tempInner.keys[i] = key;
        tempInner.keyTally++;


        /*BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey, TValue>) this;
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
            i++;
            tempKey = (TKey) (this).keys[i];
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
        } */
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

        //4. Insert index key into parent
        if (node.parentNode == null) {
            parent.keys[0] = rChild.keys[0];
            parent.keyTally++;
            parent.setChild(0, node);
            parent.setChild(1, rChild);
        } else {
            int i = ((BPTreeInnerNode<TKey, TValue>) node.parentNode).findIndexOf((TKey) rChild.keys[0]);
            parent.insertNonFullInnerNode((TKey) rChild.keys[0], i);
            if (parent.references[i + 1] != null) {
                parent.setChild(i, node); //parent.setChild(i, parent.getChild(i+1));
            }
            parent.setChild(i + 1, rChild);  //node.rightSibling already defined
        }
        if (!node.isLeaf()) {
            //move up values of rChild if inner
            rChild.keys[0] = null;
            rChild.keyTally--;
            for (int i = 0; i <= rChild.keyTally; i++) {
                rChild.setKey(i, (TKey) rChild.keys[i + 1]);
                ((BPTreeInnerNode<TKey, TValue>) rChild).references[i] = ((BPTreeInnerNode<TKey, TValue>) rChild).references[i + 1];
                //((BPTreeInnerNode<TKey, TValue>) rChild).getChild(i).parentNode = rChild;
            }
            rChild.keys[rChild.keyTally] = null;
            ((BPTreeInnerNode<TKey, TValue>) rChild).references[rChild.keyTally + 1] = null;
        }
        if (node.isLeaf()) {
            updateLinks(node, rChild, parent);
        }
        parent.resetParentNodes();

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
            parentNode.keyTally--;
        }
        ((BPTreeInnerNode<TKey, TValue>) node.parentNode).references[i + 1] = node.rightSibling;  //node.rightSibling already defined
        node.rightSibling.parentNode = node.parentNode;
        ((BPTreeInnerNode<TKey, TValue>) node.parentNode).references[i] = node;
        parentNode.keys[i] = node.rightSibling.keys[0];
        parentNode.keyTally++;
        //3. Check if parent now overfull
        if (this.parentNode.keyTally == m) {
            BPTreeNode<TKey, TValue> newParent = this.parentNode;
            while (newParent.keyTally == m) {
                newParent = splitInsideNode(newParent);
                newParent.resetParentNodes();
            }
            return newParent;
        } else {
            return this.parentNode;
        }
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
            if (node.keyTally >= Math.ceil(m / 2.0) - 1) {
                this.resetParentNodes();
                return this;
            }
            //get index (consider parent case as well
            if (!node.equals(this)) { //added in later
                for (i = 0; parent.getChild(i) != node; i++) ;
            } else {
                resetParentNodes();
                return node;
            }
            //has shareable leftSib
            if (i >= 1 && parent.getChild(i - 1) != null && parent.getChild(i - 1).keyTally > Math.ceil(m / 2.0) - 1) {
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
                resetParentNodes();
                return this;
                /*
                (redistribute between node, sibling and parent (ie take from top and then move up from fullSib)
                move child reference over)
                 */
            }
            //has shareable rightSib
            else if (parent.getChild(i + 1) != null && parent.getChild(i + 1).keyTally > Math.ceil(m / 2.0) - 1) { //&& i + 1 < parent.getChild(i+1).keyTally
                sibling = parent.getChild(i + 1);
                if (node.isLeaf()) {
                    //insert into left
                    node.keys[node.keyTally] = sibling.keys[0];
                    ((BPTreeLeafNode<TKey, TValue>) node).values[node.keyTally] = ((BPTreeLeafNode<TKey, TValue>) sibling).values[0];
                    node.keyTally++;
                    //remove from right
                    for (int j = 0; j < sibling.keyTally + 1; j++) {
                        if (j + 1 < sibling.keys.length) {
                            sibling.keys[j] = sibling.keys[j + 1];
                            ((BPTreeLeafNode<TKey, TValue>) sibling).values[j] = ((BPTreeLeafNode<TKey, TValue>) sibling).values[j + 1];
                        }
                    }
                    //(redistribute between node & sibling)
                    parent.keys[i] = sibling.keys[0];
                    //Update
                    sibling.keyTally--;
                } else { //node is NOT leaf
                    int m = (node.keyTally + 1 + sibling.keyTally) / 2;
                    node.keys[node.keyTally++] = parent.keys[i];
                    int j = 0;
                    while (node.keyTally < m) {
                        node.keys[node.keyTally] = sibling.keys[j];
                        ((BPTreeInnerNode<TKey, TValue>) node).references[node.keyTally] = ((BPTreeInnerNode<TKey, TValue>) sibling).references[j];
                        node.keyTally++;
                        sibling.keys[j] = null;
                        ((BPTreeInnerNode<TKey, TValue>) sibling).references[j] = null;
                        j++;
                    }
                    parent.keys[i] = sibling.keys[j];
                    ((BPTreeInnerNode<TKey, TValue>) node).references[node.keyTally] = ((BPTreeInnerNode<TKey, TValue>) sibling).references[j];
                    j++;
                    for (int k = 0; k < sibling.keyTally - j; k++) {
                        sibling.keys[k] = sibling.keys[k + j];
                        sibling.keys[k + j] = null;
                        ((BPTreeInnerNode<TKey, TValue>) sibling).references[k] = ((BPTreeInnerNode<TKey, TValue>) sibling).references[k + j];
                        ((BPTreeInnerNode<TKey, TValue>) sibling).references[k + j] = null;
                    }
                    ((BPTreeInnerNode<TKey, TValue>) sibling).references[sibling.keyTally - j] = ((BPTreeInnerNode<TKey, TValue>) sibling).references[sibling.keyTally];
                    ((BPTreeInnerNode<TKey, TValue>) sibling).references[sibling.keyTally] = null;
                    sibling.keyTally = sibling.keyTally - j;

                    /*//insert into node
                        //from parent
                    node.keys[node.keyTally] = (TKey) parent.keys[i];
                    node.keyTally++;
                    int mid = sibling.keyTally/2;
                    for (int j = 0; j < mid; j++) {
                        node.keys[node.keyTally] = (TKey) sibling.keys[j];
                        node.keyTally++;
                    }
                    //node.keys[node.keyTally] = (TKey) parent.keys[i];
                    //node.keyTally++;
                    ((BPTreeInnerNode<TKey, TValue>) node).setChild(node.keyTally, ((BPTreeInnerNode<TKey, TValue>) sibling).getChild(0));
                    //update parent
                    parent.keys[i] = sibling.keys[0];
                    //update Sibling
                    for (int j = 0; j < sibling.keyTally; j++) {
                        sibling.keys[j] = sibling.keys[j + 1];
                        ((BPTreeInnerNode<TKey, TValue>) sibling).references[j] = ((BPTreeInnerNode<TKey, TValue>) sibling).references[j + 1];
                    }
                    ((BPTreeInnerNode<TKey, TValue>) sibling).setChild(sibling.keyTally, null);
                    /* redistribute between node, sibling and parent (ie take from top and then move up from fullSib)
                    move child reference over
                    sibling.keyTally -= mid;*/
                }

                resetParentNodes();
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
                        if (node.keyTally > 0) {
                            newRoot.references[newRoot.keyTally] = ((BPTreeInnerNode<TKey, TValue>) node).references[node.keyTally];
                        }
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
                } /*else if (i != 0) {
                    if (parent.keyTally != 1 && parent.getChild(i + 1) != null) { //added 2nd and
                        parent.keys[i] = parent.getChild(i + 1).keys[0];
                    }
                }*/


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
        BPTreeNode<TKey, TValue> mChild;
        if (i == 0) {
            mChild = parent.getChild(i + 1);
            mChild.leftSibling = parent.getChild(i).leftSibling;
            if (mChild.rightSibling != null) {
                mChild.rightSibling.leftSibling = mChild;//parent.getChild(i).leftSibling;
            }
            mChild.rightSibling = parent.getChild(i + 2); //added in
        } else if (i == parent.keyTally) {
            mChild = parent.getChild(i - 1);
            mChild.rightSibling = parent.getChild(i).rightSibling;
            if (parent.getChild(i).rightSibling != null) {
                parent.getChild(i).rightSibling = mChild;
                //parent.getChild(i).rightSibling.leftSibling = mChild
            }
        } else {
            mChild = parent.getChild(i);
            BPTreeNode<TKey, TValue> lChild;
            BPTreeNode<TKey, TValue> rChild;
            if (mChild != null) {
                if (i!=0) {
                    lChild = parent.getChild(i-1);
                    lChild.rightSibling = mChild.rightSibling;
                }
                if (i<parent.keyTally) {
                    rChild = parent.getChild(i+1);
                    rChild.leftSibling = mChild.leftSibling;
                }
                /*
                if (mChild.leftSibling != null) mChild.leftSibling.rightSibling = mChild.rightSibling;
                if (mChild.rightSibling != null) mChild.rightSibling.leftSibling = mChild.leftSibling;*/
            }
        }
    }

    private void updateParent(int i, BPTreeInnerNode<TKey, TValue> parent) {
        /*if (parent.getChild(i).isLeaf()) {
            updateParentLinkages(i, parent);
        }*/
        //if need to remove first element: swap quickly //possibly just broke it
        boolean swap = false;
        if (i == 1 && parent.getChild(i).keyTally < Math.ceil(m / 2.0) - 1) {
            parent.references[1] = parent.references[0];
            parent.keys[0] = parent.keys[1];
            updateParentLinkages(0, parent);
        }
        //if need to remove last element: swap quickly
        else if (i + 1 == parent.keyTally && parent.getChild(i).keyTally < Math.ceil(m / 2.0) - 1) {
            parent.references[parent.keyTally - 1] = parent.references[parent.keyTally];
            parent.keys[parent.keyTally - 2] = parent.keys[parent.keyTally - 1];
            parent.references[parent.keyTally] = null;
            parent.keys[parent.keyTally - 1] = null;
            parent.keyTally--;
            if (parent.getChild(0).isLeaf()) {
                BPTreeLeafNode<TKey, TValue> lChild = (BPTreeLeafNode<TKey, TValue>) parent.getChild(i-2);
                BPTreeLeafNode<TKey, TValue> mChild = (BPTreeLeafNode<TKey, TValue>) parent.getChild(i-1);
                BPTreeLeafNode<TKey, TValue> rChild = (BPTreeLeafNode<TKey, TValue>) parent.getChild(i);
                lChild.rightSibling = mChild;
                mChild.leftSibling = lChild;
                mChild.rightSibling = rChild;
                rChild.leftSibling = mChild;
            }
            return;

            //updateParentLinkages(i + 1, parent);
        } else {
            updateParentLinkages(i, parent);
        }
        if (i != 0) {
            TKey keyx = (TKey) parent.keys[i - 1];
            TKey keyy = (TKey) parent.getChild(i - 1).keys[parent.getChild(i - 1).keyTally - 1];
            if ( ((Comparable)keyx).compareTo((Comparable)keyy)<=0) {
                parent.keys[i-1] = parent.keys[i];
            }
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
        if (i != 0) {
            //added in for order 6
            if (i == 1 && parent.keys[0] != null) {
                TKey val = (TKey) parent.getChild(0).keys[parent.getChild(0).keyTally - 1];
                if (((TKey) parent.keys[0]).compareTo(val) <= 0) {
                    parent.keys[0] = parent.getChild(1).keys[0];
                }
            }
        }
    }

    protected void resetParentNodes() {
        if (this.isLeaf()) {
            return;
        } else if (((BPTreeInnerNode<TKey, TValue>) this).getChild(0).isLeaf()) {
            for (int i = 0; i < this.keyTally + 1; i++) {
                ((BPTreeInnerNode<TKey, TValue>) this).getChild(i).setParent(this);
            }
        } else {
            for (int i = 0; i < this.keyTally + 1; i++) {
                ((BPTreeInnerNode<TKey, TValue>) this).getChild(i).setParent(this);
                ((BPTreeInnerNode<TKey, TValue>) this).getChild(i).resetParentNodes();
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
        //0.
        if (this.keyTally == 0) {
            return null;
        }
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

    public TValue[] keys() {
        //0.
        if (this.keyTally == 0) {
            return null;
        }
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
        Object[] arrKeys = new Object[totalLength];

        //4. Traverse
        tempLeaf = (BPTreeLeafNode<TKey, TValue>) tempNode;            //reinitialise tempLeaf
        int j = 0;
        while (tempLeaf != null) {
            for (int i = 0; i < tempLeaf.keyTally; i++) {
                arrKeys[j] = (TKey) tempLeaf.keys[i];
                j++;
            }
            tempLeaf = (BPTreeLeafNode<TKey, TValue>) tempLeaf.rightSibling;
        }
        //5. Return
        return (TValue[]) arrKeys;


    }


    public BPTreeNode<TKey, TValue> getLeftMostLeaf(BPTreeNode<TKey, TValue> node) {
        while (!node.isLeaf()) {
            node = ((BPTreeInnerNode<TKey, TValue>) node).getChild(0);
        }
        return node;
    }

    public void printAllKeys() {
        for (int i = 0; i < this.keyTally; i++) {
            System.out.print(this.keys[i] + " ");
        }
        System.out.println("");
    }

    public BPTreeNode<TKey, TValue> search(TKey key, BPTreeNode<TKey, TValue> node) {
        return node.searchNode(key);
    }


}