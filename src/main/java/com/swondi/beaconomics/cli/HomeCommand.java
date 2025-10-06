package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.managers.NexusManager;
import com.swondi.beaconomics.menus.shop.ShopMainMenu;
import com.swondi.beaconomics.models.Nexus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class HomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(
        @Nonnull CommandSender commandSender,
        @Nonnull Command command,
        @Nonnull String s,
        @Nonnull String[] strings
    ) {
        if (!(commandSender instanceof Player player)) return true;

        Nexus nexus = NexusManager.getNexus(player);

        if (nexus == null) {
            player.sendMessage("You don't have a Nexus! Place one to be able to teleport to it.");
        }
        else {
            player.teleport(nexus.getLocation().clone().add(0.5, 1, 0.5));
            player.sendMessage("You teleported to your Nexus!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return List.of();
    }
}
