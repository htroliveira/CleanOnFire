package com.cleanonfire.processor.processing.data.db.classbuilders;

import com.cleanonfire.processor.core.ClassBuilder;
import com.cleanonfire.processor.utils.StringUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.CONTEXT;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.ABSTRACT_CLEAN_ON_FIRE_DB;

/**
 * Created by heitorgianastasio on 03/11/17.
 */

public class CleanOnFireDBClassBuilder implements ClassBuilder {
    private static final String CLASS_NAME = "CleanOnFireDB";
    private static final String PACKAGE_NAME = "com.generated.cleanonfire.db";
    private static final ClassName SELF_TYPE = ClassName.get(PACKAGE_NAME, CLASS_NAME);

    List<ClassName> daoNames = new ArrayList<>();
    String sqlCreateScript;
    String dbName;
    int version;


    @Override
    public JavaFile build() {
        return JavaFile.builder(PACKAGE_NAME, buildCleanOnFireDB()).build();
    }

    private TypeSpec buildCleanOnFireDB() {
        return TypeSpec
                .classBuilder(CLASS_NAME)
                .addModifiers(Modifier.PUBLIC)
                .superclass(ABSTRACT_CLEAN_ON_FIRE_DB)
                .addMethods(buildGetDaoMethods())
                .addMethod(buildGetSqlCreateScriptMethod())
                .addMethod(buildInitMethod())
                .addMethod(buildGetMethod())
                .addMethod(buildGetDBNameMethod())
                .addField(buildInstanceField())
                .addMethod(buildConstructor())
                .addMethod(buildGetVersion())
                .build();


    }


    MethodSpec buildConstructor() {
        return MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CONTEXT, "context")
                .addStatement("super(context)")
                .build();
    }

    FieldSpec buildInstanceField() {
        return FieldSpec.builder(SELF_TYPE, "instance", Modifier.PRIVATE, Modifier.STATIC).build();
    }


    MethodSpec buildGetVersion() {
        return MethodSpec
                .methodBuilder("getVersion")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addStatement("return $L", version)
                .build();
    }

    MethodSpec buildInitMethod() {
        return MethodSpec
                .methodBuilder("init")
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(CONTEXT, "context")
                .addStatement("instance = new $T(context)", SELF_TYPE)
                .build();
    }

    MethodSpec buildGetMethod() {
        return MethodSpec
                .methodBuilder("get")
                .returns(SELF_TYPE)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addStatement("return instance")
                .build();
    }

    MethodSpec buildGetSqlCreateScriptMethod() {
        return MethodSpec
                .methodBuilder("sqlCreateScript")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(String.class)
                .addStatement("return $S", sqlCreateScript)
                .build();
    }

    MethodSpec buildGetDBNameMethod() {
        MethodSpec.Builder builder = MethodSpec
                .methodBuilder("getDBName")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(String.class);
        if (dbName != null && !dbName.trim().isEmpty()) {
            builder.addStatement("return $S", dbName);
        } else {
            builder.addStatement("return super.getDBName()");
        }
        return builder.build();
    }

    List<MethodSpec> buildGetDaoMethods() {
        List<MethodSpec> specs = new ArrayList<>();
        for (ClassName daoName : daoNames) {
            MethodSpec spec = MethodSpec
                    .methodBuilder("get" + StringUtils.firstLetterToUp(daoName.simpleName()))
                    .returns(daoName)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return new $T(cleanHelper)", daoName)
                    .build();
            specs.add(spec);
        }
        return specs;
    }

    public void setSqlCreateScript(String sqlCreateScript) {
        this.sqlCreateScript = sqlCreateScript;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void addDaoClassName(ClassName className) {
        daoNames.add(className);
    }

}
