package fr.swansky.ioccontainer.services;

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
    private final int MAX_ITERATION = 10000;

    public ServicesInstantiationServiceImpl() {
        objectInstantiationService = new ObjectInstantiationServiceImpl();
        enqueuedServiceDetailsList = new LinkedList<>();
        this.resolvedServices = new ArrayList<>();
    }

    @Override
    public List<ServiceDetails> instantiateServices(Set<ServiceDetails> serviceDetailsSet) throws InstanceCreationException {

        init(serviceDetailsSet);
        int iteration = 0;
        while (!this.enqueuedServiceDetailsList.isEmpty()) {
            if (iteration > MAX_ITERATION) {
                throw new InstantiationsException("Max iteration for instantiation loop.");
            }
            iteration++;
            EnqueuedServiceDetails enqueuedServiceDetails = this.enqueuedServiceDetailsList.removeFirst();

            if (enqueuedServiceDetails.isResolved()) {
                this.objectInstantiationService.createInstance(
                        enqueuedServiceDetails.getServiceDetails(),
                        enqueuedServiceDetails.getDependantsInstances()
                );
                this.registerInstantiatedService(enqueuedServiceDetails);
            } else {
                this.enqueuedServiceDetailsList.addLast(enqueuedServiceDetails);
            }
        }
        return resolvedServices;
    }

    private void registerInstantiatedService(EnqueuedServiceDetails enqueuedServiceDetails) {
        for (EnqueuedServiceDetails enqueuedService : this.enqueuedServiceDetailsList) {
            enqueuedService.addDependency(enqueuedServiceDetails.getServiceDetails());
        }
        resolvedServices.add(enqueuedServiceDetails.getServiceDetails());
    }

    private void init(Set<ServiceDetails> serviceDetailsSet) throws InstanceCreationException {

        for (ServiceDetails serviceDetails : serviceDetailsSet) {
            enqueuedServiceDetailsList.add(new EnqueuedServiceDetails(serviceDetails));
        }
    }
}
