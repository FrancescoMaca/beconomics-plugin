package com.swondi.beaconomics.menus.nexus;

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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BeaconMainMenu {

    public static Inventory build(Player player) {
        Inventory mainMenu = Bukkit.createInventory(null, 27, Constants.BEACON_MAIN_MENU_TITLE);

        // Fill the border with gray glass panes
        ItemStack color2 = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemStack color3 = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE);
        ItemMeta c2Meta = color2.getItemMeta();
        ItemMeta c3Meta = color3.getItemMeta();
        if (c2Meta != null) c2Meta.setDisplayName(" ");
        if (c3Meta != null) c3Meta.setDisplayName(" ");

        color2.setItemMeta(c2Meta);
        color3.setItemMeta(c3Meta);

        int[] gradient2Slots = new int[] { 8, 17, 24, 25, 26, 3, 4, 11, 13, 19, 20, 21 };
        int[] gradient3Slots = new int[] { 0, 1, 2, 9, 18, 5, 6, 7, 8, 15, 22, 23};

        for (int slot : gradient2Slots) mainMenu.setItem(slot, color2);
        for (int slot : gradient3Slots) mainMenu.setItem(slot, color3);

        // Pick Up button (index 10)
        mainMenu.setItem(10, createPickupButton());
        mainMenu.setItem(12, createFuelButton());
        mainMenu.setItem(14, createUpgradeButton());
        mainMenu.setItem(16, createTeamButton(player));

        return mainMenu;
    }

    private static ItemStack createPickupButton() {
        ItemStack pickUp = new ItemStack(Material.BARRIER);
        ItemMeta pickUpMeta = pickUp.getItemMeta();
        if (pickUpMeta != null) {
            pickUpMeta.setDisplayName(ChatColor.RED + "Pick Up");
            pickUpMeta.setLore(List.of(ChatColor.GRAY + "Retrieve your beacon from the ground."));

            PersistentDataContainer pdc = pickUpMeta.getPersistentDataContainer();
            NamespacedKey navigateKey = new NamespacedKey(Beaconomics.getPlugin(Beaconomics.class), Constants.UI_NAVIGATE_KEY);
            pdc.set(navigateKey, PersistentDataType.STRING, Constants.UI_NEXUS_PICKUP_MENU_VALUE);

            pickUp.setItemMeta(pickUpMeta);
        }

        return pickUp;
    }

    private static ItemStack createFuelButton() {
        ItemStack fuel = new ItemStack(Material.CHEST);
        ItemMeta fuelMeta = fuel.getItemMeta();
        if (fuelMeta != null) {
            fuelMeta.setDisplayName(ChatColor.WHITE + "Fuel");
            fuelMeta.setLore(List.of(ChatColor.GRAY + "Manage the fuel powering your beacon."));

            PersistentDataContainer pdc = fuelMeta.getPersistentDataContainer();
            NamespacedKey navigateKey = new NamespacedKey(Beaconomics.getPlugin(Beaconomics.class), Constants.UI_NAVIGATE_KEY);
            pdc.set(navigateKey, PersistentDataType.STRING, Constants.UI_NEXUS_FUEL_MENU_VALUE);

            fuel.setItemMeta(fuelMeta);
        }

        return fuel;
    }

    private static ItemStack createUpgradeButton() {
        ItemStack coreUpgrade = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta coreMeta = coreUpgrade.getItemMeta();
        if (coreMeta != null) {
            coreMeta.setDisplayName(ChatColor.AQUA + "Core Upgrades");
            coreMeta.setLore(List.of(ChatColor.GRAY + "Upgrade your beacon to unlock new abilities."));

            PersistentDataContainer pdc = coreMeta.getPersistentDataContainer();
            NamespacedKey navigateKey = new NamespacedKey(Beaconomics.getPlugin(Beaconomics.class), Constants.UI_NAVIGATE_KEY);
            pdc.set(navigateKey, PersistentDataType.STRING, Constants.UI_NEXUS_UPGRADE_MENU_VALUE);

            coreUpgrade.setItemMeta(coreMeta);
        }

        return coreUpgrade;
    }

    private static ItemStack createTeamButton(Player player) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(player);
            skullMeta.setDisplayName(ChatColor.YELLOW + "Team Settings");
            skullMeta.setLore(List.of(ChatColor.GRAY + "Manage your team and base settings."));

            PersistentDataContainer pdc = skullMeta.getPersistentDataContainer();
            NamespacedKey navigateKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_NAVIGATE_KEY);

            pdc.set(navigateKey, PersistentDataType.STRING, Constants.UI_NEXUS_TEAM_MENU_VALUE);

            playerHead.setItemMeta(skullMeta);
        }

        return playerHead;
    }
}
