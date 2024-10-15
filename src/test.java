import project.SortingProcess;
import project.arrayRandomizer;
import project.SortingProcess.*;
// import java.util.*;

public class test {
    public static void main(String[] args) {
        SortingProcess sorter;
        // int[] arrayInput = {5,1,8,0,7,2,9,4,6,3};
        int len = 1000;
        int[] arrayInput = arrayRandomizer.randomArrayGen(len);
        InsertionSort sortI = new SortingProcess().new InsertionSort(arrayInput, (int)(len*((double)(-17)/180) + (double)(985/9)));
        sorter = sortI;
        // BubbleSort sortB = new SortingProcess().new BubbleSort(arrayInput, 3-Math.ceilDiv(len, 500));
        // sorter = sortB;
        
        // Print original array
        // for (int i = 0; i < len; i++) {
        //     System.out.print(sorter.arr[i]+", ");
        // }
        // System.out.println();
        // System.out.println();

        // sorter.sort();
        
        //Check if sorted
        for (int i = 1; i < len; i++) {
            if (sorter.arr[i-1] > sorter.arr[i]) {
                System.out.println("FAILED");
            }
        }
        //Print final array
        for (int i = 0; i < len; i++) {
            System.out.print(sorter.arr[i]+", ");
        }
        System.out.println();
        System.out.println("comparisons: "+sorter.comparisons);
        System.out.println("writes: "+sorter.writes);
    }
}
