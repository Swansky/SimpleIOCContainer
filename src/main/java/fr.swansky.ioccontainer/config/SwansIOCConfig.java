package fr.swansky.ioccontainer.config;

import fr.swansky.ioccontainer.annotations.Service;

import java.lang.annotation.Annotation;
import java.util.*;

public class SwansIOCConfig {
    private final Set<Class<? extends Annotation>> customAnnotations;
    private int maxIteration = 10000;


    public SwansIOCConfig() {
        this.customAnnotations = new HashSet<>();
    }


    public int getMaxIteration() {
        return maxIteration;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    public Set<Class<? extends Annotation>> getCustomAnnotations() {
        return customAnnotations;
    }

    public void addCustomAnnotation(Class<? extends Annotation> customAnnotation) {
        customAnnotations.add(customAnnotation);
    }
    @SafeVarargs
    public final void addCustomAnnotations(Class<? extends Annotation>... customAnnotations) {
        for (Class<? extends Annotation> customAnnotation : customAnnotations) {
            addCustomAnnotation(customAnnotation);
        }
    }
}
