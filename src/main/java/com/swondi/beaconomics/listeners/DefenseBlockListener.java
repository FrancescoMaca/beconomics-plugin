package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.helpers.ItemStackCreator;
import com.swondi.beaconomics.managers.DefenseBlocksManager;
import com.swondi.beaconomics.managers.NexusManager;
import com.swondi.beaconomics.models.Nexus;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class DefenseBlockListener implements Listener {

    @EventHandler
    public void onDefenseBlockBreak(BlockBreakEvent event) {
        if (DefenseBlocksManager.isDefense(event.getBlock().getLocation())) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onDefenseBlockPlace(BlockPlaceEvent event) {
        ItemMeta handMeta = event.getItemInHand().getItemMeta();
        Player player = event.getPlayer();

        if (handMeta == null) return;

        PersistentDataContainer pdc = handMeta.getPersistentDataContainer();
        NamespacedKey defenseKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_DEFENSE_BLOCK_TAG);
        if (!pdc.has(defenseKey)) {
            return;
        }

        // It's a defense block
        Nexus nexus = NexusManager.getNexus(player);

        if (nexus == null) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Place a Nexus before placing defenses!");
            return;
        }

        Chunk nexusChunk = nexus.getLocation().getChunk();
        Chunk placementChunk = event.getBlock().getLocation().getChunk();

        if (nexusChunk.getX() != placementChunk.getX() || nexusChunk.getZ() != placementChunk.getZ()) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can place defenses only in the same chunk as your Nexus!");
            return;
        }

        DefenseBlocksManager.addDefense(event.getBlock().getType(), event.getBlock().getLocation());
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItem();

        if (itemInHand != null || event.getAction() != Action.RIGHT_CLICK_BLOCK || !player.isSneaking()) {
            return;
        }

        Block clicked = event.getClickedBlock();

        if (clicked == null || !DefenseBlocksManager.isDefense(clicked.getLocation())) {
            return;
        }

        event.setCancelled(true);

        // TODO: check if inventory is full
        DefenseBlocksManager.removeDefense(event.getClickedBlock().getLocation());

        player.getInventory().addItem(ItemStackCreator.createDefenseBlock(clicked.getType(), true));
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 0.1f);
        clicked.setType(Material.AIR);
    }
}
