/**
 * Name: Berne' Nortier
 * Student Number: 17091820
 */

@SuppressWarnings("unchecked")
public class MaxHeap<T extends Comparable<T>> extends Heap<T> {

    public MaxHeap(int capacity) {
	super(capacity);
    }

    ////// You may not change any code above this line //////

    ////// Implement the functions below this line ////// 

    @Override
    public void insert(T elem) {
        if (getCurrentSize() == getCapacity()) {
            throw new ArithmeticException("Heap is full");
        }
        int iCh = getCurrentSize()+1;
        int iPar = Math.floorDiv(iCh,2);
        mH[iCh] = elem;
        currentSize+=1;
        while (iCh!=1 && getElementAt(iCh).compareTo(getElementAt(iPar)) >= 0) {
            Swap(iCh, iPar);
            iCh = iPar;
            iPar = Math.floorDiv(iCh,2);
        }

    }

    public T removeMax() {
        if (this.isEmpty()) {
            return null;
        }
        T delEl = getElementAt(1);
        mH[1] = mH[getCurrentSize()];
        mH[getCurrentSize()] = null;
        --currentSize;

        int n = 1;
        if (getCurrentSize()==1) {
            return delEl;
        }

        restoreHeapProperty(n);
        return delEl;
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
    public void restoreHeapProperty(int n) {
        int firstCh = Math.floorDiv(getCurrentSize(),2) + 1;
        int fatChildIndex = findFattestChild(n);
        while (n<firstCh && fatChildIndex!=0) {
            Swap(findFattestChild(n), n);
            n = fatChildIndex;
            fatChildIndex = findFattestChild(n);
        }

    }


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

    private int findFattestChild(int i) {
        int fattestChIndex = i;
        //child exists?
        if (2*i > getCurrentSize()) {
            return fattestChIndex;
        }
        if (getElementAt(i).compareTo(getElementAt(2*i))<=0) {
            fattestChIndex = 2*i;
        }

        //child exists
        if ((2*i + 1) > getCurrentSize()) {
            if (fattestChIndex==i)
                return 0;
            else
                return fattestChIndex;
        }
        //if index element less than child element
        if (getElementAt(i).compareTo(getElementAt(2*i+1))<=0) {
            // and child element greater than current greatest
            if (getElementAt(2*i+1).compareTo(getElementAt(fattestChIndex))>=0) {
                fattestChIndex = 2*i + 1;
            }
        }
        if (fattestChIndex==i) {
            return 0;
        }
        return fattestChIndex;

    }


}