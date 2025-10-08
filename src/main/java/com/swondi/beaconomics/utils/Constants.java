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
    public static final String SHOP_UTILITY_BLOCKS_MENU_TITLE = ChatColor.BOLD + "Utilities 🛠";
    public static final String SHOP_DEFENCE_BLOCKS_MENU_TITLE = ChatColor.BOLD + "Defence \uD83E\uDDF1";
    public static final String SHOP_TOOLS_MENU_TITLE = ChatColor.BOLD + "Tools ⚒";
    public static final String SHOP_GENERATORS_TITLE = ChatColor.BOLD + "Generators ⚡";

    public static final String ENDER_CHEST_TITLE = ChatColor.BOLD + "Ender Chest \uD83D\uDC5C";

    public static final String BEACON_DATA_OWNER = "beacon_data_owner";
    public static final int BEACON_MAX_LEVEL = 5;

    // These PDC are used to identify if those items are special
    public static final String PDC_KIT_CHEST_TAG = "is_kit_chest";
    public static final String PDC_GENERATOR_TAG = "is_generator";
    public static final String PDC_TEMPORARY_BLOCK_TAG = "is_temporary";
    public static final String PDC_DEFENSE_BLOCK_TAG = "is_defense";

    // Players keys for persistent values
    public static final String PLAYER_PERSIST_BEACON_LEVEL_KEY = "data_player_beacon_level";
    public static final String PLAYER_GENERATORS_SLOTS_KEY = "data_player_generators_slots";
    public static final String PLAYER_STARTER_KIT_COOLDOWN_KEY = "data_player_starter_kit_cooldown";
    public static final String PLAYER_SPARK_KIT_COOLDOWN_KEY = "data_player_spark_kit_cooldown";
    public static final String PLAYER_BLAZE_KIT_COOLDOWN_KEY = "data_player_blaze_kit_cooldown";
    public static final String PLAYER_EMBER_KIT_COOLDOWN_KEY = "data_player_ember_kit_cooldown";
    public static final String PLAYER_FIRE_KIT_COOLDOWN_KEY = "data_player_fire_kit_cooldown";
    public static final String PLAYER_INFERNO_KIT_COOLDOWN_KEY = "data_player_inferno_kit_cooldown";
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
    public static final String UI_SHOP_UTILITY_BLOCKS_MENU_VALUE = "shop_utility_menu";
    public static final String UI_SHOP_GENS_MENU_VALUE = "shop_gens_menu";
    public static final String UI_SHOP_DEFENCE_MENU_VALUE = "shop_defence_menu";
    public static final String UI_SHOP_TOOLS_MENU_VALUE = "shop_tools_menu";
    public static final String UI_BEACON_LEVEL_KEY = "buy_beacon_level_";

    public static final Map<String, KitData> DATA_KITS = new HashMap<>() {{
        put("Starter", new KitData(600));
        put("Spark", new KitData(1440));
        put("Blaze", new KitData(1440));
        put("Ember", new KitData(1440));
        put("Fire", new KitData(1440));
        put("Inferno", new KitData(1440));
    }};

    public static final Map<Material, DefenseBlockData> DATA_DEFENSE_BLOCKS = new HashMap<>() {{
        put(Material.MUD_BRICKS, new DefenseBlockData(1, 50, 1000));
        put(Material.SANDSTONE, new DefenseBlockData(2, 150, 5000));
        put(Material.STONE_BRICKS, new DefenseBlockData(3, 250, 10000));
        put(Material.POLISHED_BLACKSTONE_BRICKS, new DefenseBlockData(4, 500, 20000));
        put(Material.OBSIDIAN, new DefenseBlockData(6, 1000, 30000));
        put(Material.OAK_DOOR, new DefenseBlockData(1, 50, 1000));
        put(Material.BIRCH_DOOR, new DefenseBlockData(2, 150, 5000));
        put(Material.SPRUCE_DOOR, new DefenseBlockData(3, 250, 10000));
        put(Material.CRIMSON_DOOR, new DefenseBlockData(4, 500, 20000));
        put(Material.PALE_OAK_DOOR, new DefenseBlockData(6, 1000, 30000));
    }};

    public static final Map<String, RankData> RANKS = new HashMap<>() {{
        put("Default", new RankData("Default", 0));
        put("Spark", new RankData("Spark", 1));
        put("Blaze", new RankData("Blaze", 2));
        put("Ember", new RankData("Ember", 3));
        put("Fire", new RankData("Fire", 4));
        put("Inferno", new RankData("Inferno", 5));
    }};

    public static final Map<Material, TemporaryBlockData> DATA_TEMPORARY = new HashMap<>() {{
        put(Material.COBBLESTONE, new TemporaryBlockData(500));
        put(Material.SANDSTONE, new TemporaryBlockData(600));
        put(Material.STONE, new TemporaryBlockData(700));
        put(Material.SMOOTH_STONE, new TemporaryBlockData(800));
        put(Material.PRISMARINE, new TemporaryBlockData(1000));
        put(Material.BRICKS, new TemporaryBlockData(1200));
        put(Material.NETHER_BRICKS, new TemporaryBlockData(1400));
        put(Material.QUARTZ_BLOCK, new TemporaryBlockData(1500));
        put(Material.RED_SANDSTONE, new TemporaryBlockData(1100));
        put(Material.PURPUR_BLOCK, new TemporaryBlockData(1300));
    }};

    public static final Map<Material, GeneratorData> DATA_GENERATORS = new HashMap<>() {{
        // Wool-based generators
        put(Material.WHITE_WOOL, new GeneratorData(6000, 20, "White Generator", "White Candle", Material.WHITE_CANDLE, ChatColor.WHITE));
        put(Material.LIGHT_GRAY_WOOL, new GeneratorData(12500, 20, "Light Gray Generator", "Light Gray Candle", Material.LIGHT_GRAY_CANDLE, ChatColor.GRAY));
        put(Material.GRAY_WOOL, new GeneratorData(25000, 20, "Gray Generator", "Gray Candle", Material.GRAY_CANDLE, ChatColor.DARK_GRAY));
        put(Material.BLACK_WOOL, new GeneratorData(37500, 20, "Black Generator", "Black Candle", Material.BLACK_CANDLE, ChatColor.BLACK));
        put(Material.RED_WOOL, new GeneratorData(62500, 20, "Red Generator", "Red Candle", Material.RED_CANDLE, ChatColor.RED));

        // Concrete-based generators
        put(Material.ORANGE_CONCRETE, new GeneratorData(60000, 40, "Orange Generator", "Orange Candle", Material.ORANGE_CANDLE, ChatColor.GOLD));
        put(Material.YELLOW_CONCRETE, new GeneratorData(120000, 40, "Yellow Generator", "Yellow Candle", Material.YELLOW_CANDLE, ChatColor.YELLOW));
        put(Material.LIME_CONCRETE, new GeneratorData(300000, 60, "Lime Generator", "Lime Candle", Material.LIME_CANDLE, ChatColor.GREEN));
        put(Material.GREEN_CONCRETE, new GeneratorData(1200000, 60, "Green Generator", "Green Candle", Material.GREEN_CANDLE, ChatColor.DARK_GREEN));
        put(Material.CYAN_CONCRETE, new GeneratorData(1800000, 80, "Cyan Generator", "Cyan Candle", Material.CYAN_CANDLE, ChatColor.AQUA));
        put(Material.LIGHT_BLUE_CONCRETE, new GeneratorData(1800000, 80, "Light Blue Generator", "Light Blue Candle", Material.LIGHT_BLUE_CANDLE, ChatColor.BLUE));

        // Glazed Terracotta-based generators
        put(Material.BLUE_GLAZED_TERRACOTTA, new GeneratorData(5000000, 100, "Blue Generator", "Blue Candle", Material.BLUE_CANDLE, ChatColor.DARK_BLUE));
        put(Material.PURPLE_GLAZED_TERRACOTTA, new GeneratorData(8750000, 100, "Purple Generator", "Purple Candle", Material.PURPLE_CANDLE, ChatColor.DARK_PURPLE));
        put(Material.MAGENTA_GLAZED_TERRACOTTA, new GeneratorData(15000000, 120, "Magenta Generator", "Magenta Candle", Material.MAGENTA_CANDLE, ChatColor.LIGHT_PURPLE));
        put(Material.PINK_GLAZED_TERRACOTTA, new GeneratorData(22500000, 120, "Pink Generator", "Pink Candle", Material.PINK_CANDLE, ChatColor.LIGHT_PURPLE));
        put(Material.BROWN_GLAZED_TERRACOTTA, new GeneratorData(35000000, 140, "Brown Generator", "Brown Candle", Material.BROWN_CANDLE, ChatColor.GOLD));
    }};

    public record KitData(int cooldown) {}
    public record GeneratorData(int price, int rate, String name, String dropName, Material dropType, ChatColor color) {}
    public record RankData(String name, int eChestLayers) {}
    public record DefenseBlockData(double fuelConsumption, int health, int price) { }
    public record TemporaryBlockData(int price) {}
}