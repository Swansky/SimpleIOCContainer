package fr.swansky.ioccontainer;

import fr.swansky.ioccontainer.classLoader.ClassLoader;
import fr.swansky.ioccontainer.classLoader.ClassLoaderDirectory;
import fr.swansky.ioccontainer.classLoader.ClassLoaderJarFile;
import fr.swansky.ioccontainer.config.SwansIOCConfig;
import fr.swansky.ioccontainer.directory.DirectoryResolver;
import fr.swansky.ioccontainer.directory.DirectoryType;
import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.ioccontainer.models.Directory;
import fr.swansky.ioccontainer.models.ServiceDetails;
import fr.swansky.ioccontainer.services.ClassScanning;
import fr.swansky.ioccontainer.services.ClassScanningImpl;
import fr.swansky.ioccontainer.services.ServicesInstantiationService;
import fr.swansky.ioccontainer.services.ServicesInstantiationServiceImpl;
import fr.swansky.ioccontainer.tests.commands.CommandContainer;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SwansIOC {
    private static List<ServiceDetails> serviceDetails;

    public static void main(String[] args) {

        try {
            SwansIOCConfig swansIOCConfig = new SwansIOCConfig();
            swansIOCConfig.addCustomAnnotations(CommandContainer.class);
            run(SwansIOC.class,swansIOCConfig);
            Set<ServiceDetails> serviceWithAnnotation = getServiceWithAnnotation(CommandContainer.class);
            System.out.println(serviceWithAnnotation);
        } catch (InstanceCreationException e) {
            e.printStackTrace();
        }
    }

    public static void run(Class<?> startupClass) throws InstanceCreationException {
        run(startupClass, new SwansIOCConfig());
    }

    public static void run(Class<?> startupClass, SwansIOCConfig swansIOCConfig) throws InstanceCreationException {
        ClassScanning classScanning = new ClassScanningImpl(swansIOCConfig);
        ServicesInstantiationService servicesInstantiationService = new ServicesInstantiationServiceImpl(swansIOCConfig);
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

    public static Set<ServiceDetails> getServiceWithAnnotation(Class<? extends Annotation> annotation) {
        Set<ServiceDetails> serviceDetailsSet = new HashSet<>();
        for (ServiceDetails serviceDetail : serviceDetails) {
            if (serviceDetail.getAnnotation().annotationType() == annotation) {
                serviceDetailsSet.add(serviceDetail);
            }
        }
        return serviceDetailsSet;
    }
}
