package com.memeasaur.potpissersdefault.Commands.Kits;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.*;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.KEY_CUBECORE_CHEST;
import static com.memeasaur.potpissersdefault.Util.Constants.SerializationConstants.DATA_DEFAULT_KITS;
import static com.memeasaur.potpissersdefault.Util.Methods.SerializationUtils.serializeBukkitByteArray;
import static com.memeasaur.potpissersdefault.Util.Methods.SerializationUtils.writeBinary;

public class KitsOpCommands implements CommandExecutor {
    private final PotpissersDefault plugin;
    public KitsOpCommands(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            switch (command.getName().toLowerCase()) {
                case "savedefault" -> {
                    if (strings.length > 1) {
                        p.sendMessage("?");
                        return true;
                    }
                    if (strings.length == 0) {
                        PlayerInventory pInv = p.getInventory();
                        ItemStack[] pi = pInv.getContents();
                        defaultKits.put("default", pi);
                        pInv.setContents(pi);
                        p.sendMessage("saved default kit");
                        handleSaveDefaultKits();
                        return true;
                    } else {
                        PlayerInventory pInv = p.getInventory();
                        ItemStack[] pi = pInv.getContents();
                        defaultKits.put(strings[0], pi);
                        pInv.setContents(pi);
                        p.sendMessage("saved default kit " + strings[0]);
                        handleSaveDefaultKits();
                        return true;
                    }
                }
                case "cubecorechest" -> {
                    Block block = p.getTargetBlockExact(4);
                    if (block != null && block.getState() instanceof Chest chest) {
                        PersistentDataContainer pdc = chest.getPersistentDataContainer();
                        if (pdc.has(KEY_CUBECORE_CHEST)) {
                            p.sendMessage("?");
                            return true;
                        }
                        pdc.set(KEY_CUBECORE_CHEST, PersistentDataType.BOOLEAN, Boolean.TRUE);
                        chest.update();
                        p.sendMessage("cubecorechest set");
                        return true;
                    } else {
                        p.sendMessage("?");
                        return true;
                    }
                }
            }
        }
        return false;
    }
    void handleSaveDefaultKits() { // methodize this
        if (saveDefaultKitTask == null || saveDefaultKitTask.isCancelled())
            saveDefaultKitTask = getSaveDefaultKitsTask();
        else {
            saveDefaultKitTask.cancel();
            saveDefaultKitTask = getSaveDefaultKitsTask();
        }
    }
    BukkitTask getSaveDefaultKitsTask() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                writeBinary(DATA_DEFAULT_KITS, serializeBukkitByteArray(defaultKits));
                saveDefaultKitTask = null;
                synchronized (lock) {
                    lock.notify();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
