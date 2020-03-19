/**
 * Name: Berne' Nortier
 * Student Number: 17091820
 */

public class SplayTree<T extends Comparable<T>> {

	protected enum SplayType {
		SPLAY,
		SEMISPLAY,
		NONE
	}	

	protected Node<T> root = null;
	
	/**
	 * Prints out all the elements in the tree
	 * @param verbose
	 *			If false, the method simply prints out the element of each node in the tree
	 *			If true, then the output provides additional detail about each of the nodes.
	 */
	public void printTree(boolean verbose) {
		String result;
		result = preorder(root, verbose);
		System.out.println(result);
	}
	
	protected String preorder(Node<T> node, boolean verbose) {
		if (node != null) {
			String result = "";
			if (verbose) {
				result += node.toString()+"\n";
			} else {
				result += node.elem.toString() + " ";
			}
			result += preorder(node.left, verbose);
			result += preorder(node.right, verbose);
			return result;
		}
		return "";
	}
	
	////// You may not change any code above this line //////

	////// Implement the functions below this line //////
	
	/**
	* Inserts the given element into the tree, but only if it is not already in the tree.
	* @param elem 
	* 		 	The element to be inserted into the tree
	* @return 
	*			Returns true if the element was successfully inserted into the tree. 
	*			Returns false if elem is already in the tree and no insertion took place.
	*
	*/
	public boolean insert(T elem) {
		//Your code goes here
		if (contains(elem)) {
			return false;
		}

		Node<T> p = root;
		Node<T> prev = null;
		while (p!=null) {
			prev = p;
			if (elem.compareTo(p.elem)<0) {
				p = p.left;
			}
			else {
				p = p.right;
			}
		}
		if (root == null) {
			root = new Node<T>(elem);
		}
		else if (elem.compareTo(prev.elem) < 0) {
			prev.left = new Node<T>(elem);
		}
		else {
			prev.right = new Node<T>(elem);
		}
		return true;
	}
	
	/**
	* Checks whether a given element is already in the tree.
	* @param elem 
	* 		 	The element being searched for in the tree
	* @return 
	*			Returns true if the element is already in the tree
	*			Returns false if elem is not in the tree
	*
	*/
	public boolean contains(T elem) {
		//Your code goes here
		Node<T> ptr = root;
		while (ptr != null) {
			if (elem.equals(ptr.elem)) {
				return true;
			}
			else if (elem.compareTo(ptr.elem) > 0 ) {
				ptr = ptr.right;
			}
			else {
				ptr = ptr.left;
			}
		}
		return false;
	}
	
	/**
	 * Accesses the node containing elem. 
	 * If no such node exists, the node should be inserted into the tree.
	 * If the element is already in the tree, the tree should either be semi-splayed so that 
	 * the accessed node moves up and the parent of that node becomes the new root or be splayed 
	 * so that the accessed node becomes the new root.
	 * @param elem
	 *			The element being accessed
	 * @param type
	 *			The adjustment type (splay or semi-splay or none)
	 */
	public void access(T elem, SplayType type) {
		//Your code goes here
			if (contains(elem))
			 {
				switch (type) {
					case SPLAY: splay(new Node<T>(elem));
					break;
					case SEMISPLAY: semisplay(new Node<T>(elem));
					break;
					case NONE: return;
				}
			}
		else {
				insert(elem);
			}
	}
	
	/**
	 * Semi-splays the tree using the parent-to-root strategy
	 * @param node
	 *			The node the parent of which will be the new root
	 */
	protected void semisplay(Node<T> node) {
		if (!contains(node.elem)) {
			return;
		}
		//root is already accessed element
		if (node==root) {
			return;
		}

		//Case 1: Parent is root
		Node<T> parent = findAncestor(node.elem);
		if (parent.elem.equals(root.elem)) {
			totalSplay(root, parent.elem);
			return;
		}

		//Case 2: Heterogeneous
		parent = findAncestor(node.elem);
		Node<T> grand = findAncestor(parent.elem);
		if (grand.left.elem==parent.elem && parent.right.elem==node.elem) {
			totalSplay(root, node.elem);
			return;
		}
		else if (grand.right.elem == parent.elem && parent.left.elem==node.elem) {
			totalSplay(root, node.elem);
			return;
		}

		//Case 3: Homogeneous
		parent = findAncestor(node.elem);
		totalSplay(root, parent.elem);
/*
		grand = findAncestor(parent.elem);
		if (grand.left.elem.equals(parent.elem) && parent.left.elem.equals(node.elem)) {
			parent = rightRot(parent);
		}
		else if (grand.right.elem.equals(parent.elem) && parent.right.elem.equals(node.elem)) {
			parent = leftRot(parent);
		}
		totalSplay(root, parent.elem);
*/
	}


	/**
	 * Splays the tree using the node-to-root strategy
	 * @param node
	 *			The node which will be the new root
	 */
	protected void splay(Node<T> node) {
		//Your code goes here
		totalSplay(root, node.elem);
	}

	private Node<T> totalSplay(Node<T> node, T val) {
		if (node==null) {
			return null;
		}
		//root is val
		if (node.elem==val) {
			return node;
		}
		// left of root
		if (val.compareTo(node.elem) < 0) {
			//left of child
			if (val.compareTo(node.left.elem) < 0) {
				node.left.left = totalSplay(node.left.left, val);
				node = rightRot(node);
			}
			//right of child
			else if (val.compareTo(node.left.elem) > 0) {
				node.left.right = totalSplay(node.left.right, val);
				if (node.left.right != null)
					node.left = leftRot(node.left);
			}
			if (node.left == null) {
				return node;
			}
			else {
				root = rightRot(node);
				return root;
			}
		}
		//go right
		else if (val.compareTo(node.elem) > 0) {
			//other side
			if (node.right == null) {
				return node;
			}
			if (val.compareTo(node.right.elem) < 0) {
				node.right.left  = totalSplay(node.right.left, val);
				if (node.right.left != null) {
					node.right = rightRot(node.right);
				}
			}
			else if (val.compareTo(node.right.elem) > 0) {
				node.right.right = totalSplay(node.right.right, val);
				node = leftRot(node);
			}
			if (node.right == null) {
				return node;
			}
			else {
				root = leftRot(node);
				return root;
			}
		}
		else {
			return node;
		}
	}


	private Node<T> leftRot(Node<T> node) {
		Node<T> child = node.right;
		node.right = child.left;
		child.left = node;
		return child;
	}
	private Node<T> rightRot(Node<T> node) {
		Node<T> child = node.left;
		node.left = child.right;
		child.right= node;
		return child;
	}

	protected Node<T> findAncestor(T childEl) {
		if (!contains(childEl)) {
			return null;
		}
		Node<T> ptr = root;
		Node<T> ancest = null;
		//Find ptr
		while (ptr != null) {
			if (childEl.equals(ptr.elem)) {
				return ancest;
			} else if (childEl.compareTo(ptr.elem) > 0) {
				ancest = ptr;
				ptr = ptr.right;
			} else {
				ancest = ptr;
				ptr = ptr.left;
			}
		}
		return null;
	}


}

