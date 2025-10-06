package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.PDCManager;
import com.swondi.beaconomics.tasks.DropCleanupTask;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import javax.swing.*;

public class ChestListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        // Only checks for chest break
        if (block.getType() != Material.CHEST) return;

        if (!PDCManager.canOpenChest(block, player)) {
            event.setCancelled(true);
        }
    }
    /**
     * When the chest is open cancels the event if the player who opens it
     * isn't the owner of the chest
     */
    @EventHandler
    public void onChestInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.CHEST) return;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || player.isSneaking()) return;

        Block block = event.getClickedBlock();

        // Cancel before the chest actually opens (this also prevents the sound)
        if (!PDCManager.canOpenChest(block, player)) {
            player.sendMessage(ChatColor.RED + "You can't open somebody else's Starter Kit!");
            event.setCancelled(true);
            return;
        }

        // If allowed, open manually
        player.openInventory(((Chest) block.getState()).getInventory());
    }

    /**
     *
     * @param event
     */
    @EventHandler
    public void onChestBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType() != Material.CHEST) return;

        if (!PDCManager.isSpecialChest(block)) {
             return;
        }

        DropCleanupTask.removeChest("");
    }

    /**
     * AAutomatically destroys a kit chest when its empty
     */
    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (!(event.getInventory().getHolder() instanceof Chest chest)) return;

        Block block = chest.getBlock();

        // Check if it’s actually a chest block still
        if (block.getType() != Material.CHEST) return;

        if (!(block.getState() instanceof TileState state)) return;

        NamespacedKey kitKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_KIT_CHEST_TAG);
        if (!state.getPersistentDataContainer().has(kitKey, PersistentDataType.BYTE)) return;

        // If chest is empty, remove it
        if (chest.getInventory().isEmpty()) {
            block.setType(Material.AIR);
            Bukkit.getWorld("world").spawnParticle(
                Particle.CRIMSON_SPORE,
                chest.getLocation().add(0.5, 0.5, 0.5),
                50, 0.1, 0.1, 0.1
            );
            player.playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1, 1);
        }
    }
}
