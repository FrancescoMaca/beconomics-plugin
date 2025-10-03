package com.swondi.beaconomics.debug.listeners;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.PlayerManager;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DebugBeaconLevelListener implements Listener {

    private final JavaPlugin plugin = Beaconomics.getInstance();
    private final NamespacedKey beaconLevelKey;

    public DebugBeaconLevelListener() {
        this.beaconLevelKey = new NamespacedKey(plugin, Constants.PLAYER_PERSIST_BEACON_LEVEL_KEY);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();

        int level = PlayerManager.getBeaconLevel(player);

        switch (blockType) {
            case DIAMOND_BLOCK -> {
                PlayerManager.setBeaconLevel(player, level + 1);
                player.sendMessage("§aBeacon level increased! Current level: " + level);
            }
            case EMERALD_BLOCK -> {
                PlayerManager.setBeaconLevel(player, 0);
                player.sendMessage("§cBeacon level reset!");
            }
        }
    }
}
