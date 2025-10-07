package com.swondi.beaconomics.cli;

import com.swondi.beaconomics.Beaconomics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BugReportCommand implements CommandExecutor, TabCompleter {

    private final List<String> categories = List.of("permissions", "ranks", "nexus", "generators", "other");

    @Override
    public boolean onCommand(
            @Nonnull CommandSender commandSender,
            @Nonnull Command command,
            @Nonnull String s,
            @Nonnull String[] args
    ) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /bugreport [category] [description]");
            return false;
        }

        String category = args[0].toLowerCase();
        String description = String.join(" ", List.of(args).subList(1, args.length));

        // Validate category
        if (!categories.contains(category)) {
            player.sendMessage(ChatColor.RED + "Invalid category. Valid categories: " + String.join(", ", categories));
            return false;
        }

        saveBugReport(player, category, description);

        player.sendMessage(ChatColor.GREEN + "Thank you for your bug report! We will look into it.");


        return true;
    }

    @Override
    public List<String> onTabComplete(
            @Nonnull CommandSender commandSender,
            @Nonnull Command command,
            @Nonnull String s,
            @Nonnull String[] args
    ) {
        if (args.length == 1) {
            return categories;
        }

        return List.of();
    }

    /**
     * Saves the bug report to a file (or database, or other logging system).
     * @param player The player submitting the report.
     * @param category The bug report category.
     * @param description The bug report description.
     */
    private void saveBugReport(Player player, String category, String description) {
        // Get the plugin's data folder (plugins/Beaconomics/)
        File pluginFolder = Beaconomics.getInstance().getDataFolder();

        // Make sure the folder exists
        if (!pluginFolder.exists()) {
            boolean created = pluginFolder.mkdirs();

            if (!created) {
                Bukkit.getLogger().severe("Couldn't create the bug report folder.");
            }
        }

        // Create the file path (plugins/Beaconomics/bug_reports.txt)
        File bugReportFile = new File(pluginFolder, "bug_reports.txt");

        // Save the report to the file
        try (FileWriter writer = new FileWriter(bugReportFile, true)) {
            String report = String.format(
                "Player: %s\nCategory: %s\nDescription: %s\n\n",
                player.getName(),
                category,
                description
            );
            writer.write(report);
        } catch (IOException e) {
            player.sendMessage(ChatColor.RED + "An error occurred while saving your bug report. Please try again later.");
            e.printStackTrace();
        }
    }
}
