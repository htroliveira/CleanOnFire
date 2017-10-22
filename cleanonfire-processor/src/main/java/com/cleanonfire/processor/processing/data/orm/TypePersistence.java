package com.cleanonfire.processor.processing.data.orm;

import com.squareup.javapoet.TypeName;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.type.TypeMirror;

import static com.cleanonfire.processor.processing.data.orm.ORMSupportedClassNames.BYTEARRAY_CLASSNAME;
import static com.cleanonfire.processor.processing.data.orm.ORMSupportedClassNames.DATE_CLASSNAME;
import static com.cleanonfire.processor.processing.data.orm.ORMSupportedClassNames.DOUBLE_CLASSNAME;
import static com.cleanonfire.processor.processing.data.orm.ORMSupportedClassNames.FLOAT_CLASSNAME;
import static com.cleanonfire.processor.processing.data.orm.ORMSupportedClassNames.INTEGER_CLASSNAME;
import static com.cleanonfire.processor.processing.data.orm.ORMSupportedClassNames.LONG_CLASSNAME;
import static com.cleanonfire.processor.processing.data.orm.ORMSupportedClassNames.STRING_CLASSNAME;


/**
 * Created by heitorgianastasio on 08/10/17.
 */

public enum TypePersistence {

    DATE(DATE_CLASSNAME) {
        @Override
        public String columnCreate(String columnName) {
            return new StringBuilder(columnName).append(" ")
                    .append("INTEGER")
                    .toString();
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("new $T($L.getLong($L.getColumnIndex($S)))", DATE_CLASSNAME, cursorVariableName, cursorVariableName, columnName);
        }

        @Override
        public String contentValuesParsing() {
            return ".getTime()";
        }
    },


    INT(TypeName.INT,INTEGER_CLASSNAME) {
        @Override
        public String columnCreate(String columnName) {
            return new StringBuilder(columnName).append(" ")
                    .append("INTEGER")
                    .toString();
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getInt($L.getColumnIndex($S))", cursorVariableName, cursorVariableName, columnName);
        }
    },

    LONG(TypeName.LONG,LONG_CLASSNAME) {
        @Override
        public String columnCreate(String columnName) {
            return new StringBuilder(columnName).append(" ")
                    .append("INTEGER")
                    .toString();
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getLong($L.getColumnIndex($S))", cursorVariableName, cursorVariableName, columnName);
        }
    },
    STRING(STRING_CLASSNAME) {
        @Override
        public String columnCreate(String columnName) {
            return new StringBuilder(columnName).append(" ")
                    .append("VARCHAR")
                    .toString();
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getString($L.getColumnIndex($S))", cursorVariableName, cursorVariableName, columnName);
        }
    },
    BYTEARRAY(BYTEARRAY_CLASSNAME) {
        @Override
        public String columnCreate(String columnName) {
            return new StringBuilder(columnName).append(" ")
                    .append("BLOB")
                    .toString();
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getBlob($L.getColumnIndex($S))", cursorVariableName, cursorVariableName, columnName);
        }
    },


    FLOAT(TypeName.FLOAT,FLOAT_CLASSNAME) {
        @Override
        public String columnCreate(String columnName) {
            return new StringBuilder(columnName).append(" ")
                    .append("REAL")
                    .toString();
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getFloat($L.getColumnIndex($S))", cursorVariableName, cursorVariableName, columnName);
        }
    },

    DOUBLE(DOUBLE_CLASSNAME,TypeName.DOUBLE) {
        @Override
        public String columnCreate(String columnName) {
            return new StringBuilder(columnName).append(" ")
                    .append("REAL")
                    .toString();
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getDouble($L.getColumnIndex($S))", cursorVariableName, cursorVariableName, columnName);
        }
    };


    List<TypeName> typeNames;

    TypePersistence(TypeName... typeNames) {
        this.typeNames = Arrays.asList(typeNames);
    }

    public static TypePersistence forType(TypeMirror typeMirror) {
        for (TypePersistence typePersistence : TypePersistence.values()) {
            if (typePersistence.typeNames
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toList())
                    .contains(typeMirror.toString().replace("()", "")))
                return typePersistence;
        }
        return null;
    }

    public abstract String columnCreate(String columnName);

    public abstract Statement cursorParsing(String cursorVariableName, String columnName);

    public String contentValuesParsing() {
        return "";
    }

    public static class Statement {
        String statement;
        Object[] args;

        public Statement(String statement, Object... args) {
            this.statement = statement;
            this.args = args;
        }

        public String getStatement() {
            return statement;
        }

        public Object[] getArgs() {
            return args;
        }
    }
}
