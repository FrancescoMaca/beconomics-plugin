package com.swondi.beaconomics.models;

import com.swondi.beaconomics.managers.YamlManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class Generator {
    private final String id;
    private final Material type;
    private final int rate;
    private final Material drop;
    private final Location location;

    public int nextDrop = 0;

    public Generator(Material type, int rate, Material drop, Location location) {
        this.id = location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
        this.type = type;
        this.rate = rate;
        this.drop = drop;
        this.location = location;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Material getType() {
        return type;
    }

    public int getRate() {
        return rate;
    }

    public Material getDrop() {
        return drop;
    }

    public Location getLocation() {
        return location;
    }

    public static Generator fromYaml(YamlManager source, String id) {
        String path = "generators." + id;

        int rate = source.getInt(path + ".rate");
        Location location = new Location(
            Bukkit.getWorld(source.getString(path + ".world")),
            source.getInt(path + ".x"),
            source.getInt(path + ".y"),
            source.getInt(path + ".z")
        );

        return new Generator(
            Material.getMaterial(source.getString(path + ".type")),
            source.getInt(path + ".rate"),
            Material.getMaterial(source.getString(path + ".drop")),
            location
        );
    }

    public void saveToYaml(YamlManager target) {
        String path = "generators." + id;
        target.set(path + ".type", type.name());
        target.set(path + ".rate", rate);
        target.set(path + ".drop", drop.name());
        target.set(path + ".world", location.getWorld().getName());
        target.set(path + ".x", location.getBlockX());
        target.set(path + ".y", location.getBlockY());
        target.set(path + ".z", location.getBlockZ());

        target.save();
    }

    public static void removeFromYaml(YamlManager target, String id) {
        String path = "generators." + id;

        target.remove(path);
        target.save();
    }

    @Override
    public String toString() {
        return "Generator{" +
            "id='" + id + '\'' +
            ", type=" + type +
            ", rate=" + rate +
            ", drop=" + drop +
            ", location=(" + location.getWorld().getName() + ", " +
            location.getBlockX() + ", " +
            location.getBlockY() + ", " +
            location.getBlockZ() + ")" +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Generator other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
