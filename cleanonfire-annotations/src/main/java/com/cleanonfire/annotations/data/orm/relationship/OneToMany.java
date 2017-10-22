package com.cleanonfire.annotations.data.orm.relationship;

/**
 * Created by heitorgianastasio on 07/10/17.
 */

public @interface OneToMany {
    Class value();
    boolean lazyLoading() default false;

}
