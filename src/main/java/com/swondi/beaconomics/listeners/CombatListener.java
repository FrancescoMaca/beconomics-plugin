package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.managers.BankManager;
import com.swondi.beaconomics.managers.CombatManager;
import com.swondi.beaconomics.scoreboards.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class CombatListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player damaged = (Player) event.getEntity();

        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            CombatManager.enterCombat(damaged);
            CombatManager.enterCombat(damager);
        }
    }

    @EventHandler
    public void onPlayerKill(EntityDeathEvent event) {
        // Check if the entity that died is a player
        if (event.getEntity() instanceof Player killedPlayer) {

            // Check who dealt the killing blow
            if (event.getEntity().getKiller() != null) {
                Player killer = event.getEntity().getKiller();

                // Handle the kill event
                int killedMoney = BankManager.getOnHandMoney(killedPlayer);
                int killerMoney = BankManager.getOnHandMoney(killer);

                int toTake = Math.round(killedMoney * 0.1F);

                BankManager.setOnHandMoney(killedPlayer, killedMoney - toTake);
                BankManager.setOnHandMoney(killer, killerMoney + toTake);
                Scoreboard.updateScore(killedPlayer);
                Scoreboard.updateScore(killer);
            }
        }
    }
}