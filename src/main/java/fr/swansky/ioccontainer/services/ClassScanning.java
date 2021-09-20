package fr.swansky.ioccontainer.services;

import fr.swansky.ioccontainer.models.ServiceDetails;

import java.util.Set;

public interface ClassScanning {
    Set<ServiceDetails> scanClasses(Set<Class<?>> classes);

}
