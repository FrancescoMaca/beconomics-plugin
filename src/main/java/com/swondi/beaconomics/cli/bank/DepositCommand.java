package com.swondi.beaconomics.cli.bank;

import com.swondi.beaconomics.managers.BankManager;
import com.swondi.beaconomics.scoreboards.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class DepositCommand {
    public static void run(
            @Nonnull CommandSender commandSender,
            @Nonnull Command command,
            @Nonnull String s,
            @Nonnull String[] args
    ) {
        if (!(commandSender instanceof Player player)) return;

        if (args.length < 2) {
            player.sendMessage(createHelpMessage());
            return;
        }

        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid money amount. Please enter a valid number.");
            return;
        }

        if (amount <= 0) {
            player.sendMessage(ChatColor.RED + "You cannot deposit zero or negative amounts.");
            return;
        }

        int playerOnHandMoney = BankManager.getOnHandMoney(player);

        if (playerOnHandMoney < amount) {
            player.sendMessage(ChatColor.RED + "You cannot deposit more money than you have on hand!");
            return;
        }

        BankManager.setOnHandMoney(player, playerOnHandMoney - amount);
        BankManager.addMoney(player, amount, true);
        Scoreboard.updateScore(player);

        player.sendMessage(ChatColor.GREEN + "You deposited §6" + BankManager.getFormattedMoney(amount) + ChatColor.GREEN + " in the bank");
    }

    private static String createHelpMessage() {
        return ChatColor.RED + "Usage: /bank deposit [number]";
    }
}
