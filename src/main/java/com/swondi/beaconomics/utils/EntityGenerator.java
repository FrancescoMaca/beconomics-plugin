package com.swondi.beaconomics.utils;

import com.swondi.beaconomics.Beaconomics;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;

public class EntityGenerator {

    public static void spawnFireworkExplosion(Location loc, int level) {
        World world = loc.getWorld();
        if (world == null) return;

        // Spawn firework entity
        Firework firework = world.spawn(loc.add(0.5, 1, 0.5), Firework.class);

        // Customize firework meta
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder()
            .withColor(switch (level) {
                    case 2 -> List.of(Color.GREEN);
                    case 3 -> List.of(Color.BLUE);
                    case 4 -> List.of(Color.RED);
                    case 5 -> List.of(
                        Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
                        Color.AQUA, Color.BLUE, Color.PURPLE
                    );
                    default -> List.of(Color.WHITE);
                }
            )
            .withFade(Color.BLACK)
            .with(FireworkEffect.Type.BALL)
            .flicker(true)
            .trail(true)
            .build());
        meta.setPower(0);
        firework.setFireworkMeta(meta);

        Bukkit.getScheduler().runTaskLater(Beaconomics.getInstance(), firework::detonate, 1L);
    }

}
