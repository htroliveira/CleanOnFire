package com.cleanonfire.processor.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public class ArrayUtil {


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

    private static <T> Object[] castToArray(Object src){
        if (src instanceof Object[])
            return (Object[])src;
        else if(src instanceof int[])
            return Arrays.stream((int[])src).boxed().toArray(Integer[]::new);
        else if(src instanceof long[])
            return Arrays.stream((long[])src).boxed().toArray(Long[]::new);
        else if(src instanceof double[])
            return Arrays.stream((double[])src).boxed().toArray(Double[]::new);
        return (T[])src;
    }


    public static <T> Set<T> getDuplicates(Collection<T> src){
        return src.stream()
                .filter(t -> Collections.frequency(src,t)>1)
                .collect(Collectors.toSet());
    }

    public static <T> List<T> concat(List<T>... lists){
        return Stream.of(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
