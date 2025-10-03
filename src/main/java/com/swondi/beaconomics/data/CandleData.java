package com.swondi.beaconomics.data;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class CandleData {
    private static final Map<Material, Integer> VALUES = new HashMap<>();

    static {
        // Wool (weakest gens)
        VALUES.put(Material.WHITE_CANDLE, 5);
        VALUES.put(Material.LIGHT_GRAY_CANDLE, 15);
        VALUES.put(Material.GRAY_CANDLE, 30);
        VALUES.put(Material.BLACK_CANDLE, 60);
        VALUES.put(Material.RED_CANDLE, 150);

        // Concrete (mid gens)
        VALUES.put(Material.ORANGE_CANDLE, 400);
        VALUES.put(Material.YELLOW_CANDLE, 800);
        VALUES.put(Material.LIME_CANDLE, 1_600);
        VALUES.put(Material.GREEN_CANDLE, 3_200);
        VALUES.put(Material.CYAN_CANDLE, 6_400);
        VALUES.put(Material.LIGHT_BLUE_CANDLE, 9_000);

        // Glazed Terracotta (strongest gens)
        VALUES.put(Material.BLUE_CANDLE, 15_000);
        VALUES.put(Material.PURPLE_CANDLE, 25_000);
        VALUES.put(Material.MAGENTA_CANDLE, 40_000);
        VALUES.put(Material.PINK_CANDLE, 60_000);
        VALUES.put(Material.BROWN_CANDLE, 80_000);
    }

    public static int getValue(Material material) {
        return VALUES.getOrDefault(material, 0);
    }
}
