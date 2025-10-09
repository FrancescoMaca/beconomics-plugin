package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.animations.KitAnimation;
import com.swondi.beaconomics.managers.KitManager;
import com.swondi.beaconomics.managers.PDCManager;
import com.swondi.beaconomics.managers.PlayerManager;
import com.swondi.beaconomics.models.Kit;
import com.swondi.beaconomics.tasks.DropCleanupTask;
import com.swondi.beaconomics.helpers.CommandHelper;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

import static com.swondi.beaconomics.helpers.AnimationHelper.findFirstAvailableBlock;
import static com.swondi.beaconomics.helpers.AnimationHelper.spawnFireWave;

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

        int x = player.getLocation().getBlockX();
        int z = player.getLocation().getBlockZ();

        if (Math.sqrt(x * x + z * z) <= 68) {
            player.sendMessage(ChatColor.RED + "You cannot request a kit in spawn");
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

        String name = kitName.split(" ")[0];
        long lastAcquired = PlayerManager.getKitAcquisitionTime(player, name);
        long cooldownTime = Constants.DATA_KITS.get(name).cooldown() * 1000L;
        long remainingTime = lastAcquired + cooldownTime - System.currentTimeMillis();

        if (remainingTime > 0 && !player.hasPermission("beaconomics.kit.bypasscooldown")) {
            player.sendMessage(formatTime(remainingTime));
            return;
        }

        PlayerManager.setKitAcquisitionTime(player, name);

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
                System.currentTimeMillis(),
                dropLocation
            );

            DropCleanupTask.addChest(kit);
        }, kitName);
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

    private String formatTime(long remainingTime) {
        long hours = remainingTime / 3600000;
        long min = (remainingTime % 3600000) / 60000;
        long sec = (remainingTime % 60000) / 1000;
        String message = ChatColor.RED + "You still need to wait ";

        if (hours > 0) message += hours + "h ";
        if (min > 0) message += min + "m ";
        if (sec > 0) message += sec + "s ";

        message += "before using this kit again!";

        return message;
    }

}
