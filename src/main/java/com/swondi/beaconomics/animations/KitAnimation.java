package com.swondi.beaconomics.animations;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.KitManager;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class KitAnimation {
    public static void start(Location targetLocation, Color color, Runnable onFinish) {
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

        KitManager.addFallingArmorStand(stand);

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

                    onFinish.run();
                    KitManager.removeFallingArmorStand(stand);
                    stand.remove();
                    cancel();
                    return;
                }

                // Move the armor stand down
                Location loc = stand.getLocation();
                world.playSound(stand.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);

                world.spawnParticle(Particle.FLAME, loc, 30, 0, 0.01, 0, 0.01);
                loc.setY(y);
                stand.teleport(loc);

                Particle.DustOptions redSpark = new Particle.DustOptions(color, 2);
                world.spawnParticle(Particle.DUST, targetLocation, 7, 0.1, 0.7, 0.1, 0.01, redSpark);
            }
        }.runTaskTimer(Beaconomics.getInstance(), 0L, 1L); // run every tick
    }
}
