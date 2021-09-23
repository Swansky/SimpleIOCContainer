package fr.swansky.ioccontainer.models;

import fr.swansky.swansAPI.models.ScannedClassDetails;

public class EnqueuedServiceDetails {
    private final ScannedClassDetails scannedClassDetails;
    private final Class<?>[] neededClass;
    private final Object[] dependantsInstances;

    public EnqueuedServiceDetails(ScannedClassDetails scannedClassDetails) {

        this.scannedClassDetails = scannedClassDetails;
        neededClass = scannedClassDetails.getConstructor().getParameterTypes();
        dependantsInstances = new Object[neededClass.length];
    }


    public boolean isResolved() {
        for (Object dependantsInstance : this.dependantsInstances) {
            if (dependantsInstance == null)
                return false;
        }
        return true;
    }

    public ScannedClassDetails getServiceDetails() {
        return scannedClassDetails;
    }

    public Object[] getDependantsInstances() {
        return dependantsInstances;
    }

    public boolean isDependencyRequired(ScannedClassDetails scannedClassDetails) {
        for (Class<?> aClass : neededClass) {
            if (aClass.equals(scannedClassDetails.getClassType())) {
                return true;
            }
        }
        return false;
    }

    public void addDependency(ScannedClassDetails scannedClassDetails) {
        if (isDependencyRequired(scannedClassDetails)) {
            for (int i = 0; i < this.neededClass.length; i++) {
                if (this.neededClass[i].equals(scannedClassDetails.getClassType())) {
                    dependantsInstances[i] = scannedClassDetails.getInstance();
                }
            }
        }
    }

}
