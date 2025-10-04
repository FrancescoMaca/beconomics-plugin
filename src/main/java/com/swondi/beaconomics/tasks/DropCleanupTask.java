package com.swondi.beaconomics.tasks;

import com.swondi.beaconomics.models.Kit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
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
        for (Kit chest : drops) {
            if (TickTask.tick >= (chest.getCreatedAt() + 3000)) {
                chest.getLocation().getWorld().setType(chest.getLocation(), Material.AIR);
                removeChest(chest.getId());
            }
        }
    }
}
