package com.cleanonfire.processor.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heitorgianastasio on 06/10/17.
 */

public final class Lists {
    private Lists(){}

    public static <I,O> List<O> transform(List<I> list, Mapper<I,O> mapper){
        if(list==null) return null;
        ArrayList<O> output = new ArrayList<>(list.size());
        for (I i : list){
            output.add(mapper.map(i));
        }
        return output;
    }

    public static <I> List<I> filter(List<I> list, Filter<I> filter){
        if(list==null) return null;
        ArrayList<I> output = new ArrayList<>(list.size());
        for (I i : list){
            try {
                if (filter.filter(i))
                    output.add(i);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return output;
    }




    public interface Mapper<I,O>{
        O map(I i);
    }
    public interface Filter<I>{
        boolean filter(I i);
    }
}
