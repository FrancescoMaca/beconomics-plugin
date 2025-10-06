package com.swondi.beaconomics.cli.handlers;

import com.swondi.beaconomics.cli.data.TokenCommandManager;
import com.swondi.beaconomics.managers.TeamManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class DisbandTeamHandler {

    private DisbandTeamHandler() {}

    public static void disbandTeam(CommandSender sender, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can disband teams.");
            return;
        }

        String teamName = TeamManager.getTeamOf(player);

        if (teamName == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a team!");
            return;
        }

        UUID ownerUUID = UUID.fromString(TeamManager.getTeamOwner(teamName));

        if (!ownerUUID.equals(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Only the team owner can disband the team!");
            return;
        }

        String token = TokenCommandManager.createConfirmationToken(player.getUniqueId());
        //TODO add other textual confirmation mechanism
        TextComponent confirm = new TextComponent(ChatColor.RED + "[YES]");
        confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/system " + token + " team-disband " + teamName));
        confirm.setHoverEvent(
                new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(ChatColor.YELLOW + "Click to confirm team disband of \"" + teamName + "\""
                        ).create()
                ));

        TextComponent cancel = new TextComponent(ChatColor.GREEN + "[NO]");
        cancel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/system " + token + " team-cancel " + teamName));
        cancel.setHoverEvent(
                new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(ChatColor.GRAY + "Click to annul team disband "
                        ).create()
                ));

        player.spigot().sendMessage(
                new TextComponent(ChatColor.LIGHT_PURPLE + "Confirm team disband \"" + teamName + "\"? "),
                confirm,
                new TextComponent(" "),
                cancel
        );
    }
}
