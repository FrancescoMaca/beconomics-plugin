package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.helpers.ItemStackCreator;
import com.swondi.beaconomics.managers.GeneratorManager;
import com.swondi.beaconomics.managers.NexusManager;
import com.swondi.beaconomics.managers.YamlManager;
import com.swondi.beaconomics.models.Generator;
import com.swondi.beaconomics.models.Nexus;
import com.swondi.beaconomics.tasks.GeneratorTask;
import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.helpers.EntityHelper;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class GeneratorListener implements Listener {

    private final YamlManager genYaml = new YamlManager("generator.yml");
    private final static Map<Material, Material> defaultDrops = new HashMap<>() {{
        put(Material.WHITE_WOOL, Material.WHITE_CANDLE);
        put(Material.LIGHT_GRAY_WOOL, Material.LIGHT_GRAY_CANDLE);
        put(Material.GRAY_WOOL, Material.GRAY_CANDLE);
        put(Material.BLACK_WOOL, Material.BLACK_CANDLE);
        put(Material.RED_WOOL, Material.RED_CANDLE);

        put(Material.ORANGE_CONCRETE, Material.ORANGE_CANDLE);
        put(Material.YELLOW_CONCRETE, Material.YELLOW_CANDLE);
        put(Material.LIME_CONCRETE, Material.LIME_CANDLE);
        put(Material.GREEN_CONCRETE, Material.GREEN_CANDLE);
        put(Material.CYAN_CONCRETE, Material.CYAN_CANDLE);
        put(Material.LIGHT_BLUE_CONCRETE, Material.LIGHT_BLUE_CANDLE);

        put(Material.BLUE_GLAZED_TERRACOTTA, Material.BLUE_CANDLE);
        put(Material.PURPLE_GLAZED_TERRACOTTA, Material.PURPLE_CANDLE);
        put(Material.MAGENTA_GLAZED_TERRACOTTA, Material.MAGENTA_CANDLE);
        put(Material.PINK_GLAZED_TERRACOTTA, Material.PINK_CANDLE);
        put(Material.BROWN_GLAZED_TERRACOTTA, Material.BROWN_CANDLE);
    }};

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();

        if (!defaultDrops.containsKey(block.getType())) return;

        // This finds the block the user just placed
        ItemStack itemInHand = event.getItemInHand();

        if (!EntityHelper.isGenerator(itemInHand)) {
            return;
        }

        Nexus existingNexus = NexusManager.getNexus(player);

        if (existingNexus == null) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can only place generators when you have a Nexus placed!");
            return;
        }

        if (!block.getChunk().equals(existingNexus.getLocation().getChunk())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You can only place generators in the same chunk as your Nexus!");
            return;
        }

        GeneratorManager.addGenerator(block.getLocation());

        Generator generator = new Generator(
            block.getType(),
            Constants.DATA_GENERATORS.get(itemInHand.getType()).rate(),
            defaultDrops.get(block.getType()),
            block.getLocation()
        );

        generator.saveToYaml(genYaml);
        GeneratorTask.addGenerator(generator);

        player.sendMessage(ChatColor.GREEN + "Generator has been placed!");
    }

    /**
     * This method is solely used to handle creative mode interactions. Since on creative mode you dont
     * trigger onBlockDamage.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        // Checks only possible generators
        if (!defaultDrops.containsKey(block.getType())) return;

        if (GeneratorManager.isAGeneratorLocation(blockLocation)) {
            Material genType = block.getType();

            // Drop the block
            blockLocation.getWorld().dropItemNaturally(blockLocation, ItemStackCreator.createGenerator(genType, true));

            Generator generator = new Generator(
                block.getType(),
                Constants.DATA_GENERATORS.get(genType).rate(),
                Material.CANDLE,
                block.getLocation()
            );

            GeneratorManager.removeGenerator(block.getLocation());
            Generator.removeFromYaml(genYaml, generator.getId());
            GeneratorTask.removeGenerator(generator);
        }
    }

    /**
     * Drop the generator after the first hit
     */
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Block block = event.getBlock();
        Location blockLocation = block.getLocation();

        // Checks only possible generators
        if (!defaultDrops.containsKey(block.getType())) return;

        if (GeneratorManager.isAGeneratorLocation(blockLocation)) {
            event.setCancelled(true);

            Material genType = block.getType();

            // Destroy the block
            block.setType(Material.AIR);

            // Drop the block
            blockLocation.getWorld().dropItemNaturally(blockLocation, ItemStackCreator.createGenerator(genType, true));

            Generator generator = new Generator(
                block.getType(),
                Constants.DATA_GENERATORS.get(genType).rate(),
                Material.CANDLE,
                block.getLocation()
            );

            GeneratorManager.removeGenerator(block.getLocation());
            Generator.removeFromYaml(genYaml, generator.getId());
            GeneratorTask.removeGenerator(generator);
        }
    }
}
