/**
 * Berne' Nortier
 * 17091820
 **/

import java.util.Random;

public class ToDo {
	public static void main(String[] args) {
		//Test Item
		System.out.println("\n\nTESTING ITEM IMPLEMENTATION\n");
		Item it = new Item();
		it.printItemInfo();
		it.setDescription("What a laugh");
		it.setDuration("6:30");
		it.setPriority(5);
		it.setMonth("mar");
		it.back = new Item();
		it.right = new Item();
		it.down = it.back;
		it.printItemInfo();



		//Test Calender Constructor
		Calendar cal = new Calendar();


		//Test insertAtMonth
		Random rd = new Random();
		rd.setSeed(123456);
		String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		String[] Mois = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
		Integer[] Jours = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
		for (int i = 0; i < alphabet.length*2; i++) {
			String desc = alphabet[i%26];
			String month = Mois[Math.abs(rd.nextInt() % 12)];
			Integer day = Jours[Math.abs(rd.nextInt() % 31)];
			String dur = "00:00";
			Integer prior = Math.abs(rd.nextInt()%10);
			//System.out.println(i + ": " + day +" "+month + "\t:P:" + prior + "\tD:" + desc);
			cal.addItem(day, month, desc, dur, prior);
		}
		//Addding duplicates
		cal.addItem(13,"jUn","x","00:00",5);
		cal.addItem(13,"jUn","a","00:00",1);
		cal.addItem(13,"jUn","c","00:00",2);
		cal.addItem(13,"jUn","e","00:00",7);
		cal.addItem(13,"jUn","i","00:00",4);
		cal.addItem(13,"jUn","n","00:00",3);
		cal.addItem(13,"jUn","r","00:00",99);
		cal.addItem(13,"jUn","i","00:00",5);
		cal.addItem(13,"jUn","o","00:00",5);


		//TEST GETPREVDAY
		Item delNode = cal.getItem(3,"May");
		delNode.printItemInfo();
		cal.getPrevD(delNode, delNode.getMonth(),delNode.getDay()).printItemInfo();
		cal.getPrevM(delNode.getMonth(), delNode.getDay()).printItemInfo();

		System.out.println(" ");
		delNode = cal.getItem(8,"JuL");
		delNode.printItemInfo();
		cal.getPrevD(delNode, delNode.getMonth(),delNode.getDay()).printItemInfo();
		cal.getPrevM(delNode.getMonth(), delNode.getDay()).printItemInfo();

		System.out.println(" ");
		delNode = cal.getItem(11,"Nov");
		delNode.printItemInfo();
		cal.getPrevD(delNode, delNode.getMonth(),delNode.getDay()).printItemInfo();
		cal.getPrevM(delNode.getMonth(), delNode.getDay()).printItemInfo();

		System.out.println(" ");
		delNode = cal.getItem(7,"JAN");
		delNode.printItemInfo();
		cal.getPrevD(delNode, delNode.getMonth(),delNode.getDay()).printItemInfo();
		cal.getPrevM(delNode.getMonth(), delNode.getDay()).printItemInfo();

		System.out.println(" ");
		delNode = cal.getItem(20,"MAR");
		delNode.printItemInfo();
		cal.getPrevD(delNode, delNode.getMonth(),delNode.getDay()).printItemInfo();
		cal.getPrevM(delNode.getMonth(), delNode.getDay()).printItemInfo();



		//Test Delete Item
		System.out.println("TESTING DELETE");

		//C1 : atRootDay && atRootMonth && onlyElAtDay
		cal.printDate(4, "Jan");
		cal.deleteItem(4,"JAN", "d");
		cal.printDate(4,"Jan");
		//cal.printRootMonths();
		System.out.println("");
		cal.printDate(1,"Mar");
		cal.deleteItem(1,"Mar","g");
		cal.printDate(1,"Mar");

		//C2 atRootDay && atRootMonth && !onlyElAtDay
		//Why C6?
		System.out.println("");
		cal.addItem(7,"jan","0","00:00",4);
		cal.printDate(7,"Jan");
		cal.deleteItem(7,"Jan", "0");
		cal.printDate(7,"Jan");

		//C3. atRootDay && !atRootMonth && onlyElAtDay
		System.out.println("");
		cal.printDate(11,"mar");
		cal.deleteItem(11,"Mar","u");
		cal.printDate(11,"mar");


		System.out.println("");
		cal.printDate(1,"Apr");
		cal.deleteItem(1, "APR","x");
		cal.printDate(1,"aPr");
		//cal.printRootMonths();

		System.out.println("");
		cal.printDate(7,"Mar");
		cal.deleteItem(7, "mar","s");
		cal.printDate(7,"Mar");
		cal.printMonthInfo("Mar");

		//TESTING DELETEITEMS
		cal.printDate(13,"jUn");
		cal.deleteItems(13, "JUN");
		cal.printDate(13,"jun");
		cal.printRootMonths();

		cal.printDate(19,"maY");
		cal.deleteItems(19,"may");



		//Testing clearMonth
		System.out.println("TESTING CLEARMONTH");
		cal.printMonthInfo("feb");
		cal.clearMonth("feb");
		cal.printMonthInfo("feb");

		cal.printMonthInfo("sep");
		cal.clearMonth("sep");
		cal.printMonthInfo("sep");

		cal.printMonthInfo("dec");
		cal.clearMonth("dec");
		cal.printMonthInfo("dec");




		//Testing clearDay
		System.out.println("TESTING CLEARDAY");
		cal.printDayInfo(2);
		cal.clearDay(2);
		cal.printDayInfo(2);

		cal.printDayInfo(5);
		cal.clearDay(5);
		cal.printDayInfo(5);


		//Existing Items
		it = cal.getItem(3,"JUL");
		System.out.println(it.getDay() +" " + it.getMonth() + " == 3 6");
		it = cal.getItem(5,"Nov");
		System.out.println(it.getDay() +" " + it.getMonth() + " == 5 10");
		//Empty month
		it = cal.getItem(27,"Dec");
		System.out.println(it);
		//Non-existent date
		it = cal.getItem(18,"feb");
		System.out.println(it);


		//Test getMonth


//Wrong months
		Item m = cal.getMonthItem(" Sep");
		System.out.println(m);
		m = cal.getMonthItem("december");
		System.out.println(m);
		//Empty month
		m = cal.getMonthItem("Mar");
		System.out.println(m);
		//Existing Months
		m = cal.getMonthItem("Mar");
		System.out.println(m.getDay() + " " + m.getMonth());
		m = cal.getMonthItem("Oct");
		System.out.println(m.getDay() + " " + m.getMonth());




	}

}
