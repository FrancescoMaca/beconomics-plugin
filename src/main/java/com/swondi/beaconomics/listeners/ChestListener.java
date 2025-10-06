package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.animations.KitAnimation;
import com.swondi.beaconomics.managers.KitManager;
import com.swondi.beaconomics.managers.PDCManager;
import com.swondi.beaconomics.tasks.DropCleanupTask;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.swing.*;
import java.util.Arrays;

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
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.CHEST) return;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
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
        Player player = event.getPlayer();

        if (block.getType() != Material.CHEST) return;

        if (!PDCManager.isSpecialChest(block)) return;

        Chest chest = (Chest) block.getState();
        var inv = chest.getBlockInventory();
/* FIXME
        Location location = block.getLocation();
        ArmorStand nameTag = KitAnimation.kitChestNameTags.remove(location);

        if(nameTag != null)
            nameTag.remove();
 */

        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            if (item.getType() == Material.BLACK_STAINED_GLASS_PANE) continue;

            block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), item);
        }

        inv.clear();
        block.setType(Material.AIR);

        player.playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1, 1);

        DropCleanupTask.removeChest("");
    }

    /**
     * Automatically destroys a kit chest when its empty
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
        if (Arrays.stream(chest.getInventory().getContents()).allMatch(i -> i == null || i.getType() == Material.BLACK_STAINED_GLASS_PANE)) {
            block.setType(Material.AIR);
            player.playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1, 1.5F);
        }
    }
    //TODO check if best way of doing this
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked != null && clicked.getType() == Material.BLACK_STAINED_GLASS_PANE) {
            event.setCancelled(true);
        }
    }
}
