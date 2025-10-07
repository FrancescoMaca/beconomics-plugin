package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.Beaconomics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YamlManager {

    private final File file;
    private FileConfiguration config;

    public YamlManager(String fileName) {
        if (!fileName.endsWith(".yml")) {
            fileName += ".yml";
        }

        File dataFolder = Beaconomics.getInstance().getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        this.file = new File(dataFolder, fileName);
        File parentDir = this.file.getParentFile();

        // Create the subdirectories if they don't exist
        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                Bukkit.getLogger().severe("Failed to create the directory " + parentDir.getAbsolutePath());
            }
        }

        if (!this.file.exists()) {
            try {
                // Create the file if it doesn't exist
                boolean created = this.file.createNewFile();

                if (!created) {
                    Bukkit.getLogger().severe("Failed to create the file " + this.file.getAbsolutePath());
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not create " + fileName, e);
            }
        }

        // Load the configuration
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    public Object get(String path) {
        return config.get(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public void remove(String path) {
        config.set(path, null);
        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not save " + file.getName(), e);
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfiguration() {
        return config;
    }
}
