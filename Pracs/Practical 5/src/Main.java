import java.util.Random;

public class Main {

    public static void insertAll(Heap<Integer> heap, Integer[] list) 
    {
        for(int i = 0; i < list.length; i++) {
            heap.insert(list[i]);
        }
    }

    public static void printHeap(Heap<Integer> heap) {
        if (heap.isEmpty())
        {
                System.out.println("Heap is empty");
            }
        else {
            if (heap instanceof MinHeap)
                System.out.println("Min-Heap Content:");
            else
            System.out.println("Max-Heap Content:");
                heap.display();
        }
    }

    public static boolean testMinHeapValidity(MinHeap<Integer> h) {
        for (int i = 1; i < Math.floorDiv(h.currentSize,2) ; i++) {
            if (h.getElementAt(i) > h.getElementAt(2 * i) || h.getElementAt(i) > h.getElementAt(2*i + 1)) {
                System.out.println(h.getElementAt(i) + " vs. " + h.getElementAt(2 * i) + " vs. " + h.getElementAt(2 * i + 1));
                return false;
            }
        }
        return true;
    }

    public static boolean testMaxHeapValidity(MaxHeap<Integer> h) {
        for (int i = 1 ; i < Math.floorDiv(h.getCurrentSize(),2); i++) {
            if (h.getElementAt(i) < h.getElementAt(2*i) || h.getElementAt(i) < h.getElementAt(2*i+1)) {
                System.out.println(h.getElementAt(i) + " vs. " + h.getElementAt(2 * i) + " vs. " + h.getElementAt(2 * i + 1));
                return false;
            }
        }
        return true;
    }

    public static Integer[] genList(int len) {
        Random rd = new Random();
        rd.setSeed(158756);
        if (len==0) {
            len = Math.abs(rd.nextInt()) % 50;
        }
        Integer[] list = new Integer[len];
        for (int i = 0; i < len; i++) {
            list[i] = Math.abs(rd.nextInt())%100;
        }
        return list;
    }

    public static void testMINheap(int numChecks) {
        System.out.println("============MIN HEAP=============");
        Integer[] list;
        MinHeap<Integer> min;
        boolean validChecks = true;
        for (int i = 0; i < numChecks; i++) {
            list = genList(0);
            min = new MinHeap<Integer>(list.length);
            insertAll(min, list);
            validChecks = testMinHeapValidity(min);
            if (!validChecks) {
                break;
            }
        }
        System.out.println("For insertion:" + numChecks + " checks are " + validChecks);


        for (int i = 0; i < numChecks; i++) {
            list = genList(0);
            min = new MinHeap<Integer>(list.length);
            insertAll(min, list);
            validChecks = true;

            int len = list.length;
            for (int j = 0; j < len; j++) {
                int val = min.removeMin();
                validChecks = testMinHeapValidity(min);
                if (!validChecks) {
                    System.out.println("At " + i + " At " + j);
                    break;
                }
            }
            if (!validChecks) {
                break;
            }
        }
        System.out.println("For dequeue: " + numChecks + " checks are " + validChecks);


        Random rd = new Random();
        for (int i = 0; i < numChecks; i++) {
            list = genList(0);
            min = new MinHeap<Integer>(list.length);
            insertAll(min, list);
            validChecks = true;
            min.delete(Math.abs(rd.nextInt())%100);
            validChecks = testMinHeapValidity(min);
            if (!validChecks) {
                break;
            }
        }
        System.out.println("For deletion: " + numChecks + " checks are " + validChecks);

    }

    public static void testMAXheap(int numChecks) {
        System.out.println("============MAX HEAP=============");
        Integer[] list;
        MaxHeap<Integer> max;
        boolean validChecks = true;
        for (int i = 0; i < numChecks; i++) {
            list = genList(0);
            max = new MaxHeap<>(list.length);
            insertAll(max, list);
            validChecks = testMaxHeapValidity(max);
            if (!validChecks) {
                break;
            }
        }
        System.out.println("For insertion:" + numChecks + " checks are " + validChecks);


        for (int i = 0; i < numChecks; i++) {
            list = genList(0);
            max = new MaxHeap<Integer>(list.length);
            insertAll(max, list);
            validChecks = true;

            int len = list.length;
            for (int j = 0; j < len; j++) {
                int val = max.removeMax();
                validChecks = testMaxHeapValidity(max);
                if (!validChecks) {
                    System.out.println("At " + i + " At " + j);
                    break;
                }
            }
            if (!validChecks) {
                break;
            }
        }
        System.out.println("For dequeue: " + numChecks + " checks are " + validChecks);

        Random rd = new Random();
        for (int i = 0; i < numChecks; i++) {
            list = genList(0);
            max = new MaxHeap<Integer>(list.length);
            insertAll(max, list);
            validChecks = true;
            max.delete(Math.abs(rd.nextInt())%100);
            validChecks = testMaxHeapValidity(max);
            if (!validChecks) {
                break;
            }
        }
        System.out.println("For deletion: " + numChecks + " checks are " + validChecks);
    }

    public static void main(String args[])
    {
        testMINheap(10000);
        testMAXheap(10000);

/*        Integer[] list = new Integer[]{2, 8, 6, 1, 10, 15, 3, 12, 11};
        MinHeap<Integer> min = new MinHeap<Integer>(list.length);
        insertAll(min, list);
        printHeap(min);
        min.delete(15);
        printHeap(min);
        min.delete(3);
        printHeap(min);
        System.out.println("Min : " + min.removeMin());
        printHeap(min);
        System.out.println("Min : " + min.removeMin());
        printHeap(min);
        MaxHeap<Integer> max = new MaxHeap<Integer>(list.length);
        insertAll(max, list);
        printHeap(max);
        max.delete(11);
        printHeap(max);
        max.delete(10);
        printHeap(max);
        System.out.println("Max : " + max.removeMax());
        printHeap(max);
        System.out.println("Max : " + max.removeMax());
        printHeap(max);*/
	/* Expected Output:
	Min-Heap Content:
	1 2 3 8 10 15 6 12 11
	Min-Heap Content:
	1 2 3 8 10 11 6 12
	Min-Heap Content:
	1 2 6 8 10 11 12
	Min : 1
	Min-Heap Content:
	2 8 6 12 10 11
	Min : 2
	Min-Heap Content:
	6 8 11 12 10
	Max-Heap Content:
	15 12 10 11 2 6 3 1 8
	Max-Heap Content:
	15 12 10 8 2 6 3 1
	Max-Heap Content:
	15 12 6 8 2 1 3
	Max : 15
	Max-Heap Content:
	12 8 6 3 2 1
	Max : 12
	Max-Heap Content:
	8 3 6 1 2
	*/

    }

}