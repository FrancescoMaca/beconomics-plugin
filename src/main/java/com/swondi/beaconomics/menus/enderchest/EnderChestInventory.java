package com.swondi.beaconomics.menus.enderchest;

import com.swondi.beaconomics.managers.EnderChestManager;
import com.swondi.beaconomics.managers.RankManager;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class EnderChestInventory {

    public static Inventory build(Player player) {

        Constants.Rank playerRank = RankManager.getPlayerRank(player);

        if (playerRank.eChestLayers() == 0) {
            return null;
        }

        Inventory cached = EnderChestManager.getEchest(player);
        Inventory inventory = Bukkit.createInventory(player, playerRank.eChestLayers() * 9, Constants.ENDER_CHEST_TITLE);

        if (cached != null) {
            for (int i = 0; i < playerRank.eChestLayers() * 9; i++) {
                inventory.setItem(i, cached.getContents()[i]);
            }
        }

        return inventory;
    }
}
