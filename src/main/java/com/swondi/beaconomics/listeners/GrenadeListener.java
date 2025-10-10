package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.models.Grenade;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GrenadeListener implements Listener {

    @EventHandler
    public void onGrenadeUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.FIRE_CHARGE) {

        }
    }
}
