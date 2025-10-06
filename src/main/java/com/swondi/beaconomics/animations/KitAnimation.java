package com.swondi.beaconomics.animations;

import com.swondi.beaconomics.Beaconomics;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class KitAnimation {

    public static void start(Location targetLocation, Runnable onFinish, String kitname) {
        targetLocation.setX(Math.floor(targetLocation.getX()) + 0.5);
        targetLocation.setZ(Math.floor(targetLocation.getZ()) + 0.5);

        World world = targetLocation.getWorld();

        Location startLocation = targetLocation.clone();
        startLocation.setY(255);

        // Spawn a fake entity (armor stand) to represent the chest
        ArmorStand stand = world.spawn(startLocation, ArmorStand.class, a -> {
            a.setGravity(false);
            a.setVisible(false);
            a.setMarker(true);
            a.setHelmet(new org.bukkit.inventory.ItemStack(Material.CHEST));
            a.setArms(false);
        });

        new BukkitRunnable() {
            private double y = startLocation.getY();

            @Override
            public void run() {
                y -= Math.min(Math.max(0.1, (stand.getLocation().getY() - targetLocation.getY()) * 0.2), 1);

                if (y <= targetLocation.getY()) {
                    // Place the real chest
                    world.spawnParticle(Particle.ASH, stand.getLocation(), 40, 2, 1, 2, 0.1);
                    world.getBlockAt(targetLocation).setType(Material.CHEST);

                    world.playSound(stand.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);

                    // Spawn a nametag ArmorStand above the chest
                    ArmorStand nameTag = world.spawn(targetLocation.clone().add(0, 1, 0), ArmorStand.class, a -> {
                        a.setCustomName(ChatColor.GOLD + kitname);
                        a.setCustomNameVisible(true);
                        a.setVisible(false);
                        a.setGravity(false);
                        a.setMarker(true);
                    });

                    //kitChestNameTags.put(targetLocation.clone().add(0, 1, 0),nameTag);

                    onFinish.run();
                    stand.remove();
                    cancel();
                    return;
                }

                double distance = y - targetLocation.getY();


                // Move the armor stand down
                Location loc = stand.getLocation();
                world.playSound(stand.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);

                world.spawnParticle(Particle.FLAME, loc, 30, 0, 0.01, 0, 0.01);
                loc.setY(y);
                stand.teleport(loc);

                world.spawnParticle(Particle.FIREWORK, targetLocation, 4, 0, 2, 0, 0.01 );
            }
        }.runTaskTimer(Beaconomics.getInstance(), 0L, 1L); // run every tick
    }
}
