package com.swondi.beaconomics.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamManager {
    private static final YamlManager yaml = new YamlManager("teams.yml");

    // Create a new team when a player places a beacon
    public static void createTeam(Player owner, String teamName) {
        String ownerId = owner.getUniqueId().toString();

        //FIXME useless even if triggered action is executed
        if(getTeamOf(owner) != null){
            owner.sendMessage("You are already part of a team!");
            return;
        }

        yaml.set(teamName + ".owner", ownerId);
        yaml.set(teamName + ".members", new ArrayList<String>() {{ add(ownerId); }});
        yaml.save();
    }

    // Disband the team (only owner can do this)
    public static void disbandTeam(Player owner) {
        String ownerId = owner.getUniqueId().toString();
        String teamName = getTeamOf(owner);

        //FIXME useless even if triggered action is executed
        if (teamName == null) {
            owner.sendMessage(ChatColor.RED + "You are not in a team!");
            return;
        }

        //FIXME useless even if triggered action is executed
        String teamOwner = yaml.getString(teamName + ".owner");
        if (!ownerId.equals(teamOwner)) {
            owner.sendMessage(ChatColor.RED + "Only the team owner can disband the team!");
            return;
        }

        yaml.set(teamName, null);
        yaml.save();

        owner.sendMessage(ChatColor.GREEN + "Team " + teamName + " has been disbanded with success!");
    }

    // Add a player to the owner's team
    public static void addToTeam(Player owner, String targetName) {
        String ownerId = owner.getUniqueId().toString();

        Player target = Bukkit.getPlayer(targetName);

        if (owner.getName().equals(targetName)){
            owner.sendMessage(ChatColor.RED +"You can't invite yourself!");
            return;
        }

        if (target == null) {
            owner.sendMessage("Player is offline!");
            return;
        }

        String teammateId = target.getUniqueId().toString();
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
            owner.sendMessage(ChatColor.RED + target.getName() + " is already in your team!");
            return;
        }

        // Check if teammate is already in a team
        if (getTeamOf(target) != null) {
            owner.sendMessage(ChatColor.RED + target.getName() + " is already in another team!");
            return;
        }

        members.add(teammateId);
        yaml.set(teamName + ".members", members);
        yaml.save();

        owner.sendMessage(ChatColor.GREEN + target.getName() + " has been added to your team!");
        target.sendMessage(ChatColor.GREEN + "You have joined " + teamName + "!");
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
        for (String teamName : yaml.getConfiguration().getKeys(false)) {
            String owner = yaml.getString(teamName + ".owner");
            List<String> members = yaml.getStringList(teamName + ".members");

            if (player.getUniqueId().toString().equals(owner) || members.contains(player.getUniqueId().toString())) {
                return teamName;
            }
        }
        return null;
    }

    public static String getTeamOwner(String teamName){
        return yaml.getString(teamName + ".owner");
    }

    public static boolean isTeamNameUnique(String teamName){
        return yaml.get(teamName) == null;
    }

    public static boolean isValidTeamName(String name) {
        return name.toLowerCase().matches("^[A-Za-z0-9_-]{3,16}$");
    }

}
