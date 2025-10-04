package com.swondi.beaconomics.menus.shop;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ShopMainMenu {

    public static Inventory build(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 27, Constants.SHOP_MENU_TITLE);

        Material[] gradient = {
            Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE
        };

        int gradientIndex = 0;

        for (int i = 0; i < 27; i++) {
            int row = i / 9;
            int col = i % 9;

            boolean isBorder = row == 0 || row == 2 || col == 0 || col == 8;
            if (isBorder) {
                ItemStack pane = new ItemStack(gradient[gradientIndex]);
                ItemMeta meta = pane.getItemMeta();
                meta.setDisplayName(" ");
                pane.setItemMeta(meta);
                inventory.setItem(i, pane);

                // cycle gradient
                gradientIndex = (gradientIndex + 1) % gradient.length;
            }
        }

        NamespacedKey uiActionKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_NAVIGATE_KEY);

        // Temporary Blocks
        ItemStack temporaryBlocksMenu = new ItemStack(Material.ANDESITE);
        ItemMeta temporaryBlocksMeta = temporaryBlocksMenu.getItemMeta();
        temporaryBlocksMeta.setDisplayName(ChatColor.GREEN + "Temporary Blocks");
        temporaryBlocksMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Quick blocks to climb, raid,",
                ChatColor.GRAY + "and fight your way through bases.",
                ChatColor.YELLOW + "Fast to place, easy to break."
        ));
        temporaryBlocksMeta.getPersistentDataContainer().set(uiActionKey, PersistentDataType.STRING, Constants.UI_SHOP_TEMP_BLOCKS_MENU_VALUE);
        temporaryBlocksMenu.setItemMeta(temporaryBlocksMeta);

        // Generators
        ItemStack generatorsMenu = new ItemStack(Material.PINK_CONCRETE);
        ItemMeta generatorsMeta = generatorsMenu.getItemMeta();
        generatorsMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Generators");
        generatorsMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Place down generators to earn",
                ChatColor.GRAY + "valuable " + ChatColor.GOLD + "Candles " + ChatColor.GRAY + "over time.",
                ChatColor.YELLOW + "Upgrade to increase profit!"
        ));
        generatorsMeta.getPersistentDataContainer().set(uiActionKey, PersistentDataType.STRING, Constants.UI_SHOP_GENS_MENU_VALUE);
        generatorsMenu.setItemMeta(generatorsMeta);

        // Base Defence
        ItemStack defenceBlocksMenu = new ItemStack(Material.MUD_BRICKS);
        ItemMeta defenceBlocksMeta = defenceBlocksMenu.getItemMeta();
        defenceBlocksMeta.setDisplayName(ChatColor.RED + "Base Defence");
        defenceBlocksMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Protect your base with strong",
                ChatColor.GRAY + "and defensive blocks.",
                ChatColor.YELLOW + "The tougher your base,",
                ChatColor.YELLOW + "the longer you survive."
        ));
        defenceBlocksMeta.getPersistentDataContainer().set(uiActionKey, PersistentDataType.STRING, Constants.UI_SHOP_DEFENCE_MENU_VALUE);
        defenceBlocksMenu.setItemMeta(defenceBlocksMeta);

        // Tools
        ItemStack toolsMenu = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta toolsMeta = toolsMenu.getItemMeta();
        toolsMeta.setDisplayName(ChatColor.AQUA + "Weapons & Tools");
        toolsMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Arm yourself with powerful tools",
                ChatColor.GRAY + "and weapons for battle.",
                ChatColor.YELLOW + "Unlock stronger gear as",
                ChatColor.YELLOW + "your tier level increases."
        ));
        toolsMeta.getPersistentDataContainer().set(uiActionKey, PersistentDataType.STRING, Constants.UI_SHOP_TOOLS_MENU_VALUE);
        toolsMenu.setItemMeta(toolsMeta);

        // Place menu items
        inventory.setItem(10, temporaryBlocksMenu);
        inventory.setItem(12, generatorsMenu);
        inventory.setItem(14, defenceBlocksMenu);
        inventory.setItem(16, toolsMenu);

        return inventory;
    }
}
