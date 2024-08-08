package com.memeasaur.potpissersdefault;

import com.memeasaur.potpissersdefault.Classes.LoggerData;
import com.memeasaur.potpissersdefault.Classes.LoggerUpdate;
import com.memeasaur.potpissersdefault.Classes.PlayerData;
import com.memeasaur.potpissersdefault.Commands.LogoutCommand;
import com.memeasaur.potpissersdefault.Commands.PotpissersOpCommands;
import com.memeasaur.potpissersdefault.Listeners.*;
import com.memeasaur.potpissersdefault.Listeners.EntityDamageListener;
import com.memeasaur.potpissersdefault.Listeners.PlayerLaunchProjectileListener;
import org.bukkit.entity.Piglin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class PotpissersDefault extends JavaPlugin {
    public static HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();
    public static final WeakHashMap<Piglin, LoggerData> loggerDataMap = new WeakHashMap<>();
    public static final HashMap<UUID, LoggerUpdate> loggerUpdateMap = new HashMap<>();
    public static BukkitTask savePlayerDataMapTask;
    public static BukkitTask saveLoggerDataMapTask;
    public static BukkitTask saveLoggerUpdateMapTask;
    byte taskTracker = 0;
    public static boolean isServerReady = false;
    // Combat tag start
    public static HashSet<UUID> currentlyTaggedPlayers = new HashSet<>();
    // Combat tag end

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
        // Potion start
        pm.registerEvents(new PlayerLaunchProjectileListener(this), this);
        pm.registerEvents(new EntityShootBowListener(this), this);
        pm.registerEvents(new PotionSplashListener(this), this); // combat-tag
        // Potion end
        // Logout start
        doDataDeserialization();
        LogoutCommand logoutCommand = new LogoutCommand(this);
        getCommand("logout").setExecutor(logoutCommand);
        pm.registerEvents(new LoggerListeners(), this);
        pm.registerEvents(new PlayerDeathListener(), this);
        pm.registerEvents(new AreaEffectCloudApplyListener(this), this);
        getCommand("getcombattags").setExecutor(potpissersOpCommands);
        // Logout end

        // Default start
        if (taskTracker == 0)
            isServerReady = true;
        // Default end
    }

    @Override
    public void onDisable() {
        // Logout + tag start
        doDataSerialization();
        // Logout + tag end
    }
    // Logout + tag start
    void doDataDeserialization() {

    }
    void doDataSerialization() {
    }
    // Logout + tag end
}
