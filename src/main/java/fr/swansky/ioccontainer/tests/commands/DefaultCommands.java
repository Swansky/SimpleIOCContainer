package fr.swansky.ioccontainer.tests.commands;

import fr.swansky.ioccontainer.tests.SwansPlugin;

@CommandContainer
public class DefaultCommands {
    public DefaultCommands(SwansPlugin swansPlugin) {

        System.out.println("default commands"+swansPlugin.hashCode());
    }
}
