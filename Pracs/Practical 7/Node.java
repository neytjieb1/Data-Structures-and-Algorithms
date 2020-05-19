public class Node {
  /**
   * The key stored by the node (if it is a leaf node)
   */
	protected String key;

  /**
   * This keeps track of whether the given node is a leaf node or not
   * You should update this value as necessary (when inserting into the tree)
   */
	protected boolean isLeaf;

  /**
   * A list of pointers to children nodes. 
   * These should be ordered according to the characters in the Trie object's 'letters' array.
   */
	protected Node[] ptrs = null;

  /**
   * A boolean to track whether a word ends at this node or not.
   */
	protected boolean endOfWord = false;

  /**
   * A boolean to track the level of this node.
   */
	protected int level = 0;
  
  /**
   * A constructor for a leaf node. Notice that even leaf nodes are initialized with an array of pointers. (This isn't necessary but may make your life slightly easier)
   */
	public Node(String key, int numPtrs) {
		this.key = key;
		isLeaf = true;
		ptrs = new Node[numPtrs];
	}
	
  /**
   * A constructor for a non-leaf node.
   */
	public Node(int numPtrs) {	
		isLeaf = false;
		ptrs = new Node[numPtrs];
	}

  /**
   * A utility function to give a string representation of this node
   */
	public String toString() {
		if (this.isLeaf) {
			return this.key;
		}
		else {
			String res = "";
			for (int k = 0; k < this.ptrs.length; k++) {
				if (this.ptrs[k] == null) {
					res += " 0 ";
				} else {
					res += " 1 ";
				}
			}
			return res;
		}
	}

}