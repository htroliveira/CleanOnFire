package com.cleanonfire.processor;


import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class CleanOnFireProcessor extends AbstractProcessor {

    ProcessingEnvironment processingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = processingEnvironment;
        ProcessingUtils.init(processingEnvironment);

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (SupportedAnnotation supportedAnnotation : SupportedAnnotation.values()) {
            Class<? extends Annotation> annotation = supportedAnnotation.getSupportedAnnotation();
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotation);
            try {
                supportedAnnotation.getProcessor().process(elements);
            }catch (ProcessingException pe){
                processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR,pe.getCompilerMessage(),pe.getElement());
            }
        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SupportedAnnotation.getSupportedAnnotations();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }



}
