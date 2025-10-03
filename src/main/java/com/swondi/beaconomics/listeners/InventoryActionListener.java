package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.menus.nexus.BeaconFuelMenu;
import com.swondi.beaconomics.menus.nexus.BeaconPickupMenu;
import com.swondi.beaconomics.menus.nexus.BeaconTeamMenu;
import com.swondi.beaconomics.menus.nexus.BeaconUpgradeMenu;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryActionListener implements Listener {

    private static final List<String> lockedMenus = new ArrayList<>() {{
        add(Constants.BEACON_MAIN_MENU_TITLE);
        add(Constants.BEACON_PICKUP_MENU_TITLE);
        add(Constants.BEACON_UPGRADE_MENU_TITLE);
        add(Constants.BEACON_TEAM_MENU_TITLE);
        add(Constants.SHOP_MENU_TITLE);
        add(Constants.SHOP_TEMP_BLOCKS_MENU_TITLE);
        add(Constants.SHOP_GENERATORS_TITLE);
        add(Constants.SHOP_DEFENCE_BLOCKS_MENU_TITLE);
        add(Constants.SHOP_TOOLS_MENU_TITLE);
    }};

    /**
     * A map of inventories that can be partially accessed. The list of number represents
     * the indexes that ARE LOCKED.
     */
    private static final Map<String, List<Integer>> partiallyLockedMenus = new HashMap<>() {{
        put(Constants.BEACON_FUEL_MENU_TITLE, List.of(0, 1, 9, 10, 18, 19));
    }};

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!isMenuLocked(event)) {
            return;
        }

        // prevent moving items
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        switch (clicked.getType()) {
            case BARRIER -> player.openInventory(BeaconPickupMenu.build());
            case CHEST -> player.openInventory(BeaconFuelMenu.build(player));
            case BLAZE_POWDER -> player.openInventory(BeaconUpgradeMenu.build(player));
            case PLAYER_HEAD -> player.openInventory(BeaconTeamMenu.build());
        }
    }

    /**
     * Checks if a menu is locked
     * @return true if the menu or the user click is locked and not accessible, otherwise false
     */
    private boolean isMenuLocked(InventoryClickEvent event) {
        final String menuName = event.getView().getTitle();

        // Allow interactions with the player's own inventory
        if (event.getClickedInventory() != null && event.getClickedInventory().equals(event.getWhoClicked().getInventory())) {
            return false;
        }


        // Returns false if the menu is listed in the locked ones
        if (lockedMenus.contains(menuName)) {
            return true;
        }

        // returns false if the menu is partially locked and the slot clicked is among the locked ones
        List<Integer> lockedSlots = partiallyLockedMenus.get(menuName);

        return lockedSlots != null && lockedSlots.contains(event.getSlot());
    }
}
