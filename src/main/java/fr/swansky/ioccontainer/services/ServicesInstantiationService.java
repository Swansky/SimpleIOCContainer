package fr.swansky.ioccontainer.services;

import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.ioccontainer.models.ServiceDetails;

import java.util.List;
import java.util.Set;

public interface ServicesInstantiationService {
    List<ServiceDetails> instantiateServices(Set<ServiceDetails> serviceDetailsSet) throws InstanceCreationException;
}
