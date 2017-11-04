package com.cleanonfire.processor.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heitorgianastasio on 21/09/17.
 */

public interface Validator<T> {
    ValidationResult validate(T t) throws ProcessingException;

    class ValidationResult{
        private boolean valid;
        private List<String> messages = new ArrayList<>();

        public ValidationResult(boolean valid, List<String> messages) {
            this.valid = valid;
            this.messages.addAll(messages);
        }

        public ValidationResult(boolean valid) {
            this.valid = valid;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getMessages() {
            return messages;
        }
    }
}
