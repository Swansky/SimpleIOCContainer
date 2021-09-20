package fr.swansky.ioccontainer;

import fr.swansky.ioccontainer.classLoader.ClassLoader;
import fr.swansky.ioccontainer.classLoader.ClassLoaderDirectory;
import fr.swansky.ioccontainer.classLoader.ClassLoaderJarFile;
import fr.swansky.ioccontainer.directory.DirectoryResolver;
import fr.swansky.ioccontainer.directory.DirectoryType;
import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.ioccontainer.models.Directory;
import fr.swansky.ioccontainer.models.ServiceDetails;
import fr.swansky.ioccontainer.services.ClassScanning;
import fr.swansky.ioccontainer.services.ClassScanningImpl;
import fr.swansky.ioccontainer.services.ServicesInstantiationService;
import fr.swansky.ioccontainer.services.ServicesInstantiationServiceImpl;

import java.util.List;
import java.util.Set;

public class SwansIOC {
    private static List<ServiceDetails> serviceDetails;

    public static void main(String[] args) {

        try {
            run(SwansIOC.class);
        } catch (InstanceCreationException e) {
            e.printStackTrace();
        }
    }


    public static void run(Class<?> startupClass) throws InstanceCreationException {
        ClassScanning classScanning = new ClassScanningImpl();
        ServicesInstantiationService servicesInstantiationService = new ServicesInstantiationServiceImpl();
        Directory directory = new DirectoryResolver().resolveDirectory(startupClass);


        ClassLoader classLoader = new ClassLoaderDirectory();
        if (directory.getDirectoryType() == DirectoryType.JAR_FILE) {
            classLoader = new ClassLoaderJarFile();
        }
        Set<Class<?>> classes = classLoader.locateClass(directory.getPath());

        serviceDetails = servicesInstantiationService.instantiateServices(classScanning.scanClasses(classes));
    }

    public static List<ServiceDetails> getServiceDetails() {
        return serviceDetails;
    }
}
