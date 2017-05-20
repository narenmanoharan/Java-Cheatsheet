package Algorithms;

import java.util.Arrays;

/**
 * Created by Naren on 5/19/17.
 */
public class BinarySearch {

    private int search(int[] arr, int num) {
        return binarySearchUtil(arr, num, 0, arr.length - 1);
    }

    private int binarySearchUtil(int[] arr, int num, int low, int high) {

        if(low > high){
            return -1;
        }

        int mid = low + (high - low) / 2;
        if(arr[mid] > num) {
            return binarySearchUtil(arr, num, low, mid - 1);
        } else if(arr[mid] < num) {
            return binarySearchUtil(arr, num, mid + 1, high);
        } else {
            return mid;
        }
    }

    public static void main(String[] args) {

        BinarySearch search = new BinarySearch();

        int[] arr = {1,2,3,4,5,2,3,5,1,2,43,3};

        Arrays.sort(arr);

        System.out.print(search.search(arr, 43));
        System.out.print(search.search(arr, 10));
        System.out.print(search.search(arr, 2));
        System.out.print(search.search(arr, 3));
    }
}
