import java.util.List;

/**
 * Name: B Nortier
 * Student Number: 17091820e
 */

public class TransposeOrganisingList extends OrganisingList {

    ////// Implement the function below this line //////

    /**
     Search
    Find
     Swap with predeccesor

     */
    @Override
    public ListNode searchNode(Integer key) {
        int data;
        //Nonexistent
        if (!contains(key)) {
            return null;
        }

        //First Node?
        else if (root.key == key){
            return root;
        }

        //Second Node
        else if (root.next.key == key) {
            //Swap
            ListNode node = root.next;
            root.next = node.next;
            node.next = root;
            root = node;
            //Get data
            data = node.data;
        }
        //Elsewhere
        else {
            ListNode curr = root.next.next;
            ListNode prev = root;
            while (curr.key != key) {     //don't need to check: is definitely in the list
                prev = prev.next;
                curr = curr.next;
            }
            data = curr.data;

            //Swap
            Swap(prev, curr);
        }
        //Recalculate
        calculateData();

        //Return
        ListNode node = new ListNode(key);
        node.data = data;
        return node;
    }
}
