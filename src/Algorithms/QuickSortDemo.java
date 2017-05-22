package Algorithms;

import java.util.Arrays;

/**
 * Created by Naren on 5/22/17.
 */
public class QuickSortDemo {

  public static void quickSort(int[] arr) {
    int left = 0;
    int right = arr.length - 1;

    quickSort(arr, left, right);
  }

  private static void quickSort(int[] arr, int left, int right) {

    if(left >= right) return;
    int mid = (left+right) / 2;
    int pivot =  arr[mid];

    int pIndex = partition(arr, left, right, pivot);
    quickSort(arr, left, pIndex-1);
    quickSort(arr, pIndex, right);
  }

  private static int partition(int[] arr, int left, int right, int pivot) {

    while (left <= right) {

      while (arr[left] < pivot) {
        left++;
      }

      while (arr[right] > pivot) {
        right--;
      }

      if(left <= right) {
        swap(arr, left, right);
        left++;
        right--;
      }
    }
    return left;
  }

  private static void swap(int[] arr, int left, int right) {
    int temp = arr[left];
    arr[left] = arr[right];
    arr[right] = temp;
  }

  public static void main(String[] args) {

    int[] arr = {6,3,2,6,8,31,3,62,1,1,8};

    quickSort(arr);

    System.out.println(Arrays.toString(arr));

  }
}
