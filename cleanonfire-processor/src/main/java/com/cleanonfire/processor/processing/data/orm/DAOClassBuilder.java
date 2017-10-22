package com.cleanonfire.processor.processing.data.orm;

import com.cleanonfire.annotations.data.orm.Relationship;
import com.cleanonfire.processor.core.ClassBuilder;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.cleanonfire.processor.processing.data.orm.DAOClassBundle.fieldToColumnName;
import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.CONTENT_VALUES;
import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.CURSOR;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.BASE_DAO;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.CLEANONFIRE_ORM;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.CLEAN_SQLITE_HELPER;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.QUERY_CRITERIA;
import static com.cleanonfire.processor.utils.StringUtils.firstLetterToUp;

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
                buildGetIdentificationColumnName(bundle.getIdentificationColumnName()),
                buildParseFromCursor(bundle.getMainElement()),
                buildDaoConstructor(),
                buildParseToContentValues(bundle.getMainElement()),
                buildGetId(bundle.getMainElement())

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

    private MethodSpec buildGetId(TypeElement typeElement) {
        ClassName elementClassName = ClassName.bestGuess(typeElement.getQualifiedName().toString());
        MethodSpec.Builder builder = MethodSpec.methodBuilder("getId")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .addParameter(elementClassName, "item")
                .returns(ClassName.get(Number.class));
        builder.addStatement("return $L", retrieveField(bundle.getPrimaryKeyElement(), "item"));

        return builder.build();
    }


    private MethodSpec buildGetIdentificationColumnName(String idColumnName) {
        return MethodSpec.methodBuilder("getIdentificationColumnName")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(ClassName.get(String.class))
                .addStatement("return $S", idColumnName)
                .build();
    }

    private List<MethodSpec> buildFetchOneToManyMethods(String idColumnName) {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        bundle.getRelatedFieldElements()
                .stream()
                .filter(element ->
                        element.getAnnotation(Relationship.class).relation().equals(Relationship.Type.ONE_TO_ONE))
                .forEach(element -> {

                    MethodSpec.Builder builder = MethodSpec.methodBuilder(String.format("fetch%s", firstLetterToUp(element.getSimpleName().toString())))
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .addParameter(TypeName.get(bundle.getMainElement().asType()), "element");
                    if (element.getAnnotation(Relationship.class).lazyLoad()) {
                        builder.addCode("if ($L != null) return;\n", retrieveField(element, "element"));
                    }
                    ClassName relatedDaoClassName = ClassName.bestGuess(element.asType().toString().concat("CleanDAO"));
                    builder.addStatement(
                            "$T criteria = $T\n\t.builder()\n\t.setSelection($S)\n\t.setSelectionArgs(String.valueOf($L))\n\t.build()",
                            QUERY_CRITERIA,
                            QUERY_CRITERIA,
                            fieldToColumnName.apply(element).concat("_id = ? "),
                            retrieveField(bundle.getPrimaryKeyElement(),"element")
                    );
                    builder.addStatement(assignField(element, "element", "$T.getDao($T.class).query(criteria).get(0)"), CLEANONFIRE_ORM, relatedDaoClassName);
                    methodSpecs.add(builder.build());
                });
        return methodSpecs;
    }

    private List<MethodSpec> buildFetchOneToOneMethods() {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        bundle.getRelatedFieldElements()
                .stream()
                .filter(element ->
                        element.getAnnotation(Relationship.class).relation().equals(Relationship.Type.ONE_TO_ONE))
                .forEach(element -> {
                    Relationship relationship = element.getAnnotation(Relationship.class);
                    String methodName = relationship.lazyLoad()? "lazyFetch%s":"fetch%s";
                    MethodSpec.Builder builder = MethodSpec.methodBuilder(String.format(methodName, firstLetterToUp(element.getSimpleName().toString())))
                            .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                            .returns(TypeName.VOID)
                            .addParameter(TypeName.get(bundle.getMainElement().asType()), "element");
                    if (element.getAnnotation(Relationship.class).lazyLoad()) {
                        builder.addCode("if ($L != null) return;\n", retrieveField(element, "element"));
                    }
                    ClassName relatedDaoClassName = ClassName.bestGuess(element.asType().toString().concat("CleanDAO"));
                    builder.addStatement(
                            "$T criteria = $T\n\t.builder()\n\t.setSelection($S)\n\t.setSelectionArgs(String.valueOf($L))\n\t.build()",
                            QUERY_CRITERIA,
                            QUERY_CRITERIA,
                            fieldToColumnName.apply(element).concat("_id = ? "),
                            retrieveField(bundle.getPrimaryKeyElement(),"element")
                    );
                    builder.addStatement(assignField(element, "element", "$T.getDao($T.class).query(criteria).get(0)"), CLEANONFIRE_ORM, relatedDaoClassName);
                    methodSpecs.add(builder.build());
                });
        return methodSpecs;
    }

    private MethodSpec buildFetchManyToMany(String idColumnName) {
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

        bundle.getSimpleFieldElements().forEach(element -> {
            TypePersistence typePersistence = TypePersistence.forType(element.asType());
            TypePersistence.Statement statement = typePersistence.cursorParsing("cursor", fieldToColumnName.apply(element));
            builder.addStatement(assignField(element, "result", statement.getStatement()), statement.getArgs());
        });
        bundle.getSimpleRelatedFieldElements().forEach(element ->
                builder.addStatement("fetch$L(result)", firstLetterToUp(element.getSimpleName().toString()))
        );
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
        bundle.getSimpleFieldElements().forEach(element -> {
            TypePersistence typePersistence = TypePersistence.forType(element.asType());
            String parsingSuffix = typePersistence != null ? typePersistence.contentValuesParsing() : null;
            builder.addStatement(String.format("contentValues.put($S,$L%s)", parsingSuffix), fieldToColumnName.apply(element), retrieveField(element, "item"));
        });


        return builder.addStatement("return contentValues").build();
    }

    private TypeSpec buildDaoClass(TypeElement element, MethodSpec... methodSpecs) {
        return TypeSpec.classBuilder(element.getSimpleName().toString().concat("CleanDAO"))
                .superclass(ParameterizedTypeName.get(BASE_DAO, ClassName.bestGuess(element.getQualifiedName().toString())))
                .addModifiers(Modifier.PUBLIC)
                .addMethods(Arrays.asList(methodSpecs))
                .addMethods(buildFetchOneToOneMethods())
                .build();
    }

    private String assignField(Element element, String variable, String assignment) {
        if (element.getModifiers().contains(Modifier.PRIVATE)) {
            String setterName = ProcessingUtils.getSetterName(element);
            return String.format("%s.%s(%s)", variable, setterName, assignment);
        } else {
            return String.format("%s.%s = %s", variable, element.getSimpleName().toString(), assignment);
        }
    }

    private String retrieveField(Element element, String variable) {
        if (element.getModifiers().contains(Modifier.PRIVATE)) {
            String getterName = ProcessingUtils.getGetterName(element);
            return String.format("%s.%s()", variable, getterName);
        } else {
            return String.format("%s.%s", variable, element.getSimpleName().toString());
        }
    }


}
