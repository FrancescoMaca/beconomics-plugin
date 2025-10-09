package com.swondi.beaconomics.utils;

import com.swondi.beaconomics.Beaconomics;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class UIHelper {

    public static ItemStack createBackArrow(String action_key) {
        final ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta meta = backArrow.getItemMeta();

        if (meta != null){
            NamespacedKey key = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_NAVIGATE_KEY);

            meta.setDisplayName(ChatColor.GRAY + "Back");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, action_key);
            backArrow.setItemMeta(meta);
        }

        return backArrow;
    }
}
