package fr.swansky.ioccontainer.services;

import fr.swansky.ioccontainer.config.SwansIOCConfig;
import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.ioccontainer.exceptions.InstantiationsException;
import fr.swansky.ioccontainer.models.EnqueuedServiceDetails;
import fr.swansky.ioccontainer.models.ServiceDetails;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ServicesInstantiationServiceImpl implements ServicesInstantiationService {
    private final ObjectInstantiationService objectInstantiationService;
    private final LinkedList<EnqueuedServiceDetails> enqueuedServiceDetailsList;
    private final List<ServiceDetails> resolvedServices;
    private final SwansIOCConfig swansIOCConfig;
    private final List<Class<?>> alreadyInstantiated;

    public ServicesInstantiationServiceImpl(SwansIOCConfig swansIOCConfig) {
        this.swansIOCConfig = swansIOCConfig;
        objectInstantiationService = new ObjectInstantiationServiceImpl();
        enqueuedServiceDetailsList = new LinkedList<>();
        this.resolvedServices = new ArrayList<>();
        alreadyInstantiated = new ArrayList<>();
    }

    @Override
    public List<ServiceDetails> instantiateServices(Set<ServiceDetails> serviceDetailsSet) throws InstanceCreationException {
        this.alreadyInstantiated.clear();
        this.resolvedServices.clear();
        enqueuedServiceDetailsList.clear();

        init(serviceDetailsSet);
        int iteration = 0;
        while (!this.enqueuedServiceDetailsList.isEmpty()) {
            if (iteration > swansIOCConfig.getMaxIteration()) {
                throw new InstantiationsException("Max iteration for instantiation loop.");
            }
            iteration++;
            EnqueuedServiceDetails enqueuedServiceDetails = this.enqueuedServiceDetailsList.removeFirst();

            if (this.alreadyInstantiated.contains(enqueuedServiceDetails.getServiceDetails().getServiceType())) {
                continue;
            }
            if (enqueuedServiceDetails.isResolved()) {
                createInstanceOfService(enqueuedServiceDetails);
            } else {
                this.enqueuedServiceDetailsList.addLast(enqueuedServiceDetails);
            }
        }
        return resolvedServices;
    }

    private void createInstanceOfService(EnqueuedServiceDetails enqueuedServiceDetails) throws InstanceCreationException {
        this.objectInstantiationService.createInstance(
                enqueuedServiceDetails.getServiceDetails(),
                enqueuedServiceDetails.getDependantsInstances()
        );
        this.registerInstantiatedService(enqueuedServiceDetails);
    }

    private void registerInstantiatedService(EnqueuedServiceDetails enqueuedServiceDetails) {
        for (EnqueuedServiceDetails enqueuedService : this.enqueuedServiceDetailsList) {
            enqueuedService.addDependency(enqueuedServiceDetails.getServiceDetails());
        }
        alreadyInstantiated.add(enqueuedServiceDetails.getServiceDetails().getServiceType());
        resolvedServices.add(enqueuedServiceDetails.getServiceDetails());
    }

    private void init(Set<ServiceDetails> serviceDetailsSet) throws InstanceCreationException {

        for (ServiceDetails serviceDetails : serviceDetailsSet) {
            enqueuedServiceDetailsList.add(new EnqueuedServiceDetails(serviceDetails));
        }
    }
}
