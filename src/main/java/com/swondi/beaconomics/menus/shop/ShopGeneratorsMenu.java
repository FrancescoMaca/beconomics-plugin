package com.swondi.beaconomics.menus.shop;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.helpers.ItemStackCreator;
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
        inventory.setItem(10, ItemStackCreator.createGenerator(Material.WHITE_WOOL, false));
        inventory.setItem(11, ItemStackCreator.createGenerator(Material.LIGHT_GRAY_WOOL, false));
        inventory.setItem(12, ItemStackCreator.createGenerator(Material.GRAY_WOOL, false));
        inventory.setItem(13, ItemStackCreator.createGenerator(Material.BLACK_WOOL, false));
        inventory.setItem(14, ItemStackCreator.createGenerator(Material.RED_WOOL, false));

    }

    private static void buildConcreteRow(Inventory inventory) {
        inventory.setItem(19, ItemStackCreator.createGenerator(Material.ORANGE_CONCRETE, false));
        inventory.setItem(20, ItemStackCreator.createGenerator(Material.YELLOW_CONCRETE, false));
        inventory.setItem(21, ItemStackCreator.createGenerator(Material.LIME_CONCRETE, false));
        inventory.setItem(22, ItemStackCreator.createGenerator(Material.GREEN_CONCRETE, false));
        inventory.setItem(23, ItemStackCreator.createGenerator(Material.CYAN_CONCRETE, false));
        inventory.setItem(24, ItemStackCreator.createGenerator(Material.LIGHT_BLUE_CONCRETE, false));

    }

    private static void buildTerracottaRow(Inventory inventory) {
        inventory.setItem(28, ItemStackCreator.createGenerator(Material.BLUE_GLAZED_TERRACOTTA, false));
        inventory.setItem(29, ItemStackCreator.createGenerator(Material.PURPLE_GLAZED_TERRACOTTA, false));
        inventory.setItem(30, ItemStackCreator.createGenerator(Material.MAGENTA_GLAZED_TERRACOTTA, false));
        inventory.setItem(31, ItemStackCreator.createGenerator(Material.PINK_GLAZED_TERRACOTTA, false));
        inventory.setItem(32, ItemStackCreator.createGenerator(Material.BROWN_GLAZED_TERRACOTTA, false));
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
}
