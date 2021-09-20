package fr.swansky.ioccontainer.classLoader;

import fr.swansky.ioccontainer.exceptions.ClassLocationException;

import java.util.Set;

public interface ClassLoader {
    Set<Class<?>> locateClass(String path) throws ClassLocationException;
}
