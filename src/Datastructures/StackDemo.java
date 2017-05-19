package Datastructures;

import java.util.Stack;

/**
 * Created by Naren on 5/19/17.
 */
public class StackDemo {

    public static void main(String[] args) {

        Stack<Integer> stack = new Stack<>();
        
        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.push(40);
        
        stack.forEach(System.out::print);

        System.out.println(stack.pop());

        System.out.println(stack.peek());

        System.out.println(stack.empty());

        System.out.println(stack.isEmpty());

        System.out.println(stack.size());
        
    }
    
}
