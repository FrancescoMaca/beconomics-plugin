package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class PlayerManager {

    public static int getBeaconLevel(Player player) {
        NamespacedKey key = new NamespacedKey(Beaconomics.getInstance(), Constants.PLAYER_PERSIST_BEACON_LEVEL_KEY);
        int level = player.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 1);

        return Math.max(Math.min(level, 5), 1);
    }

    public static void setBeaconLevel(Player player, int level) {
        NamespacedKey key = new NamespacedKey(Beaconomics.getInstance(), Constants.PLAYER_PERSIST_BEACON_LEVEL_KEY);
        player.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);
    }
}
