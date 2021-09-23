package fr.swansky.ioccontainer;

import fr.swansky.ioccontainer.annotations.Service;
import fr.swansky.ioccontainer.classLoader.ClassLoader;
import fr.swansky.ioccontainer.classLoader.ClassLoaderDirectory;
import fr.swansky.ioccontainer.classLoader.ClassLoaderExtension;
import fr.swansky.ioccontainer.classLoader.ClassLoaderJarFile;
import fr.swansky.ioccontainer.config.SwansIOCConfig;
import fr.swansky.ioccontainer.constants.Constants;
import fr.swansky.ioccontainer.directory.DirectoryResolver;
import fr.swansky.ioccontainer.directory.DirectoryType;
import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.ioccontainer.models.Directory;
import fr.swansky.ioccontainer.models.FrameworkExtensionDetails;
import fr.swansky.ioccontainer.services.ServicesInstantiationService;
import fr.swansky.ioccontainer.services.ServicesInstantiationServiceImpl;
import fr.swansky.ioccontainer.services.classScanning.IOCClassScanning;
import fr.swansky.ioccontainer.services.extensions.ExtensionInstantiationService;
import fr.swansky.ioccontainer.services.extensions.FrameworkClassScanner;
import fr.swansky.ioccontainer.tests.commands.CommandContainer;
import fr.swansky.swansAPI.classScanning.ClassScanning;
import fr.swansky.swansAPI.extensions.FrameworkExtension;
import fr.swansky.swansAPI.models.ScannedClassDetails;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SwansIOC {
    private static final ServicesInstantiationService servicesInstantiationService = new ServicesInstantiationServiceImpl();
    private static List<ScannedClassDetails> scannedClassDetails;
    private static Set<Class<?>> allClassesScan;
    private static Class<?> startupClass;
    private static SwansIOCConfig swansIOCConfig;
    private static ClassScanning classScanning;

    public static void main(String[] args) {
        try {
            SwansIOCConfig swansIOCConfig = new SwansIOCConfig();
            swansIOCConfig.addCustomAnnotations(CommandContainer.class);
            run(SwansIOC.class, swansIOCConfig);
        } catch (InstanceCreationException e) {
            e.printStackTrace();
        }

    }

    public static void run(Class<?> startupClass) throws InstanceCreationException {
        run(startupClass, new SwansIOCConfig());
    }

    public static void run(Class<?> startupClassParam, SwansIOCConfig swansIOCConfigParam) throws InstanceCreationException {
        startupClass = startupClassParam;
        swansIOCConfig = swansIOCConfigParam;
        classScanning = new IOCClassScanning();
        loadClasses();
        loadExtensions();

    }


    private static void loadClasses() throws InstanceCreationException {
        Directory directory = DirectoryResolver.resolveDirectory(startupClass);

        ClassLoader<Object> classLoader = new ClassLoaderDirectory();
        if (directory.getDirectoryType() == DirectoryType.JAR_FILE) {
            classLoader = new ClassLoaderJarFile();
        }

        Set<Class<?>> classes = classLoader.locateClass(directory.getPath());
        allClassesScan = classes;

        scannedClassDetails = servicesInstantiationService.instantiateServices(
                classScanning.scanClassesByAnnotations(classes, getAllDefaultAnnotation()));

    }

    private static Set<Class<? extends Annotation>> getAllDefaultAnnotation() {
        Set<Class<? extends Annotation>> defaultAnnotation = new HashSet<>(swansIOCConfig.getCustomAnnotations());
        defaultAnnotation.add(Service.class);
        return defaultAnnotation;
    }

    private static void loadExtensions() throws InstanceCreationException {
        ClassLoader<FrameworkExtension> classLoader = new ClassLoaderExtension();
        Set<Class<? extends FrameworkExtension>> extensionsClasses = classLoader.locateClass(Constants.DEFAULT_PACKAGE);

        if (extensionsClasses.isEmpty()) {
            System.out.println("No framework extension find");
        } else {
            System.out.println(extensionsClasses.size() + " framework extensions find : \t" + extensionsClasses);
        }
        Set<FrameworkExtensionDetails> frameworkExtensionDetails = new FrameworkClassScanner().scanFrameworkClass(extensionsClasses);
        ExtensionInstantiationService extensionInstantiationService = new ExtensionInstantiationService(classScanning, allClassesScan);
        extensionInstantiationService.createInstanceExtensions(frameworkExtensionDetails);

    }


    public static List<ScannedClassDetails> getServicesDetails() {
        return scannedClassDetails;
    }

    public static Set<ScannedClassDetails> getServiceWithAnnotation(Class<? extends Annotation> annotation) {
        Set<ScannedClassDetails> scannedClassDetailsSet = new HashSet<>();
        for (ScannedClassDetails serviceDetail : scannedClassDetails) {
            if (serviceDetail.getClassAnnotations().contains(annotation)) {
                scannedClassDetailsSet.add(serviceDetail);
            }
        }
        return scannedClassDetailsSet;
    }

    public static Set<Class<?>> getAllClassesScan() {
        return allClassesScan;
    }


}
