package fr.swansky.ioccontainer.classLoader;

import fr.swansky.ioccontainer.exceptions.ClassLocationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static fr.swansky.ioccontainer.constants.Constants.JAVA_BINARY_EXTENSION;

public class ClassLoaderDirectory implements ClassLoader<Object> {
    @Override
    public Set<Class<?>> locateClass(String path) throws ClassLocationException {
        Set<Class<?>> classes = new HashSet<>();
        try {
            Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .filter(this::isClass)
                    .forEach(path1 -> {
                        final String pathName = path1.toAbsolutePath().toString()
                                .split("/target/classes/")[1]
                                .replaceAll("/", ".")
                                .replaceAll("\\\\", ".")
                                .replaceAll(JAVA_BINARY_EXTENSION + "$", "");

                        try {
                            classes.add(Class.forName(pathName));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            //throw new ClassLocationException(e.getMessage(), e);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            //throw new ClassLocationException(e.getMessage(), e);
        }


        return classes;
    }

    private boolean isClass(Path path) {
        return path.toString().endsWith(".class");
    }
}
