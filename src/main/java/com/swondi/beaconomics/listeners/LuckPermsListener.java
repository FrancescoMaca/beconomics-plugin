package com.swondi.beaconomics.listeners;

import com.swondi.beaconomics.Beaconomics;
import com.swondi.beaconomics.managers.RankManager;
import com.swondi.beaconomics.utils.Constants;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;

import java.util.UUID;

public class LuckPermsListener {
    private final LuckPerms luckPerms;

    public LuckPermsListener(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    public void register() {
        EventBus eventBus = luckPerms.getEventBus();
        eventBus.subscribe(Beaconomics.getInstance(), NodeAddEvent.class, this::onPlayerRankChange);
    }

    // Not gonna lie, I don't know exactly what I should listen to, so I just found the first thing that worked
    private void onPlayerRankChange(NodeAddEvent event) {
        if (!event.isUser()) {
            Bukkit.broadcastMessage("Player is not a user");
            return;
        }

        UUID userUUID = ((User) event.getTarget()).getUniqueId();

        String rankName = "";
        for (Node node : event.getDataAfter()) {
            if (node.getKey().startsWith("group.")) {
                Bukkit.broadcastMessage("Group found!");

                rankName = node.getKey().split("\\.")[1];
                rankName = rankName.toUpperCase().charAt(0) + rankName.substring(1).toLowerCase();

                Bukkit.broadcastMessage("Group name: " + rankName);

            }
        }

        if (rankName.isEmpty()) {
            Bukkit.broadcastMessage("RankName is empty");
            return;
        }

        RankManager.setPlayerRank(userUUID, Constants.RANKS.getOrDefault(rankName, Constants.RANKS.get("Default")));
        Bukkit.broadcastMessage("Rank Set");

    }
}