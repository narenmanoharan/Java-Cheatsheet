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

public class MinStack {
  long min;
  Stack<Long> stack;

  public MinStack(){
    stack=new Stack<>();
  }

  public void push(int x) {
    if (stack.isEmpty()){
      stack.push(0L);
      min=x;
    }else{
      stack.push(x-min);//Could be negative if min value needs to change
      if (x<min) min=x;
    }
  }

  public void pop() {
    if (stack.isEmpty()) return;

    long pop=stack.pop();

    if (pop<0)  min=min-pop;//If negative, increase the min value

  }

  public int top() {
    long top=stack.peek();
    if (top>0){
      return (int)(top+min);
    }else{
      return (int)(min);
    }
  }

  public int getMin() {
    return (int)min;
  }
}