package fr.swansky.ioccontainer.services;

import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.ioccontainer.models.ServiceDetails;

public interface ObjectInstantiationService {
    void createInstance(ServiceDetails serviceDetails, Object ... params) throws InstanceCreationException;
}
