package project;
import java.util.Random;

public class arrayRandomizer {
    //Generates randomly arranged array of the first N non-negative integers
    public static int[] randomArrayGen(int N) {
        Random r = new Random();
        int[] arr = new int[N];
        //Fills array with numbers
        for (int i = 0; i < N; i++) {
            arr[i] = i;
        }
        //Shuffles array randomly
        int temp, rand;
        for (int i = 0; i < N; i++) {
            rand = r.nextInt(N);
            temp = arr[i];
            arr[i] = arr[rand];
            arr[rand] = temp;
        }
        return arr;
    }
}
