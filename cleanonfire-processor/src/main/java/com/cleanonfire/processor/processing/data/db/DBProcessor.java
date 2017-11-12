package com.cleanonfire.processor.processing.data.db;

import com.cleanonfire.annotations.data.db.Database;
import com.cleanonfire.annotations.data.db.Table;
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

    public DBProcessor(Class<Database> annotationClass) {
        super(annotationClass);
    }

    @Override
    public void process(Set<? extends Element> elements, RoundEnvironment environment) throws ProcessingException {
        DDLScriptBuilder ddlScriptBuilder = new DDLScriptBuilder();
        CleanOnFireDBClassBuilder cleanOnFireDBClassBuilder = new CleanOnFireDBClassBuilder();
        if (elements.size()>1) {
            throw new RuntimeException("You must have only one @Database specification");
        }if (elements.size()<=0){
            throw new RuntimeException("You must have at least one @Database specification");

        }else{
            Database db = elements.iterator().next().getAnnotation(Database.class);
            cleanOnFireDBClassBuilder.setVersion(db.version());
            cleanOnFireDBClassBuilder.setDbName(db.name());
        }

        for (Element element : environment.getElementsAnnotatedWith(Table.class)){
            DAOClassBundle bundle = DAOClassBundle.get((TypeElement) element);
            Validator.ValidationResult validation = validator.validate(bundle);
            if (validation.isValid()) {
                try {
                    ddlScriptBuilder.addElement(bundle);
                    JavaFile idFile = new IDClassBuilder(bundle).build();
                    buildFile(idFile);
                    ClassName idClassName = ClassName.get(idFile.packageName,idFile.typeSpec.name);

                    bundle.setIdClassName(idClassName);
                    JavaFile daoFile = new DAOClassBuilder(bundle).build();
                    buildFile(daoFile);
                    ClassName daoClassName = ClassName.get(daoFile.packageName,daoFile.typeSpec.name);
                    bundle.setIdClassName(idClassName);
                    cleanOnFireDBClassBuilder.addDaoClassName(daoClassName);


                } catch (IOException e) {
                    throw new ProcessingException(annotationClass, element, Collections.singletonList("Was not possible to write the file"));
                }
            } else {
                throw new ProcessingException(annotationClass, element, validation.getMessages());
            }
        }
        cleanOnFireDBClassBuilder.setSqlCreateScript(ddlScriptBuilder.build());
        try {
            buildFile(cleanOnFireDBClassBuilder.build());
        } catch (IOException e) {}
    }



    private void buildFile(JavaFile file) throws IOException {
        file.writeTo(ProcessingUtils.getFiler());
    }


}
