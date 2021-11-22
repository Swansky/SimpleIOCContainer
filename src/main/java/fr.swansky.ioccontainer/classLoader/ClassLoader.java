package fr.swansky.ioccontainer.classLoader;

import fr.swansky.ioccontainer.exceptions.ClassLocationException;

import java.util.Set;

public interface ClassLoader<T> {
    Set<Class<? extends T>> locateClass(String path) throws ClassLocationException;
}
