package com.cleanonfire.processor.processing.presentation.adapter.classbuilders;

import com.cleanonfire.annotations.presentation.adapter.Bind;
import com.cleanonfire.processor.core.ClassBuilder;
import com.cleanonfire.processor.processing.Utils;
import com.cleanonfire.processor.processing.presentation.adapter.AdapterClassBundle;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import static com.cleanonfire.annotations.presentation.adapter.VisualizationModel.AdapterType.RECYCLERVIEW;
import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.VIEW;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.RECYCLER_VIEW_HOLDER;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.VIEW_HOLDER;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public class ViewHolderClassBuilder implements ClassBuilder {
    AdapterClassBundle bundle;

    public ViewHolderClassBuilder(AdapterClassBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public JavaFile build() {
        return JavaFile.builder(bundle.getPackageElement().getQualifiedName().toString(), buildAdapter(bundle)).build();
    }


    private TypeSpec buildAdapter(AdapterClassBundle bundle) {

        TypeSpec.Builder builder = TypeSpec
                .classBuilder(bundle.getViewHolderClassName())
                .addModifiers(Modifier.PUBLIC);

        if (bundle.getVisualizationModel().adapterType().equals(RECYCLERVIEW)){
            builder.superclass(RECYCLER_VIEW_HOLDER);
        }else {
            builder.addSuperinterface(VIEW_HOLDER);
        }
                builder
                .addFields(buildFields())
                .addMethod(buildConstructor());

        return builder.build();
    }

    MethodSpec buildConstructor() {
        MethodSpec.Builder builder = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(VIEW,"root");
        if(bundle.getVisualizationModel().adapterType().equals(RECYCLERVIEW))
            builder.addStatement("super(root)");

        for (VariableElement variableElement : bundle.getBoundElements()) {
            Bind bind = variableElement.getAnnotation(Bind.class);
            builder.addStatement("$L = root.findViewById($L)",getFieldName(variableElement),bind.layoutId());
        }

        return builder.build();
    }
    List<FieldSpec> buildFields() {
        List<FieldSpec> specs = new ArrayList<>();
        for (VariableElement variableElement : bundle.getBoundElements()) {
            TypeName viewTypeName = Utils.getBindViewTypeName(variableElement.getAnnotation(Bind.class));
            specs.add(
                    FieldSpec.builder(viewTypeName,getFieldName(variableElement)).build()
            );
        }
        return specs;
    }


    private String getFieldName(Element element){
        return element.getSimpleName().toString().concat("View");
    }
}
