package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.TemporaryBlocksManager;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class TemporaryBlocksListener implements Listener {

    private static final NamespacedKey tempKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_TEMPORARY_BLOCK_TAG);
    private static final List<Material> temporaryBlockTypes = new ArrayList<>() {{
        add(Material.COBBLESTONE);
        add(Material.SANDSTONE);
        add(Material.STONE);
        add(Material.SMOOTH_STONE);
        add(Material.PRISMARINE);
        add(Material.BRICKS);
        add(Material.NETHER_BRICKS);
        add(Material.QUARTZ_BLOCK);
        add(Material.RED_SANDSTONE);
        add(Material.PURPUR_BLOCK);
    }};

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // check if block is among the temporary blocks (this reduces cpu load)
        if (!temporaryBlockTypes.contains(event.getBlock().getType())) return;

        TemporaryBlocksManager.handleBlockBreak(event.getBlock().getLocation());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // check if block is tagged as temporary
        ItemStack item = event.getItemInHand();

        // get PDC of the item and look for the key
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        if (pdc.has(tempKey, PersistentDataType.BYTE)) {
            Bukkit.getLogger().info("Placing temporary block at " + event.getBlock().getLocation());
            Block block = event.getBlock();
            TemporaryBlocksManager.addTemporaryBlock(block.getLocation(), block.getType(), 10000);
        }
    }
}
