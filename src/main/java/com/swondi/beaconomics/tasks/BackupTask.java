package com.swondi.beaconomics.tasks;

import com.swondi.beaconomics.managers.NexusManager;
import org.bukkit.scheduler.BukkitRunnable;

public class BackupTask extends BukkitRunnable {

    @Override
    public void run() {
        NexusManager.backup();
    }
}
