package com.swondi.beaconomics.menus.nexus;

import com.swondi.beaconomics.managers.NexusManager;
import com.swondi.beaconomics.models.Nexus;
import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.utils.UIHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BeaconFuelMenu {
    private static final ItemStack separator = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
    private static final List<Integer> availableSlots = List.of(2,3,4,5,6,7,8,11,12,13,14,15,16,17,20,21,22,23,24,25,26);

    static {
        ItemMeta meta = separator.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(" ");
            meta.setLore(List.of());
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);;
            separator.setItemMeta(meta);
        }
    }

    public static Inventory build(Player player) {
        Nexus nexus = NexusManager.getNexus(player);

        if (nexus == null) return null;

        Inventory inv = Bukkit.createInventory(player, 27, Constants.BEACON_FUEL_MENU_TITLE);

        inv.setItem(0, UIHelper.createBackArrow(Constants.UI_NEXUS_MAIN_MENU_VALUE));
        inv.setItem(1, createFuelInfos(nexus));
        inv.setItem(10, separator);
        inv.setItem(19, separator);
        inv.setItem(9, createNextPage());
        inv.setItem(18, createPreviousPage());

        int stacks = nexus.getFuelAmount() / 64;

        for (int i = 0; i < Math.min(stacks, availableSlots.size()); i++) {
            inv.setItem(availableSlots.get(i), new ItemStack(Material.COAL, 64));
        }

        if (nexus.getFuelAmount() % 64 > 0 && stacks < availableSlots.size()) {
            int remaining = nexus.getFuelAmount() % 64;
            inv.setItem(availableSlots.get(stacks), new ItemStack(Material.COAL, remaining));
        }

        return inv;
    }

    /**
     * Method to update the UI without rebuilding the inventory
     * @param nexusInventory the inventory currently opened
     * @param nexus the nexus that has the inventory
     */
    public static void updateFuelInfo(Inventory nexusInventory, Nexus nexus) {
        ItemStack updatedInfo = BeaconFuelMenu.createFuelInfos(nexus);
        nexusInventory.setItem(1, updatedInfo);
    }

    private static ItemStack createNextPage() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Next Page");
            meta.setLore(List.of(ChatColor.GRAY + "Page 1 of 2"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);;
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ItemStack createFuelInfos(Nexus nexus) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            double consumption = nexus.getFuelConsumption();
            int fuel = nexus.getFuelAmount();
            double capacity = 2688.0;
            double fuelPercentage = fuel / capacity;

            // Determine color based on remaining fuel %
            ChatColor fuelColor = ChatColor.GREEN;
            if (fuelPercentage <= 0.25) {
                fuelColor = ChatColor.RED;
            } else if (fuelPercentage <= 0.6) {
                fuelColor = ChatColor.GOLD; // Yellow is sometimes hard to read
            }

            String formattedPercentage = String.format("%.1f", fuelPercentage * 100);
            String formattedConsumption = String.format("%.1f", consumption);

            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "⛽ Nexus Fuel Info");

            meta.setLore(List.of(
                    ChatColor.GRAY + "Displays the current fuel status of your Nexus.",
                    ChatColor.DARK_GRAY + "-------------------------",
                    ChatColor.WHITE + "• Total Consumption: " + ChatColor.GOLD + formattedConsumption + ChatColor.WHITE + " coal / 30m",
                    ChatColor.WHITE + "• Capacity: " + fuelColor + fuel + ChatColor.WHITE + " / " + (int) capacity +
                            ChatColor.GRAY + " (" + fuelColor + formattedPercentage + "%" + ChatColor.GRAY + ")",
                    ChatColor.WHITE + "• Time before empty: " + ChatColor.AQUA + calculateNexusTimeBeforeEmpty(nexus),
                    ChatColor.DARK_GRAY + "-------------------------"
            ));

            item.setItemMeta(meta);
        }

        return item;
    }

    private static String calculateNexusTimeBeforeEmpty(Nexus nexus) {
        double consumption = nexus.getFuelConsumption();
        int fuel = nexus.getFuelAmount();

        if (consumption <= 0) return "∞";
        if (fuel <= 0) return "0m";

        // Each unit of fuel consumption happens every 30 minutes
        double totalMinutesLeft = (fuel / consumption) * 30;

        int totalMinutes = (int) Math.floor(totalMinutesLeft);
        int days = totalMinutes / (60 * 24);
        int hours = (totalMinutes % (60 * 24)) / 60;
        int minutes = totalMinutes % 60;

        StringBuilder formatted = new StringBuilder();
        if (days > 0) formatted.append(days).append("d ");
        if (hours > 0) formatted.append(hours).append("h ");
        if (minutes > 0 || formatted.length() == 0) formatted.append(minutes).append("m");

        return formatted.toString().trim();
    }

    private static ItemStack createPreviousPage() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Previous Page");
            meta.setLore(List.of(ChatColor.GRAY + "Page 1 of 2"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);;
            item.setItemMeta(meta);
        }
        return item;
    }
}
