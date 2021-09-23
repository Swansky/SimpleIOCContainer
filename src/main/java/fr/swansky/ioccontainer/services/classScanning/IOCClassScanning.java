package fr.swansky.ioccontainer.services.classScanning;

import fr.swansky.ioccontainer.annotations.Autowired;
import fr.swansky.swansAPI.classScanning.ClassScanning;
import fr.swansky.swansAPI.models.ScannedClassDetails;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class IOCClassScanning implements ClassScanning {


    @Override
    public Set<ScannedClassDetails> scanClassesByAnnotations(Set<Class<?>> classes, Set<Class<? extends Annotation>> classAnnotations) {
        Set<ScannedClassDetails> scannedClassDetailsSet = new HashSet<>();
        for (Class<?> aClass : classes) {
            Optional<ScannedClassDetails> scannedClassDetails = scanClassByAnnotations(aClass, classAnnotations);
            scannedClassDetails.ifPresent(scannedClassDetailsSet::add);
        }
        return scannedClassDetailsSet;
    }

    private Optional<ScannedClassDetails> scanClassByAnnotations(Class<?> scanClass, Set<Class<? extends Annotation>> classAnnotations) {

        if (scanClass.getAnnotations().length > 0) {

            Set<Class<? extends Annotation>> annotations = new HashSet<>();

            for (Class<? extends Annotation> classAnnotation : classAnnotations) {
                if (scanClass.isAnnotationPresent(classAnnotation)) {
                    annotations.add(classAnnotation);
                }
            }
            if (!annotations.isEmpty()) {
                return Optional.of(new ScannedClassDetails(scanClass, annotations, findConstructor(scanClass)));

            }
        }
        return Optional.empty();
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

    @Override
    public Set<ScannedClassDetails> scanClassesAndMethodsByAnnotations(Set<Class<?>> classes, Set<Class<? extends Annotation>> classAnnotations, Set<Class<? extends Annotation>> methodAnnotations) {
        Set<ScannedClassDetails> scannedClassDetailsSet = new HashSet<>();
        for (Class<?> aClass : classes) {
            Optional<ScannedClassDetails> scannedClassDetails = scanClassByAnnotations(aClass, classAnnotations);
            if (scannedClassDetails.isPresent()) {
                scanMethodsByAnnotations(scannedClassDetails.get(), methodAnnotations);
                scannedClassDetailsSet.add(scannedClassDetails.get());
            }
        }
        return scannedClassDetailsSet;
    }

    private void scanMethodsByAnnotations(ScannedClassDetails scannedClassDetails, Set<Class<? extends Annotation>> methodAnnotations) {
        for (Method method : scannedClassDetails.getClass().getMethods()) {
            scanMethodByAnnotations(scannedClassDetails, method, methodAnnotations);
        }
    }

    private void scanMethodByAnnotations(ScannedClassDetails scannedClassDetails, Method method, Set<Class<? extends Annotation>> methodAnnotations) {
        Set<Annotation> annotations = new HashSet<>();
        for (Class<? extends Annotation> methodAnnotation : methodAnnotations) {
            if (method.isAnnotationPresent(methodAnnotation)) {
                annotations.add(method.getAnnotation(methodAnnotation));
            }
        }
        if (!annotations.isEmpty()) {
            scannedClassDetails.addMethod(method, annotations);
        }

    }
}
