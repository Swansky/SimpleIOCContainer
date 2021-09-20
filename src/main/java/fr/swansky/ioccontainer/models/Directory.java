package fr.swansky.ioccontainer.models;

import fr.swansky.ioccontainer.directory.DirectoryType;

public class Directory {
    private final String path;
    private final DirectoryType directoryType;

    public Directory(String path, DirectoryType directoryType) {
        this.path = path;
        this.directoryType = directoryType;
    }

    public String getPath() {
        return path;
    }

    public DirectoryType getDirectoryType() {
        return directoryType;
    }
}
