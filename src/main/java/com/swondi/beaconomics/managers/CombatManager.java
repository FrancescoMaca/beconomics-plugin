package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.utils.Constants;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatManager {
    private static final Map<UUID, Long> inCombat = new HashMap<>();

    public static void enterCombat(Player player) {
        inCombat.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public static boolean isInCombat(Player player) {
        UUID uuid = player.getUniqueId();
        if (!inCombat.containsKey(uuid)) return false;

        long lastCombat = inCombat.get(uuid);
        if (System.currentTimeMillis() - lastCombat > Constants.PLAYER_COMBAT_DURATION) {
            inCombat.remove(uuid);
            return false;
        }
        return true;
    }

    public static void removeFromCombat(Player player) {
        inCombat.remove(player.getUniqueId());
    }
}
