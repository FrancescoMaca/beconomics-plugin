package com.swondi.beaconomics.cli;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class SpawnCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(
        @Nonnull CommandSender commandSender,
        @Nonnull Command command,
        @Nonnull String s,
        @Nonnull String[] strings
    ) {
        if (!(commandSender instanceof Player player)) return true;

        player.teleport(new Location(Bukkit.getWorld("world"), 0, 89, 0));

        return true;
    }

    @Override
    public List<String> onTabComplete(
        @Nonnull CommandSender commandSender,
        @Nonnull Command command,
        @Nonnull String s,
        @Nonnull String[] strings
    ) {
        return List.of();
    }
}
