package com.cleanonfire.processor.processing.data.db;

import com.cleanonfire.annotations.data.db.FieldInfo;
import com.cleanonfire.annotations.data.db.ForeignKey;
import com.cleanonfire.annotations.data.db.IgnoreField;
import com.cleanonfire.annotations.data.db.PrimaryKey;
import com.cleanonfire.annotations.data.db.Table;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.ClassName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public class DAOClassBundle {
    private static Map<TypeMirror,DAOClassBundle> BUNDLE_MAP = new HashMap<>();


    public static DAOClassBundle get(TypeElement element){
        return BUNDLE_MAP.computeIfAbsent(element.asType(), k -> new DAOClassBundle(element));
    }

    public static DAOClassBundle get(TypeMirror typeMirror){
        return BUNDLE_MAP.computeIfAbsent(typeMirror, m -> new DAOClassBundle((TypeElement) ProcessingUtils.getTypeUtils().asElement(m)));
    }




    private TypeElement mainElement;
    private Table table;
    private List<VariableElement> primaryKeyElements;
    private List<VariableElement> foreignKeyElements;
    private List<VariableElement> fieldElements;
    private PackageElement packageElement;
    private ClassName idClassName;


    private DAOClassBundle(TypeElement mainElement) {
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

    public PackageElement getPackageElement() {
        if (packageElement == null) {
            packageElement = ProcessingUtils.getElementUtils().getPackageOf(mainElement);
        }
        return packageElement;
    }

    public String getTableName() {
        return getTable().tableName().isEmpty() ? mainElement.getSimpleName().toString().toLowerCase() : getTable().tableName();
    }

    public TypeElement getMainElement() {
        return mainElement;
    }

    public Table getTable() {
        if (table == null)
            table = mainElement.getAnnotation(Table.class);
        return table;
    }


    public List<VariableElement> getPrimaryKeyElements() {
        if (primaryKeyElements == null)
            primaryKeyElements = getFieldElements()
                    .stream()
                    .filter(variableElement -> variableElement.getAnnotation(PrimaryKey.class) != null)
                    .collect(Collectors.toList());
        return primaryKeyElements;
    }


    public List<VariableElement> getForeignKeyElements() {
        if (foreignKeyElements == null)
            foreignKeyElements = getFieldElements()
                    .stream()
                    .filter(variableElement -> variableElement.getAnnotation(ForeignKey.class) != null)
                    .collect(Collectors.toList());

        return foreignKeyElements;
    }

    public boolean hasCompositePrimaryKey(){
        return getPrimaryKeyElements().size()>1;
    }


    public ClassName getIdClassName() {
        return idClassName;
    }

    public void setIdClassName(ClassName idClassName) {
        this.idClassName = idClassName;
    }
}
