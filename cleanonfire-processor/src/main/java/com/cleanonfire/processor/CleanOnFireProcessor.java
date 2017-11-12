package com.cleanonfire.processor;


import com.cleanonfire.processor.core.ProcessingException;
import com.cleanonfire.processor.utils.ProcessingUtils;
import com.google.auto.service.AutoService;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.StandardLocation;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class  CleanOnFireProcessor extends AbstractProcessor {

    ProcessingEnvironment processingEnvironment;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = processingEnvironment;
        ProcessingUtils.init(processingEnvironment);
        createFileOi(processingEnvironment.getFiler());

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        for (SupportedAnnotation supportedAnnotation : SupportedAnnotation.values()) {
            Class<? extends Annotation> annotation = supportedAnnotation.getSupportedAnnotation();
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(annotation);
            try {
                if (elements.isEmpty()) continue;
                supportedAnnotation.getProcessor().process(elements,roundEnvironment);
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


    private void createFileOi(Filer filer){
        try {
            //FileObject fileObject = filer.getResource(StandardLocation.SOURCE_OUTPUT,"assets","oi.json");
            //PrintWriter writer = new PrintWriter(fileObject.openWriter());
            Map<String,String> options = processingEnvironment.getOptions();
            options.keySet();
            //writer.printf("{\"oi\":true}");
            //writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
