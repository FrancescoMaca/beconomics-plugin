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
    private static final NamespacedKey starterKitKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PLAYER_STARTER_KIT_COOLDOWN_KEY);
    private static final NamespacedKey sparkKitKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PLAYER_SPARK_KIT_COOLDOWN_KEY);
    private static final NamespacedKey blazeKitKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PLAYER_BLAZE_KIT_COOLDOWN_KEY);
    private static final NamespacedKey emberKitKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PLAYER_EMBER_KIT_COOLDOWN_KEY);
    private static final NamespacedKey fireKitKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PLAYER_FIRE_KIT_COOLDOWN_KEY);
    private static final NamespacedKey infernoKitKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PLAYER_INFERNO_KIT_COOLDOWN_KEY);

    public static int getBeaconLevel(Player player) {
        int level = player.getPersistentDataContainer().getOrDefault(nexusLevelKey, PersistentDataType.INTEGER, 1);
        return Math.max(Math.min(level, 5), 1);
    }

    public static void setBeaconLevel(Player player, int level) {
        player.getPersistentDataContainer().set(nexusLevelKey, PersistentDataType.INTEGER, level);
    }

    public static void setKitAcquisitionTime(Player player, String kitName) {
        NamespacedKey kitKey = getKitKey(kitName);
        if (kitKey != null) {
            player.getPersistentDataContainer().set(kitKey, PersistentDataType.LONG, System.currentTimeMillis());
        } else {
            // Optionally handle invalid kitName, e.g., log an error or throw an exception
            player.sendMessage("Invalid kit name provided!");
        }
    }

    public static long getKitAcquisitionTime(Player player, String kitName) {
        NamespacedKey kitKey = getKitKey(kitName);

        if (kitKey == null) {
            return 0L;
        }
        else {
            return player.getPersistentDataContainer().getOrDefault(getKitKey(kitName), PersistentDataType.LONG, 0L);
        }
    }

    private static NamespacedKey getKitKey(String kitName) {
        switch (kitName.toLowerCase()) {
            case "starter":
                return starterKitKey;
            case "spark":
                return sparkKitKey;
            case "blaze":
                return blazeKitKey;
            case "ember":
                return emberKitKey;
            case "fire":
                return fireKitKey;
            case "inferno":
                return infernoKitKey;
            default:
                return null;
        }
    }
}
