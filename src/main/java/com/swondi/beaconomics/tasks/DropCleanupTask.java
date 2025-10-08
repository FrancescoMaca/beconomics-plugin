package com.swondi.beaconomics.tasks;

import com.swondi.beaconomics.managers.KitManager;
import com.swondi.beaconomics.models.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DropCleanupTask extends BukkitRunnable {

    private final static Set<Kit> drops = new HashSet<>();

    public static void addChest(Kit chest) {
        drops.add(chest);
    }

    public static void removeChest(String id) {
        drops.removeIf(kit -> kit.getId().equals(id));
    }

    @Override
    public void run() {
        Iterator<Kit> iterator = drops.iterator();

        while (iterator.hasNext()) {
            Kit chest = iterator.next();
            if (System.currentTimeMillis() >= (chest.getCreatedAt() + 90_000)) {
                Bukkit.getWorld("world").spawnParticle(
                    Particle.CRIMSON_SPORE,
                    chest.getLocation().add(0.5, 0.5, 0.5),
                    50, 0.1, 0.1, 0.1
                );

                KitManager.removeAllFallingArmorStands(chest.getLocation());
                chest.getLocation().getWorld().setType(chest.getLocation(), Material.AIR);
                iterator.remove();
            }
        }
    }
}
