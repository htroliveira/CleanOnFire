package com.cleanonfire.processor.processing.data.db;

import com.cleanonfire.annotations.data.db.Database;
import com.cleanonfire.annotations.data.db.Migrate;
import com.cleanonfire.annotations.data.db.Table;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by heitorgianastasio on 19/11/17.
 */

public class DBClassBundle {
    private TypeElement mainElement;
    private Database database;
    private List<VariableElement> migrations;
    private List<ClassName> daoNames = new ArrayList<>();
    private String sqlCreateScript;


    public DBClassBundle(TypeElement mainElement) {
        this.mainElement = mainElement;
    }

    public TypeElement getMainElement() {
        return mainElement;
    }

    public Database getDatabase() {
        if (database == null) {
            database = getMainElement().getAnnotation(Database.class);
        }
        return database;
    }

    public List<VariableElement> getMigrations() {
        if (migrations == null) {
            migrations = getMainElement()
                    .getEnclosedElements()
                    .stream()
                    .filter(element-> element.getKind().isField())
                    .filter(element -> element.getAnnotation(Migrate.class)!= null)
                    .map(element-> (VariableElement) element)
                    .collect(Collectors.toList());
        }
        return migrations;
    }

    public List<ClassName> getDaoNames() {
        return daoNames;
    }

    public void addDaoClassName(ClassName className) {
        daoNames.add(className);
    }

    public String getSqlCreateScript() {
        return sqlCreateScript;
    }

    public void setSqlCreateScript(String sqlCreateScript) {
        this.sqlCreateScript = sqlCreateScript;
    }
}
