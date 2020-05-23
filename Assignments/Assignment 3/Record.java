/**
 * Class for a table row
 * Stores and manipulates all values for a row in a record.
 */
public class Record {

	private Object[] columns;
	private int columnCount;

	public Record(int count) {
		this.columnCount = count;
		this.columns = new Object[columnCount];
	}

	public Object getColumn(int idx) {
		return columns[idx-1];
	}

	public void setColumn(int idx, Object obj) {

		columns[idx-1] = obj;
	}

	public int getColumnCount() {return columnCount;};

	public String getValues() {
		
		if (columns[0] != null) {
			String result = columns[0].toString();
			for (int i = 1; i < columnCount; i++)
			{
				result = result + "," + columns[i].toString();
			}
			return result;
		} else
			return "";
	}

}