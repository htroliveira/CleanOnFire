package com.cleanonfire.processor.core;

import java.util.List;

import javax.lang.model.element.Element;

/**
 * Created by heitorgianastasio on 21/09/17.
 */

public interface ElementValidator {
    ValidationResult validate(Element elementToValidate) throws ProcessingException;

    class ValidationResult{
        private boolean valid;
        private List<String> messages;

        public ValidationResult(boolean valid, List<String> messages) {
            this.valid = valid;
            this.messages = messages;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getMessages() {
            return messages;
        }
    }
}
