package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.managers.PlayerManager;
import com.swondi.beaconomics.utils.Constants;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class KitsCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(
        @Nonnull CommandSender sender,
        @Nonnull Command command,
        @Nonnull String label,
        @Nonnull String[] args
    ) {

        if (!(sender instanceof Player player)) return false;

        Map<String, Constants.KitData> kitData = Constants.DATA_KITS;

        player.spigot().sendMessage(new TextComponent(new ComponentBuilder("§6§l---- ").color(net.md_5.bungee.api.ChatColor.of("#FF5733")).append("Available Kits").color(net.md_5.bungee.api.ChatColor.of("#00FF00")).append(" ----").color(net.md_5.bungee.api.ChatColor.of("#FF5733")).create()));

        for (Map.Entry<String, Constants.KitData> entry : kitData.entrySet()) {
            String kitName = entry.getKey();
            Constants.KitData kitDetails = entry.getValue();

            if (player.hasPermission("beaconomics.kit." + kitName.toLowerCase())) {

                long lastAcquired = PlayerManager.getKitAcquisitionTime(player, kitName.toLowerCase());

                long cooldownTime = kitDetails.cooldown() * 1000L; // Convert to milliseconds
                long remainingTime = lastAcquired + cooldownTime - System.currentTimeMillis();

                // Kit Name Color
                String kitColor = switch (kitName.toLowerCase()) {
                    case "spark" -> "#FFCC00";
                    case "blaze" -> "#FFD700";
                    case "ember" -> "#FF7F32";
                    case "fire" -> "#FF6347";
                    case "inferno" -> "#8B0000";
                    default -> "#FFFFFF";
                };

                ComponentBuilder messageBuilder = new ComponentBuilder();

                messageBuilder.append(kitName).color(net.md_5.bungee.api.ChatColor.of(kitColor));

                if (remainingTime > 0) {
                    long hours = remainingTime / 3600000;
                    long minutes = (remainingTime % 3600000) / 60000;
                    long seconds = (remainingTime % 60000) / 1000;

                    if (hours > 0) {
                        messageBuilder.append(" - " + ChatColor.RED + hours + "h ");
                    }
                    else {
                        messageBuilder.append(" - " + ChatColor.RED);
                    }
                    messageBuilder.append(ChatColor.RED + "" + minutes + "m " + seconds + "s §7until available");
                } else {
                    messageBuilder.append(" - §aAvailable now!").color(net.md_5.bungee.api.ChatColor.of("#00FF00"));
                }

                player.spigot().sendMessage(messageBuilder.create());
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(
        @Nonnull CommandSender sender,
        @Nonnull Command command,
        @Nonnull String label,
        @Nonnull String[] args
    ) {
        return List.of();
    }
}
