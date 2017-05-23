package Leetcode;

import java.util.Arrays;

/**
 * Created by Naren on 5/22/17.
 */
public class MoveZeroesDemo {

  private void MoveZeroes(int[] nums) {

    int start = 0;
    int end = nums.length - 1;

    while (start<end) {

      if(nums[start] == 0 && nums[end] != 0) {
        swap(nums, start, end);
        end--;
        start++;
      }

      if(nums[start] == 0 && nums[end] == 0) {
        end--;
      } else {
        start++;
      }

    }

  }

  private void swap(int[] nums, int start, int end) {
    int temp = nums[start];
    nums[start] = nums[end];
    nums[end] = temp;
  }

  public static void main(String[] args) {

    MoveZeroesDemo mv = new MoveZeroesDemo();

    int[] nums = {0,2,3,4,0,3,2};

    int[] nums_ = {0,0,0,0,0,0,1,0,0,0,};

    mv.MoveZeroes(nums);

    System.out.println(Arrays.toString(nums));

    mv.MoveZeroes(nums_);

    System.out.println(Arrays.toString(nums_));

    int[] nums_1 = {1,0,0,0,0,0,1,0,0,0,};

    mv.MoveZeroes(nums_1);

    System.out.println(Arrays.toString(nums_1));

    int[] nums_2 = {0,0,0,0,0,0,0,0,0,0,1};

    mv.MoveZeroes(nums_2);

    System.out.println(Arrays.toString(nums_2));

  }
}
