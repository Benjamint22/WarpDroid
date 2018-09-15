package com.benjamintaillon.warpdroid;

import com.benjamintaillon.warpdroid.subclasses.CustomConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;

public class Globals {
    public static JavaPlugin plugin;
    public static CustomConfig homesConfig;
    public static CustomConfig warpsConfig;
    public static Map<UUID, Location> deathLocations;


    public static UUID generateUUIDFromString(String str) {
        return UUID.nameUUIDFromBytes(str.getBytes());
    }

    public static World getWorldByName(String name) {
        return plugin.getServer().getWorld(name);
    }

    public static World getWorldByUUID(String worldUUID) {
        return getWorldByUUID(UUID.fromString(worldUUID));
    }

    public static World getWorldByUUID(UUID worldUUID) {
        return plugin.getServer().getWorld(worldUUID);
    }

    public static Location toLocation(ConfigurationSection configSection) {
        World world = getWorldByUUID(configSection.getString("world"));
        Vector position = configSection.getVector("position");
        double yaw = configSection.getDouble("yaw");
        double pitch = configSection.getDouble("pitch");
        return new Location(world, position.getX(), position.getY(), position.getZ(), (float)yaw, (float)pitch);
    }

    public static void setLocation(ConfigurationSection configSection, Location location) {
        configSection.set("world", location.getWorld().getUID().toString());
        configSection.set("position", location.toVector());
        configSection.set("yaw", location.getYaw());
        configSection.set("pitch", location.getPitch());
    }
}
