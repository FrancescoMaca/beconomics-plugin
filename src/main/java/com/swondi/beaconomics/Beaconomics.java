package com.swondi.beaconomics;

import com.swondi.beaconomics.cli.*;
import com.swondi.beaconomics.cli.system.SystemClickCommands;
import com.swondi.beaconomics.data.YamlVerifier;
import com.swondi.beaconomics.debug.listeners.DebugBeaconLevelListener;
import com.swondi.beaconomics.events.ConnectionEvents;
import com.swondi.beaconomics.listeners.*;
import com.swondi.beaconomics.managers.*;
import com.swondi.beaconomics.tasks.BackupTask;
import com.swondi.beaconomics.tasks.DropCleanupTask;
import com.swondi.beaconomics.tasks.GeneratorTask;
import com.swondi.beaconomics.utils.Constants;
import net.luckperms.api.LuckPerms;
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

        // Load data from file
        TemporaryBlocksManager.load();
        RankManager.load();

        // Starts background tasks
        new GeneratorTask().runTaskTimer(this, 1, 1);
        new DropCleanupTask().runTaskTimer(this, 1, 20);
        new BackupTask().runTaskTimer(this, 1, 200);
        TemporaryBlocksManager.cleanupTemporaryBlocks().runTaskTimer(this, 1, 20);

        // Binds listeners
        getServer().getPluginManager().registerEvents(new EnderChestListener(), this);
        getServer().getPluginManager().registerEvents(new ConnectionEvents(), this);
        getServer().getPluginManager().registerEvents(new DefenseBlockListener(), this);
        getServer().getPluginManager().registerEvents(new TemporaryBlocksListener(), this);
        getServer().getPluginManager().registerEvents(new NexusFuelListener(), this);
        getServer().getPluginManager().registerEvents(new ChestListener(), this);
        getServer().getPluginManager().registerEvents(new GeneratorListener(), this);
        getServer().getPluginManager().registerEvents(new NexusListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryActionListener(), this);
        getServer().getPluginManager().registerEvents(new UIListener(), this);
        getServer().getPluginManager().registerEvents(new DebugBeaconLevelListener(), this);
        getServer().getPluginManager().registerEvents(new CandleSellListener(), this);
        getServer().getPluginManager().registerEvents(new CombatListener(), this);
        getServer().getPluginManager().registerEvents(new CombatLogoutListener(), this);

        // Binds luckperms API listener

        // Setup commands
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand());
        Objects.requireNonNull(getCommand("bugreport")).setExecutor(new BugReportCommand());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHomeCommand());
        Objects.requireNonNull(getCommand("enderchest")).setExecutor(new EnderChestCommand());
        Objects.requireNonNull(getCommand("bank")).setExecutor(new BankCommand());
        Objects.requireNonNull(getCommand("nexus")).setExecutor(new NexusCommand());
        Objects.requireNonNull(getCommand("team")).setExecutor(new TeamCommand());
        Objects.requireNonNull(getCommand("kits")).setExecutor(new KitsCommand());
        Objects.requireNonNull(getCommand("kit")).setExecutor(new KitCommand());
        Objects.requireNonNull(getCommand("shop")).setExecutor(new ShopCommand());
        Objects.requireNonNull(getCommand("system")).setExecutor(new SystemClickCommands());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand());
        Objects.requireNonNull(getCommand("balancetop")).setExecutor(new BalanceTopCommand());
    }

    @Override
    public void onDisable() {
        KitManager.removeAllFallingArmorStands();
        NexusManager.backup();
        TemporaryBlocksManager.backup();
        DefenseBlocksManager.backup();
    }

    public static Beaconomics getInstance() {
        return instance;
    }
}
