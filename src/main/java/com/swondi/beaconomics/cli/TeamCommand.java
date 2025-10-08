package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.cli.handlers.*;
import com.swondi.beaconomics.helpers.CommandHelper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamCommand implements CommandExecutor, TabCompleter {
    private static final List<String> SUB_CMDS = Arrays.asList("add", "kick", "create", "disband", "info");

    @Override
    public boolean onCommand(
        @Nonnull CommandSender sender,
        @Nonnull Command command,
        @Nonnull String label,
        @Nonnull String[] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(createHelpString());
            return true;
        }

        switch(args[0]) {
            case "add": InviteTeamMemberHandler.addTeamMember(player, args); break;
            case "kick": KickTeamMemberHandler.kickTeamMember(player, args); break;
            case "create": CreateTeamHandler.createTeam(player, args); break;
            case "disband": DisbandTeamHandler.disbandTeam(player, args); break;
            case "info": InfoTeamHandler.infoTeam(player); break;
            default: sender.sendMessage(createHelpString());
        }

        return true;
    }

    public String createHelpString() {
        return ChatColor.RED + "Usage: /team ["
            + ChatColor.GOLD + "add "
            + ChatColor.RED + "| "
            + ChatColor.GOLD + "kick "
            + ChatColor.RED + "| "
            + ChatColor.GOLD + "create "
            + ChatColor.RED + "| "
            + ChatColor.GOLD + "disband"
            + ChatColor.RED + "| "
            + ChatColor.GOLD + "info"
            + ChatColor.RED + "]";
    }

    @Override
    public List<String> onTabComplete(
        @Nonnull CommandSender sender,
        @Nonnull Command command,
        @Nonnull String alias,
        @Nonnull String[] args
    ) {
        if (!(sender instanceof Player)) {
            return null;
        }

        // First argument (subcommand)
        if (args.length == 1) {
            return CommandHelper.getSuggestions(args[0], SUB_CMDS);
        }

        // Example: /team add <player> → suggest online players
        if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            List<String> playerNames = new ArrayList<>();
            for (Player p : sender.getServer().getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    playerNames.add(p.getName());
                }
            }
            return playerNames;
        }

        // Example: /team kick <player> → suggest teammates (optional, can wire into TeamManager later)
        if (args.length == 2 && args[0].equalsIgnoreCase("kick")) {
            // TODO: pull actual teammates from TeamManager
            return List.of("teammate1", "teammate2", "teammate3");
        }

        return new ArrayList<>();
    }
}
