package com.cleanonfire.processor.processing.data.db;

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

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Created by heitorgianastasio on 02/10/17.
 */

public class EntityOrmProcessor extends GenericAnnotationProcessor<Table> {
    private EntityOrmValidator validator = new EntityOrmValidator();

    public EntityOrmProcessor(Class<Table> annotationClass) {
        super(annotationClass);
    }

    @Override
    public void process(Set<? extends Element> elements) throws ProcessingException {
        DDLScriptBuilder ddlScriptBuilder = new DDLScriptBuilder();
        CleanOnFireDBClassBuilder cleanOnFireDBClassBuilder = new CleanOnFireDBClassBuilder();
        for (Element element : elements) {
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
