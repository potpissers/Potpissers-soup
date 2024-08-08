package com.memeasaur.potpissersdefault.Classes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.*;

import static com.memeasaur.potpissersdefault.Util.Constants.PartyConstants.COLEADER_STRING;

public class Party implements ConfigurationSerializable { // implement methods
    public final WeakReference<Party> selfWeakReference;
    public String name;
    public UUID leader;
    public HashMap<UUID, String> roster = new HashMap<>();
    public transient HashSet<UUID> invites = new HashSet<>();
    //    public transient WeakHashMap<Player, UUID> requests = new WeakHashMap<>(); // just make this a message for the faction
    public WeakHashSet<Party> allies = new WeakHashSet<>();
    public WeakHashSet<Party> allianceRequests = new WeakHashSet<>();
    public WeakHashSet<Party> enemies = new WeakHashSet<>();
    public transient Party focus;
    public transient Location rally;
    public transient BukkitTask rallyTimer;
    public transient Location warp;
    public transient BukkitTask warpTask;
    public transient WeakHashMap<Party, DuelData> partyDuelRequests = new WeakHashMap<>();
    public transient boolean isQueued;
    public transient Party isDueling;
    public transient HashSet<UUID> isAliveDueling = new HashSet<>();

    public Party(UUID u) {
        doSetLeader(u);
        this.selfWeakReference = new WeakReference<>(this);
    }
    public void doSetLeader(UUID u) {
        this.leader = u;
        this.name = Bukkit.getOfflinePlayer(u).getName() + "'s party";
        roster.put(u, COLEADER_STRING);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of();
    }
}
