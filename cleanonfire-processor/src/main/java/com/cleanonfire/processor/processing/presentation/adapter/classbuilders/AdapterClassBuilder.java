package com.cleanonfire.processor.processing.presentation.adapter.classbuilders;

import com.cleanonfire.processor.core.ClassBuilder;
import com.cleanonfire.processor.processing.Utils;
import com.cleanonfire.processor.processing.presentation.adapter.AdapterClassBundle;
import com.cleanonfire.processor.utils.StringUtils;
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

import static com.cleanonfire.annotations.presentation.adapter.VisualizationModel.AdapterType.RECYCLERVIEW;
import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.CONTEXT;
import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.VIEW;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.BASE_LIST_ADAPTER;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.BASE_RV_ADAPTER;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.ON_ITEM_CLICK_LISTENER;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.ON_ITEM_LONG_CLICK_LISTENER;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public class AdapterClassBuilder implements ClassBuilder {
    AdapterClassBundle bundle;

    public AdapterClassBuilder(AdapterClassBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public JavaFile build() {
        return JavaFile.builder(bundle.getPackageElement().getQualifiedName().toString(), buildAdapter(bundle)).build();
    }


    private TypeSpec buildAdapter(AdapterClassBundle bundle) {
        ClassName superClass = bundle.getVisualizationModel().adapterType().equals(RECYCLERVIEW) ? BASE_RV_ADAPTER : BASE_LIST_ADAPTER;

        TypeSpec.Builder builder = TypeSpec
                .classBuilder(bundle.getMainElement().getSimpleName().toString().concat("Adapter"))
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(superClass,bundle.getMainElementClassName(),bundle.getViewHolderBinderClassName(),bundle.getViewHolderClassName()))
                .addMethod(buildGetLayoutIdMethod())
                .addMethod(buildCreateViewHolderMethod())
                .addMethod(buildConstructor())
                .addMethods(buildSetOnClickListenerMethods())
                .addMethods(buildSetOnLongClickListenerMethods())
                .addFields(buildSetOnClickListenerFields())
                .addFields(buildSetOnLongClickListenerFields());
        if(bundle.getClickableBoundElements().size()+bundle.getLongclickableBoundElements().size()>0)
            builder.addMethod(buildOnBindViewHolderMethod());
        return builder.build();
    }


    MethodSpec buildConstructor() {
        return MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get((ClassName)TypeName.get(List.class),bundle.getMainElementClassName()),"list")
                .addParameter(CONTEXT,"context")
                .addParameter(bundle.getViewHolderBinderClassName(),"binder")
                .addStatement("super(list,context,binder)")
                .build();
    }
    MethodSpec buildGetLayoutIdMethod() {
        return MethodSpec
                .methodBuilder("getLayoutId")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(int.class)
                .addStatement("return $L", bundle.getVisualizationModel().layoutId())
                .build();
    }

    MethodSpec buildCreateViewHolderMethod() {
        return MethodSpec
                .methodBuilder("createViewHolder")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(VIEW,"root" )
                .returns(bundle.getViewHolderClassName())
                .addStatement("return new $T(root)", bundle.getViewHolderClassName())
                .build();
    }


    MethodSpec buildOnBindViewHolderMethod() {
        MethodSpec.Builder builder =  MethodSpec
                .methodBuilder("onBindViewHolder")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(bundle.getViewHolderClassName(),"holder" )
                .addParameter(int.class,"position",Modifier.FINAL)
                .returns(void.class)
                .addStatement("super.onBindViewHolder(holder,position)");
        for (VariableElement element : bundle.getClickableBoundElements()) {
            builder.addCode("if(on$1LClickListener != null)\n" +
                    "\tholder.$2LView.setOnClickListener(new View.OnClickListener(){\n" +
                    "\t\t@Override\n" +
                    "\t\tpublic void onClick(View v){\n" +
                    "\t\t\ton$1LClickListener.onItemClick(list.get(position),position,v);\n" +
                    "\t\t}\n" +
                    "\t});\n",StringUtils.firstLetterToUp(element.getSimpleName()),element.getSimpleName());
        }

        for (VariableElement element : bundle.getLongclickableBoundElements()) {
            builder.addCode("if(on$1LLongClickListener != null)\n" +
                    "\tholder.$2LView.setOnLongClickListener(new View.OnLongClickListener(){\n" +
                    "\t\t@Override\n" +
                    "\t\tpublic boolean onLongClick(View v){\n" +
                    "\t\t\treturn on$1LLongClickListener.onItemLongClick(list.get(position),position,v);\n" +
                    "\t\t}\n" +
                    "\t});\n",StringUtils.firstLetterToUp(element.getSimpleName()),element.getSimpleName());
        }
        return builder.build();
    }



    List<MethodSpec> buildSetOnClickListenerMethods() {
        List<MethodSpec> specs = new ArrayList<>();
        for (VariableElement element : bundle.getClickableBoundElements()) {
            specs.add(
                    Utils.buildSetterMethod(
                            "on".concat(StringUtils.firstLetterToUp(element.getSimpleName())).concat("ClickListener"),
                            ParameterizedTypeName.get(ON_ITEM_CLICK_LISTENER,bundle.getMainElementClassName())
                    )
            );
        }
        return specs;
    }
    List<MethodSpec> buildSetOnLongClickListenerMethods() {
        List<MethodSpec> specs = new ArrayList<>();
        for (VariableElement element : bundle.getLongclickableBoundElements()) {
            specs.add(
                    Utils.buildSetterMethod("on".concat(StringUtils.firstLetterToUp(element.getSimpleName())).concat("LongClickListener"),
                            ParameterizedTypeName.get(ON_ITEM_LONG_CLICK_LISTENER,bundle.getMainElementClassName()))
            );
        }
        return specs;
    }
    List<FieldSpec> buildSetOnLongClickListenerFields() {
        List<FieldSpec> specs = new ArrayList<>();
        for (VariableElement element : bundle.getLongclickableBoundElements()) {
            specs.add(
                    FieldSpec.builder(ParameterizedTypeName.get(ON_ITEM_LONG_CLICK_LISTENER,bundle.getMainElementClassName()),"on".concat(StringUtils.firstLetterToUp(element.getSimpleName())).concat("LongClickListener")).build()
            );
        }
        return specs;
    }
    List<FieldSpec> buildSetOnClickListenerFields() {
        List<FieldSpec> specs = new ArrayList<>();
        for (VariableElement element : bundle.getClickableBoundElements()) {
            specs.add(
                    FieldSpec.builder(ParameterizedTypeName.get(ON_ITEM_CLICK_LISTENER,bundle.getMainElementClassName()),"on".concat(StringUtils.firstLetterToUp(element.getSimpleName())).concat("ClickListener")).build()
            );
        }
        return specs;
    }

}
