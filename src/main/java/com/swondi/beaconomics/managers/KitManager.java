package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.helpers.ItemStackCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class KitManager {

    private static final Map<Location, List<ArmorStand>> fallingArmorStands = new HashMap<>();

    public static void addFallingArmorStand(Location location, ArmorStand armorStand) {
        List<ArmorStand> stands = fallingArmorStands.computeIfAbsent(normalizeLocation(location), k -> new ArrayList<>());

        stands.add(armorStand);
    }

    public static void removeAllFallingArmorStands(Location location) {
        List<ArmorStand> armorStands = fallingArmorStands.get(normalizeLocation(location));
        if (armorStands != null) {
            for (ArmorStand armorStand : armorStands) {
                armorStand.remove();
            }
        }
    }

    public static void removeFallingArmorStand(Location location, ArmorStand armorStand) {
        List<ArmorStand> armorStands = fallingArmorStands.get(normalizeLocation(location));
        if (armorStands != null) {
            armorStands.removeIf(stand -> stand.equals(armorStand));
        }
    }

    public static void removeAllFallingArmorStands() {
        for (List<ArmorStand> armorStands : fallingArmorStands.values()) {
            for (ArmorStand stand : armorStands) {
                stand.remove();
            }

            armorStands.clear();
        }
    }

    private static Location normalizeLocation(Location location) {
        return new Location(
                location.getWorld(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }

    public static void giveStarterKit(Block block) {
        if (block.getType() != Material.CHEST) return;
        Chest chest = (Chest) block.getState();
        Inventory inv = chest.getBlockInventory();

        inv.clear();

        ItemStack placeholder = createPlaceholder();

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, placeholder);
        }

        inv.setItem(4, ItemStackCreator.createNexus());
        inv.setItem(11, createArmor(Material.LEATHER_BOOTS, "§fStarter Boots"));
        inv.setItem(12, createArmor(Material.LEATHER_LEGGINGS, "§fStarter Leggings"));
        inv.setItem(13, createArmor(Material.LEATHER_CHESTPLATE, "§fStarter Chestplate"));
        inv.setItem(14, createArmor(Material.LEATHER_HELMET, "§fStarter Helmet"));
        inv.setItem(15, createItemStack(Material.WOODEN_SWORD, "§fStarter Sword"));
        inv.setItem(22, createItemStack(Material.WOODEN_PICKAXE, "§fStarter Pickaxe"));
    }

    // ───────────────────────────────
    //  INFERNO KIT (strongest)
    // ───────────────────────────────
    public static void giveInfernoKit(Block block) {
        if (block.getType() != Material.CHEST) return;
        Chest chest = (Chest) block.getState();
        chest.setCustomName("§4Inferno Kit");
        chest.update();

        Inventory inv = chest.getBlockInventory();
        inv.clear();
        ItemStack placeholder = createPlaceholder();
        for (int i = 0; i < inv.getSize(); i++) inv.setItem(i, placeholder);

        Map.Entry<Enchantment,Integer> prot4 = new AbstractMap.SimpleEntry<>(Enchantment.PROTECTION, 4);
        Map.Entry<Enchantment,Integer> unb3 = new AbstractMap.SimpleEntry<>(Enchantment.UNBREAKING, 3);
        Map.Entry<Enchantment,Integer> eff3 = new AbstractMap.SimpleEntry<>(Enchantment.EFFICIENCY, 3);
        Map.Entry<Enchantment,Integer> sharp3 = new AbstractMap.SimpleEntry<>(Enchantment.SHARPNESS, 3);

        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 16);

        inv.setItem(4, goldenApple);
        inv.setItem(11, createArmor(Material.DIAMOND_BOOTS, "§4Inferno Boots", unb3, prot4));
        inv.setItem(12, createArmor(Material.DIAMOND_LEGGINGS, "§4Inferno Leggings", unb3, prot4));
        inv.setItem(13, createArmor(Material.DIAMOND_CHESTPLATE, "§4Inferno Chestplate", unb3, prot4));
        inv.setItem(14, createArmor(Material.DIAMOND_HELMET, "§4Inferno Helmet", unb3, prot4));
        inv.setItem(15, createItemStack(Material.DIAMOND_SWORD, "§4Inferno Sword", unb3, sharp3));
        inv.setItem(22, createItemStack(Material.DIAMOND_PICKAXE, "§4Inferno Pickaxe", unb3, eff3));
    }

    // ───────────────────────────────
    //  FIRE KIT (second strongest)
    // ───────────────────────────────
    public static void giveFireKit(Block block) {
        if (block.getType() != Material.CHEST) return;
        Chest chest = (Chest) block.getState();
        chest.setCustomName("§cFire Kit");
        chest.update();

        Inventory inv = chest.getBlockInventory();
        inv.clear();
        ItemStack placeholder = createPlaceholder();
        for (int i = 0; i < inv.getSize(); i++) inv.setItem(i, placeholder);

        Map.Entry<Enchantment,Integer> prot2 = new AbstractMap.SimpleEntry<>(Enchantment.PROTECTION, 2);
        Map.Entry<Enchantment,Integer> unb2 = new AbstractMap.SimpleEntry<>(Enchantment.UNBREAKING, 2);
        Map.Entry<Enchantment,Integer> eff2 = new AbstractMap.SimpleEntry<>(Enchantment.EFFICIENCY, 2);
        Map.Entry<Enchantment,Integer> sharp2 = new AbstractMap.SimpleEntry<>(Enchantment.SHARPNESS, 2);

        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 8);

        inv.setItem(4, goldenApple);
        inv.setItem(11, createArmor(Material.DIAMOND_BOOTS, "§cFire Boots", unb2, prot2));
        inv.setItem(12, createArmor(Material.DIAMOND_LEGGINGS, "§cFire Leggings", unb2, prot2));
        inv.setItem(13, createArmor(Material.DIAMOND_CHESTPLATE, "§cFire Chestplate", unb2, prot2));
        inv.setItem(14, createArmor(Material.DIAMOND_HELMET, "§cFire Helmet", unb2, prot2));
        inv.setItem(15, createItemStack(Material.DIAMOND_SWORD, "§cFire Sword", unb2, sharp2));
        inv.setItem(22, createItemStack(Material.DIAMOND_PICKAXE, "§cFire Pickaxe", unb2, eff2));
    }

    // ───────────────────────────────
    //  EMBER KIT (medium tier)
    // ───────────────────────────────
    public static void giveEmberKit(Block block) {
        if (block.getType() != Material.CHEST) return;
        Chest chest = (Chest) block.getState();
        chest.setCustomName("§eEmber Kit");
        chest.update();

        Inventory inv = chest.getBlockInventory();
        inv.clear();
        ItemStack placeholder = createPlaceholder();
        for (int i = 0; i < inv.getSize(); i++) inv.setItem(i, placeholder);

        Map.Entry<Enchantment,Integer> prot3 = new AbstractMap.SimpleEntry<>(Enchantment.PROTECTION, 3);
        Map.Entry<Enchantment,Integer> unb6 = new AbstractMap.SimpleEntry<>(Enchantment.UNBREAKING, 6);
        Map.Entry<Enchantment,Integer> sharp2 = new AbstractMap.SimpleEntry<>(Enchantment.SHARPNESS, 2);

        ItemStack goldenApple = new ItemStack(Material.GOLDEN_APPLE, 4);

        inv.setItem(4, goldenApple);
        inv.setItem(11, createArmor(Material.GOLDEN_BOOTS, "§eEmber Boots", unb6, prot3));
        inv.setItem(12, createArmor(Material.GOLDEN_LEGGINGS, "§eEmber Leggings", unb6, prot3));
        inv.setItem(13, createArmor(Material.GOLDEN_CHESTPLATE, "§eEmber Chestplate", unb6, prot3));
        inv.setItem(14, createArmor(Material.GOLDEN_HELMET, "§eEmber Helmet", unb6, prot3));
        inv.setItem(15, createItemStack(Material.GOLDEN_SWORD, "§eEmber Sword", unb6, sharp2));
        inv.setItem(22, createItemStack(Material.DIAMOND_PICKAXE, "§eEmber Pickaxe"));
    }

    // ───────────────────────────────
    //  BLAZE KIT (low tier)
    // ───────────────────────────────
    public static void giveBlazeKit(Block block) {
        if (block.getType() != Material.CHEST) return;
        Chest chest = (Chest) block.getState();
        chest.setCustomName("§6Blaze Kit");
        chest.update();

        Inventory inv = chest.getBlockInventory();
        inv.clear();
        ItemStack placeholder = createPlaceholder();
        for (int i = 0; i < inv.getSize(); i++) inv.setItem(i, placeholder);

        Map.Entry<Enchantment,Integer> prot1 = new AbstractMap.SimpleEntry<>(Enchantment.PROTECTION, 1);
        Map.Entry<Enchantment,Integer> unb1 = new AbstractMap.SimpleEntry<>(Enchantment.UNBREAKING, 1);
        Map.Entry<Enchantment,Integer> eff1 = new AbstractMap.SimpleEntry<>(Enchantment.EFFICIENCY, 1);

        inv.setItem(11, createArmor(Material.CHAINMAIL_BOOTS, "§6Blaze Boots", unb1, prot1));
        inv.setItem(12, createArmor(Material.CHAINMAIL_LEGGINGS, "§6Blaze Leggings", unb1, prot1));
        inv.setItem(13, createArmor(Material.CHAINMAIL_CHESTPLATE, "§6Blaze Chestplate", unb1, prot1));
        inv.setItem(14, createArmor(Material.CHAINMAIL_HELMET, "§6Blaze Helmet", unb1, prot1));
        inv.setItem(15, createItemStack(Material.IRON_SWORD, "§6Blaze Sword", unb1));
        inv.setItem(22, createItemStack(Material.IRON_PICKAXE, "§6Blaze Pickaxe", unb1, eff1));
    }

    // ───────────────────────────────
    //  SPARK KIT (weakest)
    // ───────────────────────────────
    public static void giveSparkKit(Block block) {
        if (block.getType() != Material.CHEST) return;
        Chest chest = (Chest) block.getState();
        chest.setCustomName("§7Spark Kit");
        chest.update();

        Inventory inv = chest.getBlockInventory();
        inv.clear();
        ItemStack placeholder = createPlaceholder();
        for (int i = 0; i < inv.getSize(); i++) inv.setItem(i, placeholder);

        inv.setItem(11, createArmor(Material.IRON_BOOTS, "§7Spark Boots"));
        inv.setItem(12, createArmor(Material.IRON_LEGGINGS, "§7Spark Leggings"));
        inv.setItem(13, createArmor(Material.IRON_CHESTPLATE, "§7Spark Chestplate"));
        inv.setItem(14, createArmor(Material.IRON_HELMET, "§7Spark Helmet"));
        inv.setItem(15, createItemStack(Material.IRON_SWORD, "§7Spark Sword"));
        inv.setItem(22, createItemStack(Material.IRON_PICKAXE, "§7Spark Pickaxe"));
    }

    private static ItemStack createItemStack(Material material, String name, Map.Entry<Enchantment,Integer>... enchants) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            if (enchants != null) {
                for (Map.Entry<Enchantment,Integer> e : enchants) {
                    meta.addEnchant(e.getKey(), e.getValue(), true);
                }
            }
            //meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ItemStack createArmor(Material material, String name, Map.Entry<Enchantment,Integer>... enchants) {
        return createItemStack(material, name, enchants);
    }

    private static ItemStack createPlaceholder() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            item.setItemMeta(meta);
        }
        return item;
    }

}
