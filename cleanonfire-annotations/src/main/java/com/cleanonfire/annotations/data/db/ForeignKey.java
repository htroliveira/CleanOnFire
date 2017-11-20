package com.cleanonfire.annotations.data.db;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by heitorgianastasio on 27/10/17.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
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
