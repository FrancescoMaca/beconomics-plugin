package com.swondi.beaconomics.models;

import com.swondi.beaconomics.managers.YamlManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class TemporaryBlock {
    private final String id;
    private final Location location;
    private final Material material;
    private final long removalTime;

    public TemporaryBlock(Location location, Material material, long removalTime) {
        this.id = location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
        this.location = location;
        this.material = material;
        this.removalTime = removalTime;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public long getRemovalTime() {
        return removalTime;
    }

    public Material getMaterial() {
        return material;
    }

    public static TemporaryBlock fromYaml(YamlManager yaml, Location location) {
        String path = "tempblocks." + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();

        World world = Bukkit.getWorld(yaml.getString(path + ".world"));
        int x = yaml.getInt(path + ".x");
        int y = yaml.getInt(path + ".y");
        int z = yaml.getInt(path + ".z");
        Material material = Material.getMaterial(yaml.getString(path + ".material"));
        long removalTime = yaml.getConfiguration().getLong(path + ".removalTime");

        return new TemporaryBlock(new Location(world, x, y, z), material, removalTime);
    }

    public void removeFromYaml(YamlManager yaml) {
        yaml.remove("tempblocks." + getId());
        yaml.save();
    }

    public void saveToYaml(YamlManager yaml) {
        String path = "tempblocks." + getId();

        yaml.set(path + ".world", location.getWorld().getName());
        yaml.set(path + ".x", location.getBlockX());
        yaml.set(path + ".y", location.getBlockY());
        yaml.set(path + ".z", location.getBlockZ());
        yaml.set(path + ".material", getMaterial().name());
        yaml.set(path + ".removalTime", getRemovalTime());

        yaml.save();
    }
}
