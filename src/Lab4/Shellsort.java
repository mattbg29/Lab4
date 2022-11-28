/* *****************************************************************************
 * Name: Matthew Green
 * Date: 30Nov2022
 * Purpose:  To run Shellsort on an input array of ints using 5 increment
 * sequences, the final one of which, having only the number 1, is equivalent to
 * insertion sort.  This class also tracks the amount of time each sort takes,
 * as well as the number of swaps and compares.
 *  * Note that at times, the numCompares will be slightly overstated (as indicated in
 * comments below), and as such this number should be treated as an approximation.
 **************************************************************************** */


package Lab4;

public class Shellsort {
    private final int[][] arrSorted; // The arrays sorted by each increment sequence in INC_SEQUENCE
    private int sizeNow = -1; // Used for converting the MGArrayList input into arrays

    // The increment sequences. The first three are as per the lab guidelines. The fourth
    // is as per the textbook's suggestion of each being ~ 2.2x the previous.
    // The 5th, 1, is essentially an insertion sort.
    // Note that the first sequence is listed twice, but the output stats of the
    // first of two is omitted. I do this because the first iteration of the program runs
    // more slowly than the rest as the JVM optimizes, which makes it harder to
    // compare the runtimes.
    private final int[][] INC_SEQUENCE = {
            {29524, 9841, 3280, 1093, 364, 121, 40, 13, 4, 1}, // this is repeated for comparison's sake (see note above)
            {29524, 9841, 3280, 1093, 364, 121, 40, 13, 4, 1},
            {30341, 10111, 3371, 1123, 373, 149, 53, 17, 5, 1},
            {29160, 9720, 3240, 1080, 360, 120, 60, 30, 10, 1},
            {30341, 13791, 6268, 2849, 1295, 588, 267, 121, 55, 25, 11, 5, 2, 1},
            {1}
    };
    private final double[] arrSortedTime = new double[INC_SEQUENCE.length]; // For storing each sequence sort time
    private final int[] numSwaps = new int[INC_SEQUENCE.length]; // For storing each sequence number of swaps
    private final int[] numCompares = new int[INC_SEQUENCE.length]; // For storing each sequence number of compares

    // Constructor for an MGArrayList, which is how the info from the user input file
    // is warehoused. This calls add() to translate the input into what will become
    // the output sorted arrays
    Shellsort(MGArrayList<Integer> arr) {
        arrSorted = new int[INC_SEQUENCE.length][arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            add(arr.get(j));
        }
        sort();
    }

    // Constructor that takes a standard array, which will be the input when
    // the program is using output from earlier runs as the input (see RunSorts class)
    Shellsort(int[] arr) {
        arrSorted = new int[INC_SEQUENCE.length][arr.length];
        for (int i : arr) {
            add(i);
        }
        sort();
    }

    // See first constructor for explanation
    void add(int item) {
        sizeNow++;
        for (int i = 0; i < INC_SEQUENCE.length; i++) {
            arrSorted[i][sizeNow] = item;
        }
    }

    // Shellsort introduces a gap number, and performs insertion sort
    // on subarrays that consist of the elements at each gap point starting at 0
    // in the original, then decrements the gap by certain steps until finally
    // the step is down to 1.
    // This version of Shellsort uses 5 different gap
    // sequences, performing an independent sort for each sequence
    private void sort() {
        for (int seqNow = 0; seqNow < INC_SEQUENCE.length; seqNow++) {
            long startTime = System.nanoTime();

            // Go through each gap in the given sequence. If the gap is larger than
            // the total number of elements in the array, continue to the next gap
            for (int gap : INC_SEQUENCE[seqNow]) {
                numCompares[seqNow]++; // incrementing for the for loop

                if (gap < arrSorted[seqNow].length / 2) {
                    numCompares[seqNow]++; // incrementing for the above compare

                    // There will be one set of sub arrays for every set of
                    // gap numbers starting at 0 and ending at gap - 1
                    for (int j = 0; j < gap; j++) {

                        // The number of elements in the given subarray
                        int numToSort = (arrSorted[seqNow].length - 1 - j) / gap;

                        // Insertion Sort the given sub array
                        insertionSort(numToSort, gap, seqNow, j);
                    }
                }
            }
            long endTime = System.nanoTime();
            arrSortedTime[seqNow] = endTime - startTime;
        }
    }

    // Perform insertion sort on each Shellsort subarray in place by iterating through 
    // the elements in the subarray, swapping any element that is smaller than its
    // predecessor until it is larger than its predecessor or at the start of the subarray
    private void insertionSort(int numToSort, int gap, int seqNow, int start) {
        for (int j = start + gap; j <= start + gap * numToSort; j += gap) {
            numCompares[seqNow]++; // incrementing for the for loop
            while ((j - gap) >= start && arrSorted[seqNow][j] < arrSorted[seqNow][j - gap]) {
                int temp = arrSorted[seqNow][j];
                arrSorted[seqNow][j] = arrSorted[seqNow][j - gap];
                arrSorted[seqNow][j - gap] = temp;
                j -= gap;
                numSwaps[seqNow]++;
                numCompares[seqNow] += 2; // incrementing for the while loop compares
            }
            numCompares[seqNow] += 2; // incrementing for the while loop exit
        }
    }
    
    // Return all arrays sorted by the various increment sequences
    int[][] getArrSorted() {
        return arrSorted;
    }

    // Return the time it took to sort
    double[] getArrSortedTime() {
        return arrSortedTime;
    }

    // Return the Increment Sequences used
    int[][] getIncSequence() {
        return INC_SEQUENCE;
    }

    // Return the total number of swaps performed
    int[] getNumSwaps() {
        return numSwaps;
    }

    // Return the total number of compares performed
    int[] getNumCompares() {
        return numCompares;
    }
}
