package com.cleanonfire.processor.processing.presentation.adapter;

import com.cleanonfire.annotations.presentation.adapter.Bind;
import com.cleanonfire.annotations.presentation.adapter.VisualizationModel;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.ClassName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public class AdapterClassBundle {
    private static Map<TypeMirror,AdapterClassBundle> BUNDLE_MAP = new HashMap<>();


    public static AdapterClassBundle get(TypeElement element){
        return BUNDLE_MAP.computeIfAbsent(element.asType(), k -> new AdapterClassBundle(element));
    }

    public static AdapterClassBundle get(TypeMirror typeMirror){
        return BUNDLE_MAP.computeIfAbsent(typeMirror, m -> new AdapterClassBundle((TypeElement) ProcessingUtils.getTypeUtils().asElement(m)));
    }




    private TypeElement mainElement;
    private VisualizationModel visualizationModel;
    private List<VariableElement> boundElements;
    private List<VariableElement> clickableBoundElements;
    private List<VariableElement> longclickableBoundElements;
    private List<TypeMirror> viewBinders;
    private PackageElement packageElement;
    private ClassName viewHolderClassName;
    private ClassName viewHolderBinderClassName;
    private ClassName mainElementClassName;



    private AdapterClassBundle(TypeElement mainElement) {
        this.mainElement = mainElement;
        String packageName = ProcessingUtils.getElementUtils().getPackageOf(mainElement).getQualifiedName().toString();
        mainElementClassName = ClassName.get(mainElement);
        viewHolderBinderClassName = ClassName.get(packageName,mainElement.getSimpleName().toString().concat("ViewHolderBinder"));
        viewHolderClassName = ClassName.get(packageName,mainElement.getSimpleName().toString().concat("ViewHolder"));
    }


    public PackageElement getPackageElement() {
        if (packageElement == null) {
            packageElement = ProcessingUtils.getElementUtils().getPackageOf(mainElement);
        }
        return packageElement;
    }


    public TypeElement getMainElement() {
        return mainElement;
    }

    public VisualizationModel getVisualizationModel() {
        if (visualizationModel == null)
            visualizationModel = mainElement.getAnnotation(VisualizationModel.class);
        return visualizationModel;
    }


    public List<VariableElement> getBoundElements() {
        if (boundElements == null)
            boundElements = mainElement.getEnclosedElements()
                    .stream()
                    .filter(element -> element.getKind().isField() && element.getAnnotation(Bind.class) != null)
                    .map(element -> (VariableElement)element)
                    .collect(Collectors.toList());
        return boundElements;
    }


    public List<TypeMirror> getViewBinders(){
        if (viewBinders == null) {
            viewBinders = getBoundElements()
                    .stream()
                    .map(element -> getViewBinderFromBind(element.getAnnotation(Bind.class)))
                    .distinct()
                    .collect(Collectors.toList());
        }

        return viewBinders;
    }

    public List<VariableElement> getClickableBoundElements() {
        if (clickableBoundElements == null) {
            clickableBoundElements = getBoundElements()
                    .stream()
                    .filter(element -> element.getAnnotation(Bind.class).clickable())
                    .collect(Collectors.toList());
        }
        return clickableBoundElements;
    }

    public List<VariableElement> getLongclickableBoundElements() {
        if (longclickableBoundElements == null) {
            longclickableBoundElements = getBoundElements()
                    .stream()
                    .filter(element -> element.getAnnotation(Bind.class).longClickable())
                    .collect(Collectors.toList());
        }
        return longclickableBoundElements;
    }

    public ClassName getViewHolderClassName() {
        return viewHolderClassName;
    }

    public ClassName getViewHolderBinderClassName() {
        return viewHolderBinderClassName;
    }

    public ClassName getMainElementClassName() {
        return mainElementClassName;
    }

    private static TypeMirror getViewBinderFromBind(Bind bind){
        try {
            bind.binder();
        }catch (MirroredTypeException mte){
            return mte.getTypeMirror();
        }
        throw new RuntimeException("Was not possible to get the binder type mirror from @Bind");
    }
}
