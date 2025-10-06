package com.swondi.beaconomics.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class HomesManager {

    private static final YamlManager yaml = new YamlManager("homes.yml");

    public static void setHome(Player player, String name) {
        UUID uuid = player.getUniqueId();
        Location loc = player.getLocation();

        String path = "homes." + uuid + "." + name.toLowerCase();
        FileConfiguration config = yaml.getConfiguration();

        config.set(path + ".world", loc.getWorld().getName());
        config.set(path + ".x", loc.getX());
        config.set(path + ".y", loc.getY());
        config.set(path + ".z", loc.getZ());
        config.set(path + ".yaw", loc.getYaw());
        config.set(path + ".pitch", loc.getPitch());

        yaml.save();
    }

    /*public Location getHome(Player player) {
        UUID uuid = player.getUniqueId();
        String path = "homes." + uuid;

        if (!yaml.get(".world")) return null;

        String world = config.getString(path + ".world");
        double x = config.getDouble(path + ".x");
        double y = config.getDouble(path + ".y");
        double z = config.getDouble(path + ".z");
        float yaw = (float) config.getDouble(path + ".yaw");
        float pitch = (float) config.getDouble(path + ".pitch");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }*/
}
