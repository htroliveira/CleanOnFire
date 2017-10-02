package com.cleanonfire.processor.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public class ProcessingException extends RuntimeException {
    Class<? extends Annotation> annotationClass;
    Element element;
    List<String> causes = new ArrayList<>();


    public ProcessingException(Class<? extends Annotation> annotationClass, Element element, List<String> causes) {
        this.annotationClass = annotationClass;
        this.element = element;
        this.causes = causes != null ? causes : this.causes;
    }


    public ProcessingException(Throwable throwable, Class<Annotation> annotationClass, Element element, List<String> causes) {
        super(throwable);
        this.annotationClass = annotationClass;
        this.element = element;
        this.causes = causes != null ? causes : this.causes;

    }

    public String getCompilerMessage() {
        StringBuilder stringBuilder = new StringBuilder()
                        .append(String.format("The %s %s ", element.getKind().toString().toLowerCase(), element.getSimpleName()))
                        .append(String.format("annotated with @%s ", annotationClass.getSimpleName()))
                        .append("cannot be processed. ");

        if (!causes.isEmpty()) {
            stringBuilder.append("Because: ");
            for (String cause : causes) {
                stringBuilder.append(String.format("%s; ", cause));
            }
        }

        return stringBuilder.toString();

    }
}
