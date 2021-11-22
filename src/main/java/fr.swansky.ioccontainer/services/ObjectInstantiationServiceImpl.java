package fr.swansky.ioccontainer.services;

import fr.swansky.swansAPI.exception.InstanceCreationException;
import fr.swansky.swansAPI.extensions.FrameworkExtension;
import fr.swansky.swansAPI.models.FrameworkExtensionDetails;
import fr.swansky.swansAPI.models.ScannedClassDetails;
import fr.swansky.swansAPI.services.ObjectInstantiationService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ObjectInstantiationServiceImpl implements ObjectInstantiationService {


    public void createInstanceService(ScannedClassDetails scannedClassDetails, Object... params) throws InstanceCreationException {
        Constructor<?> constructor = scannedClassDetails.getConstructor();
        if (constructor.getParameterCount() != params.length) {
            throw new InstanceCreationException("no same count of parameters ! ");
        }
        try {
            Object object = constructor.newInstance(params);
            scannedClassDetails.setInstance(object);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InstanceCreationException(e.getMessage(), e);
        }
    }


    public void createInstanceExtension(FrameworkExtensionDetails frameworkExtensionDetails, Object... params) throws InstanceCreationException {
        Constructor<?> constructor = frameworkExtensionDetails.getConstructor();

        try {
            Object object = constructor.newInstance(params);
            frameworkExtensionDetails.setInstance((FrameworkExtension) object);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InstanceCreationException(e.getMessage(), e);
        }
    }


}
