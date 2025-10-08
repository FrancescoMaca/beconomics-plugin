package com.swondi.beaconomics.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class GeneratorManager {
    private static final YamlManager yaml = new YamlManager("generator.yml");
    private static final Set<Location> generatorLocations = new HashSet<>();

    static {
        loadGeneratorLocations();
    }

    public static void addGenerator(Location location) {
        generatorLocations.add(location);
    }

    public static boolean isAGeneratorLocation(Location location) {
        return generatorLocations.contains(location);
    }

    public static void removeGenerator(Location location) {
        generatorLocations.remove(location);
    }

    private static void loadGeneratorLocations() {
        int generators = 0;

        for (String locationKey : yaml.getConfiguration().getConfigurationSection("generators").getKeys(false)) {

            String[] parts = locationKey.split("_");

            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);

            Location location = new Location(Bukkit.getWorld("world"), x, y, z);
            generatorLocations.add(location);

            generators++;
        }

        Bukkit.getLogger().info("[Generator Manager] Loaded " + generators + " generators!");
    }
}
