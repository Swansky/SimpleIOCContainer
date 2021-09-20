package fr.swansky.ioccontainer.tests;

import fr.swansky.ioccontainer.annotations.Autowired;
import fr.swansky.ioccontainer.annotations.Service;

@Service
public class SwansPlugin {

    @Autowired
    public SwansPlugin(Repository repository) {
        System.out.println("Create SwansPlugin ! ");
    }

}
