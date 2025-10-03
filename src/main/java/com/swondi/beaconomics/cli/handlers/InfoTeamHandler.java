package com.swondi.beaconomics.cli.handlers;

import com.swondi.beaconomics.managers.TeamManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoTeamHandler {

    private InfoTeamHandler() {}

    public static void infoTeam(CommandSender sender) {
        if (!(sender instanceof Player player)) return;

        String teamName = TeamManager.getTeamOf(player);


        if (teamName == null) {
            player.sendMessage("You are not a team!");
        }
        else {
            player.sendMessage("You are in '" + teamName + "'!");
        }
    }
}
