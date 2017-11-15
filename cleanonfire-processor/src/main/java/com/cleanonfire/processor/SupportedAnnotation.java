package com.cleanonfire.processor;

import com.cleanonfire.annotations.data.db.Database;
import com.cleanonfire.annotations.presentation.adapter.VisualizationModel;
import com.cleanonfire.processor.core.GenericAnnotationProcessor;
import com.cleanonfire.processor.processing.data.db.DBProcessor;
import com.cleanonfire.processor.processing.presentation.adapter.VisualizationModelProcessor;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by heitorgianastasio on 21/09/17.
 */

public enum SupportedAnnotation {

    DATABASE(Database.class, new DBProcessor(Database.class)),
    VISUALIZATION_MODEL(VisualizationModel.class,new VisualizationModelProcessor(VisualizationModel.class));

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
