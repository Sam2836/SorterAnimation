package project;

import tapplet.TApplet;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JOptionPane;
import java.awt.*;

/*
 * :D Quicksort is special and can't be implemented well normally with TApplet.
 */

public class Quicksort extends TApplet {

    public int arraySize; // Size of array to be sorted
    public int delay;
    public int[] array;
    public int width, height; // Width and height of user screen
    public int comparisons;
    public int writes;


    // Left and right bounds for the visual
    public int leftBound;
    public int rightbound;
    // Upper and lower bounds for the visual
    public int upperBound;
    public int lowerBound;


    // Check if array is sorted
    public boolean isSorted() {
        for (int i = 1; i < arraySize; i++) {
            if (array[i-1] > array[i]) {
                return false;
            }
        }
        return true;
    }


    // Naive sort to deal with the tiny segments
    public void naiveSort(int[] arr, int lb, int rb, Graphics g) {
        for (int i = lb; i < rb; i++) {
            for (int j = i; j < rb; j++) {
                if (arr[i] < arr[j]) {
                    comparisons++;
                    
                    swap(arr, i, j);
                    refreshScreen(arr, g);
                }
            }
        }
    }


    // Return the randomly generated array of size entered by the user
    public int[] getArray() {
        try {
            // Get array size from user
            arraySize = Integer.parseInt(
                JOptionPane.showInputDialog("Enter the size of the list to be sorted. Must be less than or equal to 1000."));
            // Ensures input is within [0, 1000]
            while (!(0 <= arraySize && arraySize <= 1000)) {
                arraySize = Integer.parseInt(
                    JOptionPane.showInputDialog("Try again. Size must be less than or equal to 1000."));
            }
            return arrayRandomizer.randomArrayGen(arraySize);
        }
        // Call method again if a non-integer or null character was entered
        catch(Exception exception) {
            return getArray();
        }
    }


    // Updates screen
    public void refreshScreen(int[] arr, Graphics g) {

        // Re-covers screen in black
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);

        // Updates rectangles
        g.setColor(Color.white);
        int rectWidth = (rightbound-leftBound)/arraySize; // Width of each rectangle depends on array length
        for (int j = 0; j < arraySize; j+=1) {
            // Redraw each rectangle in its correct position; height and spacing has been normalized to fit the screen
            g.fillRect(leftBound+j*rectWidth, (int)(lowerBound-arr[j]/(double)((double)arraySize/500)), rectWidth, (int)(arr[j]/(double)((double)arraySize/500)));
        }

        // Updates counter variables
        g.drawString("Comparisons: "+comparisons, (int)(width*((double)1/4)), (int)((height+lowerBound)/2));
        g.drawString("Writes to main array: "+writes, (int)(width*((double)1/2)), (int)((height+lowerBound)/2));

        // Single repaint
        repaint();
    }


    // Swap algorithm
    public void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        writes+=2;
    }


    // Picks decent pivot value
    public int pickPivot(int[] arr, int lb, int rb) {
        int mid = (lb + rb) / 2;
        if (arr[mid] < arr[lb]) {
            swap(arr, lb, mid);
        }
        if (arr[rb-1] < arr[lb]) {
            swap(arr, lb, rb-1);
        }
        if (arr[mid] < arr[rb-1]) {
            swap(arr, mid, rb-1);
        }
        comparisons+=3;
        return arr[rb-1];
    }


    // Actual algorithm; sorts between lb (inclusive) and rb (exclusive)
    public void quicksort(int[] arr, int lb, int rb, Graphics g) {

        if (rb-lb < 2) {
            naiveSort(arr, lb, rb, g);
        }
        else {
            // Random pivot point
            int pivot = pickPivot(arr, lb, rb);
            int i = lb;
            int j = rb-1;

            while (i != j) {
                // try {Thread.sleep(1);} catch (InterruptedException e) {}
                comparisons++;

                // Search for swappable values on either side of pivot
                while (arr[i] < pivot) {
                    comparisons++;
                    i++;
                }
                while (pivot < arr[j]) {
                    comparisons++;
                    j--;
                }

                swap(arr, i, j);
                refreshScreen(arr, g);
            }

            // i==j at this point
            quicksort(arr, lb, j, g);
            quicksort(arr, j, rb, g);
        }
    }




    public void init() {
        // I copied this toolkit segment from the first answer to a stackoverflow question for getting the current screen resolution
        // https://stackoverflow.com/questions/3680221/how-can-i-get-screen-resolution-in-java
        width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        setSize(width, height);
        Graphics g = getScreenBuffer();

        leftBound = width/16;
        rightbound = width-(width/16);
        upperBound = height/8;
        lowerBound = height-(height/6);

        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        repaint();

        array = getArray();

        // Display rectangles and counter variables on screen
        refreshScreen(array, g);
        g.drawString("Comparisons: "+comparisons, (int)(width*((double)1/4)), (int)((height+lowerBound)/2));
        g.drawString("Writes to main array: "+writes, (int)(width*((double)1/2)), (int)((height+lowerBound)/2));
        repaint();

        quicksort(array, 0, array.length, g);
        if (isSorted()) {
            g.setColor(Color.green);
            g.setFont(new Font(getName(), Font.PLAIN, 20));
            g.drawString("SORTED", width/2, height/10);
            repaint();
        }
    }
}