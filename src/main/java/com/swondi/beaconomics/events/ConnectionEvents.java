package com.swondi.beaconomics.events;

import com.swondi.beaconomics.managers.PlayerDataManager;
import com.swondi.beaconomics.scoreboards.Scoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectionEvents implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Records the join count
        PlayerDataManager.recordJoin(player);

        // Sets up the money scoreboard
        Scoreboard.setupScoreboard(player);
    }
}
