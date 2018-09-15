package com.benjamintaillon.warpdroid.subclasses;

import com.benjamintaillon.warpdroid.Globals;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.management.OperationsException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Warps {
    private static ConfigurationSection getWarpConfig(ConfigurationSection warpsConfig, String warpName) {
        ConfigurationSection warps = warpsConfig.getConfigurationSection("warps");
        if (warps == null) {
            warps = warpsConfig.createSection("warps");
        }
        for (String key: warps.getKeys(false)) {
            if (warps.getString(key + ".name").equals(warpName)) {
                return warps.getConfigurationSection(key);
            }
        }
        return null;
    }

    public static String[] getWarps() {
        FileConfiguration warpsConfig = Globals.warpsConfig.load(); // LOAD
        ConfigurationSection warps = warpsConfig.getConfigurationSection("warps");
        if (warps == null) {
            warps = warpsConfig.createSection("warps");
        }
        List<String> names = new ArrayList<>();
        for (String key: warps.getKeys(false)) {
            names.add(warps.getString(key + ".name"));
        }
        return names.toArray(new String[0]);
    }

    public static Location getWarpLocation(String warpName) {
        FileConfiguration warpsConfig = Globals.warpsConfig.load(); // LOAD
        ConfigurationSection warpConfig = getWarpConfig(warpsConfig, warpName);
        if (warpConfig != null) {
            return Globals.toLocation(warpConfig.getConfigurationSection("location"));
        }
        return null;
    }

    public static void addWarp(String warpName, Location warpLocation) throws IOException, OperationsException {
        FileConfiguration warpsConfig = Globals.warpsConfig.load(); // LOAD
        if (getWarpConfig(warpsConfig, warpName) != null) {
            throw new OperationsException("Warp \"" + warpName + "\" already exists.");
        }
        ConfigurationSection warps = warpsConfig.getConfigurationSection("warps");
        if (warps == null) {
            warps = warpsConfig.createSection("warps");
        }
        String warpKey = Globals.generateUUIDFromString(warpName).toString();
        ConfigurationSection warpConfig = warps.createSection(warpKey);
        warpConfig.set("name", warpName);
        Globals.setLocation(warpConfig.createSection("location"), warpLocation);
        Globals.warpsConfig.save(warpsConfig); // SAVE
    }

    public static void setWarp(String warpName, Location warpLocation) throws OperationsException, IOException {
        FileConfiguration warpsConfig = Globals.warpsConfig.load(); // LOAD
        ConfigurationSection warpConfig = getWarpConfig(warpsConfig, warpName);
        if (warpConfig != null) {
            Globals.setLocation(warpConfig.getConfigurationSection("location"), warpLocation);
            Globals.warpsConfig.save(warpsConfig); // SAVE
        }
        else {
            throw new OperationsException("Warp \"" + warpName + "\" does not exist.");
        }
    }

    public static void removeWarp(String warpName) throws OperationsException, IOException {
        FileConfiguration warpsConfig = Globals.warpsConfig.load(); // LOAD
        ConfigurationSection warpConfig = getWarpConfig(warpsConfig, warpName);
        if (warpConfig != null) {
            warpsConfig.set(warpConfig.getCurrentPath(), null);
            Globals.warpsConfig.save(warpsConfig); // SAVE
        }
        else {
            throw new OperationsException("Warp \"" + warpName + "\" does not exist.");
        }
    }
}
