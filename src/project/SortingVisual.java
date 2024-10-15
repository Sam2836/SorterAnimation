package project;

/*
 *  Sorting algorithms visualized with an array of differently sized rectangles
 */

import java.awt.*;
import tapplet.TApplet;
import javax.swing.*;
import project.SortingProcess.*;


public class SortingVisual extends TApplet {

    public int arraySize; // Size of array to be sorted
    public int stage; // Flag variable; 0 = title screen, 1 = animation section
    public SortingProcess sorter; // Sorter object as defined in SortingProcess.java
    public int width, height; // Width and height of user screen
    public int timeWaiting; // Time that the algorithm waits between sorting iterations

    // Left and right bounds for the visual
    public int leftBound = width/16, rightbound = width-(width/16);
    // Upper and lower bounds for the visual
    public int upperBound = height/8, lowerBound = height-(height/6);


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


    // Refreshes rectangle positions
    public void refreshRect(Graphics g) {
        g.setColor(Color.white);
        int rectWidth = (rightbound-leftBound)/arraySize; // Width of each rectangle depends on array length
        for (int j = 0; j < arraySize; j+=1) {
            // Redraw each rectangle in its correct position; height and spacing has been normalized to fit the screen
            g.fillRect(leftBound+j*rectWidth, (int)(lowerBound-sorter.arr[j]/(double)((double)arraySize/500)), rectWidth, (int)(sorter.arr[j]/(double)((double)arraySize/500)));
        }
    }


    // Effectively init() but for stage 1
    public void stage1() {
        stage = 1;
        setSize(width, height);
        Graphics g = getScreenBuffer();
        // Repaint screen in black
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        repaint();

        // Display rectangles and counter variables on screen
        refreshRect(g);
        g.drawString("Comparisons: "+sorter.comparisons, (int)(width*((double)1/4)), (int)((height+lowerBound)/2));
        g.drawString("Writes to main array: "+sorter.writes, (int)(width*((double)1/2)), (int)((height+lowerBound)/2));
        repaint();

        // Pause for 2 seconds
        try {Thread.sleep(2000);} catch(InterruptedException e) {}

        // Repeatedly do one sorting iteration then update the screen
        for (int i = 1; i > 0; i++) {
            // Do one sorting iteration
            sorter.sort(i);

            // Repaint screen black
            g.setColor(Color.black);
            g.fillRect(0,0,width,height);

            // Refresh rectangles and counter variables on screen
            refreshRect(g);
            g.drawString("Comparisons: "+sorter.comparisons, (int)(width*((double)1/4)), (int)((height+lowerBound)/2));
            g.drawString("Writes to main array: "+sorter.writes, (int)(width*((double)1/2)), (int)((height+lowerBound)/2));

            repaint();

            // Exit if array is sorted
            if (sorter.finished) {
                g.setColor(Color.green);
                g.setFont(new Font(getName(), Font.PLAIN, 20));
                g.drawString("SORTED", width/2, height/10);
                repaint();
                break;
            }
        }
    }


    public void init() {
        stage = 0;

        // I copied this toolkit segment from the first answer to a stackoverflow question for getting the current screen resolution
        // https://stackoverflow.com/questions/3680221/how-can-i-get-screen-resolution-in-java
        width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        setSize(width, height);
        Graphics g = getScreenBuffer();

        // Display height and width (debugging)
        // g.drawString("w: "+Integer.toString(width), 100, 100);
        // g.drawString("h: "+Integer.toString(height), 100, 200);
        // g.drawOval(100, 100, 5, 5);

        // Title
        g.setFont(new Font(getName(), Font.PLAIN, (int)Math.ceil((double)width/30.5)));
        g.setColor(Color.white);
        g.drawString("Sorting Algorithms Visualized", (int)(width/3.41), height/5);

        // Bubble sort button
        g.drawString("Bubble Sort", (int)(width/3.79), (int)(height/1.23));
        g.drawRoundRect(width/4, (int)(height/1.5), width/5, height/4, width/50, height/30);

        // Insertion sort button
        g.drawString("Insertion Sort", (int)(width/1.8), (int)(height/1.23));
        g.drawRoundRect((int)(width/1.805), (int)(height/1.5), width/5, height/4, width/50, height/30);

        // Cocktail Shaker sort button
        g.drawString("Cocktail", (int)(width/3.46), (int)(height/2.1));
        g.drawString("Shaker Sort", (int)(width/3.79), (int)(height/1.85));
        g.drawRoundRect(width/4, (int)(height/2.75), width/5, height/4, width/50, height/30);

        // MSD Radix Sort button
        g.drawString("MSD", (int)(width/1.62), (int)(height/2.1));
        g.drawString("Radix Sort", (int)(width/1.72), (int)(height/1.855));
        g.drawRoundRect((int)(width/1.805), (int)(height/2.75), width/5, height/4, width/50, height/30);

        repaint();
    }


    public void mouseDown(int x, int y) {
        // Only activate when on title screen (stage 0); handles button presses which start the program
        if (stage == 0) {
            if (height/1.5 + height/4 > y && y > height/1.5) {
                // Detect click on bubble sort button
                if (width/4 < x && x < width/4 + width/5) {
                    // Create BubbleSort object and assign it to the global SortingProcess object "sorter"
                    timeWaiting = 25-(int)Math.ceil((double)arraySize/20);
                    BubbleSort sortB = new SortingProcess().new BubbleSort(
                        getArray(), timeWaiting);
                    sorter = sortB;
                    // Trigger stage 1
                    stage1();
                }
                // Detect click on insertion sort button
                if (width/1.805 < x && x < width/1.805 + width/5) {
                    // Create InsertionSort object and assign it to the global SortingProcess object "sorter"
                    timeWaiting = (int)(arraySize*(-1/10) + 25);
                    InsertionSort sortI = new SortingProcess().new InsertionSort(
                        getArray(), timeWaiting);
                    sorter = sortI;
                    // Trigger stage 1
                    stage1();
                }
            }
            if ((int)(height/2.75) + height/4 > y && y > (int)(height/2.75)) {
                // Detect click on cocktail shaker sort button
                if (width/4 < x && x < width/4 + width/5) {
                    // Create CocktailShakerSort object and assign it to the global SortingProcess object "sorter"
                    timeWaiting = 75-(int)Math.ceil((double)arraySize/20);
                    CocktailShakerSort sortC = new SortingProcess().new CocktailShakerSort(
                        getArray(), timeWaiting);
                    sorter = sortC;
                    // Trigger stage 1
                    stage1();
                }
                // Detect click on MSD Radix Sort button
                if (width/1.805 < x && x < width/1.805 + width/5) {
                    // Create MSDRadixSort object and assign it to the global SortingProcess object "sorter"
                    timeWaiting = 75-(int)Math.ceil((double)arraySize/20);
                    // MSDRadixSort sortR = new SortingProcess().new MSDRadixSort(
                        // getArray(), timeWaiting);
                    // sorter = sortR;
                    // Trigger stage 1
                    stage1();
                }
            }
        }
    }


    public static void main(String[] args) {
        new SortingVisual();
    }
}