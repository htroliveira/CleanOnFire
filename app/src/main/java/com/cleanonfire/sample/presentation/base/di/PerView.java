package com.cleanonfire.sample.presentation.base.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;


/**
 * Created by heitorgianastasio on 13/11/17.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerView {
}
