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
import fr.swansky.ioccontainer.models.Directory;
import fr.swansky.ioccontainer.services.ServicesInstantiationServiceImpl;
import fr.swansky.ioccontainer.services.classScanning.IOCClassScanning;
import fr.swansky.ioccontainer.services.extensions.ExtensionInstantiationService;
import fr.swansky.ioccontainer.services.extensions.FrameworkClassScanner;
import fr.swansky.swansAPI.IOC;
import fr.swansky.swansAPI.classScanning.ClassScanning;
import fr.swansky.swansAPI.config.ConfigExtensionManager;
import fr.swansky.swansAPI.exception.InstanceCreationException;
import fr.swansky.swansAPI.extensions.FrameworkExtension;
import fr.swansky.swansAPI.models.FrameworkExtensionDetails;
import fr.swansky.swansAPI.models.ScannedClassDetails;
import fr.swansky.swansAPI.services.ServicesInstantiationService;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SwansIOC implements IOC {
    private static SwansIOC INSTANCE;
    private final ServicesInstantiationService servicesInstantiationService = new ServicesInstantiationServiceImpl();
    private final Class<?> startupClass;
    private final SwansIOCConfig swansIOCConfig;
    private final ClassScanning classScanning;
    private final ConfigExtensionManager configExtensionManager;
    private List<ScannedClassDetails> scannedClassDetails;
    private Set<Class<?>> allClassesScan;

    private SwansIOC(Class<?> startupClass, SwansIOCConfig swansIOCConfig) throws InstanceCreationException {
        this.startupClass = startupClass;
        this.swansIOCConfig = swansIOCConfig;
        this.configExtensionManager = new ConfigExtensionManager();
        classScanning = new IOCClassScanning();

    }

    public static SwansIOC InitIOC(Class<?> startupClass) throws InstanceCreationException {
        return InitIOC(startupClass, new SwansIOCConfig());
    }

    public static SwansIOC InitIOC(Class<?> startupClassParam, SwansIOCConfig swansIOCConfigParam) throws InstanceCreationException {
        if (INSTANCE == null)
            INSTANCE = new SwansIOC(startupClassParam, swansIOCConfigParam);
        return INSTANCE;
    }

    public static List<ScannedClassDetails> getServicesDetails() {
        return INSTANCE.scannedClassDetails;
    }

    public static Set<ScannedClassDetails> getServiceWithAnnotation(Class<? extends Annotation> annotation) {
        Set<ScannedClassDetails> scannedClassDetailsSet = new HashSet<>();
        for (ScannedClassDetails serviceDetail : INSTANCE.scannedClassDetails) {
            if (serviceDetail.getClassAnnotations().contains(annotation)) {
                scannedClassDetailsSet.add(serviceDetail);
            }
        }
        return scannedClassDetailsSet;
    }

    public static Set<Class<?>> getAllClassesScan() {
        return INSTANCE.allClassesScan;
    }

    public static SwansIOC getInstance() {
        return INSTANCE;
    }

    public void startIOC() throws InstanceCreationException {
        loadClasses();
        loadExtensions();
    }

    private void loadClasses() throws InstanceCreationException {
        Directory directory = DirectoryResolver.resolveDirectory(startupClass);

        ClassLoader<Object> classLoader = new ClassLoaderDirectory();
        if (directory.getDirectoryType() == DirectoryType.JAR_FILE) {
            classLoader = new ClassLoaderJarFile(this.swansIOCConfig, this.startupClass);
        }

        Set<Class<?>> classes = classLoader.locateClass(directory.getPath());
        allClassesScan = classes;

        scannedClassDetails = servicesInstantiationService.instantiateServices(
                classScanning.scanClassesByAnnotations(classes, getAllDefaultAnnotation()));

    }

    private Set<Class<? extends Annotation>> getAllDefaultAnnotation() {
        Set<Class<? extends Annotation>> defaultAnnotation = new HashSet<>(swansIOCConfig.getCustomAnnotations());
        defaultAnnotation.add(Service.class);
        return defaultAnnotation;
    }

    private void loadExtensions() throws InstanceCreationException {
        ClassLoader<FrameworkExtension> classLoader = new ClassLoaderExtension();
        Set<Class<? extends FrameworkExtension>> extensionsClasses = classLoader.locateClass(Constants.DEFAULT_PACKAGE);

        if (extensionsClasses.isEmpty()) {
            System.out.println("No framework extension find");
        } else {
            System.out.println(extensionsClasses.size() + " framework extensions find : \t" + extensionsClasses);
        }
        Set<FrameworkExtensionDetails> frameworkExtensionDetails = new FrameworkClassScanner().scanFrameworkClass(extensionsClasses);
        ExtensionInstantiationService extensionInstantiationService = new ExtensionInstantiationService(classScanning, allClassesScan, this);
        extensionInstantiationService.createInstanceExtensions(frameworkExtensionDetails);

    }

    public Class<?> getStartupClass() {
        return startupClass;
    }

    @Override
    public Set<Class<?>> getAllScanClass() {
        return allClassesScan;
    }

    @Override
    public ClassScanning getClassScanning() {
        return classScanning;
    }

    @Override
    public ServicesInstantiationService getInstantiationService() {
        return servicesInstantiationService;
    }

    @Override
    public ConfigExtensionManager getConfigExtensionManager() {
        return configExtensionManager;
    }
}
