package com.swondi.beaconomics.helpers;

import com.swondi.beaconomics.Beaconomics;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimationHelper {
    public static int findFirstAvailableBlock(Location loc) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int z = loc.getBlockZ();

        Location temp = loc.clone();
        for (int y = 255; y >= -75; y--) {
            Block block = world.getBlockAt(x, y, z);

            // As soon as we find a solid block, return the air block above it
            if (block.getType().isSolid()) {
                return y + 1;
            }
        }

        return -1;
    }

    public static void spawnFireWave(Location center, Particle particle, int maxRadius, int pointsPerRing, long delayTicks) {
        World world = center.getWorld();

        new BukkitRunnable() {
            private double radius = 0;

            @Override
            public void run() {
                if (radius > maxRadius) {
                    cancel();
                    return;
                }

                for (int i = 0; i < pointsPerRing; i++) {
                    double angle = 2 * Math.PI * i / pointsPerRing;
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    Location loc = new Location(world, x, center.getY(), z);
                    world.spawnParticle(particle, loc, 1, 0, 0, 0, 0);
                }

                radius += 0.2;
            }
        }.runTaskTimer(Beaconomics.getInstance(), 0L, delayTicks);
    }

}
