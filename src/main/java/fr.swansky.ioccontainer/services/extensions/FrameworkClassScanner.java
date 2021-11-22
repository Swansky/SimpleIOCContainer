package fr.swansky.ioccontainer.services.extensions;

import swansAPI.exception.InstanceCreationException;
import swansAPI.extensions.FrameworkExtension;
import swansAPI.models.FrameworkExtensionDetails;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

public class FrameworkClassScanner {


    public Set<FrameworkExtensionDetails> scanFrameworkClass(Set<Class<? extends FrameworkExtension>> extensionsClasses) throws InstanceCreationException {
        Set<FrameworkExtensionDetails> frameworkExtensionDetailsSet = new HashSet<>();
        try {
            for (Class<? extends FrameworkExtension> extensionsClass : extensionsClasses) {

                Constructor<? extends FrameworkExtension> constructor = extensionsClass.asSubclass(FrameworkExtension.class).getDeclaredConstructor();

                FrameworkExtensionDetails frameworkExtensionDetails
                        = new FrameworkExtensionDetails(extensionsClass, constructor);
                frameworkExtensionDetailsSet.add(frameworkExtensionDetails);
            }
        } catch (NoSuchMethodException e) {
            throw new InstanceCreationException(e.getMessage(), e);
        }
        return frameworkExtensionDetailsSet;
    }
}
