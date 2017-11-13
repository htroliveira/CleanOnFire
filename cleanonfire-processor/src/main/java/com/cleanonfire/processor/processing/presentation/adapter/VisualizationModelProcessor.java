package com.cleanonfire.processor.processing.presentation.adapter;

import com.cleanonfire.annotations.presentation.adapter.VisualizationModel;
import com.cleanonfire.processor.core.ClassBuilder;
import com.cleanonfire.processor.core.GenericAnnotationProcessor;
import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.core.Validator;
import com.cleanonfire.processor.processing.presentation.adapter.classbuilders.AdapterClassBuilder;
import com.cleanonfire.processor.processing.presentation.adapter.classbuilders.ViewHolderBinderClassBuilder;
import com.cleanonfire.processor.processing.presentation.adapter.classbuilders.ViewHolderClassBuilder;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Created by heitorgianastasio on 12/11/17.
 */

public class VisualizationModelProcessor extends GenericAnnotationProcessor<VisualizationModel> {
    VisualizationModelValidator validator = new VisualizationModelValidator();

    public VisualizationModelProcessor(Class<VisualizationModel> annotationClass) {
        super(annotationClass);
    }

    @Override
    public void process(Set<? extends Element> elements, RoundEnvironment env) throws ProcessingException {
        for (Element element : elements) {

            AdapterClassBundle bundle = AdapterClassBundle.get((TypeElement) element);
            Validator.ValidationResult validationResult = validator.validate(bundle);
            if (validationResult.isValid()) {
                AdapterClassBuilder adapterBuilder = new AdapterClassBuilder(bundle);
                ViewHolderBinderClassBuilder binderClassBuilder = new ViewHolderBinderClassBuilder(bundle);
                ViewHolderClassBuilder viewHolderClassBuilder = new ViewHolderClassBuilder(bundle);
                try {
                    buildFile(adapterBuilder);
                    buildFile(viewHolderClassBuilder);
                    buildFile(binderClassBuilder);
                } catch (Exception e) {
                    throw new ProcessingException(annotationClass, element, Collections.singletonList(e.getMessage()));
                }
            } else {
                throw new ProcessingException(annotationClass,element,validationResult.getMessages());
            }
        }
    }

    void buildFile(ClassBuilder builder) throws IOException {
        builder.build().writeTo(ProcessingUtils.getFiler());
    }
}
