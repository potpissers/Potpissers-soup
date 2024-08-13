package com.memeasaur.potpissersdefault;

import com.memeasaur.potpissersdefault.Classes.*;
import com.memeasaur.potpissersdefault.Commands.Claims.ClaimsOpCommands;
import com.memeasaur.potpissersdefault.Commands.CubecoreSignOpCommand;
import com.memeasaur.potpissersdefault.Commands.Duels.DuelsCommands;
import com.memeasaur.potpissersdefault.Commands.Duels.DuelsTabCompleter;
import com.memeasaur.potpissersdefault.Commands.GrappleOpCommand;
import com.memeasaur.potpissersdefault.Commands.Kits.KitsCommands;
import com.memeasaur.potpissersdefault.Commands.Kits.KitsOpCommands;
import com.memeasaur.potpissersdefault.Commands.Kits.KitsTabExecutor;
import com.memeasaur.potpissersdefault.Commands.LogoutCommand;
import com.memeasaur.potpissersdefault.Commands.Party.PartyCommand;
import com.memeasaur.potpissersdefault.Commands.Party.PartyTabCompleter;
import com.memeasaur.potpissersdefault.Commands.PotpissersOpCommands;
import com.memeasaur.potpissersdefault.Listeners.*;
import com.memeasaur.potpissersdefault.Commands.Warps.WarpsCommands;
import com.memeasaur.potpissersdefault.Commands.Warps.WarpsOpCommands;
import com.memeasaur.potpissersdefault.Commands.Warps.WarpsTabExecutor;
import com.memeasaur.potpissersdefault.Listeners.EntityDamageListener;
import com.memeasaur.potpissersdefault.Listeners.PlayerLaunchProjectileListener;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Piglin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.*;

import static com.memeasaur.potpissersdefault.Util.Constants.SerializationConstants.*;
import static com.memeasaur.potpissersdefault.Util.Methods.SerializationUtils.deserializeBukkit;
import static com.memeasaur.potpissersdefault.Util.Methods.SerializationUtils.readBinary;

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

    // Claims start
    public static ClaimsMap claims = new ClaimsMap();
    public static HashMap<LocationCoordinate, int[]> claimsBlockMap = new HashMap<>();
    public static Location claimPos1;
    public static Location claimPos2;
    public static HashMap<String, Arena> arenas = new HashMap<>();
    public static ArrayList<Party> teams = new ArrayList<>();
    public static LinkedHashMap<UUID, DuelData> duelQueue = new LinkedHashMap<>();
    public static LinkedHashMap<Party, String> teamfightQueue = new LinkedHashMap<>();
    public static BukkitTask saveClaimsTask;
    public static BukkitTask saveArenasTask;
    public static int worldBorderRadius = 1500;
    public static HashMap<String, LocationCoordinate> publicWarps = new HashMap<>();
    public static HashMap<String, LocationCoordinate> privateWarps = new HashMap<>();
    public static HashMap<String, ItemStack[]> defaultKits= new HashMap<>();
    public static BukkitTask savePublicWarpsTask;
    public static BukkitTask savePrivateWarpsTask;
    public static BukkitTask saveDefaultKitTask;
    public static final Object lock = new Object();
    // Claims end
    // Grapple start
    public static final WeakHashSet<FishHook> grappleHooks = new WeakHashSet<>();
    // Grapple end
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
        pm.registerEvents(new PlayerDeathListener(this), this); // Dueling
        pm.registerEvents(new AreaEffectCloudApplyListener(this), this);
        getCommand("getcombattags").setExecutor(potpissersOpCommands);
        // Logout end
        // Claims start
        ClaimsOpCommands claimsOpCommands = new ClaimsOpCommands(this);
        for (String commandName : List.of("claimspawn", "unclaim", "claimarena", "addarena", "removearena", "arenaset1", "arenaset2", "claimset1", "claimset2"))
            getCommand(commandName).setExecutor(claimsOpCommands);

        DuelsCommands duelsCommands = new DuelsCommands();
        DuelsTabCompleter duelsTabCompleter = new DuelsTabCompleter();
        for (String commandName : List.of("duel", "accept", "anon")) {
            getCommand(commandName).setExecutor(duelsCommands);
            getCommand(commandName).setTabCompleter(duelsTabCompleter);
        }
        getCommand("leavequeue").setExecutor(duelsCommands);

        KitsCommands kitsCommands = new KitsCommands();
        for (String commandName : List.of("save", "removeall"))
            getCommand(commandName).setExecutor(kitsCommands);
        KitsTabExecutor kitsTabExecutor = new KitsTabExecutor();
        for (String commandName : List.of("load", "remove", "loaddefault")) {
            getCommand(commandName).setExecutor(kitsCommands);
            getCommand(commandName).setTabCompleter(kitsTabExecutor);
        }
        KitsOpCommands kitsOpCommands = new KitsOpCommands(this);
        for (String commandName : List.of("savedefault", "cubecorechest"))
            getCommand(commandName).setExecutor(kitsOpCommands);

        getCommand("party").setExecutor(new PartyCommand(this));
        getCommand("party").setTabCompleter(new PartyTabCompleter());

        WarpsCommands warpsCommands = new WarpsCommands(this);
        getCommand("spawn").setExecutor(warpsCommands);
        WarpsTabExecutor warpsTabExecutor = new WarpsTabExecutor();
        for (String commandName : List.of("warp", "tpa", "tpahere", "tpaccept", "tpahereaccept")) {
            getCommand(commandName).setExecutor(warpsCommands);
            getCommand(commandName).setTabCompleter(warpsTabExecutor);
        }
        WarpsOpCommands warpsOpCommands = new WarpsOpCommands(this);
        for (String commandName : List.of("addwarp", "removewarp", "addprivatewarp", "removeprivatewarp"))
            getCommand(commandName).setExecutor(warpsOpCommands);

        pm.registerEvents(new ClaimListeners(this), this);
        pm.registerEvents(new InventoryClickListener(), this);
        pm.registerEvents(new BlockPlaceListener(this), this);
        pm.registerEvents(new BlockBreakListener(this), this);
        pm.registerEvents(new BlockDamageListener(), this);
        pm.registerEvents(new PlayerOpenSignListener(), this);
        pm.registerEvents(new PlayerInteractListener(), this);
        // Claims end
        // Grapple start
        pm.registerEvents(new ProjectileHitListener(this), this);
        pm.registerEvents(new PlayerFishListener(this), this);
        GrappleOpCommand grappleOpCommand = new GrappleOpCommand();
        getCommand("getgrapple").setExecutor(grappleOpCommand);
        // Grapple end

        // Cubecore sign start
        getCommand("cubecoresign").setExecutor(new CubecoreSignOpCommand());
        // Cubecore sign end

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
        // Claims start
        Plugin plugin = this;
        if (Files.exists(Paths.get(DATA_CLAIMS_MAP))) {
            isServerReady = false;
            taskTracker++;
            new BukkitRunnable() {
                @Override
                public void run() {
                    ClaimsMap tempClaims = (ClaimsMap) readBinary(DATA_CLAIMS_MAP, claims);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            claims = tempClaims;
                            taskTracker--;
                            if (taskTracker == 0)
                                isServerReady = true;
                        }
                    }.runTask(plugin);
                }
            }.runTaskAsynchronously(this);
        }
        if (Files.exists(Paths.get(DATA_DEFAULT_KITS))) {
            isServerReady = false;
            taskTracker++;
            new BukkitRunnable() {
                @Override
                public void run() {
                    HashMap<String, ItemStack[]> tempDefaultKits = (HashMap<String, ItemStack[]>) deserializeBukkit((byte[]) readBinary(DATA_DEFAULT_KITS, new byte[0]), defaultKits);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            defaultKits = tempDefaultKits;
                            taskTracker--;
                            if (taskTracker == 0)
                                isServerReady = true;
                        }
                    }.runTask(plugin);
                }
            }.runTaskAsynchronously(this);
        }
        if (Files.exists(Paths.get(DATA_PUBLIC_WARPS))) {
            isServerReady = false;
            taskTracker++;
            new BukkitRunnable() {
                @Override
                public void run() {
                    HashMap<String, LocationCoordinate> tempPublicWarps = (HashMap<String, LocationCoordinate>) readBinary(DATA_PUBLIC_WARPS, publicWarps);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            publicWarps = tempPublicWarps;
                            taskTracker--;
                            if (taskTracker == 0)
                                isServerReady = true;
                        }
                    }.runTask(plugin);
                }
            }.runTaskAsynchronously(this);
        }
        if (Files.exists(Paths.get(DATA_PRIVATE_WARPS))) {
            isServerReady = false;
            taskTracker++;
            new BukkitRunnable() {
                @Override
                public void run() {
                    HashMap<String, LocationCoordinate> tempPrivateWarps = (HashMap<String, LocationCoordinate>) readBinary(DATA_PRIVATE_WARPS, privateWarps);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            privateWarps = tempPrivateWarps;
                            taskTracker--;
                            if (taskTracker == 0)
                                isServerReady = true;
                        }
                    }.runTask(plugin);
                }
            }.runTaskAsynchronously(this);
        }
        if (Files.exists(Paths.get(DATA_ARENAS))) {
            isServerReady = false;
            taskTracker++;
            new BukkitRunnable() {
                @Override
                public void run() {
                    HashMap<String, Arena> tempArenas = (HashMap<String, Arena>) readBinary(DATA_ARENAS, arenas);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            arenas = tempArenas;
                            taskTracker--;
                            if (taskTracker == 0)
                                isServerReady = true;
                        }
                    }.runTask(plugin);
                }
            }.runTaskAsynchronously(this);
        }
        // Claims end
    }
    void doDataSerialization() {
        // Claims start
        synchronized (lock) {
            while (saveClaimsTask != null && !saveClaimsTask.isCancelled()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (savePublicWarpsTask != null && !savePublicWarpsTask.isCancelled()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (savePrivateWarpsTask != null && !savePrivateWarpsTask.isCancelled()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (saveDefaultKitTask != null && !saveDefaultKitTask.isCancelled()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (saveArenasTask != null && !saveArenasTask.isCancelled()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // Claims end
    }
    // Logout + tag end
}
