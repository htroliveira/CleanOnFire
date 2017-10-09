package com.cleanonfire.processor.processing.data.orm;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.annotations.data.orm.IgnoreField;
import com.cleanonfire.annotations.data.orm.PrimaryKey;
import com.cleanonfire.processor.core.ElementValidator;
import com.cleanonfire.processor.core.GenericAnnotationProcessor;
import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.cleanonfire.processor.utils.AndroidFrameworkClassNames.CURSOR;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.BASE_DAO;
import static com.cleanonfire.processor.utils.CleanOnFireClassNames.CLEAN_SQLITE_HELPER;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public class EntityOrmProcessor extends GenericAnnotationProcessor<Entity> {
    EntityOrmValidator validator = new EntityOrmValidator();


    public EntityOrmProcessor(Class<Entity> annotationClass) {
        super(annotationClass);
    }

    @Override
    public void process(Set<? extends Element> elements) throws ProcessingException {
        for (Element element : elements) {
            ElementValidator.ValidationResult validation = validator.validate(element);
            if (validation.isValid()) {
                new DAOClassBuilder().buildDao((TypeElement) element);
            } else {
                throw new ProcessingException(annotationClass, element, validation.getMessages());
            }
        }
    }


}
