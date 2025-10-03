package com.swondi.beaconomics.tasks;

import org.bukkit.scheduler.BukkitRunnable;

public class TickTask extends BukkitRunnable {

    public static long tick = 0;

    @Override
    public void run() {
        tick++;
    }
}
