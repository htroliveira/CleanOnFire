package com.cleanonfire.processor.processing.data.db.classbuilders;

import com.cleanonfire.annotations.data.db.ForeignKey;
import com.cleanonfire.annotations.data.db.PrimaryKey;
import com.cleanonfire.processor.core.ClassBuilder;
import com.cleanonfire.processor.processing.data.db.DAOClassBundle;
import com.cleanonfire.processor.processing.data.db.TypePersistence;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.cleanonfire.processor.utils.StringUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import static com.cleanonfire.processor.processing.data.db.Utils.assignField;
import static com.cleanonfire.processor.processing.data.db.Utils.fieldToColumnName;
import static com.cleanonfire.processor.processing.data.db.Utils.getForeignKeyDAOClassName;
import static com.cleanonfire.processor.processing.data.db.Utils.getForeignKeyTypeMirror;
import static com.cleanonfire.processor.processing.data.db.Utils.getForeignKeyTypeName;
import static com.cleanonfire.processor.processing.data.db.Utils.retrieveField;
import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.CONTENT_VALUES;
import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.CURSOR;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.BASE_DAO;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.CLEAN_ON_FIRE_DB;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.CLEAN_SQLITE_HELPER;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public class DAOClassBuilder implements ClassBuilder {
    DAOClassBundle bundle;

    public DAOClassBuilder(DAOClassBundle bundle) {
        this.bundle = bundle;
    }


    @Override
    public JavaFile build() {

        TypeSpec daoSpec = buildDaoClass(
                bundle.getMainElement(),
                buildGetTableName(bundle.getTableName()),
                buildGetIdentificationCondition(bundle),
                buildParseFromCursor(bundle.getMainElement()),
                buildDaoConstructor(),
                buildParseToContentValues(bundle.getMainElement()),
                buildGetId(bundle.getIdClassName()),
                buildGetById(bundle),
                buildDelete(bundle)

        );
        String packageName = ProcessingUtils.getElementUtils().getPackageOf(bundle.getMainElement()).getQualifiedName().toString();
        return JavaFile.builder(packageName, daoSpec)
                .build();
    }

    private MethodSpec buildGetTableName(String tableName) {
        return MethodSpec.methodBuilder("getTableName")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(ClassName.get(String.class))
                .addStatement("return $S", tableName)
                .build();
    }


    private MethodSpec buildGetId(ClassName identificationClass) {
        ClassName elementClassName = ClassName.bestGuess(bundle.getMainElement().getQualifiedName().toString());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getId")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(elementClassName, "item")
                .returns(identificationClass);
        builder.addStatement("return new $T(item)", identificationClass);

        return builder.build();
    }

    private MethodSpec buildGetIdentificationCondition(DAOClassBundle bundle) {
        List<String> conditions = new ArrayList<>();
        for (VariableElement element : bundle.getPrimaryKeyElements()) {
            String columnName = fieldToColumnName.apply(element);
            conditions.add(columnName.concat(" = ?"));
        }

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getIdentificationCondition")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(ClassName.get(String.class))
                .addStatement("return $S", String.join(" AND ", conditions));


        return builder.build();
    }

    private MethodSpec buildGetById(DAOClassBundle bundle) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getById")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(bundle.getMainElement().asType()));
        List<String> paramNames = new ArrayList<>();
        for (VariableElement element : bundle.getPrimaryKeyElements()) {
            paramNames.add(element.getSimpleName().toString());
            builder.addParameter(TypeName.get(element.asType()), element.getSimpleName().toString());
        }
        return builder.addStatement("return getById(new $T($L))", bundle.getIdClassName(), String.join(",", paramNames))
                .build();
    }

    private MethodSpec buildDelete(DAOClassBundle bundle) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("deleteById")
                .addModifiers(Modifier.PUBLIC)
                .returns(bundle.getIdClassName());
        List<String> paramNames = new ArrayList<>();
        for (VariableElement element : bundle.getPrimaryKeyElements()) {
            paramNames.add(element.getSimpleName().toString());
            builder.addParameter(TypeName.get(element.asType()), element.getSimpleName().toString());
        }
        return builder.addStatement("return deleteById(new $T($L))", bundle.getIdClassName(), String.join(",", paramNames))
                .build();
    }

    private List<MethodSpec> buildForeignKeyMethods(DAOClassBundle bundle) {
        List<MethodSpec> methodSpecList = new ArrayList<>();
        List<VariableElement> elements =
                bundle.getForeignKeyElements()
                        .stream()
                        .filter(element ->
                                !DAOClassBundle.get(getForeignKeyTypeMirror(element.getAnnotation(ForeignKey.class))).hasCompositePrimaryKey())
                        .collect(Collectors.toList());

        for (VariableElement element : elements) {
            ForeignKey foreignKey = element.getAnnotation(ForeignKey.class);
            if (foreignKey == null) continue;
            String methodName = "get" + StringUtils.firstLetterToUp(foreignKey.name().isEmpty() ? element.getSimpleName() : foreignKey.name());
            ClassName relatedDaoClassName = getForeignKeyDAOClassName(foreignKey);
            MethodSpec spec = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(getForeignKeyTypeName(foreignKey))
                    .addParameter(TypeName.get(bundle.getMainElement().asType()), "element")
                    .addStatement("$T dao = $T.get().get$L()", relatedDaoClassName, CLEAN_ON_FIRE_DB, relatedDaoClassName.simpleName())
                    .addStatement("return dao.getById($L)", retrieveField(element, "element"))
                    .build();
            methodSpecList.add(spec);
        }
        return methodSpecList;
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

        bundle.getFieldElements().forEach(element -> {
            TypePersistence typePersistence = TypePersistence.forType(element.asType());
            TypePersistence.Statement statement = typePersistence.cursorParsing("cursor", fieldToColumnName.apply(element));
            builder.addStatement(assignField(element, "result", statement.getStatement()), statement.getArgs());
        });

        return builder.addStatement("return result").build();
    }

    private MethodSpec buildParseToContentValues(TypeElement typeElement) {
        ClassName elementClassName = ClassName.bestGuess(typeElement.getQualifiedName().toString());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("parseToContentValues")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(CONTENT_VALUES)
                .addParameter(elementClassName, "item")
                .addStatement("$T contentValues = new $T()", CONTENT_VALUES, CONTENT_VALUES);
        bundle.getFieldElements().forEach(element -> {
            TypePersistence typePersistence = TypePersistence.forType(element.asType());
            if (element.getAnnotation(PrimaryKey.class) != null || element.getAnnotation(ForeignKey.class) != null) {
                builder.addCode("if ( $L > 0 ) ", retrieveField(element,"item"));
            }
            builder.addStatement("contentValues.put($S, $L)", fieldToColumnName.apply(element), typePersistence.contentValuesParsing(retrieveField(element, "item")));

        });


        return builder.addStatement("return contentValues").build();
    }

    private TypeSpec buildDaoClass(TypeElement element, MethodSpec... methodSpecs) {

        String className = element.getSimpleName().toString().concat("CleanDAO");
        return TypeSpec.classBuilder(className)
                .superclass(
                        ParameterizedTypeName.get(
                                BASE_DAO,
                                ClassName.bestGuess(element.getQualifiedName().toString()),
                                bundle.getIdClassName())
                )
                .addModifiers(Modifier.PUBLIC)
                .addMethods(buildForeignKeyMethods(bundle))
                .addMethods(Arrays.asList(methodSpecs))
                .build();
    }


}
