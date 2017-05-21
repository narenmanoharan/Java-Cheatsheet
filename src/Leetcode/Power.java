package Leetcode;

/**
 * Created by Naren on 5/21/17.
 */
public class Power {

  private static double myPow(double num, int pow) {

    if(pow == 0) {
      return 1;
    }

    if(Double.isInfinite(num)) {
      return 0;
    }

    if(pow < 0) {
      pow = -pow;
      num = 1 / num;
    }

    if(pow % 2 == 0) {
      return myPow(num * num, pow / 2);
    } else {
      return num * myPow(num * num, pow / 2);
    }

  }


  public static void main(String[] args) {

    System.out.println(myPow(10, 2));
    System.out.println(myPow(5, 3));
    System.out.println(myPow(4, 4));
    System.out.println(myPow(0, 2));
    System.out.println(myPow(16, 0));


  }
}
