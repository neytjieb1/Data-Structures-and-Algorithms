/**
 * Name: Berne' Nortier
 * Student Number: 17091820
 */

@SuppressWarnings("unchecked")
public class MinHeap<T extends Comparable<T>> extends Heap<T> {

    public MinHeap(int capacity) {
	super(capacity);
    }

    ////// You may not change any code above this line //////

    ////// Implement the functions below this line //////

    @Override
    public void insert(T elem) {
        if (getCurrentSize() == getCapacity()) {
            throw new ArithmeticException("Heap is full");
        }
        int iCh = getCurrentSize() + 1;
        int iPar = Math.floorDiv(iCh, 2);
        mH[iCh] = elem;
        this.currentSize += 1;
        while (iCh!=1  && getElementAt(iCh).compareTo(getElementAt(iPar)) <= 0) {
            Swap(iCh, iPar);
            iCh = iPar;
            iPar = Math.floorDiv(iCh, 2);
        }

    }

    public T removeMin() {
        //Empty?
        if (this.isEmpty()) {
            return null;
        }
        T delEl = getElementAt(1);
        mH[1] = mH[getCurrentSize()];
        mH[getCurrentSize()] = null;
        --currentSize;

        int n = 1;
        if (getCurrentSize() == 1) {
            return delEl;
        }
        restoreHeapProperty(n);
        return delEl;
    }

    public void restoreHeapProperty(int n) {
        int firstCh = Math.floorDiv(getCurrentSize(), 2) + 1;
        int smallestChild = findSmallestChild(n);
        while (n<firstCh && smallestChild!=0) {
            Swap(findSmallestChild(n), n);
            n=smallestChild;
            smallestChild = findSmallestChild(n);
        }
    }

    public void delete(T elem) {
        int elIndex = getIndexOfElem(elem);
        if (elIndex==0)
            return;

        mH[elIndex] = mH[currentSize];
        mH[currentSize] = null;
        currentSize--;

        if (getCurrentSize()==1) {
            return;
        }
        restoreHeapProperty(elIndex);

    }


    //Helper functions

    //find index of an element. Return 0 if not in list
    public int getIndexOfElem(T el) {
        for (int i = 1; i < currentSize+1; i++) {
            if (mH[i].compareTo(el)==0) {
                return i;
            }
        }
        return 0;
    }

    public T getElementAt(int index) {
        return (T) mH[index];
    }

    private void Swap(int iCh, int iPar) {
        Comparable<T> temp = mH[iCh];
        mH[iCh] = mH[iPar];
        mH[iPar] = temp;
    }

    //find index of smaller child. If is the smallest, then return 0(which is an invalid index)
    private int findSmallestChild(int index) {
        int smallestChIndex = index; //invalid index
        //check that children exist
        if (2*index>getCurrentSize()) {
            return smallestChIndex;
        }
        if (getElementAt(index).compareTo(getElementAt(2 * index))>=0){
            smallestChIndex = 2*index;
        }
        //check that children exist
        if ((2*index+1) > getCurrentSize() ) {
            if (smallestChIndex==index)
                return 0;
            else
                return smallestChIndex;
        }
        if (getElementAt(index).compareTo(getElementAt(2*index + 1))>=0){
            if (getElementAt(2*index+1).compareTo(getElementAt(smallestChIndex))<=0)
                smallestChIndex = 2*index+1;
        }
        if (smallestChIndex==index) {
            return 0;
        }
        return smallestChIndex;

    }

}