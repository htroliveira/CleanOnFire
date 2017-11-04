package com.cleanonfire.processor.utils;


import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by heitorgianastasio on 26/10/17.
 */

public final class Utils {
    private Utils() {
    }

    public static TypeMirror getTypeFromListOrArray(Element element) {
        Types typeUtils = ProcessingUtils.getTypeUtils();
        Elements elementUtils = ProcessingUtils.getElementUtils();

        TypeElement listTypeElement = elementUtils.getTypeElement(List.class.getCanonicalName());
        TypeMirror listTypeMirror = typeUtils.getDeclaredType(listTypeElement, typeUtils.getWildcardType(null, null));
        if (typeUtils.isSubtype(element.asType(), listTypeMirror)) {
            DeclaredType declaredType = ((DeclaredType) element.asType());
            return declaredType.getTypeArguments().get(0);
        } else if (element.asType().getKind().equals(TypeKind.ARRAY)) {
            return ((ArrayType) element.asType()).getComponentType();
        }
        return element.asType();
    }


}
