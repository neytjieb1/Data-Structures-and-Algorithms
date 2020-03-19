//Berne' Nortier
//17091820

public class BSTNode<T extends Comparable<? super T>>
{

    protected T element;
    protected BSTNode<T> left, right;

    public BSTNode() {
        left = right = null;
    }

    public BSTNode(T el) {
        this(el,null,null);
    }

    public BSTNode(T el, BSTNode<T> lt, BSTNode<T> rt) {
        this.element = el; left = lt; right = rt;
    }

    public String toString() {
        String out = element.toString();
        out += " [L: "+ (left == null ? null : left.element) + "] ";
        out += " [R: "+ (right == null ? null : right.element) + "] ";
        return out;
    }

}