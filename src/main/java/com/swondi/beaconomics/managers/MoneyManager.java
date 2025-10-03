package com.swondi.beaconomics.managers;

import org.bukkit.entity.Player;


public class MoneyManager {
    private static final YamlManager yaml = new YamlManager("money.yml");

    /**
     * Gets the target players money from file
     * @param player the target player
     * @return the amount of money
     */
    public static int getMoney(Player player) {
        String path = player.getUniqueId().toString();
        return yaml.getConfiguration().getInt(path, 0);
    }

    /**
     * Formats a player's money into a readable string
     * @param player the player
     * @return a string containing the formatted money (i.e. 7.12B, 1.23M, 12.22k)
     */
    public static String getFormattedMoney(Player player) {
        int money = getMoney(player);
        return getFormattedMoney(money);
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
     * Adds money to a player
     * @param player the player
     * @param amount the amount of money to add to the player
     */
    public static void addMoney(Player player, int amount) {
        int current = getMoney(player);
        setMoney(player, current + amount);
    }

    /**
     * Sets a players money
     * @param player the player
     * @param amount the amount of new money of the player
     */
    public static void setMoney(Player player, int amount) {
        String path = player.getUniqueId().toString();
        yaml.getConfiguration().set(path, amount);
        yaml.save();
    }
}
