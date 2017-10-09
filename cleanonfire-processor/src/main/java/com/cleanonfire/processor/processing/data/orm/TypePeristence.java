package com.cleanonfire.processor.processing.data.orm;

import com.cleanonfire.annotations.data.orm.Field;
import com.squareup.javapoet.ClassName;

import java.util.Date;

import javax.lang.model.type.TypeMirror;

import static com.cleanonfire.processor.processing.data.orm.ORMSupportedClassNames.DATE_CLASSNAME;

/**
 * Created by heitorgianastasio on 08/10/17.
 */

public enum TypePeristence {

    DATE(DATE_CLASSNAME){
        @Override
        public String columnCreate(String columnName, String defaultValue, Field field) {
            return new StringBuilder(columnName)
                    .append(" ")
                    .append("")
                    .toString();
        }

        @Override
        public String cursorParsing() {
            return null;
        }
    };


    TypePeristence(ClassName className) {
        this.className = className;
    }

    ClassName className;
    public abstract String columnCreate(String columName, String defaultValue, Field field);
    public abstract String cursorParsing();

    public static TypePeristence forType(TypeMirror typeMirror){
        for (TypePeristence typePeristence : TypePeristence.values()) {
            if (typePeristence.className.toString().equals(typeMirror.toString())) return typePeristence;
        }
        return null;
    }
}
