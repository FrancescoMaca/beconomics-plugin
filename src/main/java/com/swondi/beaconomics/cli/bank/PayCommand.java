package com.swondi.beaconomics.cli.bank;

import com.swondi.beaconomics.managers.BankManager;
import com.swondi.beaconomics.scoreboards.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class PayCommand {
    public static void run(
        @Nonnull CommandSender commandSender,
        @Nonnull Command command,
        @Nonnull String s,
        @Nonnull String[] args
    ) {
        if (!(commandSender instanceof Player player)) return;

        if (args.length < 3) {
            player.sendMessage(createHelpMessage());
            return;
        }

        Player targetPlayer = org.bukkit.Bukkit.getPlayer(args[1]);

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            player.sendMessage(ChatColor.RED + "The player " + args[1] + " is offline or does not exist.");
            return;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid money amount. Please enter a valid number.");
            return;
        }

        if (amount <= 0) {
            player.sendMessage(ChatColor.RED + "You cannot pay zero or negative amounts.");
            return;
        }

        int playerOnHandMoney = BankManager.getOnHandMoney(player);

        if (playerOnHandMoney < amount) {
            player.sendMessage(ChatColor.RED + "You cannot pay more money than you have on hand!");
            return;
        }

        BankManager.setOnHandMoney(player, playerOnHandMoney - amount);

        BankManager.addMoney(targetPlayer, amount, false);

        Scoreboard.updateScore(player);
        Scoreboard.updateScore(targetPlayer);

        player.sendMessage(ChatColor.GREEN + "You paid §6" + BankManager.getFormattedMoney(amount) + ChatColor.GREEN + " to " + targetPlayer.getName() + ".");
        targetPlayer.sendMessage(ChatColor.GREEN + "You received §6" + BankManager.getFormattedMoney(amount) + ChatColor.GREEN + " from " + player.getName() + ".");
    }

    private static String createHelpMessage() {
        return ChatColor.RED + "Usage: /bank pay [player] [amount]";
    }
}
