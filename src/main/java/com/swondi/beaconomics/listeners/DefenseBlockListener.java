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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class DefenseBlockListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
//        DefenseBlocksManager.removeTemporaryBlock(event.getBlock().getLocation());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        Material mat = block.getType();

        ItemMeta handMeta = event.getItemInHand().getItemMeta();

        PersistentDataContainer pdc = handMeta.getPersistentDataContainer();
        NamespacedKey defenseKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_DEFENSE_BLOCK_TAG);

        if (!pdc.has(defenseKey)) {
            return;
        }

        event.getPlayer().sendMessage("You placed a defense");
        DefenseBlocksManager.addDefense(event.getBlock().getType(), event.getBlock().getLocation());
    }
}
