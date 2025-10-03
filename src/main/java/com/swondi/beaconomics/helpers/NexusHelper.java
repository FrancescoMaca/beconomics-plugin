package com.swondi.beaconomics.helpers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods for the nexus
 */
public class NexusHelper {
    public static ItemStack createNexusItem() {
        ItemStack itemStack = new ItemStack(Material.BEACON);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) {
            return itemStack;
        }

        itemMeta.setDisplayName("§fNexus");
        List<String> lore = new ArrayList<>();
        lore.add("§7The core of your base.");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
