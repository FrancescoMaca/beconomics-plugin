package com.swondi.beaconomics.managers;

import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class PlayerDataManager {

    private static final YamlManager yaml = new YamlManager("players-data.yml");
    private static final String root = "players";

    public static void recordJoin(Player player) {
        UUID uuid = player.getUniqueId();
        String path = root + "." + uuid;

        yaml.set(path + ".name", player.getName());

        int joins = yaml.getInt(path + ".joins") + 1;
        yaml.set(path + ".joins", joins);

        if (!yaml.getConfiguration().contains(path + ".firstJoin")) {
            yaml.set(path + ".firstJoin", new Date().toString());
        }

        yaml.set(path + ".lastJoin", new Date().toString());

        yaml.save();
    }

    public static String getName(UUID uuid) {
        return yaml.getString(root + "." + uuid + ".name");
    }

    public static UUID getUUID(String name) {
        for (String key : yaml.getConfiguration().getConfigurationSection(root).getKeys(false)) {
            if (yaml.getString(root + "." + key + ".name").equalsIgnoreCase(name)) {
                return UUID.fromString(key);
            }
        }

        return null;
    }

    public static int getJoinCount(UUID uuid) {
        return yaml.getInt(root + "." + uuid + ".joins");
    }
}
