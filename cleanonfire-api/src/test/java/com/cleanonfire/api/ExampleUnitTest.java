package com.cleanonfire.api;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testPlain() throws Exception{
        Object[] src = {"1","2",3,4,new Object[]{5,"as",3,new Object[]{5,"as",3},new Object[]{5,"as",3}},new Object[]{5,"as",3},new String[]{"is","as","os"},new int[]{56,57,58},34,"osi",new Date()};
        Object[] result = plain(src);


    }

    public static <T>Object[] plain(T... objects){
        List<Object> list = new ArrayList<>();
        for (T t : objects) {
            if (t.getClass().isArray())
                list.addAll(Arrays.asList(plain(castToArray(t))));
            else
                list.add(t);
        }
        return list.toArray(new Object[list.size()]);
    }

    public static <T> Object[] castToArray(Object src){
        if (src instanceof Object[])
            return (Object[])src;
        else if(src instanceof int[])
            return Arrays.stream((int[])src).boxed().toArray(Integer[]::new);
        else if(src instanceof long[])
            return Arrays.stream((long[])src).boxed().toArray(Integer[]::new);
        else if(src instanceof double[])
            return Arrays.stream((double[])src).boxed().toArray(Integer[]::new);
        return (T[])src;
    }
}