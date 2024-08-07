package com.memeasaur.potpissersdefault;

import com.memeasaur.potpissersdefault.Classes.LoggerData;
import com.memeasaur.potpissersdefault.Classes.LoggerUpdate;
import com.memeasaur.potpissersdefault.Classes.PlayerData;
import com.memeasaur.potpissersdefault.Commands.PotpissersOpCommands;
import com.memeasaur.potpissersdefault.Listeners.*;
import org.bukkit.entity.Piglin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

public final class PotpissersDefault extends JavaPlugin {
    public static HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();
    public static final WeakHashMap<Piglin, LoggerData> loggerDataMap = new WeakHashMap<>();
    public static final HashMap<UUID, LoggerUpdate> loggerUpdateMap = new HashMap<>();
    public static BukkitTask savePlayerDataMapTask;
    public static BukkitTask saveLoggerDataMapTask;
    public static BukkitTask saveLoggerUpdateMapTask;
    byte taskTracker = 0;
    public static boolean isServerReady = false; // maybe somebody could sneak in with this idk

    @Override
    public void onEnable() {
        // Default start
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EntityDamageByEntityListener(this), this);
        pm.registerEvents(new EntityDamageListener(this), this);
        pm.registerEvents(new EntityDeathListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerMoveListener(), this);
        pm.registerEvents(new PlayerQuitListener(this), this);

        PotpissersOpCommands potpissersOpCommands = new PotpissersOpCommands();
        for (String commandName : List.of("heal", "feed", "freeze", "unfreeze", "setdefaultattackspeed"))
            getCommand(commandName).setExecutor(potpissersOpCommands);
        // Default end

        // Default start
        if (taskTracker == 0)
            isServerReady = true;
        // Default end
    }

    @Override
    public void onDisable() {}
}
