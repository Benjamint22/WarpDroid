package com.benjamintaillon.warpdroid.subclasses;

import com.benjamintaillon.warpdroid.Globals;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Home {
    public static Location getHomeLocation(Player player) {
        FileConfiguration homes = Globals.homesConfig.load();
        String pathToPlayerConfig = "homes." + player.getUniqueId().toString();
        if (homes.contains(pathToPlayerConfig)) {
            ConfigurationSection playerConfig = homes.getConfigurationSection(pathToPlayerConfig);
            return Globals.toLocation(playerConfig);
        }
        else {
            return Globals.getWorldByName("world").getSpawnLocation();
        }
    }

    public static void setHomeLocation(Player player) throws IOException {
        FileConfiguration homes = Globals.homesConfig.load();
        ConfigurationSection playerConfig = homes.createSection("homes." + player.getUniqueId().toString());
        playerConfig.set("playerdisplayname", player.getName());
        Globals.setLocation(playerConfig, player.getLocation());
        Globals.homesConfig.save(homes);
    }
}
