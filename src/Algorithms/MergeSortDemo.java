package Algorithms;

/**
 * Created by Naren on 5/22/17.
 */
public class MergeSortDemo {


  public static void MergeSort(int[] nums) {
    int leftStart = 0;
    int rightEnd = nums.length - 1;
    int[] temp = new int[nums.length];
    mergeSort(nums, temp, leftStart, rightEnd);
  }

  private static void mergeSort(int[] nums,int[] temp, int leftStart, int rightEnd) {
    if(leftStart >= rightEnd) return;
    int mid = (leftStart + rightEnd) / 2;
    mergeSort(nums, temp, leftStart, mid);
    mergeSort(nums, temp, mid+1, rightEnd);
    merge(nums, temp, leftStart, rightEnd);
  }

  private static void merge(int[] nums, int[] temp, int leftStart, int rightEnd) {
    int leftEnd = (leftStart + rightEnd) / 2;
    int rightStart = leftEnd + 1;
    int size = rightEnd - leftStart + 1;

    int left = leftStart;
    int right = rightStart;
    int index = leftStart;

    while (left <= leftEnd && right <= rightEnd) {

      if(nums[left] <= nums[right]) {
        temp[index] = nums[left];
        left++;
      } else {
        temp[index] = nums[right];
        right++;
      }
      index++;
    }

    System.arraycopy(nums, left, temp, index, leftEnd - left + 1);
    System.arraycopy(nums, right, temp, index, rightEnd - left + 1);
    System.arraycopy(temp, leftStart, nums, rightStart, size);
  }
}
