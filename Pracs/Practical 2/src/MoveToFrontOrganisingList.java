/**
 * Name: B Nortier
 * Student Number: 17091820
 */

public class MoveToFrontOrganisingList extends OrganisingList {

    ////// Implement the function below this line //////

    /**
     Find
     Unlink
     Get data i.t.o temp
     Front
     Recalculate
     Return data
     */
    @Override
    public ListNode searchNode(Integer key) {
        if (!contains(key)) {
            return null;
        }
        else {
            //First Node?
            if (root.key == key) {
                return root;
            }

            //Find
            ListNode temp = findNode(key);
            //Store data
            int data = temp.data;
            //Delete
            delete(key);
            //Move to front;
            temp = new ListNode(key);
            temp.next = root;
            root = temp;

            //Recalculate
            calculateData();

            //Return old data
            temp = new ListNode(key);
            temp.data = data;
            return temp;
        }
    }
}