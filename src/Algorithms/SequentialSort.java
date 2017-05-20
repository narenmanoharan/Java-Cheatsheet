package Algorithms;

/**
 * Created by Naren on 5/19/17.
 */
public class SequentialSort {

    private int search(int[] arr, int target) {

        if(arr == null) return -1;

        for(int i =0; i< arr.length; i++) {
            if(arr[i] == target) return i;
        }
        return -1;
    }

    public static void main(String[] args) {

        int[] arr = {1,2,3,4,5,1,34,2};

        SequentialSort sequentialSort = new SequentialSort();

        System.out.println(sequentialSort.search(arr, 5));
        System.out.println(sequentialSort.search(arr, 10));
        System.out.println(sequentialSort.search(arr, 1));

    }
}
