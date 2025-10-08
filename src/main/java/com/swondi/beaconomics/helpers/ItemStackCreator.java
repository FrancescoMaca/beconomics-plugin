package com.swondi.beaconomics.helpers;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static com.swondi.beaconomics.managers.BankManager.getFormattedMoney;

public class ItemStackCreator {

    /**
     * Returns a Nexus with the custom name and lore
     */
    public static ItemStack createNexus() {
        ItemStack nexus = new ItemStack(Material.BEACON);
        ItemMeta meta = nexus.getItemMeta();

        if (meta == null) return nexus;

        meta.setDisplayName(ChatColor.AQUA + "Nexus");
        List<String> lore = new ArrayList<>();
        lore.add("§7The core of your base. Defend it at all costs!");

        meta.setLore(lore);
        nexus.setItemMeta(meta);

        return nexus;
    }

    /**
     * Creates a generator item stack with a given type.
     * @param type The generator block type.
     * @param isDrop This flag is true if the generator needs to be created as a drop, meaning an actual item players have
     *               in their inventory. Otherwise, if set to false the item will be made ready for the shop inventory.
     * @return the formatted item stack.
     */
    public static ItemStack createGenerator(Material type, boolean isDrop) {
        Constants.GeneratorData data = Constants.DATA_GENERATORS.get(type);

        if (data == null) {
            return null;
        }

        ItemStack gen = new ItemStack(type);
        ItemMeta meta = gen.getItemMeta();

        if (meta != null) {
            // If it's not a drop then we add the shop lore and pdc values
            if (!isDrop) {
                NamespacedKey buyKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_ACTION_KEY);
                NamespacedKey priceKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_PRICE_KEY);
                meta.getPersistentDataContainer().set(buyKey, PersistentDataType.STRING, Constants.UI_SHOP_BUY_VALUE);
                meta.getPersistentDataContainer().set(priceKey, PersistentDataType.INTEGER, data.price());
            }

            NamespacedKey genTagKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_GENERATOR_TAG);
            meta.getPersistentDataContainer().set(genTagKey, PersistentDataType.BYTE, (byte)1);

            meta.setDisplayName(ChatColor.BOLD + "" + data.color() + data.name());

            // Creating dynamic lore
            List<String> lore = new ArrayList<>();
            lore.add("§7You can place generators only in the chunk");
            lore.add("§7where your §b§lNexus §7resides.");
            lore.add("§8-------------------------");
            lore.add("§e§lProduction§7: §f" + String.format("%.2f", 20.0 / data.rate()) + " candle/s");
            lore.add("§e§lDrop Type§7: " + data.dropName());

            if (!isDrop) {
                lore.add("§e§lPrice§7: §6$" + getFormattedMoney(data.price()));
            }

            lore.add("§8-------------------------");

            if (!isDrop) {
                lore.add("§aClick to purchase!");
            }

            meta.setLore(lore);
            gen.setItemMeta(meta);
        }

        return gen;
    }

    public static ItemStack createTemporaryBlock(Material type, boolean isDrop) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // If it's not a drop then we add the shop lore and pdc values
            if (!isDrop) {
                NamespacedKey buyKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_ACTION_KEY);
                NamespacedKey priceKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_PRICE_KEY);
                meta.getPersistentDataContainer().set(buyKey, PersistentDataType.STRING, Constants.UI_SHOP_BUY_VALUE);
                meta.getPersistentDataContainer().set(priceKey, PersistentDataType.INTEGER, Constants.DATA_TEMPORARY.get(type).price());
            }

            NamespacedKey tempKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_TEMPORARY_BLOCK_TAG);
            meta.getPersistentDataContainer().set(tempKey, PersistentDataType.BYTE, (byte)1);
            meta.setDisplayName(ChatColor.RESET + "" + type.name().charAt(0) + type.name().replaceAll("_", " ").substring(1).toLowerCase());

            // Creating dynamic lore
            List<String> lore = new ArrayList<>();
            lore.add("§7Place these blocks anywhere, but remember that after a few");
            lore.add("§7seconds they disappear!");

            if (!isDrop) {
                lore.add("§8-------------------------");
                lore.add("§e§lPrice§7: §6$" + getFormattedMoney(Constants.DATA_TEMPORARY.get(type).price()));
                lore.add("§8-------------------------");
                lore.add("§aClick to purchase!");
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }
}
