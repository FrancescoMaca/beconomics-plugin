package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.DefenseBlocksManager;
import com.swondi.beaconomics.managers.NexusManager;
import com.swondi.beaconomics.models.Nexus;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public class DefenseBlockListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        DefenseBlocksManager.removeTemporaryBlock(event.getBlock().getLocation());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack item = event.getItemInHand();
        Nexus nexus = NexusManager.getNexus(player);

        if (nexus == null) {
            player.sendMessage(ChatColor.RED + "You cannot place defenses without a beacon!");
            return;
        }

        Chunk placedChunk = block.getChunk();
        Chunk nexusChunk = nexus.getLocation().getChunk();
        if (placedChunk.getX() != nexusChunk.getX() || placedChunk.getZ() != nexusChunk.getZ()) {
            player.sendMessage(ChatColor.RED + "You cannot place defenses outside the Nexus chunk!");
            return;
        }

        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        NamespacedKey defenseKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_DEFENSE_BLOCK_TAG);

        if (!pdc.has(defenseKey)) return;

        DefenseBlocksManager.addTemporaryBlock(block, nexus);
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
       Player player = event.getPlayer();
       Block block = event.getBlock();

       player.sendMessage("You are currently hitting " + block.getType().name());
    }
}
