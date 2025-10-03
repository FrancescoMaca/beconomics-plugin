package com.swondi.beaconomics;

import com.swondi.beaconomics.cli.HomeCommand;
import com.swondi.beaconomics.cli.KitCommand;
import com.swondi.beaconomics.cli.ShopCommand;
import com.swondi.beaconomics.cli.TeamCommand;
import com.swondi.beaconomics.cli.system.SystemClickCommands;
import com.swondi.beaconomics.data.YamlVerifier;
import com.swondi.beaconomics.debug.listeners.DebugBeaconLevelListener;
import com.swondi.beaconomics.events.ConnectionEvents;
import com.swondi.beaconomics.listeners.*;
import com.swondi.beaconomics.managers.TemporaryBlocksManager;
import com.swondi.beaconomics.tasks.DropCleanupTask;
import com.swondi.beaconomics.tasks.GeneratorTask;
import com.swondi.beaconomics.tasks.TickTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Beaconomics extends JavaPlugin {
    static Beaconomics instance;

    @Override
    public void onEnable() {
        instance = this;

        if (YamlVerifier.ensureYamlIntegrity()) {
            return;
        }

        new TickTask().runTaskTimer(this, 1, 1);
        new GeneratorTask().runTaskTimer(this, 1, 1);
        new DropCleanupTask().runTaskTimer(this, 1, 20);
        TemporaryBlocksManager.cleanupTemporaryBlocks().runTaskTimer(this, 1, 20);

        getServer().getPluginManager().registerEvents(new ConnectionEvents(), this);
        getServer().getPluginManager().registerEvents(new TemporaryBlocksListener(), this);

        getServer().getPluginManager().registerEvents(new NexusFuelListener(), this);
        getServer().getPluginManager().registerEvents(new ChestListener(), this);
        getServer().getPluginManager().registerEvents(new GeneratorListener(), this);
        getServer().getPluginManager().registerEvents(new NexusListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryActionListener(), this);
        getServer().getPluginManager().registerEvents(new UIListener(), this);
        getServer().getPluginManager().registerEvents(new DebugBeaconLevelListener(), this);
        getServer().getPluginManager().registerEvents(new CandleSellListener(), this);

        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
        Objects.requireNonNull(getCommand("team")).setExecutor(new TeamCommand());
        Objects.requireNonNull(getCommand("kit")).setExecutor(new KitCommand());
        Objects.requireNonNull(getCommand("shop")).setExecutor(new ShopCommand());
        Objects.requireNonNull(getCommand("system")).setExecutor(new SystemClickCommands());
    }

    @Override
    public void onDisable() { }

    public static Beaconomics getInstance() {
        return instance;
    }
}
