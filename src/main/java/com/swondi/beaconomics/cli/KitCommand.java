package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.animations.KitAnimation;
import com.swondi.beaconomics.managers.KitManager;
import com.swondi.beaconomics.managers.PDCManager;
import com.swondi.beaconomics.models.Kit;
import com.swondi.beaconomics.tasks.DropCleanupTask;
import com.swondi.beaconomics.tasks.TickTask;
import com.swondi.beaconomics.utils.CommandHelper;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

import static com.swondi.beaconomics.utils.AnimationHelper.findFirstAvailableBlock;
import static com.swondi.beaconomics.utils.AnimationHelper.spawnFireWave;

public class KitCommand implements CommandExecutor, TabCompleter {

    private static final List<String> SUB_CMDS = List.of("starter", "inferno", "fire", "ember", "blaze", "spark");

    @FunctionalInterface
    private interface KitHandler {
        void run(Block block);
    }

    @Override
    public boolean onCommand(
        @Nonnull CommandSender sender,
        @Nonnull Command command,
        @Nonnull String label,
        @Nonnull String[] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§eUsage: /kit <name>");
            return true;
        }

        String kitName = args[0].toLowerCase();

        String permission = "beaconomics.kit." + kitName;

        if (!player.hasPermission(permission)) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this kit!");
            return true;
        }

        switch (kitName) {
            case "starter" -> handleKit(player, "Starter Kit", Color.WHITE, KitManager::giveStarterKit);
            case "inferno" -> handleKit(player, "Inferno Kit", Color.ORANGE, KitManager::giveInfernoKit);
            case "fire" -> handleKit(player, "Fire Kit", Color.RED, KitManager::giveFireKit);
            case "ember" -> handleKit(player, "Ember Kit", Color.GREEN, KitManager::giveEmberKit);
            case "blaze" -> handleKit(player, "Blaze Kit", Color.YELLOW, KitManager::giveBlazeKit);
            case "spark" -> handleKit(player, "Spark Kit", Color.TEAL, KitManager::giveSparkKit);
            default -> player.sendMessage("§cUnknown kit: " + kitName);
        }

        return true;
    }

    private void handleKit(Player player, String kitName, Color color, KitHandler kitFiller) {
        if (player.getLocation().getY() > 150) {
            player.sendMessage(ChatColor.RED + "You can only use care packages in a Y level less than 150!");
            return;
        }

        player.sendMessage("Your " + kitName + " is arriving....");

        Location dropLocation = player.getLocation().clone();
        dropLocation.setY(findFirstAvailableBlock(dropLocation));

        KitAnimation.start(dropLocation, color, () -> {
            player.sendMessage(ChatColor.GREEN + kitName + " has landed!!" + ChatColor.WHITE + " Don't sweat it though, no one can open it other than you!");
            Block blockAtDrop = player.getWorld().getBlockAt(dropLocation);
            PDCManager.tagAsKitChest(blockAtDrop);
            PDCManager.setLockedChest(blockAtDrop, player);

            spawnFireWave(dropLocation, Particle.FIREWORK, 4, 180, 1);

            // Use the passed KitFiller method to give the correct kit
            kitFiller.run(dropLocation.getBlock());

            Kit kit = new Kit(
                dropLocation.getBlockX() + "_" + dropLocation.getBlockY() + "_" + dropLocation.getBlockZ(),
                kitName,
                player.getUniqueId(),
                true,
                100,
                TickTask.tick,
                dropLocation
            );

            DropCleanupTask.addChest(kit);
        });
    }

    @Override
    public List<String> onTabComplete(
            @Nonnull CommandSender commandSender,
            @Nonnull Command command,
            @Nonnull String label,
            @Nonnull String[] args
    ) {
        if (args.length == 1) {
            return CommandHelper.getSuggestions(args[0], SUB_CMDS);
        }

        return List.of();
    }
}
