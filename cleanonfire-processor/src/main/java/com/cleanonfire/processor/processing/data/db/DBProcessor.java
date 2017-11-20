package com.cleanonfire.processor.processing.data.db;

import com.cleanonfire.annotations.data.db.Database;
import com.cleanonfire.annotations.data.db.Table;
import com.cleanonfire.processor.core.ClassBuilder;
import com.cleanonfire.processor.core.GenericAnnotationProcessor;
import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.core.Validator;
import com.cleanonfire.processor.processing.data.db.classbuilders.CleanOnFireDBClassBuilder;
import com.cleanonfire.processor.processing.data.db.classbuilders.DAOClassBuilder;
import com.cleanonfire.processor.processing.data.db.classbuilders.IDClassBuilder;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public class DBProcessor extends GenericAnnotationProcessor<Database> {
    private TableDBValidator validator = new TableDBValidator();
    private DBValidator dbValidator = new DBValidator();

    public DBProcessor(Class<Database> annotationClass) {
        super(annotationClass);
    }

    @Override
    public void process(Set<? extends Element> elements, RoundEnvironment environment) throws ProcessingException {
        DDLScriptBuilder ddlScriptBuilder = new DDLScriptBuilder();

        if (elements.size() > 1) {
            throw new RuntimeException("You must have only one @Database specification");
        }
        DBClassBundle dbClassBundle = new DBClassBundle((TypeElement) elements.iterator().next());
        Validator.ValidationResult dbValidation = dbValidator.validate(dbClassBundle);
        if (!dbValidation.isValid()) {
            throw new ProcessingException(Database.class, dbClassBundle.getMainElement(), dbValidation.getMessages());
        }

        for (Element element : environment.getElementsAnnotatedWith(Table.class)) {
            DAOClassBundle bundle = DAOClassBundle.get((TypeElement) element);
            Validator.ValidationResult validation = validator.validate(bundle);
            if (validation.isValid()) {
                try {
                    ddlScriptBuilder.addElement(bundle);

                    String packageName = ProcessingUtils.getElementUtils().getPackageOf(element).getQualifiedName().toString();

                    buildFile(new IDClassBuilder(bundle));
                    ClassName idClassName = ClassName.get(packageName, element.getSimpleName()+"ID") ;
                    bundle.setIdClassName(idClassName);

                    buildFile(new DAOClassBuilder(bundle));
                    ClassName daoClassName = ClassName.get(packageName,element.getSimpleName()+"CleanDAO");
                    dbClassBundle.addDaoClassName(daoClassName);


                } catch (IOException e) {
                    throw new ProcessingException(Table.class, element, Collections.singletonList("Was not possible to write the file"));
                }
            } else {
                throw new ProcessingException(Table.class, element, validation.getMessages());
            }
        }
        dbClassBundle.setSqlCreateScript(ddlScriptBuilder.build());
        try {
            buildFile(new CleanOnFireDBClassBuilder(dbClassBundle));
        } catch (IOException e) {
        }
    }


    private void buildFile(ClassBuilder builder) throws IOException {
        builder.build().writeTo(ProcessingUtils.getFiler());
    }


}
