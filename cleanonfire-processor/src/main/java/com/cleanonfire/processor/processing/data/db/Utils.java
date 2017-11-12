package com.cleanonfire.processor.processing.data.db;

import com.cleanonfire.annotations.data.db.FieldInfo;
import com.cleanonfire.annotations.data.db.ForeignKey;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.cleanonfire.processor.utils.StringUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.Set;
import java.util.function.Function;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

/**
 * Created by heitorgianastasio on 03/11/17.
 */

public class Utils {
    public static Function<VariableElement, String> fieldToColumnName = element -> {
        FieldInfo fieldInfo = element.getAnnotation(FieldInfo.class);
        if (fieldInfo != null && !fieldInfo.columnName().isEmpty())
            return fieldInfo.columnName();
        else return element.getSimpleName().toString();
    };

    public static String assignField(Element element, String variable, String assignment) {
        if (element.getModifiers().contains(Modifier.PRIVATE)) {
            String setterName = ProcessingUtils.getSetterName(element);
            return String.format("%s.%s(%s)", variable, setterName, assignment);
        } else {
            return String.format("%s.%s = %s", variable, element.getSimpleName().toString(), assignment);
        }
    }

    public static String retrieveField(Element element, String variable) {
        if (element.getModifiers().contains(Modifier.PRIVATE)) {
            String getterName = ProcessingUtils.getGetterName(element);
            return String.format("%s.%s()", variable, getterName);
        } else {
            return String.format("%s.%s", variable, element.getSimpleName().toString());
        }
    }

    public static MethodSpec buildGetterMethod(VariableElement element) {
        return MethodSpec.methodBuilder("get".concat(StringUtils.firstLetterToUp(element.getSimpleName())))
                .returns(TypeName.get(element.asType()))
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return this.$L", element.getSimpleName())
                .build();
    }

    public static ClassName getForeignKeyDAOClassName(ForeignKey foreignKey) {
        if (foreignKey == null) return null;
        try {
            return ClassName.get(foreignKey.target().getPackage().getName(), foreignKey.target().getSimpleName() + "CleanDAO");
        } catch (MirroredTypeException e) {
            Element element = ProcessingUtils.getTypeUtils().asElement(e.getTypeMirror());
            return ClassName.get(ProcessingUtils.getElementUtils().getPackageOf(element).getQualifiedName().toString(), element.getSimpleName().toString() + "CleanDAO");
        }
    }

    public static TypeName getForeignKeyTypeName(ForeignKey foreignKey) {
        if (foreignKey == null) return null;
        return TypeName.get(getForeignKeyTypeMirror(foreignKey));
    }

    public static TypeMirror getForeignKeyTypeMirror(ForeignKey foreignKey) {
        if (foreignKey == null) return null;
        try {
            foreignKey.target();
        } catch (MirroredTypeException e) {
            return e.getTypeMirror();
        }
        return null;
    }

    public static TypeElement getForeignKeyTypeElement(ForeignKey foreignKey) {
        if (foreignKey == null) return null;
        return (TypeElement) ProcessingUtils.getTypeUtils().asElement(getForeignKeyTypeMirror(foreignKey));
    }





}
