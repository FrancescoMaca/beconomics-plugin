package com.swondi.beaconomics.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    // UI inventories titles
    public static final String BEACON_MAIN_MENU_TITLE = "                Nexus";
    public static final String BEACON_UPGRADE_MENU_TITLE = "Nexus Upgrades";
    public static final String BEACON_TEAM_MENU_TITLE = "Your Team";
    public static final String BEACON_FUEL_MENU_TITLE = "Nexus Fuel";
    public static final String BEACON_PICKUP_MENU_TITLE = "Pick it up?";

    public static final String SHOP_MENU_TITLE = ChatColor.BOLD + "Shop";
    public static final String SHOP_TEMP_BLOCKS_MENU_TITLE = ChatColor.BOLD + "Blocks ⬛";
    public static final String SHOP_DEFENCE_BLOCKS_MENU_TITLE = ChatColor.BOLD + "Defence \uD83E\uDDF1";
    public static final String SHOP_TOOLS_MENU_TITLE = ChatColor.BOLD + "Tools ⚒";
    public static final String SHOP_GENERATORS_TITLE = ChatColor.BOLD + "Generators ⚡";

    public static final String BEACON_DATA_OWNER = "beacon_data_owner";
    public static final int BEACON_MAX_LEVEL = 5;

    // These PDC are used to identify if those items are special
    public static final String PDC_KIT_CHEST_TAG = "is_kit_chest";
    public static final String PDC_GENERATOR_TAG = "is_generator";
    public static final String PDC_TEMPORARY_BLOCK_TAG = "is_temporary";
    public static final String PDC_DEFENSE_BLOCK_TAG = "is_defense";

    // Players keys for persistent values
    public static final String PLAYER_PERSIST_BEACON_LEVEL_KEY = "data_player_beacon_level";
    public static final String PDC_LOCKED_CHEST_KIT = "kit_locked_by";

    // NBT tag keys for callback handling
    public static final String UI_NAVIGATE_KEY = "navigate_key";
    public static final String UI_ACTION_KEY = "action_key";
    public static final String UI_PRICE_KEY = "price_key";
    public static final String UI_NEXUS_PICKUP_VALUE = "nexus_pick_up";
    public static final String UI_NEXUS_MAIN_MENU_VALUE = "nexus_main_menu";
    public static final String UI_SHOP_BUY_VALUE = "shop_buy";
    public static final String UI_SHOP_MAIN_MENU_VALUE = "shop_main_menu";
    public static final String UI_SHOP_TEMP_BLOCKS_MENU_VALUE = "shop_temps_menu";
    public static final String UI_SHOP_GENS_MENU_VALUE = "shop_gens_menu";
    public static final String UI_SHOP_DEFENCE_MENU_VALUE = "shop_defence_menu";
    public static final String UI_SHOP_TOOLS_MENU_VALUE = "shop_tools_menu";

    public static final String UI_BEACON_LEVEL_KEY = "buy_beacon_level_";

    public static final Map<Material, DefenseBlockConstant> DATA_DEFENSE_BLOCKS = new HashMap<>() {{
        put(Material.MUD_BRICKS, new DefenseBlockConstant(1, 50));
        put(Material.SANDSTONE, new DefenseBlockConstant(2, 150));
        put(Material.STONE_BRICKS, new DefenseBlockConstant(3, 250));
        put(Material.POLISHED_BLACKSTONE_BRICKS, new DefenseBlockConstant(4, 500));
        put(Material.OBSIDIAN, new DefenseBlockConstant(6, 1000));
        put(Material.OAK_DOOR, new DefenseBlockConstant(1, 50));
        put(Material.BIRCH_DOOR, new DefenseBlockConstant(2, 150));
        put(Material.SPRUCE_DOOR, new DefenseBlockConstant(3, 250));
        put(Material.CRIMSON_DOOR, new DefenseBlockConstant(4, 500));
        put(Material.PALE_OAK_DOOR, new DefenseBlockConstant(6, 1000));
    }};

    public record DefenseBlockConstant(double fuelConsumption, int health) { }
}