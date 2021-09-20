package fr.swansky.ioccontainer.services;

import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.ioccontainer.models.ServiceDetails;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ObjectInstantiationServiceImpl implements ObjectInstantiationService {


    @Override
    public void createInstance(ServiceDetails serviceDetails, Object... params) throws InstanceCreationException {
        Constructor<?> constructor = serviceDetails.getConstructor();
        if (constructor.getParameterCount() != params.length) {
            throw new InstanceCreationException("no same count of parameters ! ");
        }
        try {
            Object object = constructor.newInstance(params);
            serviceDetails.setInstance(object);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InstanceCreationException(e.getMessage(), e);
        }
    }
}
