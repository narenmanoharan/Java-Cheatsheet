package Datastructures;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Naren on 5/20/17.
 */
public class ArrayDemo {



    public static void main(String[] args) {

        int[] arr1 = new int[10];
        arr1[0] = 10;
        int[] arr2 = {1,2, 2, 3, 5,6 ,3,4};

        System.out.println(arr2.length);

        Arrays.stream(arr2)
                .distinct()
                .filter(p -> p > 2)
                .sorted()
                .forEach(System.out::println);

        Arrays.sort(arr1);

        Arrays.fill(arr1, 0);

        List arrList = Arrays.asList(arr2);

        String strArr = Arrays.toString(arr1);

        System.out.println(strArr);

        String name = "Naren";

        System.out.println(name.charAt(0));

        System.out.println(name.toCharArray());

    }

}
