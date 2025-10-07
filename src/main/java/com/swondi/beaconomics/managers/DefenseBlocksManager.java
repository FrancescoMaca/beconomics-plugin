package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.models.DefenseBlock;
import com.swondi.beaconomics.models.Nexus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class DefenseBlocksManager {
    private static final YamlManager yaml = new YamlManager("defenses.yml");
    private static final Map<Location, DefenseBlock> defenses = new HashMap<>();

    public static void addTemporaryBlock(Block block, Nexus nexus) {
        DefenseBlock defenseBlock = new DefenseBlock(block.getLocation(), block.getType());

        defenses.put(block.getLocation(), defenseBlock);

//        Bukkit.broadcastMessage("Defenses amount: " +  defenses.size());
    }

    public static void removeTemporaryBlock(Location location) {
        defenses.remove(location);

//        Bukkit.broadcastMessage("Defenses amount: " +  defenses.size());
    }

    public static void load() {

    }

    public static void backup() {

    }
}
