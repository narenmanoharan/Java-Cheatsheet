package Leetcode;

/**
 * Created by Naren on 5/22/17.
 */
public class ClimbStairs {

  private static int climbs(int n) {

    if (n <= 0) return 0;
    if (n == 1) return 1;
    if (n == 2) return 2;


    int one = 2;
    int two = 1;
    int result = 0;

    for (int i = 2; i < n; i++) {
      result = one + two;
      two = one;
      one = result;
    }

    return result;
  }

  public static void main(String[] args) {
    System.out.println(climbs(4));
  }

}
