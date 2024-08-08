package com.memeasaur.potpissersdefault.Commands.Kits;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.Classes.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.SPAWN_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.WILDERNESS_CLAIM;

public class KitsCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            switch (command.getName().toLowerCase()) {
                case "save" -> { // sends to save kit area OR checks contents, which sounds kinda aids
//                    if (strings.length > 1) {
//                        p.sendMessage("Usage: /save or /save (kit name)");
//                        return true;
//                    }
//                    UUID u = p.getUniqueId();
//                    PlayerData data = playerDataMap.get(u);
//                    if (data.savedKits.size() >= SAVED_KIT_LIMIT) { // kit limit variable
//                        p.sendMessage("invalid (kit limit reached)");
//                        return true;
//                    }
//                    PlayerInventory pi = p.getInventory();
//                    ItemStack[] contents = pi.getContents();
//                    if (strings.length == 0) { // GUI
//                        int size = data.savedKits.size();
//                        data.savedKits.put(String.valueOf(size), contents);
//                        p.sendMessage("saved " + size);
//                    } else {
//                        data.savedKits.put(strings[0], contents); // ADD SUPPORT FOR MULTIPLE WORD NAMES
//                        p.sendMessage("saved " + strings[0]);
//                    }
//                    pi.setContents(contents); // update inventory
//                    return true;
                }
                case "load" -> {
//                    String claim = claims.getWorld(p.getWorld().getName()).getOrDefault(new ClaimCoordinate(p.getLocation()), wildernessClaim);
//                    if (!spawnClaims.contains(claim)) {
//                        p.sendMessage("invalid (spawn)");
//                        return true;
//                    }
//                    if (strings.length == 0) {
//                        // GUI
//                        return true;
//                    }
//                    if (strings.length > 1) {
//                        p.sendMessage("Usage: /save or /save (kit name)");
//                        return true;
//                    }
//                    UUID u = p.getUniqueId();
//                    PlayerData data = playerDataMap.get(u);
//                    if (!data.savedKits.containsKey(strings[0])) {
//                        p.sendMessage("Invalid (name)");
//                        return true;
//                    }
//                    p.getInventory().setContents(data.savedKits.get(strings[0]));
//                    p.sendMessage("loaded " + strings[0]);
//                    return true;
                }
                case "loaddefault" -> {
                    String claim = playerDataMap.get(p.getUniqueId()).currentClaim;
                    if (!claim.equals(SPAWN_CLAIM) && !p.getGameMode().equals(GameMode.CREATIVE)) {
                        p.sendMessage("invalid (spawn)");
                        return true;
                    }
                    if (strings.length > 1) {
                        p.sendMessage("invalid (strings)");
                        return true;
                    }
                    String kitName;
                    if (strings.length == 1) {
                        if (!defaultKits.containsKey(strings[0])) {
                            p.sendMessage("invalid (strings)");
                            return true;
                        }
                        else
                            kitName = strings[0];
                    }
                    else
                        kitName = "default";
                    p.getInventory().setContents(defaultKits.get(kitName));
                    p.sendMessage("loaded " + kitName + " kit");
                    return true;
                }
                case "remove" -> {
                    if (strings.length == 0) {
                        //gui
                        return true;
                    }
                    if (strings.length >= 2) {
                        p.sendMessage("usage: /save or /save (kit name)");
                        return true;
                    }
                    UUID u = p.getUniqueId();
                    PlayerData data = playerDataMap.get(u);
                    if (!data.savedKits.containsKey(strings[0])) {
                        p.sendMessage("invalid (name)");
                        return true;
                    } else {
                        data.savedKits.remove(strings[0]);
                        p.sendMessage("removed " + strings[0]);
                        return true;
                    }
                }
                case "removeall" -> {
                    if (strings.length != 0) {
                        p.sendMessage("usage: ./removeall");
                        return true;
                    } else {
                        playerDataMap.get(p.getUniqueId()).savedKits.clear();
                        p.sendMessage("removed: (all)");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
