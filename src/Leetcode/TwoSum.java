package Leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Naren on 5/21/17.
 */
public class TwoSum {


  private static Set<Pair> calculatePairs(List<Integer> nums, int target) {

    Set<Pair> res = new HashSet<>();

    if(nums.size() == 0) {
      return Collections.emptySet();
    }

    HashSet<Integer> integers = new HashSet<>();

    for (Integer n: nums) {
      int val = target - n;
      if(!integers.contains(val)) {
        integers.add(val);
      }
    }

    Iterator<Integer> intIterator = nums.iterator();

    Integer n = null;

    while (intIterator.hasNext()) {
      n = intIterator.next();
      if (integers.contains(n)) {
        Pair newPair = new Pair(n, target - n);
        res.add(newPair);
      }
    }

    return res;
  }

  public static void main(String[] args) {

    Integer[] num = {-10, 4, 1, 2, 3, 3, 3, 5, 15, 23};

    int target = 6;

    List<Integer> nums = Arrays.asList(num);

    Set<Pair> result = calculatePairs(nums, target);

    result.forEach(System.out::println);

  }
}

class Pair {

  private int left;
  private int right;

  public Pair(int left, int right) {
    this.left = left;
    this.right = right;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pair pair = (Pair) o;
    return left == pair.right && right == pair.left || left == pair.left && right == pair.right;
  }

  @Override public int hashCode() {
    int result = left;
    result = 31 * result + right;
    return result;
  }

  @Override public String toString() {
    return "Pair{" + "left=" + left + ", right=" + right + '}';
  }
}