package fr.swansky.ioccontainer.classLoader;

import fr.swansky.ioccontainer.exceptions.ClassLocationException;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static fr.swansky.ioccontainer.constants.Constants.JAVA_BINARY_EXTENSION;

public class ClassLoaderJarFile implements ClassLoader {
    @Override
    public Set<Class<?>> locateClass(String path) throws ClassLocationException {
        Set<Class<?>> classes = new HashSet<>();

        try {
            JarFile file = new JarFile(path);

            Enumeration<JarEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();

                if (!jarEntry.getName().endsWith(JAVA_BINARY_EXTENSION)) {
                    continue;
                }
                final String className = jarEntry.getName().replace(JAVA_BINARY_EXTENSION, "")
                        .replaceAll("\\\\", ".")
                        .replaceAll("/", ".");

                classes.add(Class.forName(className));
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new ClassLocationException(e.getMessage(), e);
        }
        return classes;
    }
}
