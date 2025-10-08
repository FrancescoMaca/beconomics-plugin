package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.managers.PlayerManager;
import com.swondi.beaconomics.utils.Constants;
import com.swondi.beaconomics.models.KitData;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
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

        if (!(sender instanceof Player player)) return false;  // Ensure the sender is a player

        // Map of kit names to check based on permissions
        Map<String, Constants.KitData> kitData = Constants.DATA_KITS;

        // Send title header with a separator line
        player.spigot().sendMessage(new TextComponent(new ComponentBuilder("§6§l---- ").color(net.md_5.bungee.api.ChatColor.of("#FF5733")).append("Available Kits").color(net.md_5.bungee.api.ChatColor.of("#00FF00")).append(" ----").color(net.md_5.bungee.api.ChatColor.of("#FF5733")).create()));

        // Loop through all available kits
        for (Map.Entry<String, Constants.KitData> entry : kitData.entrySet()) {
            String kitName = entry.getKey();
            Constants.KitData kitDetails = entry.getValue();

            // Check if player has permission for the current kit
            if (player.hasPermission("beaconomics.kit." + kitName.toLowerCase())) {

                // Get the last time the player acquired this kit
                long lastAcquired = PlayerManager.getKitAcquisitionTime(player, kitName.toLowerCase());

                // Calculate the remaining time
                long cooldownTime = kitDetails.cooldown() * 1000; // Convert to milliseconds
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

                // Build the message for each kit
                ComponentBuilder messageBuilder = new ComponentBuilder();

                // Append kit name with its specific color
                messageBuilder.append(kitName).color(net.md_5.bungee.api.ChatColor.of(kitColor));

                // If the remaining time is greater than zero, show the time left
                if (remainingTime > 0) {
                    // Calculate minutes and seconds remaining
                    long minutes = remainingTime / 60000;
                    long seconds = (remainingTime % 60000) / 1000;

                    // Append cooldown time in bright red
                    messageBuilder.append(" - §c" + minutes + "m " + seconds + "s §7until available").color(net.md_5.bungee.api.ChatColor.of("#FF0000"));
                } else {
                    // If available now, show the message in green
                    messageBuilder.append(" - §aAvailable now!").color(net.md_5.bungee.api.ChatColor.of("#00FF00"));
                }

                // Send the formatted message
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
