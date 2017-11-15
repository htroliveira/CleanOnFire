package com.cleanonfire.processor.processing.data.db;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.type.TypeMirror;

import static com.cleanonfire.processor.processing.data.db.TypePersistence.ORMSupportedClassNames.BYTEARRAY_CLASSNAME;
import static com.cleanonfire.processor.processing.data.db.TypePersistence.ORMSupportedClassNames.DATE_CLASSNAME;
import static com.cleanonfire.processor.processing.data.db.TypePersistence.ORMSupportedClassNames.DOUBLE_CLASSNAME;
import static com.cleanonfire.processor.processing.data.db.TypePersistence.ORMSupportedClassNames.FLOAT_CLASSNAME;
import static com.cleanonfire.processor.processing.data.db.TypePersistence.ORMSupportedClassNames.INTEGER_CLASSNAME;
import static com.cleanonfire.processor.processing.data.db.TypePersistence.ORMSupportedClassNames.LONG_CLASSNAME;
import static com.cleanonfire.processor.processing.data.db.TypePersistence.ORMSupportedClassNames.STRING_CLASSNAME;


/**
 * Created by heitorgianastasio on 08/10/17.
 */

public enum TypePersistence {

    DATE(DATE_CLASSNAME) {
        @Override
        public String sqliteEquivalent() {
            return "INTEGER";
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("new $T($L.getLong($S))", DATE_CLASSNAME, cursorVariableName, columnName);
        }

        @Override
        public Statement contentValuesParsing(String valuesVariableName, String columnName, String retrieveField) {
            return new Statement("if($L != null) $L.put($S,$L.getTime())",retrieveField, valuesVariableName,columnName,retrieveField);
        }


    },


    INT(TypeName.INT, INTEGER_CLASSNAME) {
        @Override
        public String sqliteEquivalent() {
            return "INTEGER";
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getInt($S)", cursorVariableName, columnName);
        }
    },

    LONG(TypeName.LONG, LONG_CLASSNAME) {
        @Override
        public String sqliteEquivalent() {
            return "INTEGER";
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getLong($S)", cursorVariableName, columnName);
        }
    },
    STRING(STRING_CLASSNAME) {
        @Override
        public String sqliteEquivalent() {
            return "VARCHAR";
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getString($S)", cursorVariableName, columnName);
        }
    },
    BYTEARRAY(BYTEARRAY_CLASSNAME) {
        @Override
        public String sqliteEquivalent() {
            return "BLOB";
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getBlob($S)", cursorVariableName, columnName);
        }
    },


    FLOAT(TypeName.FLOAT, FLOAT_CLASSNAME) {
        @Override
        public String sqliteEquivalent() {
            return "REAL";
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getFloat($S)", cursorVariableName, columnName);
        }
    },

    DOUBLE(DOUBLE_CLASSNAME, TypeName.DOUBLE) {
        @Override
        public String sqliteEquivalent() {
            return "REAL";
        }

        @Override
        public Statement cursorParsing(String cursorVariableName, String columnName) {
            return new Statement("$L.getDouble($S)", cursorVariableName, columnName);
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
        throw new RuntimeException("Type " + typeMirror.toString() + " is not supported");
    }

    public abstract String sqliteEquivalent();

    public abstract Statement cursorParsing(String cursorVariableName, String columnName);

    public Statement contentValuesParsing(String valuesVariableName, String columnName, String retrieveField){
        return new Statement("$L.put($S,$L)",valuesVariableName,columnName,retrieveField);
    };

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

    public static final class ORMSupportedClassNames {

        public static ClassName DATE_CLASSNAME = ClassName.get(Date.class);
        public static ClassName STRING_CLASSNAME = ClassName.get(String.class);
        public static ClassName INTEGER_CLASSNAME = ClassName.get(Integer.class);
        public static ClassName FLOAT_CLASSNAME = ClassName.get(Float.class);
        public static ClassName DOUBLE_CLASSNAME = ClassName.get(Double.class);
        public static ClassName LONG_CLASSNAME = ClassName.get(Long.class);
        public static TypeName BYTEARRAY_CLASSNAME = TypeName.get(byte[].class);
        private ORMSupportedClassNames() {
        }

    }
}
