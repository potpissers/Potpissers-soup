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
    // Logout + tag start
    public int[] logoutTimer = new int[1];
    // Logout + tag end
    public LoggerData(UUID u, ItemStack[] pi, float exp) {
        this.u = u;
        this.playerInventory = pi;
        this.exp = exp;
    }
}
