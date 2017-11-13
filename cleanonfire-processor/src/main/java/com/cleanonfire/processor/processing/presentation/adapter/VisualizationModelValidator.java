package com.cleanonfire.processor.processing.presentation.adapter;

import com.cleanonfire.annotations.presentation.adapter.Bind;
import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.core.Validator;
import com.cleanonfire.processor.processing.Utils;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.cleanonfire.processor.processing.Utils.verifyPublicGetterAndSetters;
import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.VIEW;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.VIEW_BINDER;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public class VisualizationModelValidator implements Validator<AdapterClassBundle> {
    @Override
    public ValidationResult validate(AdapterClassBundle bundle) throws ProcessingException {
        for (VariableElement element : bundle.getBoundElements()) {
            ValidationResult validationField = validateField(element, bundle);
            if (!validationField.isValid())
                return new ValidationResult(false, validationField.getMessages());
        }
        return new ValidationResult(true);
    }


    private ValidationResult validateField(VariableElement element, AdapterClassBundle bundle) {
        Bind bind = element.getAnnotation(Bind.class);

        if (!verifyPublicGetterAndSetters(element)) {
            String msg = String.format("The '%s' field is private and does't have valid and public getters and setters", element.getSimpleName().toString());
            return new ValidationResult(false, Collections.singletonList(msg));
        }

        ValidationResult viewValidation = validateView(bind);
        if (!viewValidation.isValid())
            return new ValidationResult(false, viewValidation.getMessages());


        ValidationResult binderValidation = validateBinder(bind, element);
        if (!binderValidation.isValid())
            return new ValidationResult(false, binderValidation.getMessages());

        return new ValidationResult(true);

    }


    private ValidationResult validateBinder(Bind bind, VariableElement element) {
        DeclaredType binder = (DeclaredType) Utils.getBinderTypeMirror(bind);

        ValidationResult interfaceValidation = validateBinderInterface(binder);
        if (!interfaceValidation.isValid())
            return new ValidationResult(false, Collections.singletonList(String.format("The class %s, submitted as Binder at the %s field is not a ViewBinder Implementation", binder.toString(), element.getSimpleName())));

        ValidationResult inputValidation = validateBinderInputType(binder, element);
        if (!inputValidation.isValid())
            return new ValidationResult(false, Collections.singletonList(String.format("The binder %s of the %s field doesn't support a %s input", binder.toString(), element.getSimpleName(),element.asType().toString())));

        TypeMirror viewType = Utils.getBindViewTypeMirror(bind);
        ValidationResult viewValidation = validateBinderViewType(binder,viewType);
        if (!viewValidation.isValid())
            return new ValidationResult(false, Collections.singletonList(String.format("The binder %s of the %s field can't bind a %s component", binder.toString(), element.getSimpleName(),viewType.toString())));


        return validateBinderInterface(binder);
    }



    private ValidationResult validateView(Bind bind) {
        DeclaredType view = (DeclaredType) Utils.getBindViewTypeMirror(bind);
        return validateViewHierarchy(view);
    }


    private ValidationResult validateViewHierarchy(DeclaredType type) {
        TypeMirror superclass = ((TypeElement) type.asElement()).getSuperclass();
        if (superclass.getKind().equals(TypeKind.NONE)) {
            return new ValidationResult(false, Collections.singletonList("The class submitted is not a View subclass"));
        } else if (TypeName.get(superclass).equals(VIEW) || TypeName.get(type).equals(VIEW)) {
            return new ValidationResult(true);
        } else {
            return validateViewHierarchy((DeclaredType) superclass);
        }
    }


    private ValidationResult validateBinderInputType(DeclaredType binderType, VariableElement element) {
        TypeMirror superclass = ((TypeElement) binderType.asElement()).getSuperclass();
        Optional<DeclaredType> binder =
                ((TypeElement) binderType.asElement())
                        .getInterfaces()
                        .stream()
                        .map(typeName -> ((DeclaredType) typeName))
                        .findFirst();

        if (!binder.isPresent() && superclass.getKind().equals(TypeKind.NONE)) {
            return new ValidationResult(false);
        } else if (binder.isPresent() && ProcessingUtils.getTypeUtils().isAssignable(element.asType(),binder.get().getTypeArguments().get(0))) {
            return new ValidationResult(true);
        } else {
            return validateBinderInputType((DeclaredType) superclass, element);
        }
    }

    private ValidationResult validateBinderViewType(DeclaredType binderType, TypeMirror viewType) {
        TypeMirror superclass = ((TypeElement) binderType.asElement()).getSuperclass();
        Optional<DeclaredType> binder =
                ((TypeElement) binderType.asElement())
                        .getInterfaces()
                        .stream()
                        .map(typeName -> ((DeclaredType) typeName))
                        .findFirst();

        if (!binder.isPresent() && superclass.getKind().equals(TypeKind.NONE)) {
            return new ValidationResult(false);
        } else if (binder.isPresent() && ProcessingUtils.getTypeUtils().isAssignable(viewType,binder.get().getTypeArguments().get(1))) {
            return new ValidationResult(true);
        } else {
            return validateBinderViewType((DeclaredType) superclass, viewType);
        }
    }

    private ValidationResult validateBinderInterface(DeclaredType type) {
        TypeMirror superclass = ((TypeElement) type.asElement()).getSuperclass();
        List<TypeName> interfaceNames =
                ((TypeElement) type.asElement())
                        .getInterfaces()
                        .stream()
                        .map(TypeName::get)
                        .map(typeName -> {
                            if (typeName instanceof ParameterizedTypeName)
                                return ((ParameterizedTypeName) typeName).rawType;
                            else return typeName;
                        })
                        .collect(Collectors.toList());

        if (interfaceNames.isEmpty() && superclass.getKind().equals(TypeKind.NONE)) {
            return new ValidationResult(false, Collections.singletonList("The class submitted is not a View subclass"));
        } else if (interfaceNames.contains(VIEW_BINDER)) {
            return new ValidationResult(true);
        } else {
            return validateBinderInterface((DeclaredType) superclass);
        }
    }

    /*
    * Binder se recebe o mesmo tipo do campo
    * Binder se suporta a View declarada
    * */
}
