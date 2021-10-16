package fr.swansky.ioccontainer.tests.commands;

import fr.swansky.discordCommandIOC.Commands.annotations.Command;
import fr.swansky.discordCommandIOC.Commands.annotations.CommandsContainer;
import fr.swansky.ioccontainer.tests.Test;

@CommandsContainer
public class DefaultCommands {
    public DefaultCommands() {
        System.out.println("test default commands containers");
    }

    @Command(name = "test", description = "")
    public void test(Test test) {
        test.toTest();
    }
}
