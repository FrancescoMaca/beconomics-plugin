package com.swondi.beaconomics.cli.bank;

import com.swondi.beaconomics.managers.BankManager;
import com.swondi.beaconomics.scoreboards.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class WithdrawCommand {
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
            player.sendMessage(ChatColor.RED + "You cannot withdraw zero or negative amounts.");
            return;
        }

        int playerBankMoney = BankManager.getBankMoney(player);

        if (playerBankMoney < amount) {
            player.sendMessage(ChatColor.RED + "You cannot withdraw more money than you have in the bank!");
            return;
        }

        BankManager.setBankMoney(player, playerBankMoney - amount);
        BankManager.addMoney(player, amount, false);
        Scoreboard.updateScore(player);

        player.sendMessage(ChatColor.GREEN + "You withdrawn §6" + BankManager.getFormattedMoney(amount) + ChatColor.GREEN + " from the bank");
    }

    private static String createHelpMessage() {
        return ChatColor.RED + "Usage: /bank withdraw [number]";
    }
}
