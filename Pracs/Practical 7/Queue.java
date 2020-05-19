public class Queue {

	private int first, last, size;
	private Node[] elems;

	public Queue() {
		this(100);
	}

	public Queue(int n) {
		size = n;
		elems = new Node[size];
		first = last = -1;
	}

	public boolean isFull() {
		return first == 0 && last == size-1 || first == last+1; 
	}

	public boolean isEmpty() {
		return first == -1;
	}

	public void enq(Node n) {
		if (last == size-1 || last == -1) {
			elems[0] = n;
			last = 0;
			if (first == -1)
				first = 0;
		} else
			elems[++last] = n;
	}

	public Node deq() {
		if (!this.isEmpty()) {
			Node tmp = elems[first];
			if (first == last)
				last = first = -1;
			else if (first == size-1)
				first = 0;
			else
				first++;
			return tmp;
		} else
			return null;
	}

	public String toString() {
		String res = "";
		if (this.isEmpty()) {
			res = "[  ]";
		} else {
			res = "[ " + elems[first];
			boolean rev = false;
			for (int k = 1; k < size; k++) {
				if (first <= last || rev) {
					if ((first+k) <= last)
						res += ", " + elems[(first+k)];
					else
						break;
				} else {
					res += ", " + elems[(first+k)%size];
					if ((first+k)%size <= last)
						rev = true;
				}
			}
			res += " ]";
		}
		return res;
	}
}