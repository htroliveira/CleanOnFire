package com.cleanonfire.processor.processing.hellotest;

import com.cleanonfire.processor.core.ElementValidator;
import com.cleanonfire.processor.core.ProcessingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Element;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public class SaysHelloValidator implements ElementValidator {
    @Override
    public ValidationResult validate(Element elementToValidate) throws ProcessingException {

        return new ValidationResult(true, Arrays.asList("Eu não quis","Tava feio"));
    }
}
