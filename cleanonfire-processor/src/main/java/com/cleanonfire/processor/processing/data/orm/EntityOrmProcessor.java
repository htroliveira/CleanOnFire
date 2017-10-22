package com.cleanonfire.processor.processing.data.orm;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.processor.core.Validator;
import com.cleanonfire.processor.core.GenericAnnotationProcessor;
import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

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
            DAOClassBundle bundle = new DAOClassBundle((TypeElement) element);
            Validator.ValidationResult validation = validator.validate(bundle);
            if (validation.isValid()) {
                try {
                    buildFile(new DAOClassBuilder(bundle).build());
                } catch (IOException e) {
                    throw new ProcessingException(annotationClass, element, Collections.singletonList("Was not possible to write the file"));
                }
            } else {
                throw new ProcessingException(annotationClass, element, validation.getMessages());
            }
        }
    }


    private void buildFile(JavaFile file) throws IOException {
        file.writeTo(ProcessingUtils.getFiler());
    }


}
