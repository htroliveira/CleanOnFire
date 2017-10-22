package com.cleanonfire.processor.processing.data.orm;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.annotations.data.orm.FieldInfo;
import com.cleanonfire.annotations.data.orm.IgnoreField;
import com.cleanonfire.annotations.data.orm.PrimaryKey;
import com.cleanonfire.annotations.data.orm.Relationship;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public class DAOClassBundle {
    public static Function<VariableElement, String> fieldToColumnName = element -> {
        FieldInfo fieldInfo = element.getAnnotation(FieldInfo.class);
        if (fieldInfo != null && !fieldInfo.columnName().isEmpty())
            return fieldInfo.columnName();
        else return element.getSimpleName().toString();
    };
    private TypeElement mainElement;
    private Entity entity;
    private PrimaryKey primaryKey;
    private List<VariableElement> primaryKeyElements;
    private List<VariableElement> simpleFieldElements;
    private List<VariableElement> relatedFieldElements;
    private List<VariableElement> simpleRelatedFieldElements;
    private List<VariableElement> lazyLoadingElements;
    private List<VariableElement> fieldElements;
    private List<Relationship> relationships;
    private Set<TypeElement> relatedTypeElements;


    public DAOClassBundle(TypeElement mainElement) {
        this.mainElement = mainElement;
    }


    public List<VariableElement> getFieldElements() {
        if (fieldElements == null)
            fieldElements = mainElement
                    .getEnclosedElements()
                    .stream()
                    .filter(element -> element.getKind().isField() && element.getAnnotation(IgnoreField.class) == null)
                    .map(element -> (VariableElement) element)
                    .collect(Collectors.toList());
        return fieldElements;
    }

    public String getIdentificationColumnName() {
        return fieldToColumnName.apply(getPrimaryKeyElement());
    }

    public VariableElement getPrimaryKeyElement() {
        return primaryKeyElements
                .stream().findFirst().get();
    }

    public String getTableName() {
        return getEntity().tableName().isEmpty() ? mainElement.getSimpleName().toString().toLowerCase() : getEntity().tableName();
    }

    public Set<TypeElement> getRelatedTypeElements() {
        if (relatedTypeElements == null)
            relatedTypeElements = getRelationships().stream().map(
                    this::getTypeElementFromRelationship
            ).collect(Collectors.toSet());
        return relatedTypeElements;
    }

    public TypeElement getMainElement() {
        return mainElement;
    }

    public Entity getEntity() {
        if (entity == null)
            entity = mainElement.getAnnotation(Entity.class);
        return entity;
    }

    public PrimaryKey getPrimaryKey() {
        if (primaryKey == null)
            primaryKey = getPrimaryKeyElement().getAnnotation(PrimaryKey.class);
        return primaryKey;
    }

    public List<VariableElement> getPrimaryKeyElements() {
        if (primaryKeyElements == null)
            primaryKeyElements = getFieldElements()
                    .stream()
                    .filter(variableElement -> variableElement.getAnnotation(PrimaryKey.class) != null)
                    .collect(Collectors.toList());
        return primaryKeyElements;
    }

    public List<VariableElement> getSimpleFieldElements() {
        if (simpleFieldElements == null)
            simpleFieldElements = getFieldElements()
                    .stream()
                    .filter(variableElement -> variableElement.getAnnotation(Relationship.class) == null)
                    .collect(Collectors.toList());
        return simpleFieldElements;
    }

    public List<VariableElement> getSimpleRelatedFieldElements() {
        if (simpleRelatedFieldElements == null)
            simpleRelatedFieldElements = getRelatedFieldElements()
                    .stream()
                    .filter(variableElement -> !variableElement.getAnnotation(Relationship.class).lazyLoad())
                    .collect(Collectors.toList());

        return simpleRelatedFieldElements;
    }

    public List<VariableElement> getRelatedFieldElements() {
        if (relatedFieldElements == null)
            relatedFieldElements = getFieldElements()
                    .stream()
                    .filter(variableElement -> variableElement.getAnnotation(Relationship.class) != null)
                    .collect(Collectors.toList());

        return relatedFieldElements;
    }

    public List<VariableElement> getLazyLoadingElements() {
        if (lazyLoadingElements == null)
            lazyLoadingElements = getRelatedFieldElements()
                    .stream()
                    .filter(variableElement -> variableElement.getAnnotation(Relationship.class).lazyLoad())
                    .collect(Collectors.toList());

        return lazyLoadingElements;
    }

    public List<Relationship> getRelationships() {
        if (relationships == null)
            relationships = getFieldElements()
                    .stream()
                    .map(variableElement -> variableElement.getAnnotation(Relationship.class))
                    .filter(relationship -> relationship != null)
                    .collect(Collectors.toList());
        return relationships;
    }

    private TypeElement getTypeElementFromRelationship(Relationship relationship) {
        try {
            relationship.with();
        } catch (MirroredTypeException mte) {
            TypeMirror typeMirror = mte.getTypeMirror();
            return (TypeElement) ((DeclaredType) typeMirror).asElement();
        }
        return null;
    }
}
