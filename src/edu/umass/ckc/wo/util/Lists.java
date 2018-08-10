package edu.umass.ckc.wo.util;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * <p> Created by IntelliJ IDEA.
 * User: david
 * Date: Jul 23, 2008
 * Time: 5:01:55 PM
 */
public class Lists {

    public static boolean inList (int pid, List<String> attemptedProbs) {
        for (String id: attemptedProbs)
            if (pid == Integer.parseInt(id))
                return true ;
        return false;
    }


    public static int indexOf (int[] a, int val) {
        for (int i=0;i<a.length;i++)
            if (a[i] == val)
                return i;
        return -1;
    }

    /**
     *
     * @param list
     * @param predicate Test each member of the list to see if it passes the test
     * @param <T>
     * @return Returns true if the predicate is true for some member of the list.
     * e
     */
    public static <T> boolean hasAnElement (Iterable<T> list, Predicate<T> predicate) {
        for (T p : list) {
            if (predicate.test(p)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        List<String> l = Arrays.asList("Buenos Aires", "Córdoba", "La Plata");
        boolean b = hasAnElement(l, p -> p.length() == "Cordoba".length());
        boolean c = hasAnElement(l, p -> p.length() == "xx".length());
        System.out.println("Cordoba is a member: " + b + " and xx is a member: " + c);
    }
}
