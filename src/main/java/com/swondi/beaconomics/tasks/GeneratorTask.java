package com.swondi.beaconomics.tasks;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.NexusManager;
import com.swondi.beaconomics.managers.YamlManager;
import com.swondi.beaconomics.models.Generator;
import com.swondi.beaconomics.models.Nexus;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class GeneratorTask extends BukkitRunnable {
    private final static Set<Generator> generators = new HashSet<>();

    public GeneratorTask() {
        YamlManager genYaml = new YamlManager("generator.yml");

        if (!genYaml.getConfiguration().contains("generators")) {
            return;
        }

        for (String key : genYaml.getConfiguration().getConfigurationSection("generators").getKeys(false)) {
            Beaconomics.getInstance().getLogger().info("[Beaconomics] Adding generator from file: " + key);
            generators.add(Generator.fromYaml(genYaml, key));
        }
    }

    // Better to use these accessor methods to use less I/O
    public static void addGenerator(Generator gen) {
        generators.add(gen);
    }

    public static void removeGenerator(Generator gen) {
        generators.remove(gen);
    }

    @Override
    public void run() {

        if (generators.isEmpty()) {
            return;
        }

        Generator instance = (Generator) generators.toArray()[0];
        Nexus nexus = NexusManager.getNexus(instance.getLocation().getChunk());

        Player player = Bukkit.getPlayer(nexus.getOwner());

        if(player == null || !player.isOnline())
            return;

        for (Generator generator : generators) {
            // Checking if there is a need to drop first, so if there is not, we don't calculate unnecessary things
            // If the gen is waiting to drop, then skip
            // getRate() returns ticks, and we need milliseconds, 1 tick = 50ms
            if (System.currentTimeMillis() - generator.nextDrop < generator.getRate() * 50L) {
                continue;
            }

            int nearbyItems = 0;

            for (Entity entity : generator.getLocation().getWorld().getNearbyEntities(generator.getLocation(), 3, 3, 3)) {
                if (!(entity instanceof Item)) continue;

                Item item = (Item) entity;
                if (item.getItemStack().getType() == generator.getDrop()) {
                    nearbyItems++;
                }
            }


            if (nearbyItems >= Constants.MAX_UNPICKED_CANDLES_GENERATOR) {
                continue;
            }

            // if it needs to drop then update the latest drop to the current tick
            generator.nextDrop = System.currentTimeMillis();

            Location spawnItemLocation = generator.getLocation().clone().add(0.5, 1.45, 0.5);

            generator
                .getLocation()
                .getWorld()
                .dropItemNaturally(spawnItemLocation, new ItemStack(generator.getDrop(), 1));
        }
    }
}
