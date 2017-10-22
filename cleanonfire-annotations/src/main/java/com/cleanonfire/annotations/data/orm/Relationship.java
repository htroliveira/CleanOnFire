package com.cleanonfire.annotations.data.orm;

/**
 * Created by heitorgianastasio on 07/10/17.
 */

public @interface Relationship {
    Type relation();
    Class with();
    boolean lazyLoad() default false;

    enum Type {
        ONE_TO_MANY,
        MANY_TO_MANY,
        ONE_TO_ONE,
        NONE
    }
}
