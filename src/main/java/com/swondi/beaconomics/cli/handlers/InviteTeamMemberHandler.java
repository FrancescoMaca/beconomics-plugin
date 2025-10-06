package com.swondi.beaconomics.cli.handlers;

import com.swondi.beaconomics.managers.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteTeamMemberHandler {
    private InviteTeamMemberHandler() {}

    public static void addTeamMember(CommandSender sender, String[] args) {

        if (args.length < 2 || args.length > 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /team add [Player Name]");
            return;
        }

        if (!(sender instanceof Player player)) {
            return;
        }

        TeamManager.addToTeam(player, args[1]);
    }
}
