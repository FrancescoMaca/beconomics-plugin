package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.managers.CombatManager;
import com.swondi.beaconomics.managers.RankManager;
import com.swondi.beaconomics.menus.enderchest.EnderChestInventory;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import java.util.List;

public class EnderChestCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(
        @Nonnull CommandSender commandSender,
        @Nonnull Command command,
        @Nonnull String s,
        @Nonnull String[] strings
    ) {

        if (!(commandSender instanceof Player player)) {
            return true;
        }

        Inventory echest = EnderChestInventory.build(player);

        if (!player.hasPermission("beaconomics.enderchest.user") || echest == null) {
            player.sendMessage(ChatColor.RED + "You cannot use the enderchest without a rank!");
            return true;
        }

        if (CombatManager.isInCombat(player)) {
            player.sendMessage(ChatColor.RED +"You cannot use this command while in combat!");
            return true;
        }

        player.openInventory(echest);

        return true;
    }

    @Override
    public List<String> onTabComplete(
        @Nonnull CommandSender commandSender,
        @Nonnull Command command,
        @Nonnull String s,
        @Nonnull String[] strings
    ) {
        return List.of();
    }
}
