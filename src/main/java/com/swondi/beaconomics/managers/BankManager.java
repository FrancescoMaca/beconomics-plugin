package com.swondi.beaconomics.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BankManager {
    private static final YamlManager yaml = new YamlManager("money.yml");

    public static Map<UUID, Integer> getAllPlayersMoney() {
        Map<UUID, Integer> balances = new HashMap<>();

        // Get the configuration section for all UUIDs
        FileConfiguration config = yaml.getConfiguration();
        for (String uuidString : config.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);

            // Retrieve the player's onhand money
            int onhand = config.getInt(uuidString + ".onhand", 0);

            // Retrieve the player's bank money (if present)
            int bank = config.contains(uuidString + ".bank.amount") ?
                    config.getInt(uuidString + ".bank.amount", 0) : 0;

            // Calculate total money and put it in the map
            int totalMoney = onhand + bank;
            balances.put(uuid, totalMoney);
        }

        return balances;
    }

    /**
     * Gets the target player's on-hand money from file.
     * @param player the target player
     * @return the amount of money the player has on hand
     */
    public static int getOnHandMoney(Player player) {
        String path = player.getUniqueId().toString() + ".onhand";
        return yaml.getConfiguration().getInt(path, 0);
    }

    /**
     * Gets the target player's bank money from file.
     * @param player the target player
     * @return the amount of money the player has in the bank
     */
    public static int getBankMoney(Player player) {
        String path = player.getUniqueId().toString() + ".bank.amount";
        return yaml.getConfiguration().getInt(path, 0);
    }

    /**
     * Formats a money amount into a readable string
     * @param value the amount of money
     * @return a string containing the formatted money (i.e. 7.12B, 1.23M, 12.22k)
     */
    public static String getFormattedMoney(int value) {
        if (value >= 1_000_000_000) {
            return String.format("%.2fB", value / 1_000_000_000.0);
        } else if (value >= 1_000_000) {
            return String.format("%.2fM", value / 1_000_000.0);
        } else if (value >= 1_000) {
            return String.format("%.2fK", value / 1_000.0);
        } else {
            return String.valueOf(value);
        }
    }

    /**
     * Adds money to a player (both on-hand and bank can be increased).
     * @param player the player
     * @param amount the amount of money to add to the player
     * @param toBank whether to add to the player's bank (false for on-hand)
     */
    public static void addMoney(Player player, int amount, boolean toBank) {
        if (toBank) {
            int currentBank = getBankMoney(player);
            setBankMoney(player, currentBank + amount);
        } else {
            int currentOnHand = getOnHandMoney(player);
            setOnHandMoney(player, currentOnHand + amount);
        }
    }

    /**
     * Sets a player's on-hand money
     * @param player the player
     * @param amount the amount of new on-hand money of the player
     */
    public static void setOnHandMoney(Player player, int amount) {
        String path = player.getUniqueId() + ".onhand";
        yaml.getConfiguration().set(path, amount);
        yaml.save();
    }

    /**
     * Sets a player's bank money
     * @param player the player
     * @param amount the amount of new bank money of the player
     */
    public static void setBankMoney(Player player, int amount) {
        String path = player.getUniqueId() + ".bank.amount";
        yaml.getConfiguration().set(path, amount);
        yaml.save();
    }
}
