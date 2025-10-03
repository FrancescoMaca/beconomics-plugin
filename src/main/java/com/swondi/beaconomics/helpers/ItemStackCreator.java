package com.swondi.beaconomics.helpers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackCreator {

    public static ItemStack createNexus() {
        ItemStack nexus = new ItemStack(Material.BEACON);
        ItemMeta meta = nexus.getItemMeta();

        if (meta == null) return nexus;

        meta.setDisplayName(ChatColor.AQUA + "Nexus");
        List<String> lore = new ArrayList<>();
        lore.add("§7The core of your base. Defend it at all costs!");
        meta.setLore(lore);
        nexus.setItemMeta(meta);

        return nexus;
    }
}
