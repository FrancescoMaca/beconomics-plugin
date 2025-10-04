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

public class KickTeamMemberHandler {
    private KickTeamMemberHandler() {}

    public static void kickTeamMember(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can kick members.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /team kick [Player Name]");
            return;
        }

        String token = TokenCommandManager.createConfirmationToken(player.getUniqueId());
        String playerName = args[1];

        TextComponent sure = new TextComponent(ChatColor.GREEN + "[I'M SURE]");

        sure.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/system " + token + " team-kick " + playerName));
        sure.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.YELLOW + "I'm sure, I want to throw him out >:("
                ).create()
            ));

        TextComponent cancel = new TextComponent(ChatColor.RED + "[CANCEL]");
        cancel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/system " + token + " team-cancel "));
        cancel.setHoverEvent(
            new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(ChatColor.GRAY + "Mh, maybe not!"
                ).create()
            ));

        player.spigot().sendMessage(
            new TextComponent(ChatColor.LIGHT_PURPLE + "Player '" + playerName + "' will be kicked "),
            sure,
            new TextComponent(" "),
            cancel
        );
    }
}
