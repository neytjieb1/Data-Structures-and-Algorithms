/**
 * Name: Berne' Nortier
 * Student Number: 17091820
 */

@SuppressWarnings("unchecked")
public abstract class Heap<T extends Comparable<T>> {

    int capacity;
    Comparable<T> mH[];
    int currentSize;

    public Heap(int capacity) {
        this.capacity = capacity;
        mH = new Comparable[capacity+1]; //Use index positions 1 to capacity 
        currentSize = 0;
    }

    protected int getCapacity(){
        return capacity;
    }

    protected int getCurrentSize(){
        return currentSize;
    }

    public void display() {
        for(int i = 1; i <= currentSize; i++) {
            System.out.print(mH[i] + " ");
        }
        System.out.println("");
    }

    ////// You may not change any code above this line //////

    ////// Implement the functions below this line //////

    protected boolean isEmpty() {
        if (this.currentSize == 0) {
            return true;
        }
        return false;
    }

    public abstract void insert(T elem);


    //Helper functions


}