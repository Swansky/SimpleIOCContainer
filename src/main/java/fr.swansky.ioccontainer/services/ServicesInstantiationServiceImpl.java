package fr.swansky.ioccontainer.services;

import fr.swansky.ioccontainer.constants.Constants;
import fr.swansky.ioccontainer.exceptions.InstantiationsException;
import fr.swansky.ioccontainer.models.EnqueuedServiceDetails;
import swansAPI.exception.InstanceCreationException;
import swansAPI.models.ScannedClassDetails;
import swansAPI.services.ObjectInstantiationService;
import swansAPI.services.ServicesInstantiationService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ServicesInstantiationServiceImpl implements ServicesInstantiationService {
    private final ObjectInstantiationService objectInstantiationService;
    private final LinkedList<EnqueuedServiceDetails> enqueuedServiceDetailsList;
    private final List<ScannedClassDetails> resolvedServices;

    private final List<Class<?>> alreadyInstantiated;

    public ServicesInstantiationServiceImpl() {

        objectInstantiationService = new ObjectInstantiationServiceImpl();
        enqueuedServiceDetailsList = new LinkedList<>();
        this.resolvedServices = new ArrayList<>();
        alreadyInstantiated = new ArrayList<>();
    }

    @Override
    public List<ScannedClassDetails> instantiateServices(Set<ScannedClassDetails> scannedClassDetailsSet) throws InstanceCreationException {
        init(scannedClassDetailsSet);
        int iteration = 0;
        while (!this.enqueuedServiceDetailsList.isEmpty()) {
            if (iteration > Constants.MAX_ITERATION) {
                throw new InstantiationsException("Max iteration for instantiation loop.");
            }
            iteration++;
            EnqueuedServiceDetails enqueuedServiceDetails = this.enqueuedServiceDetailsList.removeFirst();

            if (this.alreadyInstantiated.contains(enqueuedServiceDetails.getServiceDetails().getClassType())) {
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
        this.objectInstantiationService.createInstanceService(
                enqueuedServiceDetails.getServiceDetails(),
                enqueuedServiceDetails.getDependantsInstances()
        );
        this.registerInstantiatedService(enqueuedServiceDetails);
    }

    private void registerInstantiatedService(EnqueuedServiceDetails enqueuedServiceDetails) {
        for (EnqueuedServiceDetails enqueuedService : this.enqueuedServiceDetailsList) {
            enqueuedService.addDependency(enqueuedServiceDetails.getServiceDetails());
        }
        alreadyInstantiated.add(enqueuedServiceDetails.getServiceDetails().getClassType());
        resolvedServices.add(enqueuedServiceDetails.getServiceDetails());
    }

    private void init(Set<ScannedClassDetails> scannedClassDetailsSet) {

        for (ScannedClassDetails scannedClassDetails : scannedClassDetailsSet) {
            enqueuedServiceDetailsList.add(new EnqueuedServiceDetails(scannedClassDetails));
        }
    }
}
