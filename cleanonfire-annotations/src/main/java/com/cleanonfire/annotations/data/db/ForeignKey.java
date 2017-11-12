package com.cleanonfire.annotations.data.db;

/**
 * Created by heitorgianastasio on 27/10/17.
 */

public @interface ForeignKey {
    Class target();

    String name() default "";

    ForeignKeyPolicy update() default ForeignKeyPolicy.NO_ACTION;
    ForeignKeyPolicy delete() default ForeignKeyPolicy.NO_ACTION;


    enum ForeignKeyPolicy {
        RESTRICT,
        CASCADE,
        NO_ACTION,
        SET_NULL,
        SET_DEFAULT;

        public String toSQL(){
            return name().replace("_"," ");
        }
    }
}
