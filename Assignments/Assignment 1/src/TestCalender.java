/*
import java.util.concurrent.ExecutionException;
import java.util.spi.AbstractResourceBundleProvider;

public class TestCalender {
    //Attributes
    public String[] Mois = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
    public Integer[] Jours = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    public Item[] rootMonth = new Item[12];
    public Item[] rootDay = new Item[31];


    public TestCalender() {
        //Set to null
        for (int i = 0; i < 12; i++) {
            rootMonth[i] = null;
        }
        for (int i = 0; i < 31; i++) {
            rootDay[i] = null;
        }

    }

    public void addItem(int day, String month, String description, String duration, int priority) {

        //Check validity: day, month(case), desc, dur, priority
        if (!isValidMonth(month)){
            throw new ArithmeticException("Invalid Month");
        }
        //Create node
        Item newNode = new Item(description,duration,priority,day, month.toLowerCase());

        //Insert in month
        insertAtMonth(newNode);

        //link month to days if necessary as well
        updateDaysAccordingly(newNode);

    }

    public void insertAtMonth(Item newNode) {
        //Find month
        int i;
        for (i = 0; i < Mois.length; i++) {
            if (Mois[i].compareToIgnoreCase(newNode.getMonth())==0)
                break;
        }

        //if empty
        if (rootMonth[i] == null) {
            rootMonth[i] = newNode;
            newNode.down = null;
        }
        //if first & single
        else if (newNode.getDay() < rootMonth[i].getDay()) {
            Item temp = rootMonth[i];
            rootMonth[i] = newNode;
            newNode.down = temp;
        }
        //first and duplicate
        else if (newNode.getDay() == rootMonth[i].getDay()) {
            //insert duplicate at first spot
            Item temp = rootMonth[i].down;
            Item backTemp = rootMonth[i];
            rootMonth[i] = newNode;
            newNode.back = backTemp;
            newNode.down = temp;
            if (newNode.getPriority()==8 && newNode.getDescription()=="y") {
                System.out.println();
            }

        }
        //later down list
        else {
            Item prev = null;
            Item curr = rootMonth[i];

            //find appropriate place
            while (curr!=null && newNode.getDay() > curr.getDay()) {
                prev = curr;
                curr = curr.down;
            }
            //If equal???
            if (curr!=null && (newNode.getDay()==curr.getDay())) {
                //Insert duplicate
                insertDuplicateAtMonth(prev, curr, newNode);
            }
            else {
                prev.down = newNode;
                newNode.down = curr;
            }
        }
    }
    public void insertDuplicateAtMonth(Item prevDay, Item head, Item newNode){
        //Duplicate automatically implies full

        //At head
        if (newNode.getPriority() > head.getPriority()) {
            prevDay.down = newNode;
            newNode.down = head.down;
            newNode.back = head;
            head.down = null;
        }
        //Back
        else {
            //already checked first node
            Item prev = head;
            Item ptr = head.back;
            while (ptr != null && newNode.getPriority() <= ptr.getPriority()) {
                prev = ptr;
                ptr = ptr.back;
            }
            //Don't care about equal priorities
            newNode.back = ptr;
            prev.back = newNode;
        }
    }

    public void updateDaysAccordingly(Item newNode) {
        int day = newNode.getDay();

        //No events at day
        if (rootDay[day-1] == null) {
            rootDay[day-1] = newNode;
            newNode.right = null;
        }

        //Other events on day
        else {
            //Find month-values of newNode and rootMonth
            int newCtr;
            for (newCtr = 0; newCtr < Mois.length; newCtr++) {
                if (Mois[newCtr].compareToIgnoreCase(newNode.getMonth())==0)
                    break;
            }
            int firstCtr;
            for (firstCtr = 0; firstCtr < Mois.length; firstCtr++) {
                if (Mois[firstCtr].compareToIgnoreCase(rootDay[day-1].getMonth())==0)
                    break;
            }
            //System.out.println("newNode month-val: " + newCtr + "\tcurrDay month-val: " + firstCtr);

            //Equal??
            //Don't need to check since already inserted duplicates at month

            //first
            Item temp = rootDay[day-1].right;
            if (newCtr < firstCtr) {
                rootDay[day-1] = newNode;
                newNode.right = temp;
            }
            //middle
            else {
                //find
                Item prev = rootDay[day-1];
                while (temp!=null && newCtr > firstCtr) {
                    for (firstCtr = 0; firstCtr < Mois.length; firstCtr++) {
                        if (Mois[firstCtr].compareToIgnoreCase(temp.getMonth()) == 0)
                            break;
                    }
                    //System.out.println("newNode month-val: " + newCtr + "\tcurrDay month-val: " + firstCtr);
                    prev = temp;
                    temp = temp.right;
                }
                prev.right = newNode;
                newNode.right = temp;

            }

*/
/*
            //first but non-empty
            if (prev==null) {
                rootDay[day-1] = newNode;
                newNode.right = curr;
            }

            //middle
            else {
                newNode.right = curr;
                prev.right = newNode;
            }*//*

        }


    }


    public boolean isValidMonth(String month) {
        for (int i = 0; i < Mois.length; i++) {
            if (Mois[i].compareToIgnoreCase(month)==0)
                return true;
        }
        throw new ArithmeticException("Month invalid");
    }


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
                System.out.print(Jours[i] + " " + rootDay[i].getMonth() + "\n");
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

    public void printDateInfo(Integer day) {
        if (rootDay[day-1] == null) {
            System.out.println("Day " + day + " has no items");
        }
        else {
            Item temp = rootDay[day-1];
            System.out.println("For the day " + day);
            while (temp != null) {
                System.out.println(temp.getMonth() + " P: " + temp.getPriority());
                temp = temp.right;
            }
        }
    }

    public void printDate(int day, String mon) {
        Item head = findDate(day,mon);
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

    public Item findDate(int day, String mon){
        //Find month
        int i;
        for (i = 0; i < Mois.length; i++) {
            if (Mois[i].compareToIgnoreCase(mon)==0)
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

    private void deleteByCases(Item rootDayNode, Item headMonthNode, Item delNode, Item prevD, Item prevM) {
        boolean atRootMonth = headMonthNode == delNode;
        boolean atRootDay = rootDayNode == delNode;
        boolean onlyElAtDay = delNode.back == null;

        //find Mois[i]
        int m;
        for (m = 0; m < Mois.length; m++) {
            if (rootMonth[m]!=null && headMonthNode.getDay() == rootMonth[m].getDay()) {
                break;
            }
        }
        //find Jours[i]
        int j;
        for (j = 0; j < Jours.length; j++) {
            if (rootDay[j]!=null && rootDayNode.getMonth() == rootDay[j].getMonth()) {
                break;
            }
        }

        prevD = getPrevD(delNode, delNode.getDay());
        prevM = getPrevM(delNode, delNode.getDay());

        //Cases for Deletion: rootMonth, rootDay, onlyEle
        if (atRootDay && atRootMonth && onlyElAtDay) {
            //System.out.println("C1. atRootDay && atRootMonth && onlyElAtDay");
            rootDay[j] = delNode.right;
            rootMonth[m] = delNode.down;
        }
        else if (atRootDay && atRootMonth && !onlyElAtDay) {
            //System.out.println("C2. atRootDay && atRootMonth && !onlyElAtDay");
            rootDay[j] = delNode.back;
            rootMonth[m] = delNode.back;
            delNode.back.right = delNode.right;
            delNode.back.down = delNode.down;
        }
        else if (atRootDay && !atRootMonth && onlyElAtDay) {
            //System.out.println("C3. atRootDay && !atRootMonth && onlyElAtDay");
            rootDay[j] = delNode.right;
            prevM.down = delNode.down;
        }
        else if (atRootDay && !atRootMonth && !onlyElAtDay) {
            //System.out.println("C4. atRootDay && !atRootMonth && !onlyElAtDay");
            rootDay[j] = delNode.back;
            prevM.down = delNode.back;
            delNode.back.right = delNode.right;
            delNode.back.down = delNode.down;
        }
        else if (!atRootDay && atRootMonth && onlyElAtDay) {
            //System.out.println("C5. !atRootDay && atRootMonth && onlyElAtDay");
            prevD.right = delNode.right;
            rootMonth[m] = delNode.down;
        }
        else if (!atRootDay && atRootMonth && !onlyElAtDay) {
            //System.out.println("C6. !atRootDay && atRootMonth && !onlyElAtDay");
            prevD.right = delNode.back;
            rootMonth[m] = delNode.back;
            delNode.back.right = delNode.right;
            delNode.back.down = delNode.down;
        }
        else if (!atRootDay && !atRootMonth && onlyElAtDay) {
            //System.out.println("C7. !atRootDay && !atRootMonth && onlyElAtDay");
            prevD.right = delNode.right;
            prevM.down = delNode.down;
        }
        else if (!atRootDay && !atRootMonth && !onlyElAtDay) {
            //System.out.println("C8. atRootDay && !atRootMonth && !onlyElAtDay");
            prevD.right = delNode.right;
            prevM.down = delNode.back;
            delNode.back.right = delNode.right;
            delNode.back.down = delNode.down;
        }
        //System.out.println("Done! Should have deleted " + delNode.getDay() + " " + delNode.getMonth() + " " + delNode.getDescription());

    }

    private Item getPrevM(Item headMonthNode, int day) {
        Item prevM = headMonthNode;
        Item delNode = headMonthNode;
        while (delNode!= null && delNode.getDay()!=day) {
            prevM = delNode;
            delNode = delNode.down;
        }
        return prevM;
    }

    private Item getPrevD(Item delNode, int day) {
        Item prevD = rootDay[day-1];
        Item temp = rootDay[day-1];
        while (temp!=null && temp!=delNode) {
            prevD = temp;
            temp = temp.right;
        }
        return prevD;
    }


    public Item deleteItem(int day, String month, String description) {
        //Find Month
        Item headMonthNode = getMonthItem(month);

        //Traverse Month to correct day
        //Item prevM = getPrevM(headMonthNode, day);
        Item delNode = findDate(day, month);

        //not in List
        if (delNode == null)
            return null;

        //Check Description
        if (delNode.getDescription() != description) {  //System.out.println("First node at day doesn't match description");
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

        // Find previous day
        Item prevM = getPrevM(delNode, day);
        Item prevD = getPrevD(delNode, day);

        //Delete
        deleteByCases(rootDay[day-1], headMonthNode, delNode, prevD, prevM);

        //Return
        return delNode;
    }

    public void deletePriorityItem(int day, String month, int priority) {
        while (true) {
            //Find Month
            Item headMonthNode = getMonthItem(month);

            //Traverse Month to correct day
            Item prevM = getPrevM(headMonthNode, day);
            Item delNode = findDate(day, month);


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
                    //System.out.println("Should have deleted at " + delNode.getDay() + " with P:" + delNode.getPriority() + " Desc: " + delNode.getDescription());
                    continue;  //ie. restart process
                }
            }
            // Node defs first at date

            // Find correct previous day
            Item prevD = getPrevD(delNode, day);
            //delete Node
            deleteByCases(rootDay[day-1], headMonthNode, delNode, prevD, prevM);

        }

    }

    public void deleteItems(int day, String month) {
        Item delNode = findDate(day, month);
        System.out.println("Enter DeleteItems");
        while (delNode != null) {
            printDate(day, month);
            deleteItem(delNode.getDay(), delNode.getMonth(), delNode.getDescription());
            delNode = findDate(day, month);
        }
    }
        */
/*//*
/Just unlink head?
        if (findDate(day, month) == null) {
            return;
        }

        //Find Month
        Item headMonthNode = getMonthItem(month);

        //Traverse Month to correct day
        Item prevM = getPrevM(headMonthNode, day);
        Item delNode = findDate(day, month);

        //not in List
        if (delNode == null) {
            return;
        }
        // Find correct previous day
        Item prevD = getPrevD(delNode, day);

        //Unlinkings
        boolean atRootMonth = (headMonthNode == delNode);
        boolean atRootDay = (rootDay[day-1] == delNode);

        //find Mois[i]
        int m;
        for (m = 0; m < Mois.length; m++) {
            if (rootMonth[m]!=null && headMonthNode.getDay() == rootMonth[m].getDay()) {
                break;
            }
        }

        if (atRootDay && atRootMonth) {
            System.out.println(delNode.getDay() + " " + delNode.getMonth() + "\t C1. atRootDay && atRootMonth && onlyElAtDay");
            rootDay[day-1] = delNode.right;
            rootMonth[m] = delNode.down;
        }
        else if (atRootDay && !atRootMonth) {
            System.out.println(delNode.getDay() + " " + delNode.getMonth() + "\t C3. atRootDay && !atRootMonth && onlyElAtDay");
            rootDay[day-1] = delNode.right;
            prevM.down = delNode.down;
        }
        else if (!atRootDay && atRootMonth) {
            System.out.println(delNode.getDay() + " " + delNode.getMonth() + "\t C5. !atRootDay && atRootMonth && onlyElAtDay");
            prevD.right = delNode.right;
            rootMonth[m] = delNode.down;
        }
        else if (!atRootDay && !atRootMonth) {
            System.out.println(delNode.getDay() + " " + delNode.getMonth() + "\t C7. !atRootDay && !atRootMonth && onlyElAtDay");
            prevD.right = delNode.right;
            prevM.down = delNode.down;
        }
    }*//*



    public void clearMonth(String month) {
        Item headMonthNode = getMonthItem(month);
        //Empty Month
        if (headMonthNode == null) {
            return;
        }
        else {
            //while still elements in rootMonth
            while (headMonthNode != null) {
                //System.out.println("Head: " + headMonthNode.getDay() +" "+ headMonthNode.getMonth());
                //printMonthInfo(month);
                printMonthInfo("dec");
                deleteItems(headMonthNode.getDay(), headMonthNode.getMonth());
                printMonthInfo("dec");
                headMonthNode = getMonthItem(month);
            }
            //System.out.println("should be empty:");
            //printMonthInfo(month);
        }

    }

    public void clearDay(int day) {
		*/
/*All items for the given day should be deleted.
		If the day has no Items, simply do nothing.*//*

		Item headDayNode = getDayItem(day);
		//Empty
        if (headDayNode == null) {
            return;
        }
        else {
            //while still elements in day
            while (headDayNode != null) {
                deleteItems(headDayNode.getDay(), headDayNode.getMonth());
                headDayNode = getDayItem(day);
            }
        }
    }

    public void clearYear() {
        */
/*Delete all Items from the calendar.*//*

        for (int i = 0; i < 12; i++) {
            rootMonth[i] = null;
        }
        for (int i = 0; i < 31; i++) {
            rootDay[i] = null;
        }

    }


    public Item getMonthItem(String month)
    {
        //Valid?
        if (!isValidMonth(month))
            return null;
        //find Month
        int i;
        for (i = 0; i < Mois.length; i++) {
            if (Mois[i].compareToIgnoreCase(month) == 0) {
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

        Item headMonthNode = getMonthItem(month);

        //traverse Month to day
        Item temp = headMonthNode;
        while (temp!=null && temp.getDay()!=day) {
            temp = temp.down;
        }
        if (temp==null)
            return null;
        else
            return temp;
    }


    public Item getDayItem(int day)
    {
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
}

*/
