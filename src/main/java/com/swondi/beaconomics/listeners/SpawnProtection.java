package com.swondi.beaconomics.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpawnProtection implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if (isAtSpawn(event.getBlock().getLocation())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot place blocks at spawn!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if (isAtSpawn(event.getBlock().getLocation())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot break blocks at spawn!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (isAtSpawn(event.getBlock().getLocation())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot place water or lava at spawn!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (isAtSpawn(event.getBlock().getLocation())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot fill buckets at spawn!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (isAtSpawn(event.getClickedBlock().getLocation())) {
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot interact with blocks at spawn!");
            event.setCancelled(true);
        }
    }

    private boolean isAtSpawn(Location location) {
        int x = location.getBlockX();
        int z = location.getBlockZ();
        return Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)) < 68;
    }

}
