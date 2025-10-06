package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.models.TemporaryBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class TemporaryBlocksManager {
    private static final YamlManager yaml = new YamlManager("temporary.yml");
    private static final Queue<TemporaryBlock> temporaryBlocks = new LinkedList<>();
    private static final Set<Location> removedLocations = new HashSet<>();

    // Add a temporary block to the queue
    public static void addTemporaryBlock(Location location, Material material, long durationMillis) {
        TemporaryBlock tempBlock = new TemporaryBlock(location, material, System.currentTimeMillis() + durationMillis);
        temporaryBlocks.add(tempBlock);
        location.getBlock().setType(material);
    }

    public static BukkitRunnable cleanupTemporaryBlocks() {
       return new BukkitRunnable() {
           @Override
           public void run() {
               removeExpiredBlocks();
           }
       };
    }

    private static void removeExpiredBlocks() {
        while (!temporaryBlocks.isEmpty()) {
            TemporaryBlock tempBlock = temporaryBlocks.peek();
            if (tempBlock.getRemovalTime() <= System.currentTimeMillis()) {
                tempBlock.getLocation().getBlock().setType(Material.AIR);
                temporaryBlocks.poll();
            } else {
                break;
            }
        }
    }

    public static void handleBlockBreak(Location location) {
        if (!removedLocations.contains(location)) {
            Bukkit.getLogger().info("Temporary block at " + location + ".");
            removedLocations.add(location);
            temporaryBlocks.removeIf(tempBlock -> tempBlock.getLocation().equals(location));
        }
        else {
            Bukkit.getLogger().info("Trying to remove temporary block but not found");
        }
    }

    public static void load() {
        ConfigurationSection tempBlocksSection = yaml.getConfiguration().getConfigurationSection("tempblocks");

        if (tempBlocksSection == null) return;

        for (String key : tempBlocksSection.getKeys(false)) {
            // Get the location (x, y, z) from the key
            String[] parts = key.split("_");
            if (parts.length == 3) {
                try {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    int z = Integer.parseInt(parts[2]);

                    // Get other block information
                    String worldName = tempBlocksSection.getString(key + ".world");
                    String blockType = tempBlocksSection.getString(key + ".material");
                    if (worldName == null || blockType == null) continue;

                    Material material = Material.getMaterial(blockType);
                    long removalTime = tempBlocksSection.getLong(key + ".removalTime");

                    if (material != null) {
                        Location location = new Location(Bukkit.getWorld(worldName), x, y, z);

                        TemporaryBlock tempBlock = new TemporaryBlock(location, material, removalTime);
                        temporaryBlocks.add(tempBlock);

                        location.getBlock().setType(material);
                    }
                } catch (NumberFormatException e) {
                    Bukkit.getLogger().warning("Invalid location format in temporary.yml: " + key);
                }
            }
        }
    }

    public static void backup() {
        // Remove old blocs
        yaml.set("tempblocks", null);

        for (TemporaryBlock temporaryBlock : temporaryBlocks) {
            temporaryBlock.saveToYaml(yaml);
        }
    }
}
