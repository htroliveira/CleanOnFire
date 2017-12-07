package com.cleanonfire.processor.processing.data.db.classbuilders;

import com.cleanonfire.annotations.data.db.Migrate;
import com.cleanonfire.processor.core.ClassBuilder;
import com.cleanonfire.processor.processing.data.db.DBClassBundle;
import com.cleanonfire.processor.utils.StringUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.CONTEXT;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.ABSTRACT_CLEAN_ON_FIRE_DB;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.CLEAN_ON_FIRE_DB;

/**
 * Created by heitorgianastasio on 03/11/17.
 */

public class CleanOnFireDBClassBuilder implements ClassBuilder {

    DBClassBundle dbClassBundle;

    public CleanOnFireDBClassBuilder(DBClassBundle dbClassBundle) {
        this.dbClassBundle = dbClassBundle;
    }

    @Override
    public JavaFile build() {
        return JavaFile.builder(CLEAN_ON_FIRE_DB.packageName(), buildCleanOnFireDB()).build();
    }

    private TypeSpec buildCleanOnFireDB() {
        return TypeSpec
                .classBuilder(CLEAN_ON_FIRE_DB)
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
        MethodSpec.Builder builder = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CONTEXT, "context")
                .addStatement("super(context)");
        for (VariableElement element : dbClassBundle.getMigrations()) {
            Migrate migrate = element.getAnnotation(Migrate.class);
            builder.addStatement(
                    "addMigration($T.$L,$L,$L)",
                    dbClassBundle.getMainElement().asType(),
                    element.getSimpleName(),
                    migrate.fromVersion(),
                    migrate.toVersion());

        }
        return builder.build();
    }

    FieldSpec buildInstanceField() {
        return FieldSpec.builder(CLEAN_ON_FIRE_DB, "instance", Modifier.PRIVATE, Modifier.STATIC).build();
    }


    MethodSpec buildGetVersion() {
        return MethodSpec
                .methodBuilder("getVersion")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addStatement("return $L", dbClassBundle.getDatabase().version())
                .build();
    }

    MethodSpec buildInitMethod() {
        return MethodSpec
                .methodBuilder("init")
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(CONTEXT, "context")
                .addStatement("instance = new $T(context)", CLEAN_ON_FIRE_DB)
                .build();
    }

    MethodSpec buildGetMethod() {
        return MethodSpec
                .methodBuilder("get")
                .returns(CLEAN_ON_FIRE_DB)
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
                .addStatement("return $S", dbClassBundle.getSqlCreateScript())
                .build();
    }

    MethodSpec buildGetDBNameMethod() {
        MethodSpec.Builder builder = MethodSpec
                .methodBuilder("getDBName")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(String.class);
        String dbName = dbClassBundle.getDatabase().dbname();
        if (!dbName.trim().isEmpty()) {
            builder.addStatement("return $S", dbName);
        } else {
            builder.addStatement("return super.getDBName()");
        }
        return builder.build();
    }

    List<MethodSpec> buildGetDaoMethods() {
        List<MethodSpec> specs = new ArrayList<>();
        for (ClassName daoName : dbClassBundle.getDaoNames()) {
            MethodSpec spec = MethodSpec
                    .methodBuilder("get" + StringUtils.firstLetterToUp(daoName.simpleName()))
                    .returns(daoName)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return new $T(buildHelper())", daoName)
                    .build();
            specs.add(spec);
        }
        return specs;
    }




}
