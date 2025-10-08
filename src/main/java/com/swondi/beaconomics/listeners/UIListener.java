package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.helpers.ItemStackCreator;
import com.swondi.beaconomics.managers.NexusManager;
import com.swondi.beaconomics.managers.BankManager;
import com.swondi.beaconomics.managers.PlayerManager;
import com.swondi.beaconomics.menus.nexus.BeaconMainMenu;
import com.swondi.beaconomics.menus.shop.*;
import com.swondi.beaconomics.models.Nexus;
import com.swondi.beaconomics.scoreboards.Scoreboard;
import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.utils.EntityGenerator;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class UIListener implements Listener {

    // TODO: Review
    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        ItemMeta meta = clicked.getItemMeta();

        if (meta == null) return;

        NamespacedKey navigationKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_NAVIGATE_KEY);
        NamespacedKey upgradeKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_BEACON_LEVEL_KEY);
        NamespacedKey actionKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_ACTION_KEY);

        Player player = (Player) event.getWhoClicked();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        // Checks if the click has some persistent data type for beacon upgrade
        if (container.has(upgradeKey, PersistentDataType.INTEGER)) {
            int price = container.getOrDefault(upgradeKey, PersistentDataType.INTEGER, 9999999);
            handleUpgradeBeacon(event, price);

            // Not stopping the method because an item can have multiple triggers
        }

        // Checks for actions (change to environment)
        if (container.has(actionKey, PersistentDataType.STRING)) {
            event.setCancelled(true);

            String value =  container.get(actionKey, PersistentDataType.STRING);

            switch (value) {
                case Constants.UI_NEXUS_PICKUP_VALUE -> handleBeaconPickup(event);
                case Constants.UI_SHOP_BUY_VALUE -> handleBuyItem(event);
            }
        }

        // Checks for any navigations (open new inventory)
        if (container.has(navigationKey, PersistentDataType.STRING)) {
            event.setCancelled(true);

            String value = meta.getPersistentDataContainer().get(navigationKey, PersistentDataType.STRING);

            switch(Objects.requireNonNull(value)) {
                case Constants.UI_NEXUS_MAIN_MENU_VALUE -> player.openInventory(BeaconMainMenu.build(player));
                case Constants.UI_SHOP_MAIN_MENU_VALUE -> player.openInventory(ShopMainMenu.build(player));
                case Constants.UI_SHOP_GENS_MENU_VALUE -> player.openInventory(ShopGeneratorsMenu.build(player));
                case Constants.UI_SHOP_UTILITY_BLOCKS_MENU_VALUE -> player.openInventory(ShopUtilityMenu.build(player));
                case Constants.UI_SHOP_DEFENCE_MENU_VALUE -> player.openInventory(ShopDefenseBlocksMenu.build(player));
                case Constants.UI_SHOP_TEMP_BLOCKS_MENU_VALUE -> player.openInventory(ShopTempBlocksMenu.build(player));
                case Constants.UI_SHOP_TOOLS_MENU_VALUE -> player.openInventory(ShopToolsMenu.build(player));
            }
        }
    }

    private static void handleUpgradeBeacon(InventoryClickEvent event, int levelPrice) {
        ItemStack clicked = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        // Check if the input is valid in the first place
        if (clicked.getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
            player.sendMessage(ChatColor.RED + "You already have this level!");
            return;
        }

        synchronized (player) {
            int currentLevel = PlayerManager.getBeaconLevel(player);
            int playerMoney = BankManager.getOnHandMoney(player);

            if (levelPrice > playerMoney) {
                player.sendMessage(ChatColor.RED + "You don't have enough money to upgrade the Nexus");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }

            // Deduct money and upgrade
            BankManager.setOnHandMoney(player, playerMoney - levelPrice);
            PlayerManager.setBeaconLevel(player, currentLevel + 1);

            // Update UI and scoreboard
            Scoreboard.updateScore(player);
            player.closeInventory();

            if (currentLevel + 1 == Constants.BEACON_MAX_LEVEL) {
                player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "✔ "
                    + ChatColor.GREEN + "Your Nexus has reached the "
                    + ChatColor.GOLD + "FINAL LEVEL" + ChatColor.GREEN + "!");
                player.sendMessage(ChatColor.GRAY + "Maximum power unlocked. You are unstoppable!");
            } else {
                player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "✔ "
                    + ChatColor.GREEN + "Your Nexus has been upgraded to "
                    + ChatColor.AQUA + "Level " + (currentLevel + 1) + ChatColor.GREEN + "!");
                player.sendMessage(ChatColor.GRAY + "Keep upgrading to reach its full potential.");
            }

            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);

            Nexus nexus = NexusManager.getNexus(player);
            Location nexusLocation = nexus.getLocation();

            if (nexusLocation != null) {
                EntityGenerator.spawnFireworkExplosion(nexusLocation, currentLevel + 1);
            }
        }
    }

    private static void handleBeaconPickup(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        Nexus beaconBlock = NexusManager.getNexus(player);

        if (beaconBlock != null) {

            player.getInventory().addItem(ItemStackCreator.createNexus());

            NexusManager.unregisterNexus(beaconBlock.getLocation().getBlock());
            beaconBlock.getLocation().getBlock().setType(Material.AIR);

            player.sendMessage("You picked up your " + ChatColor.GOLD + "Nexus" + ChatColor.WHITE + "!");
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, .5f);
        } else {
            player.sendMessage("No beacon found!");
        }

        player.closeInventory();
    }

    private static void handleBuyItem(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        ItemMeta meta = clicked.getItemMeta();

        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey priceKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_PRICE_KEY);

        if (!container.has(priceKey, PersistentDataType.INTEGER)) {
            return;
        }

        int price = container.getOrDefault(priceKey, PersistentDataType.INTEGER, 9999999);
        int balance = BankManager.getOnHandMoney(player);

        if (balance < price) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            player.sendMessage("§cYou don't have enough money!");
            player.sendMessage(ChatColor.DARK_RED + "Withdraw some money from your bank with " + ChatColor.GOLD + "/bank withdraw [amount]");
            return;
        }

        NamespacedKey isGenKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_GENERATOR_TAG);
        NamespacedKey isDefenseKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_DEFENSE_BLOCK_TAG);

        ItemStack item;

        if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(isGenKey)) {
            item = ItemStackCreator.createGenerator(event.getCurrentItem().getType(), true);
        }
        else if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(isDefenseKey)) {
            item = ItemStackCreator.createDefenseBlock(event.getCurrentItem().getType(), true);
        }
        else {
            item = ItemStackCreator.createTemporaryBlock(event.getCurrentItem().getType(), true);
        }

        if (item == null) {
            player.sendMessage(ChatColor.RED + "There was an error while buying this item. Try again later.");
            return;
        }

        // Removes money
        BankManager.setOnHandMoney(player, balance - price);
        Scoreboard.updateScore(player);

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        player.getInventory().addItem(item);
        player.sendMessage("§aYou purchased a " + item.getItemMeta().getDisplayName() + "§a for §6$" + price);
    }
}