package com.cleanonfire.processor.core;

import java.util.Set;

import javax.lang.model.element.Element;

/**
 * Created by heitorgianastasio on 21/09/17.
 */

public abstract class GenericAnnotationProcessor<T> {
    protected Class<T> annotationClass;


    public GenericAnnotationProcessor(Class<T> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public abstract void process(Set<? extends Element> elements) throws ProcessingException;




}
