package com.swondi.beaconomics.menus.shop;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.utils.UIHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ShopTempBlocksMenu {

    private static final int[] borders = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    public static Inventory build(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 54, Constants.SHOP_TEMP_BLOCKS_MENU_TITLE);

        for (int border : borders) {
            inventory.setItem(border, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }

        inventory.setItem(0, UIHelper.createBackArrow(Constants.UI_SHOP_MAIN_MENU_VALUE));

        Object[][] blockData = {
            {Material.COBBLESTONE, 50},
            {Material.SANDSTONE, 60},
            {Material.STONE, 70},
            {Material.SMOOTH_STONE, 80},
            {Material.PRISMARINE, 100},
            {Material.BRICKS, 120},
            {Material.NETHER_BRICKS, 140},
            {Material.QUARTZ_BLOCK, 150},
            {Material.RED_SANDSTONE, 110},
            {Material.PURPUR_BLOCK, 130}
        };

        int col = 1;
        int row = 1; // second row
        for (Object[] data : blockData) {
            Material block = (Material) data[0];
            int price = (int) data[1];

            // Slot = row*9 + col
            int blockSlot = row * 9 + col;

            inventory.setItem(blockSlot, createTemporaryBlock(block, price));

            col++;
            if (col > 7) {
                col = 1;
                row += 1;
            }
        }

        return inventory;
    }

    private static ItemStack createTemporaryBlock(Material material, int price) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setLore(null);
        NamespacedKey buyKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_ACTION_KEY);
        NamespacedKey priceKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_PRICE_KEY);
        NamespacedKey tempKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_TEMPORARY_BLOCK_TAG);

        meta.getPersistentDataContainer().set(buyKey, PersistentDataType.STRING, Constants.UI_SHOP_BUY_VALUE);
        meta.getPersistentDataContainer().set(priceKey, PersistentDataType.INTEGER, price);
        meta.getPersistentDataContainer().set(tempKey, PersistentDataType.BYTE, (byte)1);

        item.setItemMeta(meta);

        return item;
    }
}
