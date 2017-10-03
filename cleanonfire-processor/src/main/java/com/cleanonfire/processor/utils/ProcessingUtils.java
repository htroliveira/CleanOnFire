package com.cleanonfire.processor.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public final class ProcessingUtils {
    private static ProcessingEnvironment environment;

    private ProcessingUtils() {
    }

    public static Messager getMessager() {
        return environment.getMessager();
    }

    public static Elements getElementUtils() {
        return environment.getElementUtils();
    }

    public static Types getTypeUtils() {
        return environment.getTypeUtils();
    }

    public static Filer getFiler() {
        return environment.getFiler();
    }

    public static void init(ProcessingEnvironment pe) {
        environment = pe;
    }

    public static String getGetterName(Element element) {
        String elementName = element.getSimpleName().toString();
        StringBuilder builder = new StringBuilder("get");
        builder.append(elementName.substring(0, 1).toUpperCase());
        builder.append(elementName.substring(1,elementName.length()));
        return builder.toString();
    }

    public static String getSetterName(Element element) {
        String elementName = element.getSimpleName().toString();
        StringBuilder builder = new StringBuilder("set");
        builder.append(elementName.substring(0, 1).toUpperCase());
        builder.append(elementName.substring(1,elementName.length()));
        return builder.toString();
    }

    public static List<String> getMethodsNames(TypeElement typeElement){
        List<String> result = new ArrayList<>();
        for (Element element : typeElement.getEnclosedElements()) {
            if(element.getKind()== ElementKind.METHOD)
                result.add(element.getSimpleName().toString());
        }
        return result;
    }

}
