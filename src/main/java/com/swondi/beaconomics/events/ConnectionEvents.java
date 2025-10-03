package com.swondi.beaconomics.events;

import com.swondi.beaconomics.managers.PlayerDataManager;
import com.swondi.beaconomics.scoreboards.Scoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionEvents implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerDataManager.recordJoin(event.getPlayer());

        Scoreboard.setupScoreboard(event.getPlayer());
    }
}
