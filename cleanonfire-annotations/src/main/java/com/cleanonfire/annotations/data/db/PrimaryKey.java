package com.cleanonfire.annotations.data.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by heitorgianastasio on 02/10/17.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface PrimaryKey {
    boolean autoincrement() default false;
}
