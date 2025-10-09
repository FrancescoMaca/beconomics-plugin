package com.swondi.beaconomics.menus.shop;

import com.swondi.beaconomics.helpers.ItemStackCreator;
import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.utils.UIHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

        inventory.setItem(10, ItemStackCreator.createUtilityBlock(Material.WATER_BUCKET, false));
        inventory.setItem(11, ItemStackCreator.createUtilityBlock(Material.HOPPER, false));
        inventory.setItem(12, ItemStackCreator.createUtilityBlock(Material.CHEST, false));
        inventory.setItem(13, ItemStackCreator.createUtilityBlock(Material.LADDER, false));
        inventory.setItem(14, ItemStackCreator.createUtilityBlock(Material.TORCH, false));

        return inventory;
    }
}
