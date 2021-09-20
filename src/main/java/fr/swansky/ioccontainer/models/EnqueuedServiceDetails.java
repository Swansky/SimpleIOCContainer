package fr.swansky.ioccontainer.models;

public class EnqueuedServiceDetails {
    private final ServiceDetails serviceDetails;
    private final Class<?>[] neededClass;
    private final Object[] dependantsInstances;

    public EnqueuedServiceDetails(ServiceDetails serviceDetails) {

        this.serviceDetails = serviceDetails;
        neededClass = serviceDetails.getConstructor().getParameterTypes();
        dependantsInstances = new Object[neededClass.length];
    }


    public boolean isResolved() {
        for (Object dependantsInstance : this.dependantsInstances) {
            if (dependantsInstance == null)
                return false;
        }
        return true;
    }

    public ServiceDetails getServiceDetails() {
        return serviceDetails;
    }

    public Object[] getDependantsInstances() {
        return dependantsInstances;
    }

    public boolean isDependencyRequired(ServiceDetails serviceDetails) {
        for (Class<?> aClass : neededClass) {
            if (aClass.equals(serviceDetails.getServiceType())) {
                return true;
            }
        }
        return false;
    }

    public void addDependency(ServiceDetails serviceDetails) {
        if (isDependencyRequired(serviceDetails)) {
            for (int i = 0; i < this.neededClass.length; i++) {
                if (this.neededClass[i].equals(serviceDetails.getServiceType())) {
                    dependantsInstances[i] = serviceDetails.getInstance();
                }
            }
        }
    }

}
