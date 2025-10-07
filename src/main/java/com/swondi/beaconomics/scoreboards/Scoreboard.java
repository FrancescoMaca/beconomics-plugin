package com.swondi.beaconomics.scoreboards;

import com.swondi.beaconomics.managers.BankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Scoreboard {
    public static void setupScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("money", Criteria.DUMMY, "§6Money");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Show formatted money in the entry instead of the numeric score
        int amount = BankManager.getOnHandMoney(player);
        String formatted = "§aMoney: " + BankManager.getFormattedMoney(amount) + "    ";
        objective.getScore(formatted).setScore(0);

        player.setScoreboard(board);
    }

    public static void updateScore(Player player) {
        org.bukkit.scoreboard.Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective("money");
        if (objective != null) {
            // Clear old entries
            for (String entry : board.getEntries()) {
                board.resetScores(entry);
            }

            int amount = BankManager.getOnHandMoney(player);

            // Add updated entry
            String formatted = "§aMoney: " + BankManager.getFormattedMoney(amount);
            objective.getScore(formatted).setScore(1);
        }
    }
}
