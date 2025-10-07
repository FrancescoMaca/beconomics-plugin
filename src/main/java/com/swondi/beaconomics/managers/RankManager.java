package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RankManager {
    private static Map<UUID, Constants.Rank> ranks = new HashMap<>();
    private static YamlManager yaml = new YamlManager("ranks.yml");

    public static void setPlayerRank(UUID uuid, Constants.Rank rank) {
        ranks.put(uuid, rank);
        yaml.set("ranks." + uuid, rank.name());
        yaml.save();
    }

    public static Constants.Rank getPlayerRank(Player player) {
        return ranks.getOrDefault(player.getUniqueId(), Constants.RANKS.get("Default"));
    }

    public static void load() {
        String path = "ranks";

        if (!yaml.getConfiguration().contains(path)) {
            Bukkit.getLogger().severe("[RankManager] Could not load ranks");
            return;
        }

        Set<String> keys = yaml.getConfiguration().getConfigurationSection(path).getKeys(false);
        int loaded = 0;

        for (String key : keys) {
            UUID playerUUID = UUID.fromString(key);
            String name = yaml.get(path + "." + key).toString();

            ranks.put(playerUUID, Constants.RANKS.get(name));
            loaded++;
        }

        Bukkit.getLogger().info("[RankManager] Loaded " + loaded + " ranks");
    }
}
