package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class PlayerManager {

    private static final NamespacedKey nexusLevelKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PLAYER_PERSIST_BEACON_LEVEL_KEY);
    private static final NamespacedKey generatorSlotsKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PLAYER_GENERATORS_SLOTS_KEY);

    public static int getGeneratorSlots(Player player) {
        return player.getPersistentDataContainer().getOrDefault(generatorSlotsKey, PersistentDataType.INTEGER, 5);
    }

    public static void setGeneratorSlots(Player player, int slots) {
        player.getPersistentDataContainer().set(generatorSlotsKey, PersistentDataType.INTEGER, slots);
    }

    public static int getBeaconLevel(Player player) {
        int level = player.getPersistentDataContainer().getOrDefault(nexusLevelKey, PersistentDataType.INTEGER, 1);
        return Math.max(Math.min(level, 5), 1);
    }

    public static void setBeaconLevel(Player player, int level) {
        player.getPersistentDataContainer().set(nexusLevelKey, PersistentDataType.INTEGER, level);
    }
}
