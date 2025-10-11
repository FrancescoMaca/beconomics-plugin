package com.swondi.beaconomics.data;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class CandleData {
    private static final Map<Material, Integer> VALUES = new HashMap<>();

    static {
        // Wool (weakest gens)
        VALUES.put(Material.WHITE_CANDLE, 10);
        VALUES.put(Material.LIGHT_GRAY_CANDLE, 15);
        VALUES.put(Material.GRAY_CANDLE, 30);
        VALUES.put(Material.BLACK_CANDLE, 50);
        VALUES.put(Material.RED_CANDLE, 100);

        // Concrete (mid gens)
        VALUES.put(Material.ORANGE_CANDLE, 200);
        VALUES.put(Material.YELLOW_CANDLE, 400);
        VALUES.put(Material.LIME_CANDLE, 700);
        VALUES.put(Material.GREEN_CANDLE, 1_300);
        VALUES.put(Material.CYAN_CANDLE, 2_700);
        VALUES.put(Material.LIGHT_BLUE_CANDLE, 3_500);

        // Glazed Terracotta (strongest gens)
        VALUES.put(Material.BLUE_CANDLE, 5_000);
        VALUES.put(Material.PURPLE_CANDLE, 8_000);
        VALUES.put(Material.MAGENTA_CANDLE, 15_000);
        VALUES.put(Material.PINK_CANDLE, 25_000);
        VALUES.put(Material.BROWN_CANDLE, 40_000);
    }

    public static int getValue(Material material) {
        return VALUES.getOrDefault(material, 0);
    }
}
