package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.utils.Constants;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryAccessListener implements Listener {

    // List of completely locked menus
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
        add(Constants.SHOP_UTILITY_BLOCKS_MENU_TITLE);
    }};

    // Map of partially locked menus with specific locked slots
    private static final Map<String, List<Integer>> partiallyLockedMenus = new HashMap<>() {{
        put(Constants.BEACON_FUEL_MENU_TITLE, List.of(0, 1, 9, 10, 18, 19));
    }};

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (isMenuLocked(event)) {
            event.setCancelled(true);
        }
    }

    /**
     * Checks if a menu or a specific slot is locked, preventing interaction.
     *
     * @param event the InventoryClickEvent
     * @return true if the menu or the slot clicked is locked, false otherwise
     */
    private boolean isMenuLocked(InventoryClickEvent event) {
        final String menuName = event.getView().getTitle();

        // Allow interactions with the player's own inventory
        if (event.getClickedInventory() != null && event.getClickedInventory().equals(event.getWhoClicked().getInventory())) {
            return false;
        }

        // Check if the menu is fully locked
        if (lockedMenus.contains(menuName)) {
            return true;
        }

        // Check if the menu is partially locked and the clicked slot is locked
        List<Integer> lockedSlots = partiallyLockedMenus.get(menuName);
        return lockedSlots != null && lockedSlots.contains(event.getSlot());
    }
}
