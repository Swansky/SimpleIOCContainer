package fr.swansky.ioccontainer.models;

import fr.swansky.swansAPI.extensions.FrameworkExtension;

import java.lang.reflect.Constructor;

public class FrameworkExtensionDetails {

    private final Class<? extends FrameworkExtension> classType;
    private FrameworkExtension instance;
    private final Constructor<? extends FrameworkExtension> constructor;

    public FrameworkExtensionDetails(Class<? extends FrameworkExtension> classType, Constructor<? extends FrameworkExtension> constructor) {
        this.classType = classType;
        this.constructor = constructor;
    }

    public Class<? extends FrameworkExtension> getClassType() {
        return classType;
    }

    public FrameworkExtension getInstance() {
        return instance;
    }

    public void setInstance(FrameworkExtension instance) {
        this.instance = instance;
    }

    public Constructor<? extends FrameworkExtension> getConstructor() {
        return constructor;
    }

}
