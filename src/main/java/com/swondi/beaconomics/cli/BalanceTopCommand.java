package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.managers.BankManager;
import com.swondi.beaconomics.managers.PlayerDataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

public class BalanceTopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(
        @Nonnull CommandSender sender,
        @Nonnull Command command,
        @Nonnull String label,
        @Nonnull String[] args
    ) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }

        int limit = 10;

        if (args.length >= 1 && args[0].matches("\\d+")) {
            int argLimit = Integer.parseInt(args[0]);
            if (argLimit > 0) {
                limit = Math.min(argLimit, 1000);
            } else {
                sender.sendMessage("§cUse a positive value as an argument");
                return true;
            }
        } else if (args.length >= 1) {
            sender.sendMessage("§cUsage: /" + label +  " [number]");
            return true;
        }

        Map<UUID, Integer> totalBalances = BankManager.getAllPlayersMoney();

        // Sort entries in descending order (largest to smallest)
        List<Map.Entry<UUID, Integer>> balances = totalBalances.entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue(Comparator.reverseOrder()))  // Reversed sorting
                .toList();

        sender.sendMessage("§6§lServer Top Balance:");

        int pos = 1;
        for (Map.Entry<UUID, Integer> entry : balances.stream().limit(limit).toList()) {
            sender.sendMessage("§e#" + pos + " §f" + PlayerDataManager.getName(entry.getKey()) + " §7- §a" + BankManager.getFormattedMoney(entry.getValue()) + "$");
            pos++;
        }

        return true;
    }
}
