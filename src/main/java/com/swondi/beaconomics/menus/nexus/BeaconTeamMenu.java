package com.swondi.beaconomics.menus.nexus;

import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.utils.UIHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BeaconTeamMenu {

    public static Inventory build() {
        Inventory inv = Bukkit.createInventory(null, 27, Constants.BEACON_TEAM_MENU_TITLE);

        inv.setItem(0, UIHelper.createBackArrow(Constants.UI_NEXUS_MAIN_MENU_VALUE));

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta meta = barrier.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "Feature Coming Soon");
            meta.setLore(Arrays.asList(
                ChatColor.GRAY + "This page is under development.",
                ChatColor.GRAY + "Stay tuned for updates!",
                ChatColor.YELLOW + "More information coming soon."
            ));
        }

        barrier.setItemMeta(meta);
        inv.setItem(13, barrier);
        return inv;
    }
}
