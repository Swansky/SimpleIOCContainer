package fr.swansky.ioccontainer.classLoader;

import fr.swansky.ioccontainer.exceptions.ClassLocationException;
import org.reflections.Reflections;
import swansAPI.extensions.FrameworkExtension;

import java.util.Set;

public class ClassLoaderExtension implements ClassLoader<FrameworkExtension> {

    @Override
    public Set<Class<? extends FrameworkExtension>> locateClass(String path) throws ClassLocationException {
        Reflections reflections = new Reflections(path);

        return reflections.getSubTypesOf(FrameworkExtension.class);
    }
}
