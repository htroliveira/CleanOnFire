package com.cleanonfire.processor.processing.data.db;

import com.cleanonfire.annotations.data.db.Migrate;
import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.core.Validator;
import com.cleanonfire.processor.processing.Utils;
import com.squareup.javapoet.TypeName;

import java.util.Collections;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import static com.cleanonfire.processor.utils.CleanOnFireClassNames.MIGRATION;

/**
 * Created by heitorgianastasio on 19/11/17.
 */

public class DBValidator implements Validator<DBClassBundle> {
    @Override
    public ValidationResult validate(DBClassBundle dbClassBundle) throws ProcessingException {
        ValidationResult mainElementValidation = validateMainElement(dbClassBundle.getMainElement());
        if(!mainElementValidation.isValid())
            return new ValidationResult(false, mainElementValidation.getMessages());

        for (VariableElement element : dbClassBundle.getMigrations()) {
            ValidationResult migratioValidation = validateMigration(element);
            if(!migratioValidation.isValid())
                return new ValidationResult(false, migratioValidation.getMessages());
        }

        return new ValidationResult(true);
    }

    private ValidationResult validateMainElement(TypeElement element){
        if(!element.getKind().isInterface())
            return new ValidationResult(false, Collections.singletonList("The @Database specification must be an interface"));

        ValidationResult typeValidation = Utils.validateTypeElement(element);
        if(!typeValidation.isValid())
            return new ValidationResult(false, typeValidation.getMessages());

        return new ValidationResult(true);
    }

    private ValidationResult validateMigration(VariableElement element){
        if(!TypeName.get(element.asType()).equals(MIGRATION)){
            String message = String.format("The @Migrate annotated element %s specification must be an instance of %s.",element.getSimpleName(),MIGRATION.reflectionName());
            return new ValidationResult(false, Collections.singletonList(message));

        }


        Migrate migrate = element.getAnnotation(Migrate.class);
        if(migrate.fromVersion()>=migrate.toVersion()){
            String message = String.format("The @Migrate annotated element %s toVersion value must be greater than the fromVersion value.",element.getSimpleName());
            return new ValidationResult(false, Collections.singletonList(message));

        }

        return new ValidationResult(true);
    }
}
