package com.memeasaur.potpissersdefault.Classes;

import org.bukkit.Location;
import org.bukkit.entity.Piglin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.WILDERNESS_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.PartyConstants.NULL_PARTY;

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
    // Duels/claims start
    public String currentClaim = WILDERNESS_CLAIM;
    public int killStreak;
    public transient boolean isQueued;
    public transient UUID isDueling;
    public WeakReference<Party> partyReference = NULL_PARTY;
    public transient HashMap<String, ItemStack[]> savedKits = new HashMap<>();
    public WeakHashMap<UUID, DuelData> duelRequests = new WeakHashMap<>();
    public HashSet<UUID> tpaRequests = new HashSet<>();
    public HashSet<UUID> tpaHereRequests = new HashSet<>();
    public boolean isTpaReady;
    public int tpaReadyTimer; public transient BukkitTask tpaReadyTask;
    public HashMap<Location, AtomicInteger> teleportTimers = new HashMap<>(); public transient HashSet<BukkitTask> teleportTasks = new HashSet<>(); // mutableInteger
    // Duels/claims end
    // Movement cd start
    public int[] movementCd = new int[1]; public transient BukkitTask movementCdTask;
    public int[] movementSpamBuffer = new int[1];
    // Movement cd end
    public PlayerData(UUID u) {
        // Default start
        this.uuid = u;
        // Default end
    }
}
