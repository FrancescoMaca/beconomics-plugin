package com.swondi.beaconomics.menus.nexus;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.PlayerManager;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static com.swondi.beaconomics.utils.UIHelper.createBackArrow;

public class BeaconUpgradeMenu {
    private static final int MAX_LEVEL = 5;
    private static final int[] UPGRADE_SLOTS = {11, 12, 13, 14, 15};

    private static final int[] UPGRADE_COSTS = {
        10_000,    // Level I (starting)
        50_000,    // Level II → unlock wool
        500_000,   // Level III → unlock concrete
        5_000_000, // Level IV → unlock terracotta
        20_000_000 // Level V → max
    };

    private static final String[] LEVEL_NAME = { "I", "II", "III", "IV", "V" };

    public static Inventory build(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, Constants.BEACON_UPGRADE_MENU_TITLE);
        int playerLevel = PlayerManager.getBeaconLevel(player);
        for (int i = 0; i < MAX_LEVEL; i++) {
            boolean active = playerLevel > i;
            // Glass color: green if unlocked, red if locked
            ItemStack pane = new ItemStack(active
                ? Material.LIME_STAINED_GLASS_PANE
                : Material.RED_STAINED_GLASS_PANE
            );

            NamespacedKey key = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_BEACON_LEVEL_KEY);

            ItemMeta meta = pane.getItemMeta();
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, UPGRADE_COSTS[i]);
            meta.setDisplayName(ChatColor.AQUA + "Level " + LEVEL_NAME[i]);

            meta.setLore(createLevelLore(i, active));
            pane.setItemMeta(meta);
            inv.setItem(UPGRADE_SLOTS[i], pane);
        }

        inv.setItem(0,  createBackArrow(Constants.UI_NEXUS_MAIN_MENU_VALUE));
        return inv;
    }

    private static List<String> createLevelLore(int i, boolean active) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "-----------------");

        // Unlock description
        switch (i + 1) {
            case 1:
                lore.add(ChatColor.WHITE + "• Unlocks 1 extra fuel slot");
                break;
            case 2:
                lore.add(ChatColor.WHITE + "• Increases resource drop by 10%");
                break;
            case 3:
                lore.add(ChatColor.WHITE + "• Unlocks Pick Up automation");
                break;
            case 4:
                lore.add(ChatColor.WHITE + "• Increases resource drop by 20%");
                lore.add(ChatColor.WHITE + "• Unlocks extra team abilities");
                break;
            case 5:
                lore.add(ChatColor.WHITE + "• Maximum upgrade");
                lore.add(ChatColor.WHITE + "• Boosts production by 50%");
                break;
        }

        lore.add(ChatColor.GRAY + "-----------------");

        // Price
        if (active) {
            lore.add(ChatColor.GREEN + "✓ Already unlocked");
        } else {
            lore.add(ChatColor.YELLOW + "Cost: $" + UPGRADE_COSTS[i]);
            lore.add("");
            lore.add(ChatColor.GRAY + "Right-click to purchase");
        }

        return lore;
    }
}
