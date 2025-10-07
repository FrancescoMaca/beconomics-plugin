package com.swondi.beaconomics.cli.bank;

import com.swondi.beaconomics.managers.BankManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class InfoCommand {

    public static void run(
        @Nonnull CommandSender commandSender,
        @Nonnull Command command,
        @Nonnull String s,
        @Nonnull String[] args
    ) {
        if (!(commandSender instanceof Player player)) return;

        int moneyInTheBank = BankManager.getBankMoney(player);

        player.sendMessage("§aYour bank balance is: §6" + BankManager.getFormattedMoney(moneyInTheBank));
    }
}
