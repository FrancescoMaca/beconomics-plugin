package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.managers.CombatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
        @Nonnull CommandSender sender,
        @Nonnull Command command,
        @Nonnull String label,
        @Nonnull String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }

        if (CombatManager.isInCombat(player)) {
            player.sendMessage(ChatColor.RED +"You cannot use this command while in combat!");
            return true;
        }

        Location spawn = new Location(Bukkit.getWorld("world"), 0.5, 89, 0.5, 180, -20);

        if (player.getLocation().equals(spawn)){
            return false;
        }

        player.teleport(spawn);

        return true;
    }
}