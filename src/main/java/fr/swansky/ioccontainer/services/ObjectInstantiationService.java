package fr.swansky.ioccontainer.services;

import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.ioccontainer.models.FrameworkExtensionDetails;
import fr.swansky.swansAPI.models.ScannedClassDetails;

public interface ObjectInstantiationService {
    void createInstanceService(ScannedClassDetails scannedClassDetails, Object... params) throws InstanceCreationException;


}
