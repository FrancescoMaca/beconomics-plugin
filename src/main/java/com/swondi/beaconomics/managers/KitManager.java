package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.helpers.ItemStackCreator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Set;

public class KitManager {

    private static final Set<ArmorStand> fallingArmorStands = new HashSet<>();

    public static void addFallingArmorStand(ArmorStand armorStand) {
        fallingArmorStands.add(armorStand);
    }

    public static void removeFallingArmorStand(ArmorStand armorStand) {
        fallingArmorStands.remove(armorStand);
    }

    public static void removeAllFallingArmorStands() {
        for (ArmorStand armorStand : fallingArmorStands) {
            armorStand.remove();
        }
    }

    public static void giveStarterKit(Block block) {
        // Ensure it’s actually a chest
        if (block.getType() != Material.CHEST) {
            return;
        }

        Chest chest = (Chest) block.getState();
        Inventory inv = chest.getBlockInventory();

        // Beacon
        ItemStack beacon = ItemStackCreator.createNexus();

        // Wooden Sword
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        if (swordMeta != null) {
            swordMeta.setDisplayName("§fStarter Sword");
            sword.setItemMeta(swordMeta);
        }

        // Wooden Pickaxe
        ItemStack pickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta pickaxeMeta = pickaxe.getItemMeta();
        if (pickaxeMeta != null) {
            pickaxeMeta.setDisplayName("§fStarter Pickaxe");
            pickaxe.setItemMeta(pickaxeMeta);
        }

        inv.addItem(
            beacon,
            sword,
            pickaxe,
            createArmor(Material.LEATHER_HELMET, "§fStarter Helmet"),
            createArmor(Material.LEATHER_CHESTPLATE, "§fStarter Chestplate"),
            createArmor(Material.LEATHER_LEGGINGS, "§fStarter Leggings"),
            createArmor(Material.LEATHER_BOOTS, "§fStarter Boots")
        );
    }

    // **Inferno Kit (strongest)**
    public static void giveInfernoKit(Block block) {
        if (block.getType() != Material.CHEST) {
            return;
        }

        Chest chest = (Chest) block.getState();
        Inventory inv = chest.getBlockInventory();

        inv.addItem(
            createItemStack(Material.DIAMOND_SWORD, "§cInferno Sword"),
            createItemStack(Material.DIAMOND_PICKAXE, "§cInferno Pickaxe"),
            createArmor(Material.DIAMOND_HELMET, "§cInferno Helmet"),
            createArmor(Material.DIAMOND_CHESTPLATE, "§cInferno Chestplate"),
            createArmor(Material.DIAMOND_LEGGINGS, "§cInferno Leggings"),
            createArmor(Material.DIAMOND_BOOTS, "§cInferno Boots")
        );
    }

    // **Fire Kit (second strongest)**
    public static void giveFireKit(Block block) {
        if (block.getType() != Material.CHEST) {
            return;
        }

        Chest chest = (Chest) block.getState();
        Inventory inv = chest.getBlockInventory();

        inv.addItem(
            createItemStack(Material.IRON_SWORD, "§6Fire Sword"),
            createItemStack(Material.IRON_PICKAXE, "§6Fire Pickaxe"),
            createArmor(Material.IRON_HELMET, "§6Fire Helmet"),
            createArmor(Material.IRON_CHESTPLATE, "§6Fire Chestplate"),
            createArmor(Material.IRON_LEGGINGS, "§6Fire Leggings"),
            createArmor(Material.IRON_BOOTS, "§6Fire Boots")
        );
    }

    // **Ember Kit (middle tier)**
    public static void giveEmberKit(Block block) {
        if (block.getType() != Material.CHEST) {
            return;
        }

        Chest chest = (Chest) block.getState();
        Inventory inv = chest.getBlockInventory();

        inv.addItem(
            createItemStack(Material.GOLDEN_SWORD, "§eEmber Sword"),
            createItemStack(Material.GOLDEN_PICKAXE, "§eEmber Pickaxe"),
            createArmor(Material.GOLDEN_HELMET, "§eEmber Helmet"),
            createArmor(Material.GOLDEN_CHESTPLATE, "§eEmber Chestplate"),
            createArmor(Material.GOLDEN_LEGGINGS, "§eEmber Leggings"),
            createArmor(Material.GOLDEN_BOOTS, "§eEmber Boots")
        );
    }

    // **Blaze Kit (lower tier)**
    public static void giveBlazeKit(Block block) {
        if (block.getType() != Material.CHEST) {
            return;
        }

        Chest chest = (Chest) block.getState();
        Inventory inv = chest.getBlockInventory();

        inv.addItem(
            createItemStack(Material.STONE_SWORD, "§6Blaze Sword"),
            createItemStack(Material.STONE_PICKAXE, "§6Blaze Pickaxe"),
            createArmor(Material.CHAINMAIL_HELMET, "§6Blaze Helmet"),
            createArmor(Material.CHAINMAIL_CHESTPLATE, "§6Blaze Chestplate"),
            createArmor(Material.CHAINMAIL_LEGGINGS, "§6Blaze Leggings"),
            createArmor(Material.CHAINMAIL_BOOTS, "§6Blaze Boots")
        );
    }

    // **Spark Kit (weakest)**
    public static void giveSparkKit(Block block) {
        if (block.getType() != Material.CHEST) {
            return;
        }

        Chest chest = (Chest) block.getState();
        Inventory inv = chest.getBlockInventory();

        inv.addItem(
            createItemStack(Material.WOODEN_SWORD, "§fSpark Sword"),
            createItemStack(Material.WOODEN_PICKAXE, "§fSpark Pickaxe"),
            createArmor(Material.LEATHER_HELMET, "§fSpark Helmet"),
            createArmor(Material.LEATHER_CHESTPLATE, "§fSpark Chestplate"),
            createArmor(Material.LEATHER_LEGGINGS, "§fSpark Leggings"),
            createArmor(Material.LEATHER_BOOTS, "§fSpark Boots")
        );
    }

    // Helper method to create an item stack with a custom name
    private static ItemStack createItemStack(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    // Helper method to create armor with a custom name
    private static ItemStack createArmor(Material material, String name) {
        ItemStack armor = new ItemStack(material);
        ItemMeta meta = armor.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            armor.setItemMeta(meta);
        }
        return armor;
    }
}
