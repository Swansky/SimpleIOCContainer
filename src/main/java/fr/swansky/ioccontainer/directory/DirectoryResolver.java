package fr.swansky.ioccontainer.directory;

import fr.swansky.ioccontainer.constants.Constants;
import fr.swansky.ioccontainer.models.Directory;

import java.io.File;

public class DirectoryResolver {

   public static Directory resolveDirectory(Class<?> startupClass)
   {
       final String path = startupClass.getProtectionDomain().getCodeSource().getLocation().getPath();
       return new Directory(path,getDirectoryType(path));
   }

    private static DirectoryType getDirectoryType(String path) {
       if(!new File(path).isDirectory() && path.endsWith(Constants.JAR_FILE_EXTENSION)){
           return DirectoryType.JAR_FILE;
       }
       return DirectoryType.DIRECTORY;
    }
}
