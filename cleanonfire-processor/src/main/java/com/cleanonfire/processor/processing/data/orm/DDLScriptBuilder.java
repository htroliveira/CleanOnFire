package com.cleanonfire.processor.processing.data.orm;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.annotations.data.orm.FieldInfo;
import com.cleanonfire.annotations.data.orm.IgnoreField;
import com.cleanonfire.processor.core.ProcessingException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public class DDLScriptBuilder {
    List<TypeElement> elements = new ArrayList<>();

    public void addElement(TypeElement element) {
        elements.add(element);
    }

    public void build(Filer filer) throws ProcessingException {


    }

    private void process(TypeElement type) {
        Entity entity = type.getAnnotation(Entity.class);
        StringBuilder scriptBuilder = new StringBuilder("create table ");
        String tableName = entity.tableName().isEmpty() ? type.getSimpleName().toString().toLowerCase() : entity.tableName();
        scriptBuilder.append(tableName).append("{");
        for (Element element : type.getEnclosedElements()) {
            if (element.getKind().isField()) {
                StringBuilder columnBuilder = new StringBuilder();
                FieldInfo fieldInfo = element.getAnnotation(FieldInfo.class);
                String columnName = "";

            }
        }


    }

    private String buildCreateTableScript(TypeElement element) {
        StringBuilder scriptBuilder = new StringBuilder("\ncreate table ").append(getTableName(element)).append("(");
        for (Element field : element.getEnclosedElements()) {
            if (field.getKind().isField() && field.getAnnotation(IgnoreField.class) != null) {
                scriptBuilder.append(buildColumnDefinition((VariableElement) field));
            }
        }
        return scriptBuilder.append(")").toString();
    }

    private String buildColumnDefinition(VariableElement element) {
        StringBuilder columnDefBuilder = new StringBuilder(" ");
        FieldInfo fieldInfo = element.getAnnotation(FieldInfo.class);
        String columnName = getColumnName(element, fieldInfo);

        columnDefBuilder.append(TypePersistence.forType(element.asType()).columnCreate(columnName));



        return columnDefBuilder.append(",").toString();
    }

    private String getTableName(TypeElement element) {
        Entity entity = element.getAnnotation(Entity.class);
        return entity.tableName().isEmpty() ? element.getSimpleName().toString().toLowerCase() : entity.tableName();
    }
    private String getColumnName(VariableElement element, FieldInfo fieldInfo) {
        return fieldInfo.columnName().isEmpty() ? element.getSimpleName().toString() : fieldInfo.columnName();
    }
}
