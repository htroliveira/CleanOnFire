package com.cleanonfire.processor.utils;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public final class ProcessingUtils {
    private static ProcessingEnvironment environment;

    private ProcessingUtils() {}

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


}
