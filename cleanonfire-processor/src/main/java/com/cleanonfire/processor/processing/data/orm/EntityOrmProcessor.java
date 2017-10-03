package com.cleanonfire.processor.processing.data.orm;

import com.cleanonfire.annotations.data.orm.Entity;
import com.cleanonfire.processor.core.ElementValidator;
import com.cleanonfire.processor.core.GenericAnnotationProcessor;
import com.cleanonfire.processor.core.ProcessingException;

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
            ElementValidator.ValidationResult validation = validator.validate(element);
            if (validation.isValid()) {
                buildDao((TypeElement) element);
            } else {
                throw  new ProcessingException(annotationClass,element, validation.getMessages());
            }
        }
    }

    private void buildDao(TypeElement element){

    }


}
