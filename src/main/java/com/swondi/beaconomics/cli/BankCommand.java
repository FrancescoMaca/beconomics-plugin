package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.cli.bank.DepositCommand;
import com.swondi.beaconomics.cli.bank.InfoCommand;
import com.swondi.beaconomics.cli.bank.PayCommand;
import com.swondi.beaconomics.cli.bank.WithdrawCommand;
import com.swondi.beaconomics.managers.CombatManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class BankCommand implements CommandExecutor, TabCompleter {
    private static final List<String> SUB_CMDS = List.of("pay", "withdraw", "deposit", "info");

    @Override
    public boolean onCommand(
        @Nonnull CommandSender commandSender,
        @Nonnull Command command,
        @Nonnull String s,
        @Nonnull String[] args
    ) {
        if (!(commandSender instanceof Player player)) {
            return true;
        }

        if (args.length == 0) {
            InfoCommand.run(commandSender, command, s, args);
            player.sendMessage(createHelpMessage());
            return true;
        }
        //FIXME may be change to allow /bank info
        if (CombatManager.isInCombat(player)) {
            player.sendMessage(ChatColor.RED +"You cannot use this command while in combat!");
            return true;
        }

        switch (args[0]) {
            case "pay" -> PayCommand.run(commandSender, command, s, args);
            case "withdraw" -> WithdrawCommand.run(commandSender, command, s, args);
            case "deposit" -> DepositCommand.run(commandSender, command, s, args);
            case "info" -> InfoCommand.run(commandSender, command, s, args);
            default -> player.sendMessage(createHelpMessage());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(
        @Nonnull CommandSender commandSender,
        @Nonnull Command command,
        @Nonnull String s,
        @Nonnull String[] strings
    ) {
        if (!(commandSender instanceof Player)) {
            return null;
        }

        if (strings.length == 1) {
            return SUB_CMDS.stream().filter((suggestion) -> suggestion.startsWith(strings[0])).toList();
        }

        if (strings.length == 2) {
            if (strings[0].equals("deposit") || strings[0].equals("withdraw")) {
                return List.of("[amount]");
            }

            if (strings[0].equals("info")) return List.of();

            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }

        if (strings.length == 3 && strings[0].equals("pay")) {
            return List.of("[amount]");
        }

        return List.of();
    }

    private String createHelpMessage() {
        return ChatColor.RED + "Usage: /bank ["
            + ChatColor.GOLD + "pay "
            + ChatColor.RED + "| "
            + ChatColor.GOLD + "withdraw "
            + ChatColor.RED + "| "
            + ChatColor.GOLD + "deposit "
            + ChatColor.RED + "| "
            + ChatColor.GOLD + "info"
            + ChatColor.RED + "]";
    }
}
