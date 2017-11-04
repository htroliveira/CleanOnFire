package com.cleanonfire.processor.processing.hellotest;

import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.core.Validator;

import javax.lang.model.element.Element;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public class SaysHelloValidator implements Validator<Element> {
    @Override
    public ValidationResult validate(Element elementToValidate) throws ProcessingException {

        return new ValidationResult(true);
    }
}
