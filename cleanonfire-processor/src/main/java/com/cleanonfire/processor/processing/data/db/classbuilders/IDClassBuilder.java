package com.cleanonfire.processor.processing.data.db.classbuilders;

import com.cleanonfire.processor.core.ClassBuilder;
import com.cleanonfire.processor.processing.data.db.DAOClassBundle;
import com.cleanonfire.processor.utils.StringUtils;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import static com.cleanonfire.processor.processing.Utils.buildGetterMethod;
import static com.cleanonfire.processor.processing.Utils.retrieveField;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.IDENTIFICATION;

/**
 * Created by heitorgianastasio on 03/11/17.
 */

public class IDClassBuilder implements ClassBuilder {
    DAOClassBundle bundle;

    public IDClassBuilder(DAOClassBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public JavaFile build() {
        return JavaFile.builder(bundle.getPackageElement().toString(), buildIdentificationClass(bundle)).build();
    }


    private TypeSpec buildIdentificationClass(DAOClassBundle bundle) {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(StringUtils.firstLetterToUp(bundle.getMainElement().getSimpleName()).concat("ID"))
                .addSuperinterface(IDENTIFICATION)
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder elementConstructorBuilder = MethodSpec.constructorBuilder()
                .addParameter(TypeName.get(bundle.getMainElement().asType()), "element");

        MethodSpec.Builder variablesConstructorBuilder = MethodSpec.constructorBuilder();

        MethodSpec.Builder identificationArgsBuilder = MethodSpec.methodBuilder("identificationArgs")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(String[].class));

        List<String> stringsValueOf = new ArrayList<>();

        for (VariableElement element : bundle.getPrimaryKeyElements()) {
            TypeName elementTypeName = TypeName.get(element.asType());
            String elementName = element.getSimpleName().toString();

            classBuilder
                    .addField(elementTypeName, elementName, Modifier.PRIVATE)
                    .addMethod(buildGetterMethod(element));

            elementConstructorBuilder
                    .addStatement("this.$L = $L", elementName, retrieveField(element, "element"));

            variablesConstructorBuilder
                    .addParameter(elementTypeName, elementName)
                    .addStatement("this.$1L = $1L", elementName);

            stringsValueOf.add(String.format("String.valueOf(%s)", elementName));
        }

        identificationArgsBuilder
                .addStatement("return new $T{$L}", TypeName.get(String[].class), String.join(",", stringsValueOf));

        return classBuilder
                .addMethod(elementConstructorBuilder.build())
                .addMethod(variablesConstructorBuilder.build())
                .addMethod(identificationArgsBuilder.build())
                .build();
    }
}
