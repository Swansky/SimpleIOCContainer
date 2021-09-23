package fr.swansky.ioccontainer.services;

import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.swansAPI.models.ScannedClassDetails;

import java.util.List;
import java.util.Set;

public interface ServicesInstantiationService {
    List<ScannedClassDetails>   instantiateServices(Set<ScannedClassDetails> scannedClassDetailsSet) throws InstanceCreationException;
}
