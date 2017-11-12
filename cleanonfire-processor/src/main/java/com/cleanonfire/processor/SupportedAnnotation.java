package com.cleanonfire.processor;

import com.cleanonfire.annotations.data.db.Database;
import com.cleanonfire.processor.core.GenericAnnotationProcessor;
import com.cleanonfire.processor.processing.data.db.DBProcessor;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by heitorgianastasio on 21/09/17.
 */

public enum SupportedAnnotation {

    ORM_ENTITY(Database.class, new DBProcessor(Database.class));

    SupportedAnnotation(Class<? extends Annotation> supportedAnnotation, GenericAnnotationProcessor processor) {
        this.supportedAnnotation = supportedAnnotation;
        this.processor = processor;

    }

    private Class<? extends Annotation> supportedAnnotation;
    private GenericAnnotationProcessor processor;

    public Class<? extends Annotation> getSupportedAnnotation() {
        return supportedAnnotation;
    }

    public GenericAnnotationProcessor getProcessor() {
        return processor;
    }


    public static Set<String> getSupportedAnnotations(){
        Set<String> stringSet = new HashSet<>();
        for (SupportedAnnotation annotation : values()) {
            stringSet.add(annotation.getSupportedAnnotation().getCanonicalName());
        }
        return stringSet;
    }

}
