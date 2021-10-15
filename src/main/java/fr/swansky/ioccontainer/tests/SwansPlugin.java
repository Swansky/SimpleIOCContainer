package fr.swansky.ioccontainer.tests;


import fr.swansky.ioccontainer.config.SwansIOCConfig;
import fr.swansky.ioccontainer.exceptions.InstanceCreationException;
import fr.swansky.ioccontainer.tests.commands.CommandContainer;

import static fr.swansky.ioccontainer.SwansIOC.run;


public class SwansPlugin {

    public static void main(String[] args) {
        try {
            SwansIOCConfig swansIOCConfig = new SwansIOCConfig();
            swansIOCConfig.addCustomAnnotations(CommandContainer.class);
            run(SwansPlugin.class, swansIOCConfig);
            System.out.println("test");
        } catch (InstanceCreationException e) {
            e.printStackTrace();
        }

    }

}
