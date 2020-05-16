/**
 * A B+ tree internal node
 * @param <TKey> the data type of the key
 * @param <TValue> the data type of the value
 */
class BPTreeInnerNode<TKey extends Comparable<TKey>, TValue> extends BPTreeNode<TKey, TValue> {
	
	protected Object[] references; 
	
	public BPTreeInnerNode(int order) {
		this.m = order;
		// The strategy used here first inserts and then checks for overflow. 
		// Thus an extra space is required i.e. m instead of m-1/m+1 instead of m.
		// You can change this if needed. 
		this.keys = new Object[m];
		this.references = new Object[m + 1];
	}
	
	@SuppressWarnings("unchecked")
	public BPTreeNode<TKey, TValue> getChild(int index) {
		return (BPTreeNode<TKey, TValue>)this.references[index];
	}

	public void setChild(int index, BPTreeNode<TKey, TValue> child) {
		this.references[index] = child;
		if (child != null)
			child.setParent(this);
	}
	
	@Override
	public boolean isLeaf() {
		return false;
	}

	////// You should not change any code above this line //////

	////// Implement functions below this line //////

	public int findIndexOf(TKey key) {
		for (int i = 0; i < this.keyTally; i++) {
			TKey keyx = (TKey) this.keys[i];
			if  (keyx.compareTo(key) > 0) return i;
		}
		return this.keyTally;
	}

}