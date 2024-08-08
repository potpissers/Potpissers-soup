package com.memeasaur.potpissersdefault.Classes;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.io.Serializable;

public class ClaimCoordinate implements Serializable {
    final public int x;
    final public int z;
    public ClaimCoordinate(int x, int z) {
        this.x = x;
        this.z = z;
    }
    public ClaimCoordinate(Block block) {
        this.x = block.getX();
        this.z = block.getZ();
    }
    public ClaimCoordinate(Location location) {
        this.x = location.getBlockX();
        this.z = location.getBlockZ();
    }
    public ClaimCoordinate(String name, String x, String z) {
        this.x = Integer.parseInt(x);
        this.z = Integer.parseInt(z);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClaimCoordinate that = (ClaimCoordinate) o;
        return x == that.x && z == that.z;
    }
    @Override
    public int hashCode() {
        return 31 * (31 * x + z);
//        int result = 1;
//        result = 31 * result + x;
//        result = 31 * result + z;
//        return result;
    }
}
