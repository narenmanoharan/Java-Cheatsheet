package Leetcode;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Naren on 5/21/17.
 */
public class ConcatStringStream {

  public static void main(String[] args) {

    String[] names = {"Narendra", "Kumar", "Manoharan"};

    String res = Arrays.stream(names)
        .collect(Collectors.joining(" "));

    System.out.println(res);

  }
}
