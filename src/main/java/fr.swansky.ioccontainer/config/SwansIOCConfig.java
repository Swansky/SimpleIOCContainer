package fr.swansky.ioccontainer.config;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static fr.swansky.ioccontainer.constants.Constants.DEFAULT_PACKAGE;

public class SwansIOCConfig {
    private final Set<Class<? extends Annotation>> customAnnotations;
    private final List<String> packageNameToScan = new ArrayList<>();
    private int maxIteration = 10000;

    public SwansIOCConfig() {
        this.customAnnotations = new HashSet<>();
        this.packageNameToScan.add(DEFAULT_PACKAGE);
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

    public List<String> getPackageNameToScan() {
        return packageNameToScan;
    }
}
