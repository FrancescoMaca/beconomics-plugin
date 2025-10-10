package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.models.DefenseBlock;
import com.swondi.beaconomics.models.Nexus;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefenseBlocksManager {

    private static final YamlManager yaml = new YamlManager("defenses.yml");
    private static final Map<Location, DefenseBlock> defenses = new HashMap<>();

    static {
        load();
    }

    public static Set<Location> getDefenseBlocksLocation() {
        return defenses.keySet();
    }

    public static boolean isDefense(Location location) {
        return defenses.containsKey(location);
    }

    public static void addDefense(Material type, Location location){
        Constants.DefenseBlockData defenseBlockData = Constants.DATA_DEFENSE_BLOCKS.get(type);

        if(defenseBlockData == null) {
            Bukkit.broadcastMessage("Defense block not found: " + type);
            return;
        }

        defenses.put(location, new DefenseBlock(location, type));

        // Continue here
        Bukkit.broadcastMessage("Defenses count: " + defenses.size());
    }

    public static void removeDefense(Location location){
        DefenseBlock defenseBlock = defenses.remove(location);

        if (defenseBlock == null) return;

        Nexus nexus = NexusManager.getNexus(defenseBlock.getLocation().getChunk());

        if (nexus == null) return;

        // Continue here
        Bukkit.broadcastMessage("Defenses count: " + defenses.size());
    }

    public static DefenseBlock getDefense(Location location) {
        return defenses.get(location);
    }

    public static void backup(){
        for (DefenseBlock block : defenses.values() ){
            block.saveToYaml(yaml);
        }
    }

    public static void load(){
        Set<String> blockKeys = yaml.getConfiguration().getConfigurationSection("defense").getKeys(false);

        for (String s : blockKeys) {

            DefenseBlock db = DefenseBlock.fromYaml(yaml, s);

            defenses.put(db.getLocation(), db);
        }
    }

}
