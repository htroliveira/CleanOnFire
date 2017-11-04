package com.cleanonfire.processor.processing.data.db;

import com.cleanonfire.annotations.data.db.FieldInfo;
import com.cleanonfire.annotations.data.db.ForeignKey;
import com.cleanonfire.annotations.data.db.PrimaryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;

import static com.cleanonfire.processor.processing.data.db.Utils.fieldToColumnName;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public class DDLScriptBuilder {
    List<DAOClassBundle> bundles = new ArrayList<>();

    public void addElement(DAOClassBundle bundle) {
        bundles.add(bundle);
    }

    public String build() {
        StringBuilder script = new StringBuilder();
        for (DAOClassBundle bundle : bundles) {
            script.append(process(bundle));
        }
        return script.toString();
    }

    private String process(DAOClassBundle bundle) {
        StringBuilder scriptBuilder = new StringBuilder("create table ");
        scriptBuilder.append(bundle.getTableName()).append("(");
        for (VariableElement element : bundle.getFieldElements()) {
            scriptBuilder.append(buildColumnDefinition(element));
            if (!bundle.hasCompositePrimaryKey()) {
                scriptBuilder.append(buildSinglePrimaryKeyDefinition(element));
            }
            scriptBuilder.append(',');
        }
        if (bundle.hasCompositePrimaryKey())
            scriptBuilder.append(buildCompositePrimaryKeyDefinition(bundle.getPrimaryKeyElements())).append(',');

        for (VariableElement element : bundle.getForeignKeyElements()) {
            scriptBuilder.append(buildForeignKeyDefinition(element)).append(',');
        }

        return scriptBuilder.deleteCharAt(scriptBuilder.lastIndexOf(",")).append(");").toString();
    }


    private String buildColumnDefinition(VariableElement element) {
        FieldInfo fieldInfo = element.getAnnotation(FieldInfo.class);
        String columnName = fieldToColumnName.apply(element);
        String type = TypePersistence.forType(element.asType()).sqliteEquivalent();
        String unique ="";
        String nullable ="";
        String defaultValue ="";

        if (fieldInfo!=null) {
            unique = fieldInfo.unique() ? "UNIQUE" : "";
            nullable = fieldInfo.nullable() ? "" : "NOT NULL";
            defaultValue = fieldInfo.defaultValue();
        }
        return String.format("%s %s %s %s %s", columnName, type, nullable, unique, defaultValue);
    }


    private String buildCompositePrimaryKeyDefinition(List<VariableElement> elements) {
        StringBuilder compositePrimaryKeyDefinition = new StringBuilder("PRIMARY KEY(");
        String fields = String.join(",", elements.stream().map(fieldToColumnName).collect(Collectors.toList()));
        return compositePrimaryKeyDefinition.append(fields).append(')').toString();
    }

    private String buildSinglePrimaryKeyDefinition(VariableElement element) {
        PrimaryKey primaryKey = element.getAnnotation(PrimaryKey.class);
        if (primaryKey == null) return "";
        String autoincrement = primaryKey.autoincrement() ? "AUTOINCREMENT" : "";
        return String.format("PRIMARY KEY %s", autoincrement);
    }


    private String buildForeignKeyDefinition(VariableElement element) {
        ForeignKey foreignKey = element.getAnnotation(ForeignKey.class);
        DAOClassBundle relatedTableBundle = getRelatedTableBundle(foreignKey);
        String columnName = fieldToColumnName.apply(element);
        String relatedTable = relatedTableBundle.getTableName();
        String relatedField = fieldToColumnName.apply(relatedTableBundle.getPrimaryKeyElements().get(0));

        return String.format("FOREIGN KEY (%s) REFERENCES %s(%s)", columnName, relatedTable, relatedField);
    }

    private DAOClassBundle getRelatedTableBundle(ForeignKey foreignKey) {
        try {
            foreignKey.target();
        } catch (MirroredTypeException e) {
            return DAOClassBundle.get(e.getTypeMirror());
        }

        throw new RuntimeException("Couldn't get the related table bundle");
    }

}
