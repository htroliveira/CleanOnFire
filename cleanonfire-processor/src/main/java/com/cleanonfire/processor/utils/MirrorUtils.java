package com.cleanonfire.processor.utils;

import javax.lang.model.type.MirroredTypeException;

/**
 * Created by heitorgianastasio on 03/10/17.
 */

public final class MirrorUtils {
    private MirrorUtils(){}

    public static String getName(Class clazz){
        try {
            return clazz.getCanonicalName();
        } catch (MirroredTypeException mte){
            return mte.getTypeMirror().toString();
        }
    }
}
