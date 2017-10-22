package com.cleanonfire.processor.core;

import com.squareup.javapoet.JavaFile;

import javax.lang.model.element.Element;

/**
 * Created by heitorgianastasio on 10/10/17.
 */

public interface ClassBuilder {

     JavaFile build();
}
