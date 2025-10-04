package com.swondi.beaconomics.models;

import com.swondi.beaconomics.managers.YamlManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class Kit {
    private final String id;
    private final String name;
    private final UUID owner;
    private final boolean locked;
    private final int cooldown;
    private final long createdAt;
    private final Location location;

    public Kit(
        String id,
        String name,
        UUID owner,
        boolean locked,
        int cooldown,
        Long createdAt,
        Location location
    ) {
        this.id = location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
        this.name = name;
        this.owner = owner;
        this.locked = locked;
        this.cooldown = cooldown;
        this.createdAt = createdAt;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getCooldown() {
        return cooldown;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public UUID getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Creates a copy of this kit with optional overrides.
     * Pass null for values you don’t want to change.
     */
    public Kit copyWith(
        String name,
        UUID owner,
        Boolean locked,
        Integer cooldown,
        Location location,
        Long createdAt
    ) {
        return new Kit(
            id,
            name != null ? name : this.name,
            owner != null ? owner : this.owner,
            locked != null ? locked : this.locked,
            cooldown != null ? cooldown : this.cooldown,
            createdAt != null ? createdAt : this.createdAt,
            location != null ? location : this.location
        );
    }

    /**
     * Save this kit to a YamlManager
     */
    public void saveToYaml(YamlManager yaml) {
        String path = "kits." + id;
        yaml.set(path + ".name", name);
        yaml.set(path + ".owner", owner.toString());
        yaml.set(path + ".locked", locked);
        yaml.set(path + ".cooldown", cooldown);
        yaml.set(path + ".createdAt", createdAt);
        yaml.set(path + ".world", location.getWorld().getName());
        yaml.set(path + ".x", location.getBlockX());
        yaml.set(path + ".y", location.getBlockY());
        yaml.set(path + ".z", location.getBlockZ());
        yaml.save();
    }

    /**
     * Load a Kit from YamlManager by name
     */
    public static Kit fromYaml(YamlManager yaml, String id) {
        String path = "kits." + id;
        String name = yaml.getString(path + ".name");
        UUID owner = UUID.fromString(yaml.getString(path + ".owner"));
        boolean locked = yaml.getBoolean(path + ".locked");
        int cooldown = yaml.getInt(path + ".cooldown");
        Location location = new Location(
            Bukkit.getWorld(yaml.getString(path + ".world")),
            yaml.getInt(path + ".x"),
            yaml.getInt(path + ".y"),
            yaml.getInt(path + ".z")
        );

        long createdAt = yaml.getConfiguration().getLong(path + ".createdAt");
        return new Kit(
            id,
            name,
            owner,
            locked,
            cooldown,
            createdAt,
            location
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Kit other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
