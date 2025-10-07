package com.swondi.beaconomics.menus.shop;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.utils.UIHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ShopUtilityMenu {
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
        Inventory inventory = Bukkit.createInventory(player, 54, Constants.SHOP_UTILITY_BLOCKS_MENU_TITLE);

        for (int border : borders) {
            inventory.setItem(border, separator);
        }

        inventory.setItem(0, UIHelper.createBackArrow(Constants.UI_SHOP_MAIN_MENU_VALUE));

        Object[][] blockData = new Object[][] {
            {Material.WATER_BUCKET, 1000},
            {Material.HOPPER, 5000},
            {Material.CHEST, 2500},
            {Material.LADDER, 1500},
            {Material.TORCH, 500},
        };

        int col = 1;
        int row = 1;
        for (Object[] data : blockData) {
            Material block = (Material) data[0];
            int price = (int) data[1];
            int blockSlot = row * 9 + col;

            inventory.setItem(blockSlot, createUtilityBlock(block, price));

            col++;
            if (col > 7) {
                col = 1;
                row += 1;
            }
        }

        return inventory;
    }

    private static ItemStack createUtilityBlock(Material material, int price) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return item;
        }

        meta.setLore(null);
        NamespacedKey buyKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_ACTION_KEY);
        NamespacedKey priceKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_PRICE_KEY);

        meta.getPersistentDataContainer().set(buyKey, PersistentDataType.STRING, Constants.UI_SHOP_BUY_VALUE);
        meta.getPersistentDataContainer().set(priceKey, PersistentDataType.INTEGER, price);

        item.setItemMeta(meta);

        return item;
    }
}
