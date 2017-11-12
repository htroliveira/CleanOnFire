package com.cleanonfire.processor.processing.presentation.adapter.classbuilders;

import com.cleanonfire.annotations.presentation.adapter.Bind;
import com.cleanonfire.processor.core.ClassBuilder;
import com.cleanonfire.processor.processing.Utils;
import com.cleanonfire.processor.processing.presentation.adapter.AdapterClassBundle;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import static com.cleanonfire.annotations.presentation.adapter.VisualizationModel.AdapterType.RECYCLERVIEW;
import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.CONTEXT;
import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.VIEW;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.BASE_LIST_ADAPTER;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.BASE_RV_ADAPTER;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.VIEW_HOLDER_BINDER;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public class ViewHolderBinderClassBuilder implements ClassBuilder {
    AdapterClassBundle bundle;

    public ViewHolderBinderClassBuilder(AdapterClassBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public JavaFile build() {
        return JavaFile.builder(bundle.getPackageElement().getQualifiedName().toString(), buildAdapter(bundle)).build();
    }


    private TypeSpec buildAdapter(AdapterClassBundle bundle) {
        TypeSpec.Builder builder = TypeSpec
                .classBuilder(bundle.getViewHolderBinderClassName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(VIEW_HOLDER_BINDER,bundle.getMainElementClassName(),bundle.getViewHolderClassName()))
                .addMethod(buildBindMethod())
                .addMethod(buildConstructor())
                .addFields(buildFields());

        return builder.build();
    }

    List<FieldSpec> buildFields() {
        List<FieldSpec> specs = new ArrayList<>();
        for (TypeMirror typeMirror : bundle.getViewBinders()) {
            TypeName typeName = TypeName.get(typeMirror);
            String fieldName = ((DeclaredType)typeMirror).asElement().getSimpleName().toString();
            specs.add(
                    FieldSpec.builder(typeName,fieldName,Modifier.PRIVATE).build()
            );
        }
        return specs;
    }

    MethodSpec buildConstructor() {
        MethodSpec.Builder builder =  MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        for (TypeMirror typeMirror : bundle.getViewBinders()) {
            TypeName typeName = TypeName.get(typeMirror);
            String fieldName = ((DeclaredType)typeMirror).asElement().getSimpleName().toString();
            builder.addStatement("$L = new $T()",fieldName,typeName);
        }
        return builder.build();
    }
    MethodSpec buildBindMethod() {
        MethodSpec.Builder builder =  MethodSpec
                .methodBuilder("bind")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(bundle.getViewHolderClassName(),"viewHolder")
                .addParameter(bundle.getMainElementClassName(),"element")
                .returns(void.class);
        for (VariableElement element : bundle.getBoundElements()) {
            Bind bind = element.getAnnotation(Bind.class);
            String binderTypeName = ((DeclaredType)Utils.getBinderTypeMirror(bind)).asElement().getSimpleName().toString();
            builder.addStatement("$L.bind($L,viewHolder.$L)",binderTypeName,Utils.retrieveField(element,"element"),element.getSimpleName().toString().concat("View"));
        }

        return builder.build();
    }



}
