package com.benjamintaillon.warpdroid.subclasses;

import com.benjamintaillon.warpdroid.Globals;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfig {

    private File file;

    public CustomConfig(String filename) {
        this.file = new File(Globals.plugin.getDataFolder(), filename);
    }

    public FileConfiguration load() {
        return YamlConfiguration.loadConfiguration(this.file);
    }

    public void save(FileConfiguration config) throws IOException {
        config.save(file);
    }

}
