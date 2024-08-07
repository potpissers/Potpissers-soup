package com.memeasaur.potpissersdefault.Classes;

import org.bukkit.entity.Piglin;

import java.util.UUID;

public class PlayerData { // implements configSerial
    // Default start
    public final UUID uuid;
    public boolean frozen = false;
    public transient Piglin logger = null;
    // Default end
    public PlayerData(UUID u) {
        // Default start
        this.uuid = u;
        // Default end
    }
}
