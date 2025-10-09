package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.models.DefenseBlock;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DefenseBlocksManager {

    private static final YamlManager yaml = new YamlManager("defenses.yml");
    private static final Map<Location, DefenseBlock> defenses = new HashMap<>();

    private static final Map<Location, ArmorStand> healthStands = new HashMap<>();

    static {
        load();
    }

    public static void damageDefense(Location loc, int damage) {
        DefenseBlock block = defenses.get(loc);
        if (block == null) return;

        int newHP = block.getHealth() - damage;
        int maxHP = Constants.DATA_DEFENSE_BLOCKS.get(block.getType()).health();

        if (newHP <= 0) {
            block.getLocation().getBlock().setType(Material.AIR);
            removeDefense(loc);
            removeHealthStand(loc);
        } else {
            block.setHealth(newHP);
            showHealthStand(block, newHP, maxHP);
        }
    }

    private static void showHealthStand(DefenseBlock block, int currentHP, int maxHP) {
        Location loc = block.getLocation().clone().add(0.5, 1.2, 0.5);
        String name = "§c❤ " + currentHP + "/" + maxHP;

        ArmorStand stand = healthStands.get(loc);

        if (stand == null || stand.isDead()) {
            stand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            stand.setVisible(false);
            stand.setSmall(true);
            stand.setGravity(false);
            stand.setMarker(true);
            healthStands.put(loc, stand);
        }

        stand.setCustomName(name);
        stand.setCustomNameVisible(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                removeHealthStand(loc);
            }
        }.runTaskLater(Beaconomics.getInstance(), 20 * 5);
    }

    private static void removeHealthStand(Location loc) {
        ArmorStand stand = healthStands.remove(loc);
        if (stand != null && !stand.isDead()) {
            stand.remove();
        }
    }


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
            //Bukkit.getEntity(UUID.fromString("8b9b4cca-85da-442f-b125-ac5db03e993f")).sendMessage("Saving block: " + block.getType() + " as " + block.getType().name());
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
