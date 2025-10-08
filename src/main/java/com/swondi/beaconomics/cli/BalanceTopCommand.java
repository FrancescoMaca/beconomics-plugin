package com.swondi.beaconomics.cli;


import com.swondi.beaconomics.managers.YamlManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class BalanceTopCommand implements CommandExecutor {

    private final YamlManager yaml = new YamlManager("money.yml");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

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
            sender.sendMessage("§cIncorrect usage. Please use: /" + label +  " [number]");
            return true;
        }

        Map<String, Double> totalBalances = new HashMap<>();
        //FIXME find a more efficient way without use disk access
        for (String uuidStr : yaml.getConfiguration().getKeys(false)) {
            double onHand = yaml.getConfiguration().getDouble(uuidStr + ".onhand", 0);
            double bank = yaml.getConfiguration().getDouble(uuidStr + ".bank.amount", 0);
            double total = onHand + bank;

            OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuidStr));
            String name = offPlayer.getName() != null ? offPlayer.getName() : uuidStr;

            totalBalances.put(name, total);
        }

        List<Map.Entry<String, Double>> sorted = new ArrayList<>(totalBalances.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        sender.sendMessage("§6§lServer Top Balance:");
        int pos = 1;
        for (Map.Entry<String, Double> entry : sorted.stream().limit(limit).toList()) {
            sender.sendMessage("§e#" + pos + " §f" + entry.getKey() + " §7- §a" + String.format("%.2f", entry.getValue()) + "K");
            pos++;
        }

        return true;
    }
}