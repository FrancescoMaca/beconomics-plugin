package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.managers.CombatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogoutListener implements Listener {


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (CombatManager.isInCombat(player)) {
            player.setHealth(0.0);
            Bukkit.broadcastMessage("§c" + player.getName() + " tried to escape combat and has died!");
        }

        CombatManager.removeFromCombat(player);
    }
}
