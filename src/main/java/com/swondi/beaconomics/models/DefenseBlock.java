package com.swondi.beaconomics.models;

import com.swondi.beaconomics.managers.YamlManager;
import com.swondi.beaconomics.utils.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

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


    public void saveToYaml(YamlManager yaml) {
        String path = "defense." + getId();

        yaml.set(path + ".type", type.name());
        yaml.set(path + ".health", health);
    }

    public static DefenseBlock fromYaml(YamlManager yaml, String id) {
        String path = "defense." + id;

        if (!yaml.getConfiguration().contains(path)) return null;

        int health = yaml.getInt(path + ".health");
        String type = yaml.getString(path + ".type");

        String[] parts = id.split("_");

        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        int z = Integer.parseInt(parts[2]);

        Location location = new Location(Bukkit.getWorld("world"),x, y, z);

        return new DefenseBlock(location, Material.valueOf(type), health);
    }
}
