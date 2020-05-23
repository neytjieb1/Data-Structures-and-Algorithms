public class Main {
	

	public static void main(String[] args) 
	{

		String[] columns = new String[]{"StudentId", "Name", "Surname"};
		Table student = new Table("Student", columns);

		//Add some records
		Record rec1 = new Record(3);
		rec1.setColumn(1, Integer.valueOf(16230943));
		rec1.setColumn(2, new String("Lerato"));
		rec1.setColumn(3, new String("Molefe"));
		student.insert(rec1);
		Record rec2 = new Record(3);
		rec2.setColumn(1, Integer.valueOf(17248830));
		rec2.setColumn(2, new String("Isabel"));
		rec2.setColumn(3, new String("Muller"));
		student.insert(rec2);
		Record rec3 = new Record(3);
		rec3.setColumn(1, Integer.valueOf(16094340));
		rec3.setColumn(2, new String("John"));
		rec3.setColumn(3, new String("Botha"));
		student.insert(rec3);
		Record rec4 = new Record(3);
		rec4.setColumn(1, Integer.valueOf(17012340));
		rec4.setColumn(2, new String("Michael"));
		rec4.setColumn(3, new String("Evans"));
		student.insert(rec4);

		//Select All - No indexes
		student.select(null, null, null);

		//Select All Ordered By - No indexes
		student.select(null, null, "StudentId");

		student.createIndex("PrimKey", "StudentId");
		student.createIndex("SecKey", "Surname");
		student.printIndex("PrimKey");
		student.printIndex("SecKey");

		/*//Select All Ordered By - Use index
		student.select(null, null, "StudentId");
		student.select(null, null, "Name");
		student.select(null, null, "Surname");

		//Select All Where Specific - Use index if available
		student.select("StudentId", 17248830, null);
		student.select("StudentId", 17123456, null);
		student.select("Name", "Michael", null);
		student.select("Name", "Mike", null);
		student.select("Surname", "Botha", null);
		student.select("Surname", "Oldfield", null);

		//Delete All Where Specific - Use index if available
		student.delete("StudentId", 17248830);
		student.delete("Surname", "Evans");

		//Updated indexes
		student.printIndex("PrimKey");
		student.printIndex("SecKey");

		//Select All
		student.select(null, null, null);

		//Delete All
		student.delete(null, null);

		//Updated indexes
		student.printIndex("PrimKey");
		student.printIndex("SecKey");

		//Select All
		student.select(null, null, null);*/

		/* Expected Output:
		16230943,Lerato,Molefe
		17248830,Isabel,Muller
		16094340,John,Botha
		17012340,Michael,Evans

		No indexes found

		Level 1 [ 17012340]
		Level 2 [ 16094340 16230943]
		Level 2 [ 17012340 17248830]

		Level 1 [ Molefe]
		Level 2 [ Botha Evans]
		Level 2 [ Molefe Muller]

		16094340,John,Botha
		16230943,Lerato,Molefe
		17012340,Michael,Evans
		17248830,Isabel,Muller

		No suitable index found

		16094340,John,Botha
		17012340,Michael,Evans
		16230943,Lerato,Molefe
		17248830,Isabel,Muller

		17248830,Isabel,Muller

		Record(s) not found

		17012340,Michael,Evans

		Record(s) not found

		16094340,John,Botha

		Record(s) not found

		Level 1 [ 16230943]
		Level 2 [ 16094340]
		Level 2 [ 16230943]

		Level 1 [ Molefe]
		Level 2 [ Botha]
		Level 2 [ Molefe]

		16230943,Lerato,Molefe
		16094340,John,Botha

		Level 1 []

		Level 1 []

		No records found
		*/
	}

}