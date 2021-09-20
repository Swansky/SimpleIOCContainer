package fr.swansky.ioccontainer.tests;

import fr.swansky.ioccontainer.annotations.Autowired;
import fr.swansky.ioccontainer.annotations.Service;

@Service
public class Repository {

    @Autowired
    public Repository() {
        System.out.println("repository created !");
    }
}
