package com.cleanonfire.processor.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.cleanonfire.processor.utils.StringUtils.firstLetterToUp;

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
        return new StringBuilder("get").append(firstLetterToUp(element.getSimpleName())).toString();

    }

    public static String getSetterName(Element element) {
        return new StringBuilder("set").append(firstLetterToUp(element.getSimpleName())).toString();
    }

    public static List<ExecutableElement> getPublicMethods(TypeElement typeElement) {
        List<ExecutableElement> result = new ArrayList<>();
        for (Element element : typeElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD && !element.getModifiers().contains(Modifier.PRIVATE))
                result.add((ExecutableElement) element);
        }
        return result;
    }

}
