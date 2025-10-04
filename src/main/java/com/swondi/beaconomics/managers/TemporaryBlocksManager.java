package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.models.TemporaryBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TemporaryBlocksManager {

    private static Queue<TemporaryBlock> temporaryBlocks = new LinkedList<>();
    private static Set<Location> removedLocations = new HashSet<>();

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
}
