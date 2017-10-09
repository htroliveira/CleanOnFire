package com.cleanonfire.processor.processing.data.orm;

import com.cleanonfire.annotations.data.orm.IgnoreField;
import com.cleanonfire.annotations.data.orm.PrimaryKey;
import com.cleanonfire.processor.core.ElementValidator;
import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.utils.Lists;
import com.cleanonfire.processor.utils.ProcessingUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public class EntityOrmValidator implements ElementValidator {


    public EntityOrmValidator() {
    }

    @Override
    public ValidationResult validate(Element elementToValidate) throws ProcessingException {
        List<String> messages = new ArrayList<>();

        if (!elementToValidate.getKind().isClass()) {
            messages.add(elementToValidate.getSimpleName().toString().concat(" is not a class"));
            return new ValidationResult(false, messages);
        }

        TypeElement typeElement = (TypeElement) elementToValidate;

        for (Element element : typeElement.getEnclosedElements()) {
            if (element.getKind().isField() && element.getAnnotation(IgnoreField.class) == null) {
                ValidationResult fieldValidation = validateFields((VariableElement) element, typeElement);
                messages.addAll(fieldValidation.getMessages());
                if (!fieldValidation.isValid())
                    return new ValidationResult(false, messages);
            }
        }

        return new ValidationResult(true, messages);
    }

    private ValidationResult validateFields(VariableElement fieldElement, TypeElement typeElement) {
        List<String> messages = new ArrayList<>();

        if (!verifyPublicGetterAndSetters(fieldElement, typeElement)) {
            messages.add(String.format("The '%s' field is private or protected and does't have valid and public getters and setters", fieldElement.getSimpleName().toString()));
            return new ValidationResult(false, messages);
        }

        PrimaryKey pk = fieldElement.getAnnotation(PrimaryKey.class);
        if (pk != null) {
            ValidationResult primaryKeyValidation = validatePrimaryKey(fieldElement, pk);
            if (!primaryKeyValidation.isValid()) {
                messages.addAll(primaryKeyValidation.getMessages());
                return new ValidationResult(false, messages);
            }
        }

        return new ValidationResult(true, messages);
    }

    private ValidationResult validatePrimaryKey(VariableElement element, PrimaryKey primaryKey) {
        List<String> messages = new ArrayList<>();
        TypeKind typeKind = element.asType().getKind();
        if (!(typeKind.equals(TypeKind.INT) || typeKind.equals(TypeKind.LONG))) {
            messages.add("A @PrimaryKey field must be of 'long' or 'int' type");
            return new ValidationResult(false, messages);
        }

        return new ValidationResult(true, messages);
    }

    private boolean verifyPublicGetterAndSetters(Element fieldElement, TypeElement typeElement) {
        if (fieldElement.getModifiers().contains(Modifier.PRIVATE) ||
            fieldElement.getModifiers().contains(Modifier.PROTECTED)) {

            String getterName = ProcessingUtils.getGetterName(fieldElement);
            String setterName = ProcessingUtils.getSetterName(fieldElement);
            List<ExecutableElement> filteredMethods = Lists.filter(
                    ProcessingUtils.getPublicMethods(typeElement),
                    method ->   (method.getSimpleName().toString().equals(getterName) &&
                                 method.asType().toString().replace("()","").equals(fieldElement.asType().toString()) &&
                                 !method.getModifiers().contains(Modifier.PROTECTED)&&
                                 !method.getModifiers().contains(Modifier.PRIVATE))
                                ||//or
                                (method.getSimpleName().toString().equals(setterName)&&
                                 method.getParameters().get(0).asType().equals(fieldElement.asType())&&
                                 !method.getModifiers().contains(Modifier.PROTECTED)&&
                                 !method.getModifiers().contains(Modifier.PRIVATE))
            );
            return Lists.transform(filteredMethods, element -> element.getSimpleName().toString()).containsAll(Arrays.asList(getterName,setterName));

        } else return true;
    }
}
