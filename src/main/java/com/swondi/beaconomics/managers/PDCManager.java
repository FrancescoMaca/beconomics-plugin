package com.swondi.beaconomics.managers;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PDCManager {
    private static final NamespacedKey ownerKey = new NamespacedKey(Beaconomics.getInstance(), Constants.BEACON_DATA_OWNER);








    /** Set owner UUID on a beacon block */
    public static void setBeaconOwner(Block block, Player player) {
        if (block == null || block.getType() != org.bukkit.Material.BEACON) return;
        BlockState state = block.getState();
        if (state instanceof TileState tileState) {
            tileState.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, player.getUniqueId().toString());
            tileState.update();
        }
    }

    /** Get owner UUID from a beacon block */
    public static UUID getBeaconOwner(Block block) {
        if (block == null || block.getType() != org.bukkit.Material.BEACON) return null;
        BlockState state = block.getState();
        if (state instanceof TileState tileState) {
            String uuid = tileState.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
            if (uuid != null) return UUID.fromString(uuid);
        }
        return null;
    }

    /** Check if player can access beacon menu */
    public static boolean canAccessBeaconMenu(Block block, Player player) {
        UUID owner = getBeaconOwner(block);
        return owner != null && owner.equals(player.getUniqueId());
    }


    public static void setLockedChest(Block block, Player player) {
        if (!(block.getState() instanceof TileState state)) return;

        PersistentDataContainer pdc = state.getPersistentDataContainer();
        NamespacedKey lockKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_LOCKED_CHEST_KIT);

        pdc.set(lockKey, PersistentDataType.STRING, player.getUniqueId().toString());
        state.update();
    }

    public static boolean canOpenChest(Block block, Player player) {
        if (!(block.getState() instanceof TileState state)) return true;
        PersistentDataContainer pdc = state.getPersistentDataContainer();

        NamespacedKey lockKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_LOCKED_CHEST_KIT);

        if (!pdc.has(lockKey)) return true;

        String ownerId = pdc.get(lockKey, PersistentDataType.STRING);
        if (ownerId == null) return true;

        return ownerId.equals(player.getUniqueId().toString());
    }

    public static boolean isSpecialChest(Block block) {
        if (block.getType() != Material.CHEST) return false;
        if (!(block.getState() instanceof TileState state)) return false;

        NamespacedKey lockKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_LOCKED_CHEST_KIT);

        if (state.getPersistentDataContainer().has(lockKey, PersistentDataType.STRING)) return true;

        return false;
    }

    // Tagging methods
    public static void tagAsKitChest(Block block) {
        if (!(block.getState() instanceof TileState state)) return;

        NamespacedKey kitKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_KIT_CHEST_TAG);
        state.getPersistentDataContainer().set(kitKey,  PersistentDataType.BYTE, (byte)1);
        state.update();
    }
}
