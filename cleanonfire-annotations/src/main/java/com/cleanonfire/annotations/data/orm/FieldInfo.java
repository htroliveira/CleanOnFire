package com.cleanonfire.annotations.data.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

/**
 * Created by heitorgianastasio on 07/10/17.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface FieldInfo {
    boolean unique() default false;
    String columnName() default "";
    boolean nullable() default true;

}
