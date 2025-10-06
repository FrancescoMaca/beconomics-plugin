package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.managers.HomesManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHomeCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only player can use this command.");
            return true;
        }

        //TODO add check on how many home a player can have

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Use: /sethome <name>");
            return true;
        }

        String name = args[0];
        HomesManager.setHome(player, name);
        player.sendMessage(ChatColor.GREEN + "✅ Home '" + name + "' set with success!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return List.of();
    }
}
