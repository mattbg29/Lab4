/* *****************************************************************************
 * Name: Matthew Green
 * Date: 30Nov2022
 * Purpose:  To run the sorts as per the Lab's instructions, using numbers
 * from the user's input file.  This class will convert this set of unsorted
 * numbers into a sorted and reverse sorted list of numbers for running the sort
 * analysis on the sorted and reverse sorted versions of the array, rather than
 * using separate input files from the user.
 **************************************************************************** */


package Lab4;

import java.io.PrintWriter;

public class RunSorts {
    private final MGArrayList<Integer> input; // user input
    private int[] arrHeap, arrSortedHeap, arrReverseHeap; // Heap sorted arrays
    private double inputHeapTime, sortedHeapTime, reverseHeapTime; // Heap sorted times
    private int inputHeapSwaps, sortedHeapSwaps, reverseHeapSwaps; // Heap sorted num swaps
    private int inputHeapCompares, sortedHeapCompares, reverseHeapCompares; // Heap sorted num compares
    private int[][] arrShell, arrSortedShell, arrReverseShell; // Shell sorted arrays
    private final double[] inputShellTime, sortedShellTime, reverseShellTime; // Shell sorted times
    private int[] inputShellSwaps, sortedShellSwaps, reverseShellSwaps; // Shell sorted num swaps
    private int[] inputShellCompares, sortedShellCompares, reverseShellCompares; // Shell sorted num compares
    private final int[][] incSequence; // the increment sequences used for Shellsort

    // In the constructor, I run the sort() method 10 times and average the
    // output runtimes, in order to reduce the impact of any external system processes
    // on a given runtime result
    RunSorts(MGArrayList<Integer> input) {
        this.input = input;
        Shellsort shell = new Shellsort(input);
        incSequence = shell.getIncSequence();
        inputShellTime = new double[incSequence.length];
        sortedShellTime = new double[incSequence.length];
        reverseShellTime = new double[incSequence.length];

        for (int i = 1; i <= 10; i++) {
            run();
        }

        inputHeapTime /= 10.0;
        sortedHeapTime /= 10.0;
        reverseHeapTime /= 10.0;

        for (int i = 0; i < inputShellTime.length; i++) {
            inputShellTime[i] /= 10.0;
            sortedShellTime[i] /= 10.0;
            reverseShellTime[i] /= 10.0;
        }
    }

    private void run() {
        // Heapsort the input and retrieve output data
        Heapsort heap = new Heapsort(input); // run this twice to give JVM a chance to optimize
        Heapsort heap2 = new Heapsort(input);
        arrHeap = heap2.getArrHeapSort();
        inputHeapTime += heap2.getSortTime();
        inputHeapSwaps = heap2.getNumSwaps();
        inputHeapCompares = heap2.getNumCompares();

        // Heapsort the already sorted input and retrieve output data
        Heapsort heapSorted = new Heapsort(arrHeap);
        arrSortedHeap = heapSorted.getArrHeapSort();
        sortedHeapTime += heapSorted.getSortTime();
        sortedHeapSwaps = heapSorted.getNumSwaps();
        sortedHeapCompares = heapSorted.getNumCompares();

        // Reverse the order of the already sorted input, then
        // Heapsort this and retrieve output data
        int[] arrReverse = reverse(arrSortedHeap);
        Heapsort heapReverse = new Heapsort(arrReverse);
        arrReverseHeap = heapReverse.getArrHeapSort();
        reverseHeapTime += heapReverse.getSortTime();
        reverseHeapSwaps = heapReverse.getNumSwaps();
        reverseHeapCompares = heapReverse.getNumCompares();

        // Shellsort the input and retrieve output data
        Shellsort shell = new Shellsort(input);
        arrShell = shell.getArrSorted();
        inputShellSwaps = shell.getNumSwaps();
        inputShellCompares = shell.getNumCompares();

        // Shellsort the already sorted input and retrieve output data
        Shellsort shellSorted = new Shellsort(arrHeap);
        arrSortedShell = shellSorted.getArrSorted();
        sortedShellSwaps = shellSorted.getNumSwaps();
        sortedShellCompares = shellSorted.getNumCompares();

        // Shellsort the reverse sorted input and retrieve output data
        Shellsort shellReverse = new Shellsort(arrReverse);
        arrReverseShell = shellReverse.getArrSorted();
        reverseShellSwaps = shellReverse.getNumSwaps();
        reverseShellCompares = shellReverse.getNumCompares();

        for (int i = 0; i < inputShellTime.length; i++) {
            inputShellTime[i] += shell.getArrSortedTime()[i];
            sortedShellTime[i] += shellSorted.getArrSortedTime()[i];
            reverseShellTime[i] += shellReverse.getArrSortedTime()[i];
        }
    }

    // Reverse the order of an array
    private int[] reverse(int[] arr) {
        int[] reverse = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reverse[i] = arr[arr.length - i - 1];
        }
        return reverse;
    }

    // Print the output to the file that the user entered
    void print(PrintWriter output) {
        output.println("Output for data containing " + input.size() + " integers");
        output.println("All times are in nanoseconds\n");

        // Print the summary data from the Heapsorts
        output.println("Summary of heap sort | times | swaps | compares:");
        output.println("Sorting an unsorted array: | " + inputHeapTime + " | " + inputHeapSwaps + " | " + inputHeapCompares);
        output.println("Sorting a sorted array: | " + sortedHeapTime + " | " + sortedHeapSwaps + " | " + sortedHeapCompares);
        output.println("Sorting a reverse sorted array: | " + reverseHeapTime + " | " + reverseHeapSwaps + " | " + reverseHeapCompares + "\n");

        // Print the summary data from the Shellsorts using each of the increment sequences
        // The first sequence is skipped, as it is repeated in the second sequence to
        // allow for runtime comparison after the JVM has had a chance to optimize
        // the sort process
        for (int i = 1; i < incSequence.length; i++) {
            output.println("Summary of shell sort | times | swaps | compares | using the following increment sequence:");
            printSequence(i, output);
            output.println("Sorting an unsorted array: | " + inputShellTime[i] + " | " + inputShellSwaps[i] + " | " + inputShellCompares[i]);
            output.println("Sorting a sorted array: | " + sortedShellTime[i] + " | " + sortedShellSwaps[i] + " | " + sortedShellCompares[i]);
            output.println("Sorting a reverse sorted array: | " + reverseShellTime[i] + " | " + reverseShellSwaps[i] + " | " + reverseShellCompares[i] + "\n");
        }

        // Check that all of the sorts were successful, and if so,
        // print the sorted numbers
        if (sortSuccess()) {
            output.println("Input sorted:");
            printSortedNums(output);
        } else {
            output.println("Sort failed");
        }

        output.close();
    }

    // Print the sorted numbers
    private void printSortedNums(PrintWriter output) {
        for (int i = 0; i < input.size(); i++) {
            output.println(arrHeap[i]);
        }
    }

    // Check that every sort attempt was successful by returning false if
    // any element in a sorted array is smaller than its predecessor
    private boolean sortSuccess() {
        for (int i = 1; i < input.size(); i++) {
            if (arrHeap[i] < arrHeap[i - 1] || arrSortedHeap[i] < arrSortedHeap[i - 1] || arrReverseHeap[i] < arrReverseHeap[0]) {
                return false;
            }
            for (int j = 0; j < incSequence.length; j++) {
                if (arrShell[j][i] < arrShell[j][i - 1] || arrSortedShell[j][i] < arrSortedShell[j][i - 1] || arrReverseShell[j][i] < arrReverseShell[j][i - 1]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Print the increment sequence used for the given Shellsort
    private void printSequence(int sequence, PrintWriter output) {
        int lengthNow = incSequence[sequence].length;
        for (int i = 0; i < lengthNow; i++) {
            output.print(incSequence[sequence][i]);
            if (i < lengthNow - 1) {
                output.print(", ");
            }
        }
        output.println();
    }
}
