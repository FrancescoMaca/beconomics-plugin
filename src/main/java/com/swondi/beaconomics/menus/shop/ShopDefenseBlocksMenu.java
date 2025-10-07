package com.swondi.beaconomics.menus.shop;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.models.DefenseBlock;
import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.utils.UIHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ShopDefenseBlocksMenu {

    private static final int[] borders = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
    private static final ItemStack separator = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

    static {
        ItemMeta meta = separator.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(" ");
            meta.setLore(List.of());
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);;
            separator.setItemMeta(meta);
        }
    }

    public static Inventory build(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 54, Constants.SHOP_DEFENCE_BLOCKS_MENU_TITLE);

        // Set the borders (glass panes) in the inventory
        for (int border : borders) {
            inventory.setItem(border, separator);
        }

        // Set the back button
        inventory.setItem(0, UIHelper.createBackArrow(Constants.UI_SHOP_MAIN_MENU_VALUE));

        inventory.setItem(10, createDefenseBlockItem(Material.MUD_BRICKS, 50, 1,1000));
        inventory.setItem(11, createDefenseBlockItem(Material.SANDSTONE, 150, 2, 5000));
        inventory.setItem(12, createDefenseBlockItem(Material.STONE_BRICKS, 250, 3, 10000));
        inventory.setItem(13, createDefenseBlockItem(Material.POLISHED_BLACKSTONE_BRICKS, 500, 4, 20000));
        inventory.setItem(14, createDefenseBlockItem(Material.OBSIDIAN, 1000, 6, 30000));
        inventory.setItem(19, createDefenseBlockItem(Material.OAK_DOOR, 50, 1, 1000));
        inventory.setItem(20, createDefenseBlockItem(Material.BIRCH_DOOR, 150, 2, 5000));
        inventory.setItem(21, createDefenseBlockItem(Material.SPRUCE_DOOR, 250, 3, 10000));
        inventory.setItem(22, createDefenseBlockItem(Material.CRIMSON_DOOR, 500,4,  20000));
        inventory.setItem(23, createDefenseBlockItem(Material.PALE_OAK_DOOR, 1000, 6, 30000));

        return inventory;
    }

    private static ItemStack createDefenseBlockItem(Material material, int health, int fuelConsumption, int price) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            // Define custom namespace keys for persistent data
            NamespacedKey buyKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_ACTION_KEY);
            NamespacedKey priceKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_PRICE_KEY);
            NamespacedKey defenseKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_DEFENSE_BLOCK_TAG);

            // Set the custom model data for unique identification
            meta.getPersistentDataContainer().set(buyKey, PersistentDataType.STRING, Constants.UI_SHOP_BUY_VALUE);
            meta.getPersistentDataContainer().set(priceKey, PersistentDataType.INTEGER, price);
            meta.getPersistentDataContainer().set(defenseKey, PersistentDataType.BYTE, (byte)1);

            // Set custom name (e.g., "Mud Bricks" for the material name)
            String name = Arrays.stream(material.name().replace("_", " ").split(" "))
                .map((s) -> s.charAt(0) + s.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
            meta.setDisplayName("§6" + name);

            // Set lore with health and price information
            meta.setLore(List.of(
                "§7Health: " + health + " HP",
                "§7Fuel cost: " + fuelConsumption + " coal/h",
                "§8-------------------------",
                "§e§lPrice§7: §6$" + price,
                "§8-------------------------",
                "§aClick to purchase!"
            ));

            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }
}
