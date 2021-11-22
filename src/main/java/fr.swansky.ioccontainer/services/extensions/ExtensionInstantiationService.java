package fr.swansky.ioccontainer.services.extensions;

import fr.swansky.ioccontainer.SwansIOC;
import swansAPI.classScanning.ClassScanning;
import swansAPI.exception.InstanceCreationException;
import swansAPI.extensions.FrameworkExtension;
import swansAPI.models.FrameworkExtensionDetails;

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
            FrameworkExtension object = constructor.newInstance();
            frameworkExtensionDetails.setInstance(object);
            object.setIoc(swansIOC);
            object.load();

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new InstanceCreationException(e.getMessage(), e);
        }
    }
}
