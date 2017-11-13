package com.cleanonfire.annotations.presentation.adapter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by heitorgianastasio on 12/11/17.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface Bind {
    int layoutId();
    Class view();
    Class binder();
    boolean clickable() default false;
    boolean longClickable() default false;
}
