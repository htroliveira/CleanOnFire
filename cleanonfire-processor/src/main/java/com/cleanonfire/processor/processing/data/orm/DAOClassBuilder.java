package com.cleanonfire.processor.processing.data.orm;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.annotations.data.orm.IgnoreField;
import com.cleanonfire.annotations.data.orm.PrimaryKey;
import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.CURSOR;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.BASE_DAO;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.CLEAN_SQLITE_HELPER;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public class DAOClassBuilder {



    public void buildDao(TypeElement element) {
        Entity annotation = element.getAnnotation(Entity.class);
        String tableName = annotation.tableName().isEmpty() ? element.getSimpleName().toString().toLowerCase() : annotation.tableName();
        String idColumnName = getIdentificationColumnName(element);
        TypeSpec daoSpec = buildDaoClass(
                element,
                buildGetTableName(tableName),
                buildGetIdentificationColumnName(idColumnName),
                buildParseFromCursor(element),
                buildDaoConstructor()
        );
        try {
            String packageName = ProcessingUtils.getElementUtils().getPackageOf(element).getQualifiedName().toString();
            JavaFile.builder(packageName, daoSpec)
                    .build()
                    .writeTo(ProcessingUtils.getFiler());
        } catch (IOException e) {
            throw new ProcessingException(Entity.class, element, Arrays.asList());
        }

    }

    private MethodSpec buildGetTableName(String tableName) {
        return MethodSpec.methodBuilder("getTableName")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(ClassName.get(String.class))
                .addStatement("return $S", tableName)
                .build();
    }


    private MethodSpec buildGetIdentificationColumnName(String idColumnName) {
        return MethodSpec.methodBuilder("getIdentificationColumnName")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(ClassName.get(String.class))
                .addStatement("return $S", idColumnName)
                .build();
    }

    private MethodSpec buildDaoConstructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CLEAN_SQLITE_HELPER, "helper")
                .addStatement("super(helper)")
                .build();
    }

    private MethodSpec buildParseFromCursor(TypeElement typeElement) {
        ClassName elementClassName = ClassName.bestGuess(typeElement.getQualifiedName().toString());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("parseFromCursor")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(elementClassName)
                .addParameter(CURSOR, "cursor")
                .addStatement("$T result = new $T()", elementClassName, elementClassName);
        for (Element element : typeElement.getEnclosedElements()) {
            if (element.getKind().isField() && element.getAnnotation(IgnoreField.class) == null) {
                String cursorMethod = getCursorMethod(element.asType());
                if (element.getModifiers().contains(Modifier.PROTECTED) || element.getModifiers().contains(Modifier.PRIVATE)) {
                    String setterName = ProcessingUtils.getSetterName(element);
                    builder.addStatement("result.$L(cursor.$L(cursor.getColumnIndex($S)))", setterName, cursorMethod, element.getSimpleName().toString());
                } else {
                    builder.addStatement("result.$L = cursor.$L(cursor.getColumnIndex($S))", element.getSimpleName().toString(), cursorMethod, element.getSimpleName().toString());
                }
            }
        }
        return builder.addStatement("return result").build();
    }

    private TypeSpec buildDaoClass(TypeElement element, MethodSpec... methodSpecs) {
        return TypeSpec.classBuilder(element.getSimpleName().toString().concat("CleanDAO"))
                .superclass(ParameterizedTypeName.get(BASE_DAO, ClassName.bestGuess(element.getQualifiedName().toString())))
                .addModifiers(Modifier.PUBLIC)
                .addMethods(Arrays.asList(methodSpecs))
                .build();
    }
    private String getCursorMethod(TypeMirror typeMirror) {
        if (typeMirror.getKind().equals(TypeKind.INT) || typeMirror.toString().equals(Integer.class.getTypeName()))
            return "getInt";
        else if (typeMirror.getKind().equals(TypeKind.FLOAT) || typeMirror.toString().equals(Float.class.getTypeName()))
            return "getFloat";
        else if (typeMirror.getKind().equals(TypeKind.DOUBLE) || typeMirror.toString().equals(Double.class.getTypeName()))
            return "getDouble";
        else if (typeMirror.getKind().equals(TypeKind.LONG) || typeMirror.toString().equals(Date.class.getTypeName()) || typeMirror.toString().equals(Long.class.getTypeName()))
            return "getLong";
        else if (typeMirror.toString().equals(String.class.getTypeName()))
            return "getString";
        else return "getBlob";
    }

    private String getIdentificationColumnName(TypeElement typeElement) {
        for (Element element : typeElement.getEnclosedElements()) {
            if (element.getAnnotation(PrimaryKey.class) != null && element.getKind().isField())
                return element.getSimpleName().toString();
        }
        return "_id";
    }
}
