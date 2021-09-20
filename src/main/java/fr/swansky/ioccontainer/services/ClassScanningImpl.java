package fr.swansky.ioccontainer.services;

import fr.swansky.ioccontainer.annotations.Autowired;
import fr.swansky.ioccontainer.annotations.Service;
import fr.swansky.ioccontainer.config.SwansIOCConfig;
import fr.swansky.ioccontainer.models.ServiceDetails;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassScanningImpl implements ClassScanning {
    private final SwansIOCConfig swansIOCConfig;
    private final Set<ServiceDetails> serviceDetailsSet;

    public ClassScanningImpl(SwansIOCConfig swansIOCConfig) {
        this.swansIOCConfig = swansIOCConfig;
        this.serviceDetailsSet = new HashSet<>();
    }

    @Override
    public Set<ServiceDetails> scanClasses(Set<Class<?>> classes) {
        this.serviceDetailsSet.clear();

        for (Class<?> classService : classes) {
            if (classService.isInterface()) {
                continue;
            }
            findClassesWithAnnotations(classService,swansIOCConfig.getCustomAnnotations());
            findClassesWithAnnotation(classService,Service.class);

        }

        return serviceDetailsSet;
    }

    private void findClassesWithAnnotations(Class<?> classService, List<Class<? extends Annotation>> annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            findClassesWithAnnotation(classService,annotation);
        }
    }

    private void findClassesWithAnnotation(Class<?> classService, Class<? extends Annotation> annotation) {
        if (classService.isAnnotationPresent(annotation)) {
            ServiceDetails serviceDetails = new ServiceDetails(
                    classService,
                    classService.getAnnotation(annotation),
                    findConstructor(classService)
            );
            serviceDetailsSet.add(serviceDetails);
        }
    }


    private Constructor<?> findConstructor(Class<?> classService) {
        if (classService.getDeclaredConstructors().length > 1) {
            for (Constructor<?> declaredConstructor : classService.getDeclaredConstructors()) {
                if (declaredConstructor.isAnnotationPresent(Autowired.class)) {
                    return declaredConstructor;
                }
            }
        }
        return classService.getDeclaredConstructors()[0];
    }
}
