package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.menus.shop.ShopMainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class ShopCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUB_COMMANDS = List.of("shop");

    @Override
    public boolean onCommand(
        @Nonnull CommandSender commandSender,
        @Nonnull Command command,
        @Nonnull String s,
        @Nonnull String[] strings
    ) {
        if (!(commandSender instanceof Player player)) return true;

        player.openInventory(ShopMainMenu.build(player));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        return List.of();
    }
}
