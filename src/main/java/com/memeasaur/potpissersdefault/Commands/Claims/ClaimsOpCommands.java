package com.memeasaur.potpissersdefault.Commands.Claims;

import com.memeasaur.potpissersdefault.Classes.Arena;
import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.Classes.LocationCoordinate;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.*;
import static com.memeasaur.potpissersdefault.Util.Constants.SerializationConstants.DATA_ARENAS;
import static com.memeasaur.potpissersdefault.Util.Methods.ClaimUtils.*;
import static com.memeasaur.potpissersdefault.Util.Methods.SerializationUtils.writeBinary;

public class ClaimsOpCommands implements CommandExecutor {
    private final PotpissersDefault plugin;
    public ClaimsOpCommands(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            switch (command.getName().toLowerCase()) {
                case "claimspawn" -> {
                    if (strings.length != 0) {
                        p.sendMessage("?");
                        return true;
                    }
                    return doClaimArea(p, SPAWN_CLAIM, plugin);
                }
                case "unclaim" -> {
                    if (strings.length != 0) {
                        p.sendMessage("?");
                        return true;
                    }
                    String claim = claims.getWorld(p.getWorld().getName()).getOrDefault(new ClaimCoordinate(p.getLocation()), WILDERNESS_CLAIM);
                    if (claim.equals(WILDERNESS_CLAIM)) {
                        p.sendMessage("?");
                        return true;
                    }
                    return doUnclaimArea(p, claim, plugin);
                }
                case "claimarena" -> {
                    if (strings.length != 1) {
                        p.sendMessage("?");
                        return true;
                    }
                    Arena arena = arenas.getOrDefault(strings[0], null);
                    if (arena == null) {
                        p.sendMessage("?");
                        return true;
                    }
                    return doClaimArea(p, arena.name, plugin);
                }
                case "addarena" -> {
                    if (strings.length != 1) {
                        p.sendMessage("?");
                        return true;
                    }
                    if (arenas.containsKey(strings[0])) {
                        p.sendMessage("?");
                        return true;
                    }
                    arenas.put(strings[0], new Arena(strings[0]));
                    handleSaveArenas();
                    p.sendMessage(strings[0] + " created");
                    return true;
                }
                case "removearena" -> {
                    if (strings.length != 1) {
                        p.sendMessage("?");
                        return true;
                    }
                    Arena arena = arenas.get(strings[0]);
                    if (arena == null) {
                        p.sendMessage("?");
                        return true;
                    }
                    doUnclaimArea(p, arena.name, plugin);
                    arenas.remove(strings[0]);
                    handleSaveArenas();
                    p.sendMessage(strings[0] + " removed + unclaimed");
                    return true;
                }
                case "arenaset1" -> {
                    Arena arena = arenas.getOrDefault(strings[0], null);
                    if (doArenaArgLocationCheck(strings, p, arena))
                        return true;
                    arena.warp1 = new LocationCoordinate(p.getLocation());
                    handleSaveArenas();
                    p.sendMessage("set 1");
                    return true;
                }
                case "arenaset2" -> { // create method
                    Arena arena = arenas.getOrDefault(strings[0], null);
                    if (doArenaArgLocationCheck(strings, p, arena))
                        return true;
                    arena.warp2 = new LocationCoordinate(p.getLocation());
                    handleSaveArenas();
                    p.sendMessage("set 2");
                    return true;
                }
                case "claimset1" -> {
                    return doClaimSet(strings, p, true);
                }
                case "claimset2" -> {
                    return doClaimSet(strings, p, false);
                }
            }
        }
        return false;
    }
    void handleSaveArenas() {
        if (saveArenasTask == null || saveArenasTask.isCancelled())
            saveArenasTask = getSaveArenasTask();
        else {
            saveArenasTask.cancel();
            saveArenasTask = getSaveArenasTask();
        }
    }
    BukkitTask getSaveArenasTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                writeBinary(DATA_ARENAS, arenas);
                saveArenasTask = null;
            }
        }.runTaskAsynchronously(plugin);
    }
    boolean doArenaArgLocationCheck(String[] args, Player p, Arena arena) {
        if (args.length != 1) {
            p.sendMessage("invalid (args)");
            return true;
        }
        if (arena == null) {
            p.sendMessage("?");
            return true;
        }
        ClaimCoordinate lc = new ClaimCoordinate(p.getLocation().getBlock());
        if (!claims.getWorld(p.getWorld().getName()).getOrDefault(lc, WILDERNESS_CLAIM).equals(arena.name)) {
            p.sendMessage("invalid (claim)");
            return true;
        }
        return false;
    }
}
