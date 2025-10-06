package com.swondi.beaconomics.models;

import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class DefenseBlock {
    private final Location location;
    private final Material type;
    private int health;

    public double getFuelConsumption() {
        return Constants.DATA_DEFENSE_BLOCKS.get(type).fuelConsumption();
    }

    public String getId() {
        return location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
    }

    public Location getLocation() {
        return location;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Material getType() {
        return type;
    }

    public DefenseBlock(Location location, Material type, int health) {
        this.location = location;
        this.type = type;
        this.health = health;
    }
    public DefenseBlock(Location location, Material type) {
        this.location = location;
        this.type = type;

        // Default health
        this.health = Constants.DATA_DEFENSE_BLOCKS.get(type).health();
    }


    public void saveToYaml(YamlConfiguration yaml) {
        String path = "temporary." + getId();

        yaml.set(path + ".type", type);
        yaml.set(path + ".health", health);
    }

    public static DefenseBlock fromYaml(YamlConfiguration yaml, Location location) {
        String path = "temporary." + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();

        if (!yaml.contains(path)) return null;

        int health = yaml.getInt(path + ".health");
        String type = yaml.getString(path + ".type");

        return new DefenseBlock(location, Material.valueOf(type), health);
    }
}
