package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.managers.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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
}