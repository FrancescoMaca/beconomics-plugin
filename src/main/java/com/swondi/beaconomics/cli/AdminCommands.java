package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.helpers.CommandHelper;
import com.swondi.beaconomics.helpers.ItemStackCreator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class AdminCommands implements CommandExecutor, TabCompleter {
    private static final List<String> SUB_CMDS = List.of("grenade");

    @Override
    public boolean onCommand(
        @Nonnull CommandSender sender,
        @Nonnull Command command,
        @Nonnull String label,
        @Nonnull String[] args
    ) {
        if (!(sender instanceof Player player)) return true;

        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You must be an admin to run this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /admin [grenade]");
        }

        if (args[0].equals("grenade")) {
            player.getInventory().addItem(ItemStackCreator.createGrenade());
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /admin [grenade]");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(
        @Nonnull CommandSender sender,
        @Nonnull Command command,
        @Nonnull String label,
        @Nonnull String[] args
    ) {
        if (args.length == 1) {
            return CommandHelper.getSuggestions(args[0], SUB_CMDS);
        }

        return List.of();
    }
}
