package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.managers.NexusManager;
import com.swondi.beaconomics.managers.YamlManager;
import com.swondi.beaconomics.menus.nexus.BeaconFuelMenu;
import com.swondi.beaconomics.models.Nexus;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NexusFuelListener implements Listener {
    private final static YamlManager yaml = new YamlManager("nexuses.yml");
    private final static Material fuel = Material.COAL;

    @EventHandler
    public void onItemInteraction(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (!event.getView().getTitle().equals(Constants.BEACON_FUEL_MENU_TITLE)) {
            return;
        }

        event.setCancelled(true);
        Nexus nexus = NexusManager.getNexus(player);

        if (nexus == null || clickedItem == null || clickedItem.getType() != fuel) {
            return;
        }

        Inventory playerInventory = player.getInventory();
        Inventory nexusFuelInventory = event.getView().getTopInventory();
        Inventory clickedInventory = event.getClickedInventory();

        if (clickedInventory == null) return;

        // Check if the clicked item is from the player's inventory (bottom)
        if (clickedInventory.equals(playerInventory)) {
            int remaining = addItemToNexus(nexusFuelInventory, clickedItem.getAmount());

            if (remaining != clickedItem.getAmount()) {
                removeItemFromPlayer(playerInventory, clickedItem.getAmount() - remaining, event.getSlot());
                player.sendMessage(ChatColor.GREEN + "" + clickedItem.getAmount() + " fuel added to Nexus");
            }
            else {
                player.sendMessage(ChatColor.RED + "Nexus inventory is full.");
                return;
            }

            nexus.setFuelAmount(nexus.getFuelAmount() + clickedItem.getAmount() - remaining);

            // Updates the fuel paper item in real time
            BeaconFuelMenu.updateFuelInfo(nexusFuelInventory, nexus);

            // playing sounds
            player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 0.5F, 2.0F);
        }
        // Check if the clicked item is from the opened inventory (top)
        else if (clickedInventory.equals(nexusFuelInventory)) {

            int remaining = addItemToPlayer(playerInventory, clickedItem.getAmount());

            if (remaining != clickedItem.getAmount()) {
                takeFromNexus(nexusFuelInventory, clickedItem.getAmount() - remaining);
                player.sendMessage(ChatColor.RED + "removed " + clickedItem.getAmount() + " fuel from Nexus.");
            }
            else {
                player.sendMessage(ChatColor.RED + "Player inventory is full.");
                return;
            }

            nexus.setFuelAmount(nexus.getFuelAmount() - clickedItem.getAmount() + remaining);

            // Updates the fuel paper item in real time
            BeaconFuelMenu.updateFuelInfo(nexusFuelInventory, nexus);

            // playing sounds
            player.playSound(player.getLocation(), Sound.ENTITY_EGG_THROW, 0.5F, 2.0F);
        }
    }

    /**
     * Adds a specific amount of fuel to the nexus
     * @param nexusInventory the nexus inventory
     * @param amount the amount of fuel to add to the nexus
     * @return the amount of items left that couldn't be fit in the inventory. A return that is equal to the amount
     * set is considered a "full inventory", a return of 0 means all items were added.
     */
    private int addItemToNexus(Inventory nexusInventory, int amount) {
        for (int i = 0; i < nexusInventory.getSize(); i++) {
            ItemStack item = nexusInventory.getItem(i);

            // Slot is free, return 0 since all items were added
            if (item == null) {
                nexusInventory.setItem(i, new ItemStack(fuel, amount));
                return 0;
            }

            // If the type is not fuel nor air then it's something else, lets skip it
            if (item.getType() != fuel) continue;

            // Found a slot with space left
            if (item.getAmount() < item.getMaxStackSize()) {

                // If the slot in the nexus plus what im adding is still less that the max stack
                // then we just set that slot as the sum of the two
                if (item.getAmount() + amount < item.getMaxStackSize()) {
                    nexusInventory.setItem(i, new ItemStack(fuel, item.getAmount() + amount));
                    return 0;
                }
                else {
                    // otherwise if the addition is more than the stack size
                    // we set that stack to the maxStackSize, and add the remaining in the next slot
                    // the next slot might not exist, so we just recursively call this method to
                    // add the leftovers
                    nexusInventory.setItem(i, new ItemStack(fuel, fuel.getMaxStackSize()));

                    return addItemToNexus(nexusInventory, (item.getAmount() + amount - fuel.getMaxStackSize()));
                }
            }
        }

        // Returns the initial amount, meaning all slots are full
        return amount;
    }

    /**
     * Removes items from the player inventory
     * @param playerInventory the inventory of the player
     * @param amount the amount of fuel to remove (usually what has been added to the nexus)
     * @param slot the slot to remove the fuel from
     */
    private void removeItemFromPlayer(Inventory playerInventory, int amount, int slot) {
        ItemStack itemAtSlot = playerInventory.getItem(slot);

        if (itemAtSlot == null || itemAtSlot.getType() != fuel) return;

        if (itemAtSlot.getAmount() <= amount) {
            playerInventory.setItem(slot, null);
        }
        else {
            playerInventory.setItem(slot, new ItemStack(fuel, itemAtSlot.getAmount() - amount));
        }
    }

    private void takeFromNexus(Inventory nexusInventory, int amount) {
        for (int i = nexusInventory.getSize() - 1; i >= 0; i--) {

            ItemStack item = nexusInventory.getItem(i);

            if (item == null || item.getType() != fuel) continue;

            // The slot has the same amount of fuel as the one requested to remove
            if (amount == item.getAmount()) {
                nexusInventory.setItem(i, null);
                return;
            }
            else if (amount < item.getAmount()) {
                // The slot has more item than what we need to remove
                nexusInventory.setItem(i, new ItemStack(fuel, item.getAmount() - amount));
                return;
            }
            else {
                // The slot has fewer items than what we need to remove
                nexusInventory.setItem(i, null);
                takeFromNexus(nexusInventory, amount - item.getAmount());
                return;
            }
        }
    }

    private int addItemToPlayer(Inventory playerInventory, int amount) {
        for (int i = 0; i < playerInventory.getSize(); i++) {
            // Removes armor slot as storage slots
            if (i >= 36 && i <= 39) continue;

            ItemStack item = playerInventory.getItem(i);

            // First free slot we add this!
            if (item == null) {
                playerInventory.setItem(i, new ItemStack(fuel, amount));
                return 0;
            }

            if (item.getType() != fuel) continue;

            if (item.getAmount() < item.getMaxStackSize()) {

                if (item.getAmount() + amount < item.getMaxStackSize()) {
                    playerInventory.setItem(i, new ItemStack(fuel, item.getAmount() + amount));
                    return 0;
                }
                else {
                    playerInventory.setItem(i, new ItemStack(fuel, item.getMaxStackSize()));

                    return addItemToPlayer(playerInventory, item.getAmount() + amount - item.getMaxStackSize());
                }

            }
        }
        return amount;
    }
}
