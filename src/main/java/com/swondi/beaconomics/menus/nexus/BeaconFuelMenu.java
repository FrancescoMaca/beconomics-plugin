package com.swondi.beaconomics.menus.nexus;

import com.swondi.beaconomics.managers.NexusManager;
import com.swondi.beaconomics.models.Nexus;
import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.utils.UIHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BeaconFuelMenu {
    private static final ItemStack separator = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

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
        Nexus nexus = NexusManager.getNexus(player);

        if (nexus == null) return null;

        Inventory inv = Bukkit.createInventory(player, 27, Constants.BEACON_FUEL_MENU_TITLE);

        inv.setItem(0, UIHelper.createBackArrow(Constants.UI_NEXUS_MAIN_MENU_VALUE));
        inv.setItem(1, separator);
        inv.setItem(10, separator);
        inv.setItem(19, separator);
        inv.setItem(9, createNextPage());
        inv.setItem(18, createPreviousPage());
        return inv;
    }

    private static ItemStack createNextPage() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Next Page");
            meta.setLore(List.of(ChatColor.GRAY + "Page 1 of 2"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);;
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ItemStack createPreviousPage() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Previous Page");
            meta.setLore(List.of(ChatColor.GRAY + "Page 1 of 2"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);;
            item.setItemMeta(meta);
        }
        return item;
    }
}
