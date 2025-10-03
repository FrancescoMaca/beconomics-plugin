package com.swondi.beaconomics.menus.nexus;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class BeaconPickupMenu {

    public static Inventory build() {
        Inventory mainMenu = Bukkit.createInventory(null, 27, Constants.BEACON_PICKUP_MENU_TITLE);

        mainMenu.setItem(11, createNoButton());
        mainMenu.setItem(15, createYesButton());
        mainMenu.setItem(13, createNote());

        return mainMenu;
    }

    private static ItemStack createNoButton() {
        ItemStack no = new  ItemStack(Material.RED_CONCRETE);
        NamespacedKey key = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_NAVIGATE_KEY);

        ItemMeta noMeta = no.getItemMeta();

        noMeta.setDisplayName(ChatColor.RED + "No");
        noMeta.setLore(null);
        noMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        noMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Constants.UI_NEXUS_MAIN_MENU_VALUE);

        no.setItemMeta(noMeta);

        return no;
    }

    private static ItemStack createYesButton() {
        ItemStack yes = new  ItemStack(Material.GREEN_CONCRETE);
        NamespacedKey key = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_ACTION_KEY);

        ItemMeta yesMeta = yes.getItemMeta();
        yesMeta.setDisplayName(ChatColor.GREEN + "Yes");
        yesMeta.setLore(null);
        yesMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        yesMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Constants.UI_NEXUS_PICKUP_VALUE);

        yes.setItemMeta(yesMeta);

        return yes;
    }

    private static ItemStack createNote() {
        ItemStack note = new ItemStack(Material.PAPER);
        ItemMeta noteMeta = note.getItemMeta();
        noteMeta.setDisplayName(ChatColor.RED + "Warning");
        // Add lore lines as a List<String>
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_RED + "Removing your beacon will:");
        lore.add(ChatColor.GRAY + " • Release all defenses of your base");
        lore.add(ChatColor.GRAY + " • Stop generators from being powered");
        lore.add(ChatColor.GRAY + " • Cause structures to lose health");
        lore.add("");
        lore.add(ChatColor.YELLOW + "If you leave it too long,");
        lore.add(ChatColor.YELLOW + "your entire base may collapse!");
        lore.add("");
        lore.add(ChatColor.RED + "This action is permanent until");
        lore.add(ChatColor.RED + "you place a new beacon.");

        noteMeta.setLore(lore);
        noteMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        note.setItemMeta(noteMeta);
        return note;
    }
}
