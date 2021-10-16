package fr.swansky.ioccontainer.tests.commands;

import fr.swansky.discordCommandIOC.Commands.annotations.CommandsContainer;

@CommandsContainer
public class AdminCommands {
    public AdminCommands() {
        System.out.println("test admin commands containers");
    }
}
