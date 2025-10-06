package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.models.DefenseBlock;
import com.swondi.beaconomics.models.Nexus;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Handles all Nexus interactions.
 */
public class NexusManager {
    private final static YamlManager yaml = new YamlManager("nexuses.yml");
    private final static Map<String, Nexus> nexuses = new HashMap<>();

    // Autoload from file on startup
    static {
        ConfigurationSection nexusesSection = yaml.getConfiguration().getConfigurationSection("nexuses");

        if (nexusesSection != null) {
            for (String key : nexusesSection.getKeys(false)) {
                ConfigurationSection map = nexusesSection.getConfigurationSection(key);
                if (map == null) continue;

                int x = map.getInt("x");
                int y = map.getInt("y");
                int z = map.getInt("z");
                String nexusId = x + "_" + y + "_" + z;

//                Map<Location, DefenseBlock> defenseForNexus = DefenseBlocksManager.getAllDefensesFor(nexusId)
//                    .stream()
//                    .collect(Collectors.toMap(
//                        DefenseBlock::getLocation,
//                        defenseBlock -> defenseBlock)
//                    );

                Nexus nexus = Nexus.fromYaml(yaml, nexusId, Map.of());

                if (nexus == null) continue;

                nexuses.put(nexus.getId(), nexus);
                Bukkit.getLogger().info("[NexusManager] Added Nexus at (" + x + ", " + y + ", " + z + ") for " + nexus.getOwner());
            }
        } else {
            Bukkit.getLogger().warning("[NexusManager] Nexus file section not found. No nexuses to initialize");
        }
    }

    /**
     * Registers a Nexus on the behalf of a player
     * @param block the beacon that was placed
     * @param player the player who placed the Nexus
     */
    public static void registerNexus(Block block, Player player) {
        if (block.getType() != Material.BEACON) {
            Bukkit.getLogger().log(Level.WARNING, "[NexusManager] Trying to register a non-beacon block! (" + block.getType() + ")");
            return;
        }

        Nexus nexus = new Nexus(player.getUniqueId(), block.getLocation(), 0, Map.of());

        // Saves the nexus in the map
        nexuses.put(nexus.getId(), nexus);

        // Saves the nexus on the file
        nexus.saveToYaml(yaml);
    }

    /**
     * Removes a Nexus from the map. Does not need a player since anyone can remove a Nexus
     * @param block the nexus to unregister
     */
    public static void unregisterNexus(Block block) {
        if (block.getType() != Material.BEACON) {
            Bukkit.getLogger().log(Level.WARNING, "[NexusManager] Trying to unregister a non-beacon block! (" + block.getType() + ")");
            return;
        }

        Location nexusLocation = block.getLocation();
        String nexusId = nexusLocation.getBlockX() + "_" + nexusLocation.getBlockY() + "_" + nexusLocation.getBlockZ();
        Nexus nexus = nexuses.get(nexusId);

        if (nexus == null) {
            Bukkit.getLogger().warning("[NexusManager] Nexus " + nexusId + "not found!");
            return;
        }

        // Remove nexus from map
        nexuses.remove(nexusId);

        // Remove nexus from file
        nexus.removeFromYaml(yaml);
    }

    /** Checks if a beacon can be placed in this chunk */
    public static boolean canPlaceAt(Location location) {
        Chunk targetChunk = location.getChunk();

        for (Nexus nexus : nexuses.values()) {
            Chunk occupiedChunk = nexus.getLocation().getChunk();

            if (occupiedChunk.getX() == targetChunk.getX() && occupiedChunk.getZ() == targetChunk.getZ()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a player has a nexus
     * @param player the target player
     * @return true if the player has a nexus, false otherwise
     */
    public static boolean playerHasNexus(Player player) {
        return nexuses.values().stream().anyMatch(nexus -> nexus.getOwner().equals(player.getUniqueId()));
    }

    /**
     * Returns the player nexus
     * @param player the player retrieving the nexus
     * @return the nexus object if it exists, otherwise null
     */
    public static Nexus getNexus(Player player) {
        for (Nexus nexus : nexuses.values()) {
            if (nexus.getOwner().equals(player.getUniqueId())) {
                return nexus;
            }
        }

        return null;
    }

    /**
     * Gets a Nexus in a specific chunk
     * @param chunk the chunk you want the nexus at
     * @return the nexus in the chunk or null if it's free
     */
    public static Nexus getNexus(Chunk chunk) {
        for (Nexus nexus : nexuses.values()) {
            Chunk occupiedChunk = nexus.getLocation().getChunk();

            if (occupiedChunk.getX() == chunk.getX() && occupiedChunk.getZ() == chunk.getZ()
                    && occupiedChunk.getWorld().equals(chunk.getWorld())) {
                return nexus;
            }
        }

        return null;
    }

    public static void backup() {
        // TODO: add "hasChanged" flag to see if its necessary to save the nexus to file

        int count = 0;
        for (Nexus nexus : nexuses.values()) {
            try {
                nexus.saveToYaml(yaml);
                count++;
            }
            catch (Exception e) {
                Bukkit.getLogger().severe("Nexus with ID \"" + nexus.getId() + "\" could not be saved to disk: " + e.getMessage());
            }
        }

        Bukkit.getLogger().info("[NexusManager] Backup complete. Saved " + count + " nexuses to disk");
    }
}
