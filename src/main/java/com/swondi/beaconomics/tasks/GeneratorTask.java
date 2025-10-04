package com.swondi.beaconomics.tasks;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.YamlManager;
import com.swondi.beaconomics.models.Generator;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class GeneratorTask extends BukkitRunnable {
    private final static Set<Generator> generators = new HashSet<>();
    private static int currentTick = 0;


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
        for (Generator generator : generators) {
            // Checking if there is a need to drop first, so if there is not, we don't calculate unnecessary things
            // If the gen is waiting to drop, then skip
            if (currentTick - generator.nextDrop < generator.getRate()) {
                continue;
            }

            // if it needs to drop then update the latest drop to the current tick
            generator.nextDrop = currentTick;

            Location spawnItemLocation = generator.getLocation().clone().add(0.5, 1.5, 0.5);

            generator
                .getLocation()
                .getWorld()
                .dropItemNaturally(spawnItemLocation, new ItemStack(generator.getDrop(), 1));
        }

        // Updates tick and resets the counter for overflows
        // This will create problems for spawners that have a drop rate > 2000
        currentTick++;

        if  (currentTick > 50000) {
            currentTick = 0;
        }
    }
}
