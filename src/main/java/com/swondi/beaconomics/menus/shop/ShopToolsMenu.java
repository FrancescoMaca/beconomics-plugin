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

import java.util.List;

public class ShopToolsMenu {

    private static final int[] borders = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    public static Inventory build(Player player) {
        Inventory inv = Bukkit.createInventory(player, 54, Constants.SHOP_TOOLS_MENU_TITLE);

        for (int border : borders) {
            inv.setItem(border, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        // Chainmail set (early-game)
        inv.setItem(10, createShopItem(Material.CHAINMAIL_HELMET, "§7Chainmail Helmet", 5000));
        inv.setItem(19, createShopItem(Material.CHAINMAIL_CHESTPLATE, "§7Chainmail Chestplate", 7000));
        inv.setItem(28, createShopItem(Material.CHAINMAIL_LEGGINGS, "§7Chainmail Leggings", 6000));
        inv.setItem(37, createShopItem(Material.CHAINMAIL_BOOTS, "§7Chainmail Boots", 4000));
        inv.setItem(11, createShopItem(Material.STONE_SWORD, "§7Stone Sword", 3000));
        inv.setItem(20, createShopItem(Material.STONE_PICKAXE, "§7Stone Pickaxe", 2500));

        // Iron set (mid-game)
        inv.setItem(12, createShopItem(Material.IRON_HELMET, "§fIron Helmet", 15000));
        inv.setItem(21, createShopItem(Material.IRON_CHESTPLATE, "§fIron Chestplate", 25000));
        inv.setItem(30, createShopItem(Material.IRON_LEGGINGS, "§fIron Leggings", 20000));
        inv.setItem(39, createShopItem(Material.IRON_BOOTS, "§fIron Boots", 12000));
        inv.setItem(13, createShopItem(Material.IRON_SWORD, "§fIron Sword", 10000));
        inv.setItem(22, createShopItem(Material.IRON_PICKAXE, "§fIron Pickaxe", 8000));

        // Diamond set (late-game)
        inv.setItem(14, createShopItem(Material.DIAMOND_HELMET, "§bDiamond Helmet", 50000));
        inv.setItem(23, createShopItem(Material.DIAMOND_CHESTPLATE, "§bDiamond Chestplate", 80000));
        inv.setItem(32, createShopItem(Material.DIAMOND_LEGGINGS, "§bDiamond Leggings", 70000));
        inv.setItem(41, createShopItem(Material.DIAMOND_BOOTS, "§bDiamond Boots", 40000));
        inv.setItem(15, createShopItem(Material.DIAMOND_SWORD, "§bDiamond Sword", 30000));
        inv.setItem(24, createShopItem(Material.DIAMOND_PICKAXE, "§bDiamond Pickaxe", 25000));


        inv.setItem(0, UIHelper.createBackArrow(Constants.UI_SHOP_MAIN_MENU_VALUE));

        return inv;
    }

    private static ItemStack createShopItem(Material material, String name, int price) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return item;

        meta.setDisplayName(name);
        meta.setLore(List.of(
                "§7Buy this item to upgrade your gear.",
                "§8-------------------------",
                "§e§lPrice§7: §6$" + price,
                "§8-------------------------",
                "§aClick to purchase!"
        ));

        // Add buy key + price key
        NamespacedKey buyKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_ACTION_KEY);
        NamespacedKey priceKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_PRICE_KEY);

        meta.getPersistentDataContainer().set(buyKey, PersistentDataType.STRING, Constants.UI_SHOP_BUY_VALUE);
        meta.getPersistentDataContainer().set(priceKey, PersistentDataType.INTEGER, price);

        item.setItemMeta(meta);
        return item;
    }
}