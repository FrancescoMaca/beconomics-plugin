package com.swondi.beaconomics.helpers;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

public class EntityHelper {

    private static final NamespacedKey generatorKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_GENERATOR_TAG);
    private static final NamespacedKey defenseBlockKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_DEFENSE_BLOCK_TAG);
    private static final  NamespacedKey temporaryBlockKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_TEMPORARY_BLOCK_TAG);

    public static boolean isGenerator(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) {
            return false;
        }

        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        return pdc.has(generatorKey);
    }
}
