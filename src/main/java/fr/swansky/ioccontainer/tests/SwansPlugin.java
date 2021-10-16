package fr.swansky.ioccontainer.tests;


import fr.swansky.discordCommandIOC.DiscordCommandIOC;
import fr.swansky.discordCommandIOC.config.DiscordCommandIOCConfig;
import fr.swansky.ioccontainer.SwansIOC;
import fr.swansky.ioccontainer.config.SwansIOCConfig;
import fr.swansky.swansAPI.exception.InstanceCreationException;


import static fr.swansky.ioccontainer.SwansIOC.CreateIOC;


public class SwansPlugin {

    public static void main(String[] args) {
        try {
            SwansIOCConfig swansIOCConfig = new SwansIOCConfig();
            DiscordCommandIOCConfig discordCommandIOCConfig = new DiscordCommandIOCConfig();
            discordCommandIOCConfig.addObjectToAutoInjects(new Test());
            SwansIOC swansIOC = SwansIOC.CreateIOC(SwansPlugin.class, swansIOCConfig);
            swansIOC.getConfigExtensionManager().addConfigExtension(discordCommandIOCConfig);
            swansIOC.CreateIOC();

            DiscordCommandIOC.getCommandManager().commandUser(null,"test",null);
        } catch (InstanceCreationException e) {
            e.printStackTrace();
        }

    }

}
