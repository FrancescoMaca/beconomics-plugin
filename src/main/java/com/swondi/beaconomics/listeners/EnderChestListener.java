package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.managers.EnderChestManager;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class EnderChestListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Get the inventory that was clicked
        Inventory inventory = event.getInventory();

        // Ignore events that are not from the ender chest
        if (!event.getView().getTitle().equals(Constants.ENDER_CHEST_TITLE)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        EnderChestManager.saveEchest(player, inventory);
    }
}
