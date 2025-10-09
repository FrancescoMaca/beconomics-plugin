package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.models.DefenseBlock;
import com.swondi.beaconomics.utils.Constants;
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
    /*
    public boolean damageBlock(Block block, int damage) {
        Location loc = block.getLocation();
        if (!blockHealth.containsKey(loc)) return false;

        int currentHP = blockHealth.get(loc) - damage;
        int maxHP = getMaxHealth(block.getType());

        if (currentHP <= 0) {
            block.breakNaturally();
            blockHealth.remove(loc);
            removeBlock(loc);

            ArmorStand stand = healthStands.remove(loc);
            if (stand != null) stand.remove();
            return true;
        } else {
            blockHealth.put(loc, currentHP);
            saveBlock(block.getLocation(), block.getType(), currentHP);

            return false;
        }
    }*/

    public static void addDefense(Material type, Location location){
        Constants.DefenseBlockData defenseBlockData = Constants.DATA_DEFENSE_BLOCKS.get(type);
        if(defenseBlockData == null)
            return;

        defenses.put(location, new DefenseBlock(location, type));
    }

    public static void removeDefense(Location location){
        defenses.remove(location);
    }

    public static void backup(){
        for (DefenseBlock block : defenses.values() ){
            block.saveToYaml(yaml);
        }
    }

    public static void load(){
        Set<String> blockKeys = yaml.getConfiguration().getConfigurationSection("defense").getKeys(false);

        for (String s : blockKeys){
            DefenseBlock db = DefenseBlock.fromYaml(yaml, s);
        }
    }

}
