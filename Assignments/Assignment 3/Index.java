/**
 * Class for a table index
 * Stores and manipulates all values from a table column in a B+ tree.
 */
public class Index {

	private String name;
	private String column;
	private BPTree index;

	public Index(String name, String column, BPTree index) {

		this.name = name;
		this.column = column;
		this.index = index; 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumnName() {
		return column;
	}

	public void setColumnName(String column) {
		this.column = column;
	}

	public BPTree getIndex() {
		return index;
	}

	public void setIndex(BPTree index) {
		this.index = index;
	}

}