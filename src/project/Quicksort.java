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


    // Naive sort to deal with the tiny segments
    public void naiveSort(int[] arr, int lb, int rb, Graphics g) {
        for ()
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

        // Repaint screen in black
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        repaint();

        // Repaints rectangles
        g.setColor(Color.white);
        int rectWidth = (rightbound-leftBound)/arraySize; // Width of each rectangle depends on array length
        for (int j = 0; j < arraySize; j+=1) {
            // Redraw each rectangle in its correct position; height and spacing has been normalized to fit the screen
            g.fillRect(leftBound+j*rectWidth, (int)(lowerBound-arr[j]/(double)((double)arraySize/500)), rectWidth, (int)(arr[j]/(double)((double)arraySize/500)));
        }

        // Repaints counter variables
        g.drawString("Comparisons: "+comparisons, (int)(width*((double)1/4)), (int)((height+lowerBound)/2));
        g.drawString("Writes to main array: "+writes, (int)(width*((double)1/2)), (int)((height+lowerBound)/2));
        repaint();
    }


    // Actual algorithm; sorts between lb (inclusive) and rb (exclusive)
    void quicksort(int[] arr, int lb, int rb, Graphics g) {

        // Random pivot point
        int pivot = arr[new Random().nextInt(rb-lb)+lb];
        int i = lb;
        int j = rb-1;

        while (i != j) {
            try {Thread.sleep(10);} catch (InterruptedException e) {}
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

            // Swap values at i and j
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            writes++; writes++;
            refreshScreen(arr, g);
        }
        
        // i==j at this point
        quicksort(arr, 0, j, g);
        quicksort(arr, j, arr.length, g);
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
    }
}