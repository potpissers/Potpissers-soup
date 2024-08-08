package com.memeasaur.potpissersdefault.Classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.io.Serializable;
import java.util.Objects;

public class LocationCoordinate implements Serializable {
    public final String worldName;
    public final int x;
    public final int y;
    public final int z;
    public LocationCoordinate(String name, int x, int y, int z) {
        this.worldName = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public LocationCoordinate(Location location) {
        this.worldName = location.getWorld().getName();
        this.x = location.blockX();
        this.y = location.blockY();
        this.z = location.blockZ();
    }
    public LocationCoordinate(Block block) {
        this.worldName = block.getWorld().getName();
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
    }
    public Location toLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationCoordinate that = (LocationCoordinate) o;
        return x == that.x && y == that.y && z == that.z && Objects.equals(worldName, that.worldName);
    }
    @Override
    public int hashCode() {
        return 31 * (31 * (31 * worldName.hashCode() + x) + y) + z;
//        int result = worldName.hashCode();
//        result = result * 31 + x;
//        result = result * 31 + y;
//        result = result * 31 + z;
//        return result;
    }
}
