import java.util.Random;
		import java.io.*;
public class myMain
{
	static String[] months = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
	static String ipsum = "Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua Ut enim ad minim veniam";
	static String[] descriptions;
	static Calendar myCal;

	public static void printSchedule(Calendar cal){
		int itemCount = 0;
		for(String s: months){
			System.out.println(s);
			for(int i = 0; i < 31; i++){
				Item head = cal.getItem(i+1, s);
				System.out.println("\tDay " + (i+1) + " Items: ");
				if(head != null){
					while(head != null){
						itemCount++;
						System.out.print(head.getMonth());
						System.out.print("\t\t" + head.getDescription() + ", ");
						System.out.print(head.getDuration() + ", ");
						System.out.print(head.getPriority());
						System.out.println();
						head = head.back;
					}
				}
			}
		}
		System.out.println("\nTotal Items: " + itemCount);
	}

	private static String makeRandomTime(Random rd){
		StringBuilder sb = new StringBuilder();
		int hours =rd.nextInt(24)+1;
		if(hours<10){
			sb.append(0);
		}
		sb.append(hours);
		sb.append(":");
		int mins = rd.nextInt(59)+1;
		if(mins < 10){
			sb.append(mins);
			sb.append(0);
		}else
			sb.append(mins);
		return sb.toString();
	}


	private static boolean deleteRandomItem(Random rd){
		int day = rd.nextInt(31)+1;
		String month = months[rd.nextInt(12)];
		Item target = myCal.getItem(day,month);
		if(target == null) return false;
		String desc = descriptions[rd.nextInt(descriptions.length)];
		Item res = myCal.deleteItem(day,month, desc);
		return res != null;
	}

	private static void deleteRandomPriorityItem(Random rd){
		int day = rd.nextInt(31)+1;
		String month = months[rd.nextInt(12)];

		myCal.deletePriorityItem(day,month, rd.nextInt(10)+1);
	}

	private static void deleteRandomDayItem(Random rd){
		int day = rd.nextInt(31)+1;
		String month = months[rd.nextInt(12)];
		myCal.deleteItems(day,month);
	}

	private static void insertRandomItem(Random rd){
		myCal.addItem(rd.nextInt(31)+1,months[rd.nextInt(12)],descriptions[rd.nextInt(descriptions.length)],makeRandomTime(rd),rd.nextInt(10)+1);
	}

	public static void main(String[] args)
	{
		int actionCount = 60;
		ipsum.trim();
		descriptions = ipsum.split(" ");
		myCal = new Calendar();
		Random rd = new Random();
		rd.setSeed(System.currentTimeMillis());
		long startTime = System.currentTimeMillis();

		//Inserting randomly

		for(int i = 0; i < actionCount; i++){
			myCal.addItem(rd.nextInt(31)+1,months[rd.nextInt(12)],descriptions[rd.nextInt(descriptions.length)],makeRandomTime(rd),rd.nextInt(10)+1);
		}
		long endTime = System.currentTimeMillis() - startTime;


		System.out.println("Time to insert: " + endTime + "ms");

		int deleteCounter = 0;
		startTime = System.currentTimeMillis();
		//Randomly delete itesm until all inserted items have been deleted
		while(deleteCounter < actionCount-1){
			if (deleteCounter==56) {
				System.out.println(deleteCounter);
				myCal.printRootMonths();
			}
			if(deleteRandomItem(rd))deleteCounter++;

		}
		endTime = System.currentTimeMillis() - startTime;
		System.out.println("Time to Delete: " + endTime + "ms");
		myCal.printRootMonths();
		//printSchedule(myCal);


		//Randomly call insert and different delete functions
		int i = 0;
		while(i < actionCount){
			int action = rd.nextInt(12);
			if(action < 6){
				insertRandomItem(rd);
			}else if(action < 8){
				deleteRandomItem(rd);
			}else if(action < 10){
				deleteRandomPriorityItem(rd);
			}else {
				deleteRandomDayItem(rd);
			}
			i++;
		}
		printSchedule(myCal);
	}
}


