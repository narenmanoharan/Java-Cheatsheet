package Datastructures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Naren on 5/19/17.
 */
public class LinkedListDemo {


    public static void main(String[] args) {

        List<Integer> integerList = new LinkedList<>();

        integerList.add(10);
        integerList.add(80);
        integerList.add(90);
        integerList.add(40);
        integerList.add(50);
        integerList.add(60);

        integerList.forEach(System.out::println);

        integerList.sort(Integer::compareTo);

        System.out.println();

        Iterator<Integer> integerIterator = integerList.iterator();

        while (integerIterator.hasNext()) {
            int x = integerIterator.next();
            if(x == 50) integerIterator.remove();
        }

//        integerList.removeIf(x -> x == 50);


        integerList.remove(integerList.size()-1);

        integerList.forEach(System.out::println);


    }

}
