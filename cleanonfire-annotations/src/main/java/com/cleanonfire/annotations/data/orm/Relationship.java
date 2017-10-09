package com.cleanonfire.annotations.data.orm;

/**
 * Created by heitorgianastasio on 07/10/17.
 */

public @interface Relationship {
    RelationType relation();
    Class with();
    enum RelationType{
        ONE_TO_MANY,
        MANY_TO_MANY,
        ONE_TO_ONE,
        NONE
    }
}
