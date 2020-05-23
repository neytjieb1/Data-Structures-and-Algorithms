/**
 * A simplistic database table class. Uses the record class to store row data and the index class to
 * maintain indexes for specific columns.
 * Class also implements basic SQL methods. Uses the error class for common error messages.
 */
public class Table {

    private String name;
    private String[] columns;
    private Record[] records;
    private Index[] indexes;
    private int rowId;
    private int recordCount;
    private int indexCount;

    public Table(String name, String[] columns) {
        this.rowId = 1; //start index in records array
        this.recordCount = 0;
        this.indexCount = 0;
        this.name = name;
        this.columns = columns;
        this.records = new Record[1000]; //initial size of table
        this.indexes = new Index[10]; //initial number of indexes
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRecordCount() {
        return this.recordCount;
    }

    public int getIndexCount() {
        return this.indexCount;
    }

    public String debug() {
        String result = "";
        if (indexCount > 0) {
            for (int i = 0; i < indexCount; i++) {
                Index idx = indexes[i];
                result = result + idx.getIndex().debug();
                if ((i + 1) < indexCount)
                    result = result + " ";
            }
        } else {
            result = "No Indexes!";
        }
        return result;
    }

    ////// You may not change any code above this line //////


    ////// Implement the functions below this line //////

    /**
     * Insert the given "rec" in this table. Every record needs to have a unique row id, which is never reused.
     * Should indexes be present, they need to be updated.
     */
    //SQL: insert into table values (rec)
    @SuppressWarnings("unchecked")
    public void insert(Record rec) {
        // Your code goes here
        //Insert the record + update keyTally
        this.records[this.rowId++] = rec;
        this.recordCount++;
        //Check if indices exist
        for (int i = 0; i < rec.getColumnCount(); i++) { //for every record-entry
            for (int j = 0; j < 10; j++) {
                if (this.indexes[j] == null) {
                    return;
                }
                //existence --> update index (ie. just insert?)
                if (this.columns[i].equals(indexes[j].getColumnName())) {
                    //update the index set
                    indexes[j].getIndex().insert(this.recordCount - 1, rec.getColumn(i));
                }
            }
        }
    }


    /**
     * Represents the full delete SQL query. Depending on given parameters, different subqueries are called.
     */
    //SQL: delete from table where column equals value
    public void delete(String column, Object value) {
        if (column == null && value == null) {
            this.delete();
        } else {
            this.deletep(column, value);
        }
    }


    /**
     * Delete all the records in this table where the given "column" matches "value". Needs to use the index
     * for "column" and call the search method of the used B+ tree. If no index exists for "column", conventional
     * record iteration and search should be used. Deleted rows remain empty and the records array should not be
     * compacted. recordCount however should be updated. Should indexes be present, they need to be updated.
     */
    //SQL: delete from table where column equals value
    @SuppressWarnings("unchecked")
    private void deletep(String column, Object value) {

        // Your code goes here
    }


    /**
     * Delete all the records in this table. recordCount and row id should be updated. Should also
     * reset all indexes if present.
     */
    //SQL: delete from table
    private void delete() {

        // Your code goes here
    }


    /**
     * Represents full select SQL query. Depending on given parameters, different subqueries are called.
     */
    //SQL: select * from table where column equals value order by ocolumn
    public void select(String column, Object value, String ocolumn) {
        if (column == null && value == null) {
            if (ocolumn != null)
                this.select(ocolumn);
            else
                this.select();
        } else {
            this.select(column, value);
        }
        System.out.println();
    }


    /**
     * Print all the records in this table where the given "column" matches "value". Should call the getValues
     * method of the record class. Needs to use the index for "column" and call the search method of the used
     * B+ tree. If no index exists for "column", conventional record iteration and search should be used.
     * If the table is empty, print error message 1.
     * If no record matches, print error message 4.
     * SQL: select * from table where column equals value
     */
    @SuppressWarnings("unchecked")
    private void select(String column, Object value) {              //can we assume a valid columns will be sent in?
        // Your code goes here
        if (this.records==null || this.recordCount==0) {
            System.out.println(Error.Err1);
            return;
        }
        //Find columns
        int i;
        for (i = 0; i < this.indexes.length; i++) {
            if (this.indexes[i].getName().equals(column)) break;
        }
        //Find all in column that match value
        Object[] colTreeValues = this.indexes[i].getIndex().values();

        //find first entry
        int j;
        for (j = 0; j < colTreeValues.length; j++) {
            if (colTreeValues[j].equals(value)) {
                break;
            }
        }
        //no entries found
        if (j==colTreeValues.length) {
            System.out.println(Error.Err4);
            return;
        }
        while (colTreeValues[j].equals(value)) {
            System.out.println(this.records[k].getValues());
            j++;
        }


        //for each entry
        /*for (int indexValues = 0; indexValues < colTreeValues.length; indexValues++) {
            //find in record
            for (int k = 1; k <= this.recordCount; k++) {
                if (this.records[k].getColumn(i).equals(colTreeValues[indexValues])) {
                    System.out.println(this.records[k].getValues());
                    break;
                }
            }
        }*/
    }


    /**
     * Print all the records in this table ordered by the given "ocolumn" in ascending order. Should call
     * the getValues method of the record class. Needs to use the index for ocolumn and call the values
     * method of the used B+ tree.
     * If the table is empty, print error message 1.
     * If no indexes are present, print error message 2.
     * If there is no index available for "ocolumn", print error message 3.
     //SQL: select * from table order by ocolumn
     */
    //Finished
    private void select(String ocolumn) {
        // Your code goes here
        if (this.recordCount == 0) {
            System.out.println(Error.Err1);
            return;
        }
        if (this.indexCount == 0) {
            System.out.println(Error.Err2);
            return;
        }

        Object[] values = null;
        int indexRecordsCol;
        for (indexRecordsCol = 0; indexRecordsCol < this.columns.length; indexRecordsCol++) {
            if (ocolumn.equals(this.columns[indexRecordsCol])) {
                values = this.indexes[indexRecordsCol].getIndex().values();
                break;
            }
        }
        if (values == null) {
            System.out.println(Error.Err3);
            return;
        }
        //for each entry in oColumns
        for (int indexValues = 0; indexValues < values.length; indexValues++) {
            //find in record
            for (int k = 1; k <= this.recordCount; k++) {
                if (this.records[k].getColumn(indexRecordsCol).equals(values[indexValues])) {
                    System.out.println(this.records[k].getValues());
                    break;
                }
            }
        }
    }


    /**
     * Print all the records in this table. Should call the getValues method of the record class. If
     * the table is empty print error message 1.
     //SQL: select * from table
     */
    //Finished
    private void select() {
        // Your code goes here
        if (this.records == null || this.recordCount==0) {
            System.out.println(new Error().Err1);
        }
        for (int i = 1; i <= this.recordCount; i++) {
            System.out.println(this.records[i].getValues());
        }
    }


    /**
     * Create an index called "name" using the record values from "column" as keys and the row id as value.
     * The created B+ tree must match the data type of "column". Return true if successful and false if
     * column does not exist.
     */
    //Broken
    public boolean createIndex(String name, String column) {
        // Your code goes here
        int indexRecordsCol;
        boolean found = false;
        for (indexRecordsCol = 0; indexRecordsCol < this.columns.length; indexRecordsCol++) {
            if (column.equals(this.columns[indexRecordsCol])) {
                found = true;
                break;
            }
        }
        if (found) {

            BPTree< Comparable, Integer > indexTree = new BPTree<Comparable, Integer >(4); //this.records[1].getClass()
            this.indexes[this.indexCount++] = new Index(name, column, indexTree);
            for (int i = 1; i <= this.recordCount; i++) {
                indexTree.insert(this.records[i].getColumn(indexRecordsCol + 1), i);
            }
        }
        return found;

    }


    /**
     * Print all the keys in the index "name". Should call the print method of the used B+ tree.
     */
    //Finished
    public void printIndex(String name) {
        // Your code goes here
        int i;
        for (i = 0; i < this.indexCount; i++) {
            String iName = this.indexes[i].getName();
            if (iName.equals(name)) {
                this.indexes[i].getIndex().print();
                break;
            }
        }
    }


    //Helper methods


}