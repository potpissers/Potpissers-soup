package com.memeasaur.potpissersdefault.Commands.Warps;

import com.memeasaur.potpissersdefault.Classes.LocationCoordinate;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.SerializationConstants.DATA_PRIVATE_WARPS;
import static com.memeasaur.potpissersdefault.Util.Constants.SerializationConstants.DATA_PUBLIC_WARPS;
import static com.memeasaur.potpissersdefault.Util.Methods.SerializationUtils.serializeBukkitByteArray;
import static com.memeasaur.potpissersdefault.Util.Methods.SerializationUtils.writeBinary;

public class WarpsOpCommands implements CommandExecutor {
    private final PotpissersDefault plugin;
    public WarpsOpCommands(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            switch (command.getName().toLowerCase()) {
                case "addwarp", "setwarp", "createwarp" -> {
                    if (strings.length != 1) {
                        p.sendMessage("?");
                        return true;
                    }
                    if (publicWarps.containsKey(strings[0])) {
                        p.sendMessage("?");
                        return true;
                    }
                    publicWarps.put(strings[0], new LocationCoordinate(p.getLocation()));
                    p.sendMessage(strings[0] + " warp set");
                    handleSavePublicWarps();
                    return true;
                }
                case "removewarp", "unsetwarp", "deletewarp" -> {
                    if (strings.length != 1) {
                        p.sendMessage("?");
                        return true;
                    }
                    if (!publicWarps.containsKey(strings[0])) {
                        p.sendMessage("?");
                        return true;
                    }
                    publicWarps.remove(strings[0]);
                    p.sendMessage(strings[0] + " warp removed");
                    handleSavePublicWarps();
                    return true;
                }
                case "addprivatewarp" -> {
                    if (strings.length != 1) {
                        p.sendMessage("?");
                        return true;
                    }
                    if (privateWarps.containsKey(strings[0])) {
                        p.sendMessage("?");
                        return true;
                    }
                    privateWarps.put(strings[0], new LocationCoordinate(p.getLocation()));
                    p.sendMessage(strings[0] + " warp set");
                    handleSavePrivateWarps();
                    return true;
                }
                case "removeprivatewarp" -> {
                    if (strings.length != 1) {
                        p.sendMessage("?");
                        return true;
                    }
                    if (!privateWarps.containsKey(strings[0])) {
                        p.sendMessage("?");
                        return true;
                    }
                    privateWarps.remove(strings[0]);
                    p.sendMessage(strings[0] + " warp removed");
                    handleSavePrivateWarps();
                    return true;
                }
            }
        }
        return false;
    }
    void handleSavePublicWarps() {
        if (savePublicWarpsTask == null || savePublicWarpsTask.isCancelled())
            savePublicWarpsTask = getSavePublicWarpsTask();
        else {
            savePublicWarpsTask.cancel();
            savePublicWarpsTask = getSavePublicWarpsTask();
        }
    }
    BukkitTask getSavePublicWarpsTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                writeBinary(DATA_PUBLIC_WARPS, publicWarps);
                savePublicWarpsTask = null;
                synchronized (lock) {
                    lock.notify();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
    void handleSavePrivateWarps() {
        if (savePrivateWarpsTask == null || savePrivateWarpsTask.isCancelled())
            savePrivateWarpsTask = getSavePrivateWarpsTask();
        else {
            savePrivateWarpsTask.cancel();
            savePrivateWarpsTask = getSavePrivateWarpsTask();
        }
    }
    BukkitTask getSavePrivateWarpsTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                writeBinary(DATA_PRIVATE_WARPS, privateWarps);
                savePrivateWarpsTask = null;
                synchronized (lock) {
                    lock.notify();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
