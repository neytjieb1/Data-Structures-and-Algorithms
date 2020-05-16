import java.awt.*;

/**
 * A B+ tree generic node
 * Abstract class with common methods and data. Each kind of node implements this class.
 * @param <TKey> the data type of the key
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
	

	protected BPTreeNode() 
	{
		this.keyTally = 0;
		this.parentNode = null;
		this.leftSibling = null;
		this.rightSibling = null;
	}

	public int getKeyCount() 
	{
		return this.keyTally;
	}
	
	@SuppressWarnings("unchecked")
	public TKey getKey(int index) 
	{
		return (TKey)this.keys[index];
	}

	public void setKey(int index, TKey key) 
	{
		this.keys[index] = key;
	}

	public BPTreeNode<TKey, TValue> getParent() 
	{
		return this.parentNode;
	}

	public void setParent(BPTreeNode<TKey, TValue> parent) 
	{
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
	public void print(BPTreeNode<TKey, TValue> node)
	{
		level++;
		if (node != null) {
			System.out.print("Level " + level + " ");
			node.printKeys();
			System.out.println();

			// If this node is not a leaf, then 
        		// print all the subtrees rooted with this node.
        		if (!node.isLeaf())
			{	BPTreeInnerNode inner = (BPTreeInnerNode<TKey, TValue>)node;
				for (int j = 0; j < (node.m); j++)
    				{
        				this.print((BPTreeNode<TKey, TValue>)inner.references[j]);
    				}
			}
		}
		level--;
	}

	/**
	 * Print all the keys in this node
	 */
	protected void printKeys()
	{
		System.out.print("[");
    		for (int i = 0; i < this.getKeyCount(); i++)
    		{
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
	public TValue search(TKey key) 
	{
		BPTreeNode<TKey, TValue> tempNode = this;

		//go down inners
		while (!tempNode.isLeaf()) {
			BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey,TValue>) tempNode;
			tempNode = tempInner.getChild(tempInner.findIndexOf(key));
		}

		//correct leaf found
		BPTreeLeafNode<TKey, TValue> tempLeaf = (BPTreeLeafNode<TKey, TValue>) tempNode;
		//find index of key between right values
		int testIndex = tempLeaf.getIndexOf(key);


		if (testIndex==keyTally) {
			return null;
		}
		else if (!tempLeaf.keys[testIndex].equals(key)) {
			return null;
		}
		else {
			return tempLeaf.getValue(testIndex);
		}
	}



	/**
	 * Insert a new key and its associated value into the B+ tree. The root node of the
	 * changed tree should be returned.
	 */
	public BPTreeNode<TKey, TValue> insert(TKey key, TValue value) 
	{
		// Your code goes here
		if (this.isLeaf() && this.keyTally<m-1) {
			this.insertNonFulLeafNode(key, value);
		}

		else if (this.isLeaf() && this.keyTally==(m-1) && this.parentNode==null) { 		//ie it's the root
			this.insertNonFulLeafNode(key, value);
			return splitInsideNode(this);
		}

		else if (this.isLeaf() && this.keyTally==(m-1)) {
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
			//6. return parent node
			return newRoot;
		}

		else {			//not a leaf
			//cast to inner to find child
			BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey,TValue>) this;
			//insert into child
			tempInner.getChild(tempInner.findIndexOf(key)).insert(key, value);
		}
		return this;
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

			if (i+1 < m) {
				tempVal = (TValue) ((BPTreeLeafNode<TKey, TValue>) this).values[++i];
				tempKey = (TKey) this.keys[i];
			}


		} while (tempVal != null && i < keys.length+1) ;
		if (i+1 < m || key!=null) {
			keys[i] = key;
			((BPTreeLeafNode<TKey, TValue>) this).values[i] = val;
		}
		keyTally++;

	}

	protected BPTreeNode<TKey,TValue> splitInsideNode(BPTreeNode<TKey,TValue> node) {
		//happens at root or inside node
		//1. Create rChild
		BPTreeNode<TKey, TValue> rChild;
		if (this.isLeaf()) {
			rChild = new BPTreeLeafNode<>(m);
		}
		else {
			rChild = new BPTreeInnerNode<>(m);
		}
		//2. Move data over to rChild
		redistributeData(this, rChild);
		//3. create Parent
		BPTreeInnerNode<TKey, TValue> parent = new BPTreeInnerNode<>(this.m);
		//4. Insert index key
		parent.keys[0] = rChild.keys[0];
		parent.keyTally++;
		//5. Update links
		parent.references[0] = this;
		this.parentNode = parent;
		parent.references[1] = rChild;
		updateLinks(this, rChild,parent);
		//6. return parent
		return parent;

	}

	protected void redistributeData(BPTreeNode<TKey,TValue> node, BPTreeNode<TKey,TValue> rChild) {
		int midIndex;
		if (node.keyTally==m) midIndex = Math.floorDiv(node.keyTally, 2);
		else midIndex = Math.floorDiv(node.keyTally-1, 2);
		int numMoveValues = node.keyTally - (midIndex);

		//CASE: leaf
		if (node.isLeaf()) {
			//move over half of keys & values to rChild
			for (int i = midIndex; i < node.keyTally; i++) {
				rChild.keys[i-midIndex] = node.keys[i];
				((BPTreeLeafNode<TKey, TValue>)rChild).values[i-midIndex] = ((BPTreeLeafNode<TKey, TValue>)node).values[i];

				node.keys[i] = null;
				((BPTreeLeafNode<TKey, TValue>)node).values[i] = null;
			}
		}
		//CASE: !leaf
		else {
			for (int i = midIndex; i < node.keyTally; i++) {
				rChild.keys[i-midIndex] = node.keys[i];
				((BPTreeInnerNode<TKey, TValue>)rChild).references[i-midIndex] = ((BPTreeInnerNode<TKey, TValue>)node).references[i];

				node.keys[i] = null;
				((BPTreeInnerNode<TKey, TValue>)node).references[i] = null;
			}
		}
		rChild.keyTally = rChild.keyTally+numMoveValues;
		node.keyTally = node.keyTally-numMoveValues;

	}

	protected void updateLinks(BPTreeNode<TKey,TValue> lChild, BPTreeNode<TKey,TValue> rChild, BPTreeNode<TKey,TValue> parentNode) {
		//must work for leaves and non-leaves
		rChild.rightSibling = lChild.rightSibling;
		lChild.rightSibling = rChild;
		rChild.leftSibling = lChild;
		rChild.parentNode = lChild.parentNode;
		//update parent's references? Can work out indices, but will be done otherwhere
	}

	protected BPTreeNode<TKey,TValue> addRChildToParent(TKey key, TValue value, BPTreeNode<TKey,TValue> node) {
		//1. Get index
		int i = ((BPTreeInnerNode<TKey,TValue>)node.parentNode).findIndexOf(key);
		//2. Add (originally thought to move up?
		((BPTreeInnerNode<TKey,TValue>)node.parentNode).references[i+1] = node.rightSibling;  //node.rightSibling already defined
		parentNode.keys[i] = node.rightSibling.keys[0];   //is this necessary?
		parentNode.keyTally++;								//is this necessary
		//3. Check if parent now overfull
		if (this.parentNode.keyTally == m) {
			BPTreeNode<TKey, TValue> newParent = splitInsideNode(this.parentNode);
			return newParent;
		}
		else return this.parentNode;

		/*  WHY DID I WANT TO DO THIS?
		find index of parent key   //findInfimum(parent, key)
            //so parent.references[i+1] must point to rChild
    	for (i+1 to keyTally) in parent
        move up keys and add tempKey (which starts out as 1st key in rChild)
        move up references and add tempRef (which starts as rChild)
		*/
	}






	/**
	 * Delete a key and its associated value from the B+ tree. The root node of the
	 * changed tree should be returned.
	 */
	public BPTreeNode<TKey, TValue> delete(TKey key) 
	{
		// Your code goes here
		//At leaf level
		//do NOT remove separator
		return null;
	}



	/**
	 * Return all associated key values on the B+ tree in ascending key order. An array
	 * of the key values should be returned.
	 */
	@SuppressWarnings("unchecked")
	public TValue[] values() 
	{
		// Your code goes here
		//1. Go down to smallest
		BPTreeNode<TKey, TValue> tempNode = this;
		while (!tempNode.isLeaf()) {
			BPTreeInnerNode<TKey, TValue> tempInner = (BPTreeInnerNode<TKey,TValue>) tempNode;
			tempNode = tempInner.getChild(0);
		}
		BPTreeLeafNode<TKey, TValue> tempLeaf = (BPTreeLeafNode<TKey, TValue>) tempNode;

		//2. Get Length
		int totalLength = 0;
		while (tempLeaf!=null) {
			totalLength += tempLeaf.keyTally;
			tempLeaf = (BPTreeLeafNode<TKey, TValue>)tempLeaf.rightSibling;
		}

		//3. Create Array
		Object[] arrValues = new Object[totalLength];

		//4. Traverse
		tempLeaf = (BPTreeLeafNode<TKey, TValue>) tempNode;			//reinitialise tempLeaf
		int j = 0;
		while (tempLeaf!=null) {
			for (int i = 0; i < tempLeaf.keyTally; i++) {
				arrValues[j] = (TValue)tempLeaf.values[i];
				j++;
			}
			tempLeaf = (BPTreeLeafNode<TKey, TValue>)tempLeaf.rightSibling;
		}
		//5. Return
		return (TValue[]) arrValues;


	}

}