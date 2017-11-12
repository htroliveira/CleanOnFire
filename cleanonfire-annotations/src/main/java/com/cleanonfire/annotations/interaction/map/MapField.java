package com.cleanonfire.annotations.interaction.map;

/**
 * Created by heitorgianastasio on 04/11/17.
 */

public @interface MapField {
    String field();
    Class conversor() default void.class;

}
