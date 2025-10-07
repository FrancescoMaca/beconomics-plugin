package com.swondi.beaconomics.cli.system;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.cli.data.TokenCommandManager;
import com.swondi.beaconomics.managers.PlayerDataManager;
import com.swondi.beaconomics.managers.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class SystemClickCommands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(
        @Nonnull CommandSender sender,
        @Nonnull Command command,
        @Nonnull String label,
        @Nonnull String[] args
    ) {
        if (!(sender instanceof Player player)) return true;

        if (!command.getName().equalsIgnoreCase("system")) {
            return true;
        }

        if (args.length < 2 || args[1] == null) {
            Beaconomics.getInstance().getLogger().warning("Player " + sender.getName() + " is trying to access hidden commands.");
            return true;
        }

        String token = args[0];
        String hiddenCommand = args[1];

        boolean isTokenValid = TokenCommandManager.isConfirmationTokenValid(player.getUniqueId(), token);

        // This prevents the command to be run without a valid token
        if (!isTokenValid) {
            return true;
        }

        switch (hiddenCommand) {
            case "team-cancel":
                TokenCommandManager.voidTokenConfirmation(player.getUniqueId());
                sender.sendMessage(ChatColor.RED + "Action cancelled.");
                break;
            case "team-kick":
                UUID targetUUID = PlayerDataManager.getUUID(args[2]);

                if (targetUUID != null) {
                    TeamManager.removeFromTeam(player, targetUUID);
                }
                else {
                    player.sendMessage(ChatColor.RED + args[2] + " is not in your team");
                }

                break;
            case "team-disband":
                TeamManager.disbandTeam(player);
                player.sendMessage(ChatColor.WHITE + "Team '" + ChatColor.AQUA + args[2] + ChatColor.WHITE + "' disband!");
                break;
            case "team-create":
                TeamManager.createTeam(player, args[2]);
                player.sendMessage(ChatColor.WHITE + "Team '" + ChatColor.AQUA + args[2] + ChatColor.WHITE + "' created!");
                break;
        }

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
