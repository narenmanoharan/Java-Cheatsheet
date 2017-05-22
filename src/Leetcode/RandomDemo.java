package Leetcode;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Naren on 5/21/17.
 */
public class RandomDemo {

  public static void main(String[] args) {

    List random = Collections.unmodifiableList(new Random()
                          .ints(0, 101)
                          .limit(20)
                          .boxed()
                          .collect(Collectors.toList()));

    System.out.println(random);

  }


}
