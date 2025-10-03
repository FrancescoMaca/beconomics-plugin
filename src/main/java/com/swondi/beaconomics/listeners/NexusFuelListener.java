package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.managers.NexusManager;
import com.swondi.beaconomics.managers.YamlManager;
import com.swondi.beaconomics.models.Nexus;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class NexusFuelListener implements Listener {
    private final static YamlManager yaml = new YamlManager("nexuses.yml");
    private final static Material fuel = Material.RAW_COPPER;

    @EventHandler
    public void onItemInteraction(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) {
            return;
        }

        // Handle interaction in the BeaconFuelMenu
        if (event.getView().getTitle().equals(Constants.BEACON_FUEL_MENU_TITLE)) {
            InventoryAction action = event.getAction();

            // Handling raw copper placed in the BeaconFuelMenu (adding fuel)
            if (action == InventoryAction.PLACE_ONE || action == InventoryAction.PLACE_ALL || action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                if (clickedItem.getType() != fuel) {
                    player.sendMessage(ChatColor.RED + "You can only add Raw Copper to your Nexus!");
                    event.setCancelled(true);
                    return;
                }

                if (event.getClickedInventory().equals(event.getInventory())) {
                    // Handle placing the raw copper into the Beacon Fuel Menu
                    player.sendMessage("You added " + clickedItem.getAmount() + "x Raw Copper to your Nexus fuel.");
                    Nexus nexus = NexusManager.getNexus(player);
                    if (nexus != null) {
                        int newFuelAmount = nexus.getFuelAmount() + clickedItem.getAmount();
                        nexus.setFuelAmount(newFuelAmount);
                        nexus.saveToYaml(yaml);
                        player.sendMessage(ChatColor.GREEN + "Your Nexus fuel is now updated!");
                    }
                }
            }

            // Prevent picking up raw copper from the BeaconFuelMenu
            if (action == InventoryAction.PICKUP_ONE || action == InventoryAction.PICKUP_ALL) {
                if (event.getClickedInventory().equals(event.getInventory())) { // If clicked inside BeaconFuelMenu
                    player.sendMessage(ChatColor.RED + "You cannot pick up Raw Copper from the Beacon Fuel Menu!");
                    event.setCancelled(true); // Prevent picking up raw copper
                }
            }
        }

        // Handle interactions in the player's inventory (not affecting Nexus fuel)
        if (event.getClickedInventory().equals(player.getInventory())) {
            // Shift-click: handling placing copper in the player's inventory
            if (event.getAction() == InventoryAction.PLACE_ONE || event.getAction() == InventoryAction.PLACE_ALL) {
                if (clickedItem.getType() == fuel) {
                    return; // Do nothing if raw copper is placed in the player's inventory
                }
            }

            // Handle picking raw copper in player’s inventory, but do not update Nexus fuel
            if (event.getAction() == InventoryAction.PICKUP_ONE || event.getAction() == InventoryAction.PICKUP_ALL) {
                if (clickedItem.getType() == fuel) {
                    return; // Safe to leave as it won't affect Nexus
                }
            }
        }
    }
}
