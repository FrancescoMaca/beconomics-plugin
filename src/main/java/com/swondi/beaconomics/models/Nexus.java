package com.swondi.beaconomics.models;

import com.swondi.beaconomics.managers.YamlManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class Nexus {
    private final UUID owner;
    private final Location location;
    private int fuelAmount;
    private int fuelConsumption;

    public Nexus(UUID owner, Location location, int fuelAmount, int fuelConsumption) {
        this.owner = owner;
        this.location = location;
        this.fuelAmount = fuelAmount;
        this.fuelConsumption = fuelConsumption;
    }

    public String getId() {
        return location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
    }

    public Location getLocation() {
        return location.clone();
    }

    public UUID getOwner() {
        return owner;
    }

    public int getFuelAmount() {
        return fuelAmount;
    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(int fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public void setFuelAmount(int fuelAmount) {
        this.fuelAmount = fuelAmount;
    }

    public void saveToYaml(YamlManager yaml) {
        String path = "nexuses." + getId();
        String world = location.getWorld() == null ? "world" : location.getWorld().getName();

        yaml.set(path + ".owner", owner.toString());
        yaml.set(path + ".x", location.getBlockX());
        yaml.set(path + ".y", location.getBlockY());
        yaml.set(path + ".z", location.getBlockZ());
        yaml.set(path + ".world", world);
        yaml.set(path + ".fuelAmount", fuelAmount);
        yaml.set(path + ".fuelConsumption", fuelConsumption);
        yaml.save();
    }

    public void removeFromYaml(YamlManager yaml) {
        String path = "nexuses." + getId();

        yaml.remove(path);
    }

    public static Nexus fromYaml(YamlManager yaml, String id) {
        String path = "nexuses." + id;

        if (!yaml.getConfiguration().contains(path)) return null;

        UUID owner = UUID.fromString(yaml.getString(path + ".owner"));

        Location nexusLocation = new Location(
            Bukkit.getWorld(yaml.getString(path + ".world")),
            yaml.getInt(path + ".x"),
            yaml.getInt(path + ".y"),
            yaml.getInt(path + ".z")
        );

        int fuelAmount = yaml.getInt(path + ".fuelAmount");
        int fuelConsumption = yaml.getInt(path + ".fuelConsumption");

        return new Nexus(
            UUID.fromString(yaml.getString(path + ".owner")),
            nexusLocation,
            fuelAmount,
            fuelConsumption == 0 ? 16 : fuelConsumption
        );
    }
}
