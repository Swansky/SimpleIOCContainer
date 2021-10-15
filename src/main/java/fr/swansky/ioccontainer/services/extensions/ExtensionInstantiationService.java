package fr.swansky.ioccontainer.services.extensions;

import fr.swansky.ioccontainer.SwansIOC;
import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.ioccontainer.models.FrameworkExtensionDetails;
import fr.swansky.swansAPI.classScanning.ClassScanning;
import fr.swansky.swansAPI.extensions.FrameworkExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class ExtensionInstantiationService {
    private final ClassScanning classScanning;
    private final Set<Class<?>> allScanClasses;
    private final SwansIOC swansIOC;


    public ExtensionInstantiationService(ClassScanning classScanning, Set<Class<?>> allScanClasses, SwansIOC swansIOC) {
        this.classScanning = classScanning;
        this.allScanClasses = allScanClasses;
        this.swansIOC = swansIOC;
    }

    public void createInstanceExtensions(Set<FrameworkExtensionDetails> frameworkExtensionDetailsSet) throws InstanceCreationException {
        try {
            for (FrameworkExtensionDetails frameworkExtensionDetails : frameworkExtensionDetailsSet) {
                createInstanceExtension(frameworkExtensionDetails, allScanClasses, classScanning);
            }
        } catch (InstanceCreationException e) {
            throw new InstanceCreationException(e.getMessage(), e);
        }
    }


    private void createInstanceExtension(FrameworkExtensionDetails frameworkExtensionDetails, Set<Class<?>> allScanClasses, ClassScanning classScanning) throws InstanceCreationException {
        Constructor<? extends FrameworkExtension> constructor = frameworkExtensionDetails.getConstructor();

        try {
            FrameworkExtension object =  constructor.newInstance();
            frameworkExtensionDetails.setInstance(object);
            object.setAllScanClass(allScanClasses);
            object.setClassScanning(classScanning);
            object.setIoc(swansIOC);
            object.load();

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InstanceCreationException(e.getMessage(), e);
        }
    }
}
