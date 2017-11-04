package com.cleanonfire.processor.processing.data.db;

import com.cleanonfire.annotations.data.db.ForeignKey;
import com.cleanonfire.annotations.data.db.Table;
import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.core.Validator;
import com.cleanonfire.processor.utils.ArrayUtil;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.cleanonfire.processor.processing.data.db.Utils.fieldToColumnName;
import static com.cleanonfire.processor.processing.data.db.Utils.getForeignKeyTypeElement;
import static com.cleanonfire.processor.processing.data.db.Utils.getForeignKeyTypeMirror;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public class EntityOrmValidator implements Validator<DAOClassBundle> {
    private List<String> tableNames = new ArrayList<>();


    @Override
    public ValidationResult validate(DAOClassBundle bundle) throws ProcessingException {

        if (!bundle.getMainElement().getKind().isClass())
            return new ValidationResult(false, Collections.singletonList(bundle.getMainElement().getSimpleName().toString().concat(" is not a class")));

        if (tableNames.contains(bundle.getTableName()))
            return new ValidationResult(false, Collections.singletonList(bundle.getTableName().concat(" table is duplicated")));
        else
            tableNames.add(bundle.getTableName());

        ValidationResult fieldsValidation = validateFields(bundle);
        if (!fieldsValidation.isValid())
            return new ValidationResult(false, fieldsValidation.getMessages());

        ValidationResult primaryKeyValidation = validatePrimaryKey(bundle);
        if (!primaryKeyValidation.isValid())
            return new ValidationResult(false, primaryKeyValidation.getMessages());

        ValidationResult relationsValidation = validateRelations(bundle);
        if (!relationsValidation.isValid())
            return new ValidationResult(false, relationsValidation.getMessages());


        return new ValidationResult(true);
    }


    private ValidationResult validateFields(DAOClassBundle bundle) {
        for (VariableElement element : bundle.getFieldElements()) {
            if (!verifyPublicGetterAndSetters(element)) {
                String msg = String.format("The '%s' field is private or protected and does't have valid and public getters and setters", element.getSimpleName().toString());
                return new ValidationResult(false, Collections.singletonList(msg));
            }
            try{
                TypePersistence.forType(element.asType());
            }catch (RuntimeException e){
                String msg = String.format("The type '%s' is not supported", element.asType().toString());
                return new ValidationResult(false,Collections.singletonList(msg));
            }
        }



        Set<String> duplicatedColumnNames =
                ArrayUtil.getDuplicates(bundle.getFieldElements()
                        .stream()
                        .map(fieldToColumnName)
                        .collect(Collectors.toList()));
        if (!duplicatedColumnNames.isEmpty())
            return new ValidationResult(false, Collections.singletonList("The entity have duplicated columns: " + String.join(",", duplicatedColumnNames)));


        return new ValidationResult(true);
    }

    private ValidationResult validateRelations(DAOClassBundle bundle) {
        for (VariableElement variableElement : bundle.getForeignKeyElements()) {
            ForeignKey foreignKey = variableElement.getAnnotation(ForeignKey.class);
            TypeElement relatedTypeElement = getForeignKeyTypeElement(foreignKey);
            if(relatedTypeElement.getAnnotation(Table.class)==null)
                return new ValidationResult(false, Collections.singletonList(variableElement.getSimpleName().toString().concat(" is not related to other Table")));
            else if (DAOClassBundle.get(relatedTypeElement.asType()).hasCompositePrimaryKey())
                return new ValidationResult(false, Collections.singletonList("Foreign keys related with Composite Primary Key Tables are not supported yet"));
        }

        return new ValidationResult(true);
    }

    private ValidationResult validatePrimaryKey(DAOClassBundle bundle) {
        if (bundle.getPrimaryKeyElements().size() < 1)
            return new ValidationResult(false, Collections.singletonList("A entity must have one primary key field "));

        for (VariableElement element : bundle.getPrimaryKeyElements()) {
            TypeKind typeKind = element.asType().getKind();
            if (!typeKind.equals(TypeKind.INT) &&
                    !typeKind.equals(TypeKind.LONG) &&
                    !TypeName.get(element.asType()).equals(ClassName.get(Long.class)) &&
                    !TypeName.get(element.asType()).equals(ClassName.get(Integer.class))) {
                return new ValidationResult(false, Collections.singletonList("A @PrimaryKey field must be a Long or an Integer"));
            }
        }
        return new ValidationResult(true);
    }

    private boolean verifyPublicGetterAndSetters(Element fieldElement) {
        if (fieldElement.getModifiers().contains(Modifier.PRIVATE) ||
                fieldElement.getModifiers().contains(Modifier.PROTECTED)) {

            String getterName = ProcessingUtils.getGetterName(fieldElement);
            String setterName = ProcessingUtils.getSetterName(fieldElement);

            return ProcessingUtils.getPublicMethods((TypeElement) fieldElement.getEnclosingElement())
                    .stream()
                    .filter(
                            method ->
                                    (method.getSimpleName().toString().equals(getterName) &&
                                            method.asType().toString().replace("()", "").equals(fieldElement.asType().toString()) &&
                                            method.getParameters().isEmpty() &&
                                            !method.getModifiers().contains(Modifier.PROTECTED) &&
                                            !method.getModifiers().contains(Modifier.PRIVATE))
                                            ||//or
                                            (method.getSimpleName().toString().equals(setterName) &&
                                                    method.getParameters().get(0).asType().equals(fieldElement.asType()) &&
                                                    !method.getModifiers().contains(Modifier.PROTECTED) &&
                                                    !method.getModifiers().contains(Modifier.PRIVATE))
                    )
                    .map(element -> element.getSimpleName().toString())
                    .collect(Collectors.toList())
                    .containsAll(Arrays.asList(getterName, setterName));

        } else return true;
    }

}
