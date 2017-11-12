package com.cleanonfire.processor.processing.hellotest;

import com.cleanonfire.annotations.SaysHello;
import com.cleanonfire.processor.core.GenericAnnotationProcessor;
import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.core.Validator.ValidationResult;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public class SaysHelloProcessor extends GenericAnnotationProcessor<SaysHello> {
    TypeName TOAST = ClassName.get("android.widget","Toast");

    SaysHelloValidator validator = new SaysHelloValidator();

    public SaysHelloProcessor(Class<SaysHello> annotationClass) {
        super(annotationClass);
    }

    @Override
    public void process(Set<? extends Element> elements, RoundEnvironment env) throws ProcessingException {
        for (Element element : elements) {
            ValidationResult validation = validator.validate(element);
            if (validation.isValid()) {
                buildFile(element);
            } else {
                throw  new ProcessingException(annotationClass,element, validation.getMessages());
            }
        }
    }

    private void buildFile(Element element){
        MethodSpec main = MethodSpec.methodBuilder("hello")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(ClassName.get("android.content","Context"),"context")
                .addStatement("$T.makeText(context,$S,$T.LENGTH_SHORT).show()",TOAST, "Hello,"+element.getSimpleName(),TOAST)
                .beginControlFlow("")
                .endControlFlow()
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("Hello"+element.getSimpleName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        JavaFile javaFile = JavaFile.builder(ProcessingUtils.getElementUtils().getPackageOf(element).getQualifiedName().toString(), helloWorld)
                .build();

        try {
            javaFile.writeTo(ProcessingUtils.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
