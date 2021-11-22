package fr.swansky.ioccontainer.classLoader;

import fr.swansky.ioccontainer.exceptions.ClassLocationException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static fr.swansky.ioccontainer.constants.Constants.JAVA_BINARY_EXTENSION;

public class ClassLoaderJarFile implements ClassLoader<Object> {
    @Override
    public Set<Class<?>> locateClass(String path) throws ClassLocationException {
        Set<Class<?>> classes = new HashSet<>();
        System.out.println("1");
        try {
            JarFile file = new JarFile(path);
            System.out.println("2");
            Enumeration<JarEntry> entries = file.entries();
            System.out.println("3");
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                System.out.println("4");
                if (!jarEntry.getName().endsWith(JAVA_BINARY_EXTENSION)) {
                    continue;
                }
                System.out.println(jarEntry.getName());
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
