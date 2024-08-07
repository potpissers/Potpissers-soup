package com.memeasaur.potpissersdefault.Classes;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class LoggerUpdate { // implements configSerialize
    public final Location location;
    public final double health;
    public final ItemStack[] inventory;
    public LoggerUpdate(Location location, double health, ItemStack[] pi) {
        this.location = location;
        this.health = health;
        this.inventory = pi;
    }
}