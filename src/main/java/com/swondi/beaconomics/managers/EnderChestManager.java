package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class EnderChestManager {

    private static final Map<UUID, Inventory> echests = new HashMap<>();

    /**
     * Saves the player ender chest in ram and runs an async thread to save it on the disk async.
     * @param player the player changing the e chest content.
     * @param inventory the player ender chest inventory.
     */
    public static void saveEchest(Player player, Inventory inventory) {

        // Optionally, cache the inventory in memory if needed
        echests.put(player.getUniqueId(), inventory);

        String ss = Arrays.stream(inventory.getContents()).filter(Objects::nonNull).map((s) -> s.getType().name()).collect(Collectors.joining(", "));
        Bukkit.broadcastMessage("Added: " + ss);

        new BukkitRunnable() {
            @Override
            public void run() {
                YamlManager yamlManager = new YamlManager("echest/" + player.getUniqueId() + ".yml");

                // Prepare a map to save non-null items and their positions
                Map<Integer, ItemStack> itemsWithPositions = new HashMap<>();
                int i = 0;
                // Save each item and its position (null items are skipped)
                for (int slot = 0; slot < inventory.getSize(); slot++) {
                    ItemStack item = inventory.getItem(slot);
                    if (item != null) {
                        i++;
                        itemsWithPositions.put(slot, item);
                    } else {
                        // Save null items if you want to keep empty slots
                        itemsWithPositions.put(slot, null);
                    }
                }

                Bukkit.broadcastMessage("Saved e chest to file (" + i + ")");
                // Save the inventory contents to file
                yamlManager.set("enderchest", itemsWithPositions);
            }
        }.runTaskAsynchronously(Beaconomics.getInstance());
    }

    /**
     * Gets the ender chest from cache or from file, depending on if the player has a map entry
     * @param player the player accessing the ender chest
     * @return the inventory of the ender chest
     */
    public static Inventory getEchest(Player player) {
        // Check cache first
        if (echests.containsKey(player.getUniqueId())) {
            Bukkit.broadcastMessage("EChest Cache hit");
            return echests.get(player.getUniqueId());
        }

        // If not cached, load it from disk
        YamlManager yamlManager = new YamlManager("echest/" + player.getUniqueId() + ".yml");
        MemorySection echestContent = (MemorySection) yamlManager.get("enderchest");
        Inventory inventory = Bukkit.createInventory(player, 54, Constants.ENDER_CHEST_TITLE);
        Bukkit.broadcastMessage(echestContent.getKeys(false).toString());

        // Iterate through each slot (key) in the ender chest data
        for (String key : echestContent.getKeys(false)) {
            int slot = Integer.parseInt(key);
            Object itemObject = echestContent.get(key);

            if (itemObject == null) {
                Bukkit.broadcastMessage("key: " + key + " not found");

                continue;
            }

            if (itemObject instanceof ItemStack item) {
                // Set the item in the corresponding inventory slot
                inventory.setItem(slot, item);
                Bukkit.broadcastMessage("Item added at " + slot);
            }
            else {
                Bukkit.broadcastMessage("Item is not a map");
            }
        }

        return inventory;
    }

    /**
     * Frees up space when player goes offline
     * @param player the player that does not need a map entry
     */
    public static void clearCache(Player player) {
        echests.remove(player.getUniqueId());
    }
}