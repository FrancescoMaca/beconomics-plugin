package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.helpers.EntityHelper;
import com.swondi.beaconomics.helpers.ItemStackCreator;
import com.swondi.beaconomics.managers.GeneratorManager;
import com.swondi.beaconomics.managers.NexusManager;
import com.swondi.beaconomics.menus.nexus.BeaconMainMenu;
import com.swondi.beaconomics.models.Nexus;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NexusListener implements Listener {

    /**
     * When a block is broken it checks if it's a nexus, if it is, it removes it from the list
     */
    @EventHandler
    public void onNexusBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType() != Material.BEACON) return;

        NexusManager.unregisterNexus(block);

        // If player is in creative does not drop the beacon
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_GLASS_BREAK, 0.5f, 1.7f);
            return;
        }

        // Otherwise overrides the default drop with a custom one
        event.setDropItems(false);
        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_GLASS_BREAK, 0.5f, 1.7f);

        block.getLocation().getWorld().dropItemNaturally(block.getLocation(), ItemStackCreator.createNexus());
    }

    /**
     * Monitors when a nexus is placed
     */
    @EventHandler
    public void onBeaconPlace(BlockPlaceEvent event) {
        ItemStack nexusItem = event.getItemInHand();
        Player player = event.getPlayer();
        Block nexus = event.getBlock();

        if (nexusItem.getType() != Material.BEACON) return;

        // Already has a beacon?
        if (NexusManager.playerHasNexus(player)) {
            player.sendMessage("§cYou already have a Nexus placed!");
            event.setCancelled(true);
            return;
        }

        // Check if another beacon exists in the same chunk
        if (!NexusManager.canPlaceAt(nexus.getLocation())) {
            player.sendMessage("§cA Nexus is already in this chunk!");
            event.setCancelled(true);
            return;
        }

        // Register nexus
        NexusManager.registerNexus(nexus, player);

        // Sounds
        player.sendMessage("You have placed a " + ChatColor.GOLD + "Nexus" + ChatColor.WHITE + "! Defend it with your " + ChatColor.RED + "life" + ChatColor.WHITE + "!");
        player.sendMessage(
            ChatColor.RED + "WARNING" + ChatColor.WHITE +": If you pickup or destroy the Nexus, the faction will be disbanded. The Nexus is the core of your base, thus if someone breaks it:\n" +
            "- Your team will be disbanded\n" +
            "- Every team member will be kicked\n" +
            "- The "+ ChatColor.GOLD + "/nexus" + ChatColor.WHITE + " will not work anymore"
        );
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 0.5f);
    }

    /**
     * Handles when blocks are placed in the same as a chunk containing a Nexus
     * @param event the event to place block
     */
    @EventHandler
    public void onBeaconRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = event.getClickedBlock();
        Player player = event.getPlayer();

        // Only handle BEACON clicks
        if (clickedBlock == null || clickedBlock.getType() != Material.BEACON) {
            return; // just ignore, don’t send a message
        }

        event.setCancelled(true);

        // Check if the player owns a nexus
        Nexus playerNexus = NexusManager.getNexus(player);

        if (playerNexus == null) {
            player.sendMessage(ChatColor.RED + "You can't interact with somebody else's Nexus!");
            return;
        }

        // Compare chunks
        Chunk ownChunk = playerNexus.getLocation().getChunk();
        Chunk targetChunk = clickedBlock.getChunk();

        if (ownChunk.getX() == targetChunk.getX() && ownChunk.getZ() == targetChunk.getZ()) {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.2f, 2f);

            player.openInventory(BeaconMainMenu.build(player));
        } else {
            player.sendMessage(ChatColor.RED + "This beacon doesn’t belong to you!");
        }
    }

    /**
     * Anti-grief in the chunk where your beacon is located
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Chunk location = event.getBlock().getLocation().getChunk();

        Nexus nexusInChunk = NexusManager.getNexus(location);

        if (nexusInChunk != null && !nexusInChunk.getOwner().equals(player.getUniqueId())) {
            ItemStack block = event.getItemInHand();
            ItemMeta meta = block.getItemMeta();

            if (meta == null || meta.getPersistentDataContainer().has(new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_TEMPORARY_BLOCK_TAG))) {
                return;
            }

            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot build too close to another player's Nexus!");
        }
    }

    /**
     * Prevent breaking blocks in chunks that contain a Nexus
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlock().getLocation().getChunk();

        Nexus nexusInChunk = NexusManager.getNexus(chunk);

        // Players can destroy the Nexus
        if (event.getBlock().getType() == Material.BEACON) return;
        if (GeneratorManager.isAGeneratorLocation(event.getBlock().getLocation())) return;

        // Cancel if chunk has a Nexus and the player is not the owner
        if (nexusInChunk != null && !nexusInChunk.getOwner().equals(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot break blocks in another player's Nexus chunk!");
        }
    }

    /**
     * Prevents fluid to expand in a different type of chunk than what they already are in
     */
    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Chunk chunkTo = event.getToBlock().getLocation().getChunk();
        Chunk chunkFrom = event.getBlock().getLocation().getChunk();
        Nexus nexusTo = NexusManager.getNexus(chunkTo);
        Nexus nexusFrom = NexusManager.getNexus(chunkFrom);

        // Case 1: flowing into empty/unclaimed chunk: cancel event
        if (nexusTo == null && nexusFrom != null) {
            event.setCancelled(true);
            return;
        }

        // Case 2: flowing from unclaimed chunk into a claimed chunk: cancel event
        if (nexusFrom == null && nexusTo != null) {
            event.setCancelled(true);
            return;
        }

        // Case 3: flowing between two different Nexus chunks: cancel event
        if (nexusFrom != null && !nexusFrom.getId().equals(nexusTo.getId())) {
            event.setCancelled(true);
        }
    }

    /**
     * Placing fluid is not like placing a block so it needs a separate event listener
     */
    @EventHandler
    public void onPlayerPlaceFluid(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Chunk chunk = event.getBlockClicked().getLocation().getChunk();

        Nexus nexusInChunk = NexusManager.getNexus(chunk);

        // Cancel if chunk is claimed and player is NOT owner
        if (nexusInChunk != null && !nexusInChunk.getOwner().equals(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot place fluids in another player's Nexus chunk!");
        }
    }

    /**
     * ??? I don't think this should be here
     */
    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) return;

        Chunk chunk = block.getLocation().getChunk();
        Nexus nexusInChunk = NexusManager.getNexus(chunk);

        // Only cancel if chunk is claimed by someone else
        if (nexusInChunk != null && !nexusInChunk.getOwner().equals(player.getUniqueId())) {
            switch (block.getType()) {
                case OAK_TRAPDOOR, OAK_DOOR, IRON_DOOR, DISPENSER, DROPPER, CHEST, TRAPPED_CHEST, HOPPER -> {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cannot interact with blocks in another player's Nexus chunk!");
                }
            }
        }
    }
}
