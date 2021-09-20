package fr.swansky.ioccontainer.config;

import fr.swansky.ioccontainer.annotations.Service;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwansIOCConfig {
    private final List<Class<? extends Annotation>> customAnnotations;
    private int maxIteration = 10000;


    public SwansIOCConfig() {
        this.customAnnotations = new ArrayList<>();
    }


    public int getMaxIteration() {
        return maxIteration;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    public List<Class<? extends Annotation>> getCustomAnnotations() {
        return Collections.unmodifiableList(customAnnotations);
    }

    public void addCustomAnnotation(Class<? extends Annotation> customAnnotation) {
        if (!customAnnotations.contains(customAnnotation)) {
            customAnnotations.add(customAnnotation);
        }
    }
}
