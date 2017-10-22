package com.cleanonfire.processor.processing.data.orm;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.Date;

/**
 * Created by heitorgianastasio on 06/10/17.
 */

public final class ORMSupportedClassNames {

    private ORMSupportedClassNames() {}

    public static ClassName DATE_CLASSNAME = ClassName.get(Date.class);
    public static ClassName STRING_CLASSNAME = ClassName.get(String.class);
    public static ClassName INTEGER_CLASSNAME = ClassName.get(Integer.class);
    public static ClassName FLOAT_CLASSNAME = ClassName.get(Float.class);
    public static ClassName DOUBLE_CLASSNAME = ClassName.get(Double.class);
    public static ClassName LONG_CLASSNAME = ClassName.get(Long.class);
    public static TypeName BYTEARRAY_CLASSNAME = TypeName.get(byte[].class);

}
