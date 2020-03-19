/**
 * Berne' Nortier
 * 17091820
 **/

import java.util.concurrent.ExecutionException;
import java.util.spi.AbstractResourceBundleProvider;

public class Calendar {
	//Attributes
	public String[] Mois = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
	public Integer[] Jours = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
	public Item[] rootMonth = new Item[12];
	public Item[] rootDay = new Item[31];


	public Calendar() {
		//Set to null
		for (int i = 0; i < 12; i++) {
			rootMonth[i] = null;
		}
		for (int i = 0; i < 31; i++) {
			rootDay[i] = null;
		}

	}

	//Insertion
	public void addItem(int day, String month, String description, String duration, int priority) {
		//Create node
		Item newNode = new Item(description,duration,priority,day, month.toLowerCase());


		//Insert in month
		insertAtMonth(newNode);
		//link month to days if necessary as well
		updateDaysAccordingly(newNode);
	}

	private void insertAtMonth(Item newNode) {
		int m = newNode.getMonth();
		//if Month is empty
		if (rootMonth[m] == null) {
			rootMonth[m] = newNode;
			rootMonth[m].down = null;
		}

		//if before Root & single date
		else if (newNode.getDay() < rootMonth[m].getDay()) {
			Item temp = rootMonth[m];
			rootMonth[m] = newNode;
			newNode.down = temp;
		}

		//if at root (ie. duplicate)
		else if (newNode.getDay() == rootMonth[m].getDay()) {
			//highest priority
			if (newNode.getPriority() >= rootMonth[m].getPriority()) {
				newNode.down = rootMonth[m].down;
				newNode.back = rootMonth[m];
				rootMonth[m].down = null;
				rootMonth[m] = newNode;

				/*Item temp = rootMonth[m].down;
				Item backTemp = rootMonth[m];
				rootMonth[m] = newNode;
				newNode.back = backTemp;
				newNode.down = temp;*/
			}
			else {
				//lower priority
				Item prev = rootMonth[m];
				Item curr = rootMonth[m];
				while (curr!=null && curr.getPriority() > newNode.getPriority()) {
					prev = curr;
					curr = curr.back;
				}
				prev.back = newNode;
				newNode.back = curr;
			}

		}
		//later down list
		else {
			Item prev = null;
			Item curr = rootMonth[m];
			//find appropriate place
			while (curr!=null && newNode.getDay() > curr.getDay()) {
				prev = curr;
				curr = curr.down;
			}

			//Equal day: Insert a duplicate
			if (curr!=null && (newNode.getDay()==curr.getDay())) {
				//first priority
				if (newNode.getPriority() >= curr.getPriority()) {
					newNode.down = curr.down;
					newNode.back = curr;
					curr.down = null;
					prev.down = newNode;
				}
				//lower priority
				else {
					prev = curr;
					while (curr != null && newNode.getPriority() < curr.getPriority()) {
						prev = curr;
						curr = curr.back;
					}
					prev.back = newNode;
					newNode.back = curr;
				}
			}
			//at end
			else {
				prev.down = newNode;
				newNode.down = curr;
			}
		}
	}

	private void updateDaysAccordingly(Item newNode) {
		Integer day = newNode.getDay();
		Integer month = newNode.getMonth();
		//If empty
		if (rootDay[day-1]==null) {
			this.rootDay[day-1] = newNode;
		}

		//At root: duplicate
		else if (rootDay[day-1].getMonth().equals(month)) {
			if (rootDay[day-1].getPriority() <= newNode.getPriority()) {
				newNode.back = rootDay[day-1].back;
				newNode.right = rootDay[day-1].right;
				rootDay[day-1].right = null;  //?
				rootDay[day-1].down = null;
				rootDay[day-1] = newNode;
			}
			else {
				//do nothing. already linked at month since at back
			}
		}
		//before root
		else if (month < rootDay[day-1].getMonth()) {
			//Item prevD = getPrevD(newNode,month,day);
			Item temp = rootDay[day-1];
			rootDay[day-1] = newNode;
			newNode.right = temp;
		}

		//not at root
		else {
			Item prevD = getPrevD(newNode,month,day);
			//if prevD is last day in list
			if (prevD.right == null) {
				prevD.right = newNode;
				newNode.right = null;
			}
			//duplicate month
			else if (prevD.right.getMonth().equals(month)) {
				if (prevD.right.getPriority() <= newNode.getPriority()) {
					Item curr = prevD.right;
					newNode.back = curr;
					newNode.right = curr.right;
					newNode.down = curr.down;
					curr.right = null;
					curr.down = null;
					prevD.right = newNode;
				}
				else {
					// at back. do nothing
				}
			}
			//between months
			else {
				newNode.right = prevD.right;
				prevD.right = newNode;
			}
		}

	}

	/*private void updateDaysAccordingly(Item newNode) {
		int day = newNode.getDay();

		//No events at day
		if (rootDay[day-1] == null) {
			rootDay[day-1] = newNode;
			newNode.right = null;
		}
		//Other events on day
		else {
			int n = newNode.getMonth();
			int r = rootDay[day-1].getMonth();

			//first Month: insert as new Root
			if (n < r) {
				newNode.right =rootDay[day-1].right;
				rootDay[day-1] = newNode;
			}
			//first Month & equal to root
			else if (n==r) {
				if (newNode.getPriority() >= rootDay[day-1].getPriority()) {
					newNode.right = rootDay[day-1].right;
					rootDay[day-1].right = newNode;
				}
				else {
					//already inserted duplicates at month, so day linked correctly
				}
			}
			//later in day
			else {
				//find
				Item prev = rootDay[day-1];
				Item temp = rootDay[day-1];
				while (temp!=null && temp.getMonth()>newNode.getMonth()) {
					prev = temp;
					temp = temp.right;
				}
				//duplicate day + higher priority
				if (temp.getMonth() == newNode.getMonth() && temp.getPriority() <= newNode.getPriority()) {
					prev.right = newNode;
					newNode.right = temp;
				}
				//duplicate day + lower priority
				else if (temp.getMonth() == newNode.getMonth() && temp.getPriority() > newNode.getPriority()) {
					//do nothing, months already linked correctly
				}
				//between months
				else if (temp.getMonth() < newNode.getMonth()) {

					prev.right = newNode;
					newNode.right = temp.right;
				}
			}
		}


	}*/

	//Prints
	public void printRootMonths() {
		System.out.println("\nFirst values for each month:");
		for (int i = 0; i < rootMonth.length; i++) {
			if (rootMonth[i] != null) {
				System.out.print(Mois[i] + " " + rootMonth[i].getDay() + " P:" + rootMonth[i].getPriority()+ "\n");
			}
			else {
				System.out.print(Mois[i] + " " + rootMonth[i] + "\n");
			}
		}
		System.out.println(" ");
	}
	public void printRootDays() {
		System.out.println(" ");
		for (int i = 0; i < rootDay.length; i++) {
			if (rootDay[i] != null) {
				System.out.print(Jours[i] + " " + Mois[rootDay[i].getMonth()] + "\n");
			}
			else {
				System.out.print(Jours[i] + " " + rootDay[i] + "\n");
			}
		}
	}
	public void printMonthInfo(String month) {
		//Find month
		int i;
		for (i = 0; i < Mois.length; i++) {
			if (Mois[i].compareToIgnoreCase(month)==0)
				break;
		}
		if (rootMonth[i] == null) {
			System.out.println("Month " + month + " has no items");
		}
		else{
			Item temp = rootMonth[i];
			System.out.println("For the month of " + month.toLowerCase());
			while (temp!= null){
				System.out.println(temp.getDay() + " P:" + temp.getPriority());
				temp = temp.down;
			}
		}
	}
	public void printDayInfo(Integer day) {
		if (rootDay[day-1] == null) {
			System.out.println("Day " + day + " has no items");
		}
		else {
			Item temp = rootDay[day-1];
			System.out.println("For the day " + day);
			while (temp != null) {
				System.out.println(Mois[temp.getMonth()] + " P: " + temp.getPriority());
				temp = temp.right;
			}
		}
	}
	public void printDate(int day, String mon) {
		Item head = getItem(day,mon);
		if (head==null) {
			System.out.println(mon.toUpperCase() + " " + day + " does not exist");
		}
		else {
			System.out.println(mon.toUpperCase() + " " + day + ":");
			while (head!=null) {
				System.out.println("\tPriority: " + head.getPriority() + "\tDesc: " + head.getDescription());
				head = head.back;
			}
		}
	}

	//Getters
	public Item getPrevM(Integer month, int day) {
		Item prevM = rootMonth[month];
		Item delNode = rootMonth[month];
		while (delNode!= null && delNode.getDay()!=day) {
			prevM = delNode;
			delNode = delNode.down;
		}
		return prevM;
	}

	public Item getPrevD(Item delNode, Integer month, Integer day) {
		Item prevD = this.rootDay[day-1];
		Item ptr = this.rootDay[day-1];

		while (ptr!=null && ptr.getMonth()<(month)) {
			prevD = ptr;
			ptr = ptr.right;
		}
		return prevD;
	}

	public Item getMonthItem(String month) {
		//find Month
		int i;
		for (i = 0; i < Mois.length; i++) {
			if (month.compareToIgnoreCase(Mois[i])==0) {
				if (rootMonth[i] == null)
					return null;
				else
					return rootMonth[i];
			}
		}
		//didn't find month
		return null;
	}

	public Item getItem(int day, String month) {
		//Find month
		int i;
		for (i = 0; i < Mois.length; i++) {
			if (Mois[i].compareToIgnoreCase(month)==0)
				break;
		}
		//Traverse Month
		Item ptr = rootMonth[i];
		while (ptr!=null && ptr.getDay()!=day) {
			ptr = ptr.down;
		}
		//not in List
		if (ptr == null) {
			return null;
		}
		//ptr now pointing at day
		else {
			return ptr;
		}
	}
	public Item getDayItem(int day) {
		int i;
		for (i=0; i<Jours.length; i++) {
			if (Jours[i].compareTo(day) == 0) {
				if (rootDay[i] == null)
					return null;
				else
					return rootMonth[i];
			}
		}
		return null;
	}

	//Deletion
	public Item deleteItem(int day, String month, String description) {
		Item n = new Item();
		Integer m = n.isValidMonth(month);

		//Find Month
		Item headMonthNode = getMonthItem(Mois[m]);
		//Find Day
		Item delNode = getItem(day, Mois[m]);
		//not in List
		if (delNode == null)
			return null;

		//Check Description
		if (delNode.getDescription().compareTo(description)!=0) {
			Item temp = delNode;
			Item prevDescr = null;
			//Traverse until find & delete
			while (temp != null && temp.getDescription() != description) {
				prevDescr = temp;
				temp = temp.back;
			}
			// Not in list
			if (temp == null) {
				return null;
			}
			//In list but at back. Unlink
			else if (prevDescr!=delNode)
			{
				prevDescr.back = temp.back;
				return delNode;
			}
		} //delNode at head of all elements at date
		// Find previous Nodes
		Item prevM = getPrevM(m, day);
		Item prevD = getPrevD(delNode, m, day);
		//Delete
		deleteByCases(rootDay[day-1], headMonthNode, delNode, prevD, prevM);
		//Return
		return delNode;
	}
	private void deleteByCases(Item rootDayNode, Item headMonthNode, Item delNode, Item prevD, Item prevM) {
		Integer m = delNode.getMonth();
		Integer d = delNode.getDay();

		boolean atRootMonth = (rootMonth[m] == delNode);
		boolean atRootDay = (rootDay[d-1] == delNode);
		boolean onlyElAtDay = (delNode.back == null);

		prevD = getPrevD(delNode, delNode.getMonth(), delNode.getDay());
		prevM = getPrevM(delNode.getMonth(), delNode.getDay());

		//Cases for Deletion: rootMonth, rootDay, onlyEle
		if (atRootDay && atRootMonth && onlyElAtDay) {
			//C1. atRootDay && atRootMonth && onlyElAtDay
			rootDay[d-1] = delNode.right;
			rootMonth[m] = delNode.down;
		}
		else if (atRootDay && atRootMonth && !onlyElAtDay) {
			//C2. atRootDay && atRootMonth && !onlyElAtDay
			Item newH = delNode.back;
			rootDay[d-1] = newH;
			rootMonth[m] = newH;
			newH.right = delNode.right;
			newH.down = delNode.down;
		}
		else if (atRootDay && !atRootMonth && onlyElAtDay) {
			//C3. atRootDay && !atRootMonth && onlyElAtDay
			rootDay[d-1] = delNode.right;
			prevM.down = delNode.down;
		}
		else if (atRootDay && !atRootMonth && !onlyElAtDay) {
			//C4. atRootDay && !atRootMonth && !onlyElAtDay
			Item newH = delNode.back;
			rootDay[d-1] = newH;
			prevM.down = newH;
			newH.right = delNode.right;
			newH.down = delNode.down;
		}
		else if (!atRootDay && atRootMonth && onlyElAtDay) {
			//C5. !atRootDay && atRootMonth && onlyElAtDay
			prevD.right = delNode.right;
			rootMonth[m] = delNode.down;
		}
		else if (!atRootDay && atRootMonth && !onlyElAtDay) {
			//C6. !atRootDay && atRootMonth && !onlyElAtDay
			Item newH = delNode.back;
			prevD.right = newH;
			rootMonth[m] = newH;
			newH.right = delNode.right;
			newH.down = delNode.down;
		}
		else if (!atRootDay && !atRootMonth && onlyElAtDay) {
			//C7. !atRootDay && !atRootMonth && onlyElAtDay
			prevD.right = delNode.right;
			prevM.down = delNode.down;
		}
		else if (!atRootDay && !atRootMonth && !onlyElAtDay) {
			//C8. atRootDay && !atRootMonth && !onlyElAtDay
			Item newH = delNode.back;
			prevD.right = newH;
			prevM.down = newH;
			newH.right = delNode.right;
			newH.down = delNode.down;
		}

	}
	public void deletePriorityItem(int day, String month, int priority) {
		while (true) {
			//Find Month
			Item headMonthNode = getMonthItem(month);
			Item n = new Item();
			Integer m = n.isValidMonth(month);

			//Traverse Month to correct day
			Item prevM = getPrevM(m, day);
			Item delNode = getItem(day, month);


			if (delNode == null) {
				return;
			}

			//Find Priority
			if (delNode.getPriority() != priority) {
				Item temp = delNode;
				Item prevPrior = null;
				while (temp != null && temp.getPriority() != priority) {
					prevPrior = temp;
					temp = temp.back;
				}
				// Not in list
				if (temp == null) {
					return;
				}
				//In list but at back. Unlink
				else if (prevPrior != delNode) {
					prevPrior.back = temp.back;
					continue;  //ie. restart process
				}
			}
			// Node defs first at date

			// Find correct previous day
			Item prevD = getPrevD(delNode, m, day);
			//delete Node
			deleteByCases(rootDay[day-1], headMonthNode, delNode, prevD, prevM);

		}

	}
	public void deleteItems(int day, String month) {
		Item delNode = getItem(day, month);
		while (delNode != null) {
			deleteItem(delNode.getDay(), Mois[delNode.getMonth()], delNode.getDescription());
			delNode = getItem(day, month);
		}
	}

	//Clearing
	public void clearMonth(String month) {
		Item headMonthNode = getMonthItem(month);
		//Empty Month
		if (headMonthNode == null) {
			return;
		}
		else {
			//while still elements in rootMonth
			while (headMonthNode != null) {
				deleteItems(headMonthNode.getDay(), Mois[headMonthNode.getMonth()]);
				headMonthNode = getMonthItem(month);
			}
		}

	}
	public void clearDay(int day) {
		Item headDayNode = getDayItem(day);
		//Empty
		if (headDayNode == null) {
			return;
		}
		else {
			//while still elements in day
			while (headDayNode != null) {
				deleteItems(headDayNode.getDay(), Mois[headDayNode.getMonth()]);
				headDayNode = getDayItem(day);
			}
		}
	}
	public void clearYear() {
		for (int i = 0; i < 12; i++) {
			rootMonth[i] = null;
		}
		for (int i = 0; i < 31; i++) {
			rootDay[i] = null;
		}

	}


}

