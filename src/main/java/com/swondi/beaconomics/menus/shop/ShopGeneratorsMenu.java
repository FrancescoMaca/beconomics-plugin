package com.swondi.beaconomics.menus.shop;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.PlayerManager;
import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.utils.UIHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static com.swondi.beaconomics.managers.BankManager.getFormattedMoney;

public class ShopGeneratorsMenu {

    private static final int[] borders = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
    private static final ItemStack separator = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

    static {
        ItemMeta meta = separator.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(" ");
            meta.setLore(List.of());
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);;
            separator.setItemMeta(meta);
        }
    }

    public static Inventory build(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 54, Constants.SHOP_GENERATORS_TITLE);
        int playerLevel = PlayerManager.getBeaconLevel(player);

        for (int border : borders) {
            inventory.setItem(border, separator);
        }

        if (playerLevel >= 2) {
            buildWoolRow(inventory);
        }
        else {
            buildLockedRow(inventory, 1);
        }

        if (playerLevel >= 3) {
            buildConcreteRow(inventory);
        }
        else {
            buildLockedRow(inventory, 2);
        }

        if (playerLevel >= 4) {
            buildTerracottaRow(inventory);
        }
        else {
            buildLockedRow(inventory, 3);
        }

        inventory.setItem(0, UIHelper.createBackArrow(Constants.UI_SHOP_MAIN_MENU_VALUE));

        return inventory;
    }

    private static void buildWoolRow(Inventory inventory) {
        inventory.setItem(10, createGenerator(
            "§f§lWhite Generator§r",
            Material.WHITE_WOOL,
            1,
            "§fWhite Candle",
            6000
        ));
        inventory.setItem(11, createGenerator(
            "§7§lLight Gray Generator§r",
            Material.LIGHT_GRAY_WOOL,
            1,
            "Light Gray Candle",
            12500
        ));
        inventory.setItem(12, createGenerator(
            "§8§lGray Generator§r",
            Material.GRAY_WOOL,
            1,
            "§8Gray Candle",
            25000
        ));
        inventory.setItem(13, createGenerator(
            "§7§lBlack Generator§r",
            Material.BLACK_WOOL,
            1,
            "§7Black Candle",
            37500
        ));
        inventory.setItem(14, createGenerator(
            "§c§lRed Generator§r",
            Material.RED_WOOL,
            1,
            "§cRed Candle",
            62500
        ));
    }

    private static void buildConcreteRow(Inventory inventory) {
        inventory.setItem(19, createGenerator(
            "§6§lOrange Generator§r",
            Material.ORANGE_CONCRETE,
            2, // production rate
            "§6Orange Candle",
            60000
        ));
        inventory.setItem(20, createGenerator(
            "§e§lYellow Generator§r",
            Material.YELLOW_CONCRETE,
            2,
            "§eYellow Candle",
            120000
        ));
        inventory.setItem(21, createGenerator(
            "§a§lLime Generator§r",
            Material.LIME_CONCRETE,
            3,
            "§aLime Candle",
            300_000
        ));
        inventory.setItem(22, createGenerator(
            "§2§lGreen Generator§r",
            Material.GREEN_CONCRETE,
            3,
            "§2Green Candle",
            1_200_000
        ));
        inventory.setItem(23, createGenerator(
            "§b§lCyan Generator§r",
            Material.CYAN_CONCRETE,
            4,
            "§bCyan Candle",
            1_800_000
        ));
        inventory.setItem(24, createGenerator(
            "§3§lLight Blue Generator§r",
            Material.LIGHT_BLUE_CONCRETE,
            4,
            "§3Light Blue Candle",
            1_800_000
        ));
    }

    private static void buildTerracottaRow(Inventory inventory) {
        inventory.setItem(28, createGenerator(
            "§9§lBlue Generator§r",
            Material.BLUE_GLAZED_TERRACOTTA,
            5, // production rate
            "§9Blue Candle",
            5_000_000
        ));
        inventory.setItem(29, createGenerator(
            "§5§lPurple Generator§r",
            Material.PURPLE_GLAZED_TERRACOTTA,
            5,
            "§5Purple Candle",
            8_750_000
        ));
        inventory.setItem(30, createGenerator(
            "§d§lMagenta Generator§r",
            Material.MAGENTA_GLAZED_TERRACOTTA,
            6,
            "§dMagenta Candle",
            15_000_000
        ));
        inventory.setItem(31, createGenerator(
            "§d§lPink Generator§r",
            Material.PINK_GLAZED_TERRACOTTA,
            6,
            "§dPink Candle",
            22_500_000
        ));
        inventory.setItem(32, createGenerator(
            "§6§lBrown Generator§r",
            Material.BROWN_GLAZED_TERRACOTTA,
            7,
            "§6Brown Candle",
            35_000_000
        ));
    }

    private static  void buildLockedRow(Inventory inventory, int index) {
        for(int i = (index * 9) + 1; i < (index * 9) + 8; i++) {
            ItemStack bar = new ItemStack(Material.IRON_BARS);
            ItemMeta meta = bar.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§c§lLocked");
                meta.setLore(List.of(
                    "Unlock this upgrading your",
                    "beacon to level §6" + (index + 1)
                ));
                bar.setItemMeta(meta);
            }

            inventory.setItem(i, bar);
        }
    }

    private static ItemStack createGenerator(String name, Material material, float rate, String dropType, int price) {
        ItemStack gen = new ItemStack(material);
        ItemMeta meta = gen.getItemMeta();

        // Adding action key
        if (meta != null) {
            NamespacedKey buyKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_ACTION_KEY);
            NamespacedKey priceKey = new NamespacedKey(Beaconomics.getInstance(), Constants.UI_PRICE_KEY);
            NamespacedKey genTagKey = new NamespacedKey(Beaconomics.getInstance(), Constants.PDC_GENERATOR_TAG);

            meta.getPersistentDataContainer().set(buyKey, PersistentDataType.STRING, Constants.UI_SHOP_BUY_VALUE);
            meta.getPersistentDataContainer().set(priceKey, PersistentDataType.INTEGER, price);
            meta.getPersistentDataContainer().set(genTagKey, PersistentDataType.BYTE, (byte)1);

            meta.setDisplayName(name);
            meta.setLore(List.of(
                "§7You can place generators only in the chunk",
                "§7where your §b§lNexus §7resides.",
                "§8-------------------------",
                "§e§lProduction§7: §f" + rate + " candle/s",
                "§e§lDrop Type§7: " + dropType,
                "§e§lPrice§7: §6$" + getFormattedMoney(price),
                "§8-------------------------",
                "§aClick to purchase!"
            ));
            gen.setItemMeta(meta);
        }

        return gen;
    }
}
