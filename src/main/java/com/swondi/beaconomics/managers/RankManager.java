package com.swondi.beaconomics.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class RankManager {

    private static final YamlManager yaml = new YamlManager("ranks.yml");

    public static void setRank(Player player, String rank) {
        UUID uuid = player.getUniqueId();

        yaml.set("ranks." + uuid + ".name", rank.toLowerCase());
        yaml.set("ranks." + uuid + ".lastKnownName", player.getName());
        yaml.save();
    }

    public static String getRank(Player player) {
        UUID uuid = player.getUniqueId();
        FileConfiguration config = yaml.getConfiguration();
        String path = "ranks." + uuid + ".name";

        if (!config.contains(path)) {
            return "default";
        }

        return config.getString(path, "default");
    }

}
