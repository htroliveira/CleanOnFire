package com.cleanonfire.annotations.presentation.adapter;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public @interface Bind {
    int layoutId();
    Class view();
    Class binder();
    boolean clickable() default false;
    boolean longClickable() default false;
}
