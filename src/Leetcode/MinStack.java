package Leetcode;

import java.util.Stack;

/**
 * Created by Naren on 5/23/17.
 */
class MinStack {

  private int min = Integer.MAX_VALUE;
  private Stack<Integer> stack = new Stack<>();

  public void push(int x) {
    // only push the old minimum value when the current
    // minimum value changes after pushing the new value x
    if(x <= min){
      stack.push(min);
      min=x;
    }
    stack.push(x);
  }

  public void pop() {
    // if pop operation could result in the changing of the current minimum value,
    // pop twice and change the current minimum value to the last minimum value.
    if(stack.pop() == min)
      min=stack.pop();
  }

  public int top() {
    return stack.peek();
  }

  public int getMin() {
    return min;
  }
}