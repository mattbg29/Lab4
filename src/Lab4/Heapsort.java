/* *****************************************************************************
 * Name: Matthew Green
 * Date: 30Nov2022
 * Purpose:  To run Heapsort on an input array of ints.  This class also tracks
 * the amount of time the sort takes, as well as the number of swaps and compares.
 * Note that at times, the numCompares will be slightly overstated (as indicated in
 * comments below), and as such this number should be treated as an approximation.
 **************************************************************************** */


package Lab4;

public class Heapsort {
    private int sizeNow = -1; // for building the array to be sorted from the user input
    private final int[] arrHeapSort; // for holding the unsorted input and sorted output
    private final double sortTime; // for tracking the sorting runtime
    private int numSwaps = 0; // for tracking the total number of swaps
    private int numCompares = 0; // for tracking the total number of compares

    // A constructor that takes a standard array of ints as input,
    // builds a heap, sorts the heap, and tracks the time to build and
    // sort the heap
    Heapsort(int[] arr) {
        arrHeapSort = new int[arr.length];
        long startTime = System.nanoTime();

        for (int i : arr) {
            add(i);
        }

        sort();
        long endTime = System.nanoTime();
        sortTime = endTime - startTime;
    }

    // A constructor that takes an MGArrayList of Integers as input,
    // builds a heap, sorts the heap, and tracks the time to build and
    // sort the heap
    Heapsort(MGArrayList<Integer> arr) {
        arrHeapSort = new int[arr.size()];
        long startTime = System.nanoTime();

        for (int j = 0; j < arr.size(); j++) {
            add(arr.get(j));
        }

        sort();
        long endTime = System.nanoTime();
        sortTime = endTime - startTime;
    }

    // For creating a max-heap from the user inputs.
    // A max-heap is a data structure where every parent is larger than its children,
    // where in this case children are defined as the element at the index level of
    // parent * 2 + 1 and + 2.
    void add(int item) {
        arrHeapSort[++sizeNow] = item;
        int child = sizeNow;
        int parent = (child - 1) / 2;

        // The max-heap maintains its integrity as you add to it by swapping
        // the new child with its parent if the child is larger
        while (parent >= 0 && arrHeapSort[child] > arrHeapSort[parent]) {
            swap(parent, child);
            child = parent;
            parent = (child - 1) / 2;
            numCompares += 2;
        }
        numCompares += 2; // this will overstate numCompares when parent < 0
    }

    // For swapping two ints in the heap as part of the heapifying process
    private void swap(int a, int b) {
        int temp = arrHeapSort[b];
        arrHeapSort[b] = arrHeapSort[a];
        arrHeapSort[a] = temp;
        numSwaps++;
    }

    // Now that the heap is built, sorting the array via heapsort involves
    // swapping the first element with the last element, then reheapifying while
    // ignoring the last element, plus any element after it that has already been
    // swapped from the front.  Since the element at the front of a max-heap must
    // by definition be the largest, this will result in an array sorted from
    // smallest to largest
    private void sort() {
        while (sizeNow > 0) {
            numCompares++; // incrementing for the sizeNow > 0 while loop
            swap(0, sizeNow--);
            if (sizeNow == 0) {
                break;
            }
            int parent = 0;
            int largerChild = largerChild(1, 2);

            // The heap maintains its integrity as you remove from it by
            // swapping the removed item with the last item that has yet to
            // be swapped, then swapping this new parent with its larger child
            // and continuing this process until the heap is restored.
            while (arrHeapSort[parent] < arrHeapSort[largerChild]) {
                swap(parent, largerChild);
                parent = largerChild;
                int child = parent * 2 + 1;
                if (child >= sizeNow) {
                    break;
                }
                numCompares++; // incrementing for the child >= sizeNow check
                largerChild = largerChild(child, child + 1);
                numCompares++; // incrementing for the current while loop
            }
            numCompares++; // incrementing for when the current while loop exits
        }
        numCompares++; // incrementing for when the overall while loop exits
    }

    // Identify the larger of the parent's two children
    private int largerChild(int a, int b) {
        if (b > sizeNow || arrHeapSort[a] >= arrHeapSort[b]) {
            numCompares += 2; // This will overstate the numCompares when b <= sizeNow
            return a;
        }
        numCompares += 2;
        return b;
    }

    // Return the sorted array
    int[] getArrHeapSort() {
        return arrHeapSort;
    }

    // Return the total sort time
    double getSortTime() {
        return sortTime;
    }

    // Return the total number of swaps
    int getNumSwaps() {
        return numSwaps;
    }

    // Return the total number of compares performed
    int getNumCompares() {
        return numCompares;
    }

}
