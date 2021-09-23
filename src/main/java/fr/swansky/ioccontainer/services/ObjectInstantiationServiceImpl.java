package fr.swansky.ioccontainer.services;

import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.ioccontainer.models.FrameworkExtensionDetails;
import fr.swansky.swansAPI.extensions.FrameworkExtension;
import fr.swansky.swansAPI.models.ScannedClassDetails;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ObjectInstantiationServiceImpl implements ObjectInstantiationService {


    @Override
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
