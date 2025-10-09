package com.swondi.beaconomics.menus.shop;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.helpers.ItemStackCreator;
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

        inventory.setItem(10, ItemStackCreator.createDefenseBlock(Material.MUD_BRICKS, false));
        inventory.setItem(11, ItemStackCreator.createDefenseBlock(Material.SANDSTONE, false));
        inventory.setItem(12, ItemStackCreator.createDefenseBlock(Material.STONE_BRICKS, false));
        inventory.setItem(13, ItemStackCreator.createDefenseBlock(Material.POLISHED_BLACKSTONE_BRICKS, false));
        inventory.setItem(14, ItemStackCreator.createDefenseBlock(Material.OBSIDIAN, false));
        inventory.setItem(19, ItemStackCreator.createDefenseBlock(Material.OAK_DOOR, false));
        inventory.setItem(20, ItemStackCreator.createDefenseBlock(Material.BIRCH_DOOR, false));
        inventory.setItem(21, ItemStackCreator.createDefenseBlock(Material.SPRUCE_DOOR, false));
        inventory.setItem(22, ItemStackCreator.createDefenseBlock(Material.CRIMSON_DOOR, false));
        inventory.setItem(23, ItemStackCreator.createDefenseBlock(Material.PALE_OAK_DOOR, false));

        return inventory;
    }
}
