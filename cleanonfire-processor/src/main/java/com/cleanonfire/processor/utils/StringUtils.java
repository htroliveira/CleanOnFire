package com.cleanonfire.processor.utils;

/**
 * Created by heitorgianastasio on 21/10/17.
 */

public final class StringUtils {
    private StringUtils(){}

    public static String firstLetterToUp(CharSequence sequence){
        String src = sequence.toString();
        return src.substring(0,1).toUpperCase()+src.substring(1,src.length());
    }
}
