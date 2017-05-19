package Datastructures;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Naren on 5/19/17.
 */
public class QueueDemo {


    public static void main(String[] args) {

        Queue<String> queue = new ArrayDeque<>();

        queue.offer("sup");
        queue.offer("hi");
        queue.offer("hey");
        queue.offer("hello");

        queue.forEach(System.out::print);

        System.out.println(queue.poll());

        System.out.println(queue.peek());

        queue.forEach(System.out::print);

        System.out.println(queue.isEmpty());

        System.out.println(queue.size());
    }

}
