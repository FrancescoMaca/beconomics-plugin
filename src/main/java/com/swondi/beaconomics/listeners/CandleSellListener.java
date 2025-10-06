package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.data.CandleData;
import com.swondi.beaconomics.managers.MoneyManager;
import com.swondi.beaconomics.scoreboards.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CandleSellListener implements Listener {
    // Map that maps candles to their respective ChatColor
    private static final Map<Material, ChatColor> candleColors = new HashMap<>();

    static {
        // Initialize the map with candle types and their corresponding colors
        candleColors.put(Material.WHITE_CANDLE, ChatColor.WHITE);
        candleColors.put(Material.ORANGE_CANDLE, ChatColor.GOLD);
        candleColors.put(Material.MAGENTA_CANDLE, ChatColor.LIGHT_PURPLE);
        candleColors.put(Material.LIGHT_BLUE_CANDLE, ChatColor.AQUA);
        candleColors.put(Material.YELLOW_CANDLE, ChatColor.YELLOW);
        candleColors.put(Material.LIME_CANDLE, ChatColor.GREEN);
        candleColors.put(Material.PINK_CANDLE, ChatColor.LIGHT_PURPLE);
        candleColors.put(Material.GRAY_CANDLE, ChatColor.DARK_GRAY);
        candleColors.put(Material.LIGHT_GRAY_CANDLE, ChatColor.GRAY);
        candleColors.put(Material.CYAN_CANDLE, ChatColor.DARK_AQUA);
        candleColors.put(Material.PURPLE_CANDLE, ChatColor.DARK_PURPLE);
        candleColors.put(Material.BLUE_CANDLE, ChatColor.DARK_BLUE);
        candleColors.put(Material.BROWN_CANDLE, ChatColor.GREEN);
        candleColors.put(Material.GREEN_CANDLE, ChatColor.DARK_GREEN);
        candleColors.put(Material.RED_CANDLE, ChatColor.DARK_RED);
        candleColors.put(Material.BLACK_CANDLE, ChatColor.BLACK);
    }

    /**
     * Listens for right-clicks on a candle item.
     */
    @EventHandler
    public void onRightClickCandle(PlayerInteractEvent event) {
        if (!event.getAction().toString().contains("RIGHT_CLICK")) return;

        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        Material type = handItem.getType();
        int value = CandleData.getValue(type);
        if (value <= 0) return;

        int totalAmount = 0;

        // Loop over inventory and remove all stacks of this candle type
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack slotItem = player.getInventory().getItem(i);

            if (slotItem != null && slotItem.getType() == type) {
                totalAmount += slotItem.getAmount();
                player.getInventory().setItem(i, null);
            }
        }

        if (totalAmount == 0) return;

        int totalValue = value * totalAmount;

        // Add money & update scoreboard
        MoneyManager.addMoney(player, totalValue);
        Scoreboard.updateScore(player);

        player.sendMessage(ChatColor.GREEN + "You sold " + totalAmount + "x " + formatName(type) + " for $" + totalValue + "!");

        // Prevent ghost placement
        event.setUseItemInHand(org.bukkit.event.Event.Result.DENY);
        event.setCancelled(true);
    }

    /**
     * Formats the name of a material
     * @param material the material
     * @return The formatted name with spaces instead of underscores
     */
    private String formatName(Material material) {
        String candleName = material.name().replaceAll("_", " ").toLowerCase();

        ChatColor color = candleColors.getOrDefault(material, ChatColor.GREEN);

        return color + candleName + ChatColor.GREEN;
    }
}
