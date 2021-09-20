package fr.swansky.ioccontainer.services;

import fr.swansky.ioccontainer.annotations.Autowired;
import fr.swansky.ioccontainer.annotations.Service;
import fr.swansky.ioccontainer.models.ServiceDetails;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

public class ClassScanningImpl implements ClassScanning {
    @Override
    public Set<ServiceDetails> scanClasses(Set<Class<?>> classes) {
        Set<ServiceDetails> serviceDetailsSet = new HashSet<>();

        for (Class<?> classService : classes) {
            if (classService.isInterface()) {
                continue;
            }
            if (classService.isAnnotationPresent(Service.class)) {
                ServiceDetails serviceDetails = new ServiceDetails(
                        classService,
                        classService.getAnnotation(Service.class),
                        findAutowiredConstructor(classService)
                );
                serviceDetailsSet.add(serviceDetails);
            }
        }

        return serviceDetailsSet;
    }

    private Constructor<?> findAutowiredConstructor(Class<?> classService) {
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
