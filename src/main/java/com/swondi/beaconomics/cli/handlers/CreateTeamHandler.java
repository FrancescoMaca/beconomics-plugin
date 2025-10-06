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

public class CreateTeamHandler {
    private CreateTeamHandler() {}

    public static void createTeam(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can create teams.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /team create [Team Name]");
            return;
        }

        if(TeamManager.getTeamOf(player) != null){
            sender.sendMessage(ChatColor.RED + "You are already part of a team!");
            return;
        }

        if(!TeamManager.isValidTeamName(args[1])){
            sender.sendMessage(ChatColor.RED + "Team name must not contain invalid digits!");
            return;
        }

        String teamName = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));

        if (teamName.length() < 2 || teamName.length() > 30) {
            return;
        }

        if(!TeamManager.isTeamNameUnique(teamName)) {
            sender.sendMessage(ChatColor.RED + "Team name already taken!");
            return;
        }

        String token = TokenCommandManager.createConfirmationToken(player.getUniqueId());

        TextComponent confirm = new TextComponent(ChatColor.GREEN + "[CONFIRM]");
        confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/system " + token + " team-create " + teamName));
        confirm.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.YELLOW + "Click to confirm creating \"" + teamName + "\""
            ).create()
        ));

        TextComponent cancel = new TextComponent(ChatColor.RED + "[CANCEL]");
        cancel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/system " + token + " team-cancel " + teamName));
        cancel.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.GRAY + "Click to cancel team creation"
            ).create()
        ));

        player.spigot().sendMessage(
            new TextComponent(ChatColor.LIGHT_PURPLE + "Confirm team creation \"" + teamName + "\"? "),
            confirm,
            new TextComponent(" "),
            cancel
        );
    }
}
