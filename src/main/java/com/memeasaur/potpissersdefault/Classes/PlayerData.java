package com.memeasaur.potpissersdefault.Classes;

import org.bukkit.entity.Piglin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class PlayerData { // implements configSerial
    // Default start
    public final UUID uuid;
    public boolean frozen = false;
    public transient Piglin logger = null;
    // Default end
    // Tag + logout start
    public transient boolean canSafeLog;
    public int[] combatTag = new int[1]; //public transient BukkitTask combatTagTask;
    public int[] logoutTimer = new int[1]; public transient BukkitTask logoutTask;
    // Tag + logout end
    public PlayerData(UUID u) {
        // Default start
        this.uuid = u;
        // Default end
    }
}
