package project;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * Holds all sorting algorithms used in the project as subclasses of SortingProcess.
 */

public class SortingProcess {
    
    public int[] arr; // Main array
    public int len; // Length of array
    public long comparisons; // Counts comparisons between elements
    public long writes; // Counts writes to main array
    public int millisSleep; // Time to pause in between sorting loop iterations (milliseconds)
    public boolean finished = false; // Flag when sorting is done

    // Will be overridden by subclasses
    public void sort(int i) {
    }

    // Check if array is sorted
    public boolean isSorted() {
        for (int i = 1; i < len; i++) {
            if (arr[i-1] > arr[i]) {
                return false;
            }
        }
        return true;
    }


    /*
     * Bubble Sort Algorithm (OLD VERSION IS COMMENTED OUT BELOW CURRENT VERSION)
     */

    public class BubbleSort extends SortingProcess {
        // Constructor method
        public BubbleSort(int[] arrayInput, int sleepTime) {
            arr = arrayInput;
            len = arr.length;
            comparisons = 0;
            writes = 0;
            millisSleep = sleepTime;
        }

        // Primary method that is called by the main program to sort the array
        // DOES ONLY ONE ITERATION; it is looped in SortingVisual.java
        public void sort(int i) {
            // Force the loop to wait to make runtime reasonable
            try {Thread.sleep(millisSleep);} catch (InterruptedException e) {}

            // Loop up the array to len-i
            int temp;
            for (int j = 0; j < len-i; j++) {
                comparisons++;
                // Swap the current and next element if out of order
                if (arr[j] > arr[j+1]) {
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
                writes +=2;
            }

            // Check if array is sorted
            if (isSorted()) {
                finished = true;
            }
        }
    }

    // OLD VERSION
    // public class BubbleSort extends SortingProcess {

    //     // Constructor method
    //     public BubbleSort(int[] arrayInput, int sleepTime) {
    //         arr = arrayInput;
    //         len = arr.length;
    //         comparisons = 0;
    //         writes = 0;
    //         millisSleep = sleepTime;
    //     }
        
    //     // Swap the elements at positions j-1 and j in the array
    //     private void swapUp(int index) {
    //         int temp = arr[index-1];
    //         arr[index-1] = arr[index];
    //         arr[index] = temp;
    //         writes++; writes++;
    //     }

    //     // Primary method that is called by the main program to sort the array
    //     // DOES ONLY ONE ITERATION; it is looped in SortingVisual.java
    //     public void sort(int i) {
    //        // Force the loop to wait to make runtime reasonable
            // try {Thread.sleep(millisSleep);} catch (InterruptedException e) {}
    //         // Loop through subsets of the array and swap out of order elements
    //         for (int j = i; j > 0; j--) {

    //             // Swap elements if they are out of order
    //             comparisons++;
    //             if (arr[j-1] > arr[j]) {
    //                 swapUp(j);
    //             }
                
    //             // Check if array is sorted
    //             if (isSorted()) {
    //                 finished = true;
    //             }
    //         }
    //     }
    // }


    /*
     * Insertion Sort Algorithm
     */

    public class InsertionSort extends SortingProcess {

        // Constructor method
        public InsertionSort(int[] arrayInput, int sleepTime) {
            arr = arrayInput;
            len = arr.length;
            comparisons = 0;
            writes = 0;
            millisSleep = sleepTime;
        }

        // Shift all the elements of arr[] between lowerBound and upperBound up by one spot.
        // The gap at arr[lowerBound] is filled by newValue, and arr[upperBound] is overwritten.
        private void shiftUpAndInsert(int newValue, int upperBound, int lowerBound) {
            for (int i = upperBound; i > lowerBound; i--) {
                arr[i] = arr[i-1];
                writes++;
            }
            arr[lowerBound] = newValue;
            writes++;
        }

        // Basic linear search between lowerBound and upperBound to find the correct place for value
        private int linearSearch(int value, int upperBound, int lowerBound) {
            int i = upperBound;
            // If the order is wrong step down the array until the else{} triggers
            while (i > lowerBound) {
                comparisons++;
                if (arr[i-1] > value) {
                    i--;
                }
                else {
                    return i;
                }
            }
            return i;
        }

        // Binary search on a subset of the array to find the correct place to insert value
        private int binarySearch(int value, int upperBound, int lowerBound) {
            // Base case
            if (upperBound == lowerBound) { return lowerBound; }
            // If the subset to search is small, use linear search because the time complexity is better
            if (upperBound-lowerBound < 4) {
                return linearSearch(value, upperBound, lowerBound);
            }
            // Otherwise cut the subset in half and reapply the method with the new bounds
            int i = (upperBound+lowerBound)/2;
            comparisons++;
            if (value < arr[i]) {
                upperBound = i;
                i = binarySearch(value, upperBound, lowerBound);
            }
            else if (arr[i] < value) {
                lowerBound = i;
                i = binarySearch(value, upperBound, lowerBound);
            }
            return i;
        }
        
        // Primary method that is called by the main program to sort the array
        // DOES ONLY ONE ITERATION; it is looped in SortingVisual.java
        public void sort(int i) {
            int temp, index;

            // Force the loop to wait to make runtime reasonable
            try {Thread.sleep(millisSleep);} catch (InterruptedException e) {}

            // Search for the correct place for the next element
            temp = arr[i];
            index = binarySearch(temp, i, 0);

            // After the correct place is found, insert the element and move everything else up
            shiftUpAndInsert(temp, i, index);

            // Check if array is sorted
            if (isSorted()) {
                finished = true;
            }
        }
    }


    /*
     * Cocktail Shaker Sort Algorithm
     */

    public class CocktailShakerSort extends SortingProcess {

        // Constructor method
        public CocktailShakerSort(int[] arrayInput, int sleepTime) {
            arr = arrayInput;
            len = arr.length;
            comparisons = 0;
            writes = 0;
            millisSleep = sleepTime;
        }

        // Primary method that is called by the main program to sort the array
        // DOES ONLY ONE ITERATION; it is looped in SortingVisual.java
        public void sort(int i) {
            // Force the loop to wait to make runtime reasonable
            try {Thread.sleep(millisSleep);} catch (InterruptedException e) {}

            // Essentially bubble sort but in both directions
            i -= 1;
            int temp;
            // Forwards direction
            for (int j = i; j < len-i-1; j++) {
                comparisons++;
                // Swap the current and next element if out of order
                if (arr[j] > arr[j+1]) {
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
                writes+=2;
            }
            // Backwards direction
            for (int j = len-i-1; j > i; j--) {
                comparisons++;
                if (arr[j] < arr[j-1]) {
                    temp = arr[j-1];
                    arr[j-1] = arr[j];
                    arr[j] = temp;
                }
                writes+=2;
            }

            // Check if array is sorted
            if (isSorted()) {
                finished = true;
            }
        }
    }


    /*
     * QuickSort algorithm
     * 
     * fuck
     */

    //  public class QuickSort extends SortingProcess {

    //     // Flag if partition is done being separated
    //     private boolean partitionFlag;
    //     // Keeps track of pivot points in order of construction
    //     private int currentPivotValue;
    //     private int[] finalPivotIndices;
    //     private int[] currentArr;

    //     // Constructor method
    //     public QuickSort(int[] arrayInput, int sleepTime) {
    //         arr = arrayInput;
    //         len = arr.length;
    //         comparisons = 0;
    //         writes = 0;
    //         millisSleep = sleepTime;
    //         partitionFlag = true;
    //         finalPivotIndices = new int[(int)(Math.log(len)/Math.log(2))+2];
    //         currentArr = arr;
    //     }

    //     // Primary method that is called by the main program to sort the array
    //     // DOES ONLY ONE ITERATION; it is looped in SortingVisual.java
    //     public void sort(int i) {
    //         // Force the loop to wait to make runtime reasonable
    //         try {Thread.sleep(millisSleep);} catch (InterruptedException e) {}

    //         if (partitionFlag) {
    //             currentPivotValue = currentArr[new Random().nextInt(currentArr.length)];
    //             partitionFlag = false;
    //         }

            

    //     }
    // }


    /*
     * MSD Radix sort Algorithm
     */

    // public class MSDRadixSort extends SortingProcess {
    //     String[] numStrings; // Holds string version of elements

    //     // Constructor method
    //     public MSDRadixSort(int[] arrayInput, int sleepTime) {
    //         arr = arrayInput;
    //         len = arr.length;
    //         comparisons = 0;
    //         writes = 0;
    //         millisSleep = sleepTime;
    //         numStrings = new String[len];
    //         // Convert array to strings when object is created
    //         numToString();
    //     }

    //     // Convert each number to a 4-digit string (i.e. 56 --> "0056")
    //     public void numToString() {
    //         for (int i = 0; i < len; i++) {
    //             if (arr[i] < 10) {
    //                 numStrings[i] = "000"+Integer.toString(arr[i]);
    //             }
    //             else if (arr[i] < 100) {
    //                 numStrings[i] = "00"+Integer.toString(arr[i]);
    //             }
    //             else if (arr[i] < 1000) {
    //                 numStrings[i] = "0"+Integer.toString(arr[i]);
    //             }
    //         }
    //     }

    //     public void stringToNum() {
    //         for (int i = 0; i < len; i++) {
    //             arr[i] = Integer.parseInt(numStrings[i]);
    //         }
    //     }

    //     // Primary method that is called by the main program to sort the array
    //     // DOES ONLY ONE ITERATION; it is looped in SortingVisual.java
    //     public void sort(int i) {

    //     }
    // }
}
