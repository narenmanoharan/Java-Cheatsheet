package Leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naren on 5/22/17.
 */
public class FizzBuzz {

  private static List<CharSequence> FizzBuzzDemo(int n) {

    List<CharSequence> result = new ArrayList<>();

    for(int i=1; i <=n ; i++) {

      if (i % 3 == 0 && i % 5 == 0) {
        result.add("FizzBuzz");
      }
      else if(i % 3 == 0) {
        result.add("Fizz");
      }
      else if(i % 5 == 0) {
        result.add("Buzz");
      }
      else {
        result.add(Integer.toString(i));
      }
    }

    return result;
  }

  public static void main(String[] args) {

    System.out.println(FizzBuzzDemo(35));

  }

}
