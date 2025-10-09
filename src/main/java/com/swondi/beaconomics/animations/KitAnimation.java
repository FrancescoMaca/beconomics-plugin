package com.swondi.beaconomics.animations;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.KitManager;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class KitAnimation {
    public static void start(Location targetLocation, Color color, Runnable onFinish, String kitName) {
        targetLocation.setX(Math.floor(targetLocation.getX()) + 0.5);
        targetLocation.setZ(Math.floor(targetLocation.getZ()) + 0.5);

        World world = targetLocation.getWorld();

        Location startLocation = targetLocation.clone();
        startLocation.setY(255);

        // Spawn a fake entity (armor stand) to represent the chest
        ArmorStand fallingArmorStand = world.spawn(startLocation, ArmorStand.class, a -> {
            a.setGravity(false);
            a.setVisible(false);
            a.setMarker(true);
            a.setHelmet(new org.bukkit.inventory.ItemStack(Material.CHEST));
            a.setArms(false);
        });

        KitManager.addFallingArmorStand(targetLocation, fallingArmorStand);

        new BukkitRunnable() {
            private double y = startLocation.getY();

            @Override
            public void run() {
                y -= Math.min(Math.max(0.1, (fallingArmorStand.getLocation().getY() - targetLocation.getY()) * 0.2), 1);

                if (y <= targetLocation.getY()) {
                    // Place the real chest
                    world.spawnParticle(Particle.ASH, fallingArmorStand.getLocation(), 40, 2, 1, 2, 0.1);
                    world.getBlockAt(targetLocation).setType(Material.CHEST);

                    world.playSound(fallingArmorStand.getLocation(), Sound.BLOCK_ANVIL_HIT, 1, 1);

                    // Spawn a nametag ArmorStand above the chest
                    ArmorStand nameTagArmorStand = world.spawn(targetLocation.clone().add(0, 1, 0), ArmorStand.class, a -> {
                        a.setCustomName(getChatColorFromColor(color) + kitName);
                        a.setCustomNameVisible(true);
                        a.setVisible(false);
                        a.setGravity(false);
                        a.setMarker(true);
                    });

                    onFinish.run();
                    KitManager.addFallingArmorStand(targetLocation, nameTagArmorStand);
                    KitManager.removeFallingArmorStand(targetLocation, fallingArmorStand);

                    fallingArmorStand.remove();
                    cancel();
                    return;
                }

                // Move the armor stand down
                Location loc = fallingArmorStand.getLocation();
                world.playSound(fallingArmorStand.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1);

                world.spawnParticle(Particle.FLAME, loc, 30, 0, 0.01, 0, 0.01);
                loc.setY(y);
                fallingArmorStand.teleport(loc);

                Particle.DustOptions redSpark = new Particle.DustOptions(color, 2);
                world.spawnParticle(Particle.DUST, targetLocation, 7, 0.1, 0.7, 0.1, 0.01, redSpark);
            }
        }.runTaskTimer(Beaconomics.getInstance(), 0L, 1L); // run every tick
    }

    private static ChatColor getChatColorFromColor(Color color) {

        if (color == Color.RED) {
            return ChatColor.RED;
        }
        else if (color == Color.ORANGE) {
            return ChatColor.GOLD;
        }
        else if (color == Color.GREEN) {
            return ChatColor.GREEN;
        }
        else if (color == Color.YELLOW) {
            return ChatColor.YELLOW;
        }
        else if (color == Color.TEAL) {
            return ChatColor.DARK_AQUA;
        }

        return ChatColor.WHITE;
    }
}
