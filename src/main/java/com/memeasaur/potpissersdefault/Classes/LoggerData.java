package com.memeasaur.potpissersdefault.Classes;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class LoggerData {
    public final UUID u;
    public final ItemStack[] playerInventory;
    public final float exp;
    public boolean eating = false;
    public BukkitTask task;
    public LoggerData(UUID u, ItemStack[] pi, float exp) {
        this.u = u;
        this.playerInventory = pi;
        this.exp = exp;
    }
}
