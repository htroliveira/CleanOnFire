package com.cleanonfire.annotations.data.db;

/**
 * Created by heitorgianastasio on 27/10/17.
 */

public @interface ForeignKey {
    Class target();

    String name() default "";

    UpdatePolicy update() default UpdatePolicy.NONE;
    DeletePolicy delete() default DeletePolicy.NONE;

    enum DeletePolicy {
        ON_DELETE_CASCADE,
        ON_DELETE_RESTRICT,
        NONE
    }

    enum UpdatePolicy {
        ON_UPDATE_RESTRICT,
        ON_UPDATE_CASCADE,
        NONE
    }
}
