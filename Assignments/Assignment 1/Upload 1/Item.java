/**
* Berne' Nortier
* 17091820
 **/

public class Item
{
	//ATTRIBUTES
	private String[] Mois = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
		//links
	public Item back = null; // The next Item (back) of this to-do list on the same date
	public Item right = null; // The next Item (right) of this Item on the same day (1st to 31st).
	public Item down = null; // The next Item (down) of this Item in the same month.
		//others
	private String description;
	private String duration; // The number of minutes/hours that the Item will take.
	private Integer priority;
	private Integer day;
	private Integer month;
	public Boolean isFirst;


	public Item(String desc, String dur, Integer p, Integer d, String m) {
		setDescription(desc);
		setDuration(dur);
		setPriority(p);
		setDay(d);
		setMonth(m);
		back = null;
		right = null;
		down = null;
		isFirst = true;
	}

	public Item() 	{
		this("","",0,0,"");
		back = null;
		right = null;
		down = null;
	}


	public void setDescription(String desc)
	{
		description = desc;
	}	
	
	public void setDuration(String dur) {
		//Length
		if (dur.length() != 5) {
			duration = "00:00";
			return;
		}
		//Semicolon
		Character ch = dur.charAt(2);
		int hour = Integer.parseInt(dur.substring(0,2));
		int min = Integer.parseInt(dur.substring(3));
		if (ch != ':') {
			duration = "00:00";
			return;
		}
		if (hour < 0 || hour > 24) {
			duration = "00:00";
			return;
		}
		if (min < 0 || min > 59) {
			duration = "00:00";
			return;
		}
		duration = dur;
	}	
	
	public void setPriority(int p) {
		if (p<0) {
			priority = 0;
		}
		priority = p;
	}

	public void setDay(int d)
	{
		day = d;
	}

	public void setMonth(String m)
	{
		month = isValidMonth(m);
	}

	//Validity
	public Integer isValidMonth(String month) {
		for (int i = 0; i < Mois.length; i++) {
			if (Mois[i].compareToIgnoreCase(month)==0)
				return i;
		}
		return -1;
	}

	public String getDescription() {
		return description;
	}	
	
	public String getDuration() {
		return duration;
	}
	
	public int getPriority() {
		return priority;
	}

	public int getDay() {return day;}

	public Integer getMonth() {return month;}

	public void printItemInfo() {
		//System.out.println("ITEM INFO");
		if (getMonth()!=-1) {
			System.out.println(getDay() + " " + Mois[getMonth()] + " P:" + priority + " D: "+description);
			System.out.println("Back:" + back);
			System.out.println("Down:" + down);
			System.out.println("Right:" + right);
		}
		else
			System.out.println("Invalid month");
	}

}
