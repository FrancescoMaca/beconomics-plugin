package com.swondi.beaconomics.managers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TeamManager {
    private static final YamlManager yaml = new YamlManager("teams.yml");

    // Create a new team when a player places a beacon
    public static void createTeam(Player owner, String teamName) {
        String ownerId = owner.getUniqueId().toString();

        yaml.set(teamName + ".owner", ownerId);
        yaml.set(teamName + ".members", new ArrayList<String>() {{ add(ownerId); }});
        yaml.save();
    }

    // Disband the team (only owner can do this)
    public static void disbandTeam(Player owner) {
        String ownerId = owner.getUniqueId().toString();
        String teamName = getTeamOf(owner);

        if (teamName == null) {
            owner.sendMessage(ChatColor.RED + "You are not in a team!");
            return;
        }

        String teamOwner = yaml.getString(teamName + ".owner");
        if (!ownerId.equals(teamOwner)) {
            owner.sendMessage(ChatColor.RED + "Only the team owner can disband the team!");
            return;
        }

        yaml.set(teamName, null);
        yaml.save();

        owner.sendMessage(ChatColor.GREEN + "Team " + teamName + " has been disbanded!");
    }

    // Add a player to the owner's team
    public static void addToTeam(Player owner, Player teammate) {
        String ownerId = owner.getUniqueId().toString();
        String teammateId = teammate.getUniqueId().toString();
        String teamName = getTeamOf(owner);

        if (teamName == null) {
            owner.sendMessage(ChatColor.RED + "You don't have a team yet!");
            return;
        }

        String teamOwner = yaml.getString(teamName + ".owner");
        if (!ownerId.equals(teamOwner)) {
            owner.sendMessage(ChatColor.RED + "Only the team owner can add members!");
            return;
        }

        List<String> members = yaml.getStringList(teamName + ".members");
        if (members.contains(teammateId)) {
            owner.sendMessage(ChatColor.RED + teammate.getName() + " is already in your team!");
            return;
        }

        // Check if teammate is already in a team
        if (getTeamOf(teammate) != null) {
            owner.sendMessage(ChatColor.RED + teammate.getName() + " is already in another team!");
            return;
        }

        members.add(teammateId);
        yaml.set(teamName + ".members", members);
        yaml.save();

        owner.sendMessage(ChatColor.GREEN + teammate.getName() + " has been added to your team!");
        teammate.sendMessage(ChatColor.GREEN + "You have joined " + teamName + "!");
    }

    // Remove a player from the owner's team
    public static void removeFromTeam(Player owner, UUID teammate) {
        String ownerId = owner.getUniqueId().toString();
        String teamName = getTeamOf(owner);

        if (teamName == null) {
            owner.sendMessage(ChatColor.RED + "You are not in a team!");
            return;
        }

        if (owner.getUniqueId() == teammate) {
            owner.sendMessage(ChatColor.RED + " You cannot kick yourself! If you want to leave the team you have to disband it doing " + ChatColor.GOLD + "/team disband");
        }

        String teamOwner = yaml.getString(teamName + ".owner");
        if (!ownerId.equals(teamOwner)) {
            owner.sendMessage(ChatColor.RED + "Only the team owner can remove members!");
            return;
        }

        List<String> members = yaml.getStringList(teamName + ".members");
        String teammateName = PlayerDataManager.getName(teammate);

        if (!members.contains(teammate.toString())) {
            owner.sendMessage(ChatColor.RED + teammateName + " is not in your team!");
            return;
        }

        members.remove(teammate.toString());
        yaml.set(teamName + ".members", members);
        yaml.save();

        owner.sendMessage(ChatColor.GREEN + teammateName + " has been removed from your team!");
    }

    public static String getTeamOf(Player player) {
        if (!yaml.getConfiguration().contains("teams")) return null;

        for (String teamName : Objects.requireNonNull(yaml.getConfiguration().getConfigurationSection("teams")).getKeys(false)) {
            String owner = yaml.getString("teams." + teamName + ".owner");
            List<String> members = yaml.getStringList("teams." + teamName + ".members");

            if (owner.equals(player.getUniqueId().toString()) || members.contains(player.getUniqueId().toString())) {
                return teamName;
            }
        }

        return null;
    }

}
