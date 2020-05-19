import java.awt.datatransfer.StringSelection;

public class Trie {
    protected char[] letters;
    protected Node root = null;
    private int numPtrs;

    public Trie(char[] letters) {
        this.letters = letters;
        this.numPtrs = letters.length + 1;
    }


    //Provided Helper functions

    private int index(char c) {
        for (int k = 0; k < letters.length; k++) {
            if (letters[k] == (c)) {
                return k + 1;
            }
        }
        return -1;
    }

    private char character(int i) {
        if (i == 0) {
            return '#';
        } else {
            return letters[i - 1];
        }
    }

    private String nodeToString(Node node, boolean debug) {
        if (node.isLeaf) {
            return node.key;
        } else {
            String res = "";
            for (int k = 0; k < node.ptrs.length; k++) {
                if (node.ptrs[k] != null) {
                    res += " (" + character(k) + ",1) ";
                } else if (debug) {
                    res += " (" + character(k) + ",0) ";
                }
            }
            return res;
        }
    }

    public void print(boolean debug) {
        Queue queue = new Queue();
        Node n = root;
        if (n != null) {
            n.level = 1;
            queue.enq(n);
            while (!queue.isEmpty()) {
                n = queue.deq();
                System.out.print("Level " + n.level + " [");
                System.out.print(nodeToString(n, debug));
                System.out.println("]");
                for (int k = 0; k < n.ptrs.length; k++) {
                    if (n.ptrs[k] != null) {
                        n.ptrs[k].level = n.level + 1;
                        queue.enq(n.ptrs[k]);
                    }
                }
            }
        }
    }


    ////// You may not change any code above this line //////
//=================================================================================================================
    ////// Implement the functions below this line //////


    // Function to insert the given key into the trie at the correct position.
    public void insert(String key) {
        // Your code goes here
        char[] K = key.toCharArray();

        if (this.root == null) {
            this.root = new Node(key, this.numPtrs);
        } else if (this.root != null && this.root.isLeaf) {
            Node parent = new Node(numPtrs);
            parent.ptrs[index(this.root.key.toCharArray()[0])] = this.root;
            this.root = parent;
        }
        //continue normally

        int i = 0;
        Node p = this.root;
        boolean inserted = false;
        while (!inserted) {
            if (i == key.length()) {
                p.ptrs[0] = new Node(key, numPtrs);
                p.endOfWord = true;
                inserted = true;
            } else if (p.ptrs[index(K[i])] == null) {      //why zero?
                p.ptrs[index(K[i])] = new Node(key, numPtrs);
                inserted = true;
            } else if (p.ptrs[index(K[i])].isLeaf) {
                String K_leaf_key = p.ptrs[index(K[i])].key;
                char[] K_leaf = K_leaf_key.toCharArray();
                do {
                    Node tempNonLeaf = new Node(numPtrs);
                    p.ptrs[index(K[i])] = tempNonLeaf;
                    p = tempNonLeaf;
                    i++;
                } while (i < K_leaf_key.length() && K[i] == K_leaf[i]);   //is this enough?
                if (i == key.length()) {
                    p.endOfWord = true;
                    p.ptrs[0] = new Node(key, numPtrs);
                    p.ptrs[index(K_leaf[i])] = new Node(K_leaf_key, numPtrs);
                } else if (i == K_leaf_key.length()) {
                    p.endOfWord = true;
                    p.ptrs[0] = new Node(K_leaf_key, numPtrs);
                    p.ptrs[index(key.toCharArray()[i])] = new Node(key, numPtrs);
                } else {
                    p.ptrs[index(K[i])] = new Node(key, numPtrs);
                    p.ptrs[index(K_leaf[i])] = new Node(K_leaf_key, numPtrs);
                }
                inserted = true;
            } else {
                p = p.ptrs[index(K[i++])];
            }
        }
    }


    // Function to determine if a node with the given key exists.
    public boolean contains(String key) {
        // Your code goes here
        int i = 0;
        Node p = root;
        char[] K = key.toCharArray();
        while (!p.isLeaf) {
            if (p.ptrs[0] != null && i==K.length) {
                p = p.ptrs[0];
                if (p.key == key) return true;
            }

            if (p.ptrs[0] != null && p.ptrs[0].equals(key)) {
                return true;
            }
            //letter not in alphabet
            else if (index(K[i])==-1) {
                return false;
            }
            //this word not contained in alphabet
            else if (p.ptrs[index(K[i])] == null) {
                return false;
            }
            //has word in # part equalling key?
            else if (i == key.length()) {
                if (p.ptrs[0] != null && p.ptrs[0].equals(key)) {
                    return true;
                }
                else {
                    return false;
                }
            }
             else {
                p = p.ptrs[index(K[i])];
                i++;
            }
        }
        if (p.key == key) {
            return true;
        }
        else {
            return false;
        }
    }


    // Function to print all the keys in the trie in alphabetical order.
    public void printKeyList() {

        // Your code goes here

    }


    //Helper functions

}