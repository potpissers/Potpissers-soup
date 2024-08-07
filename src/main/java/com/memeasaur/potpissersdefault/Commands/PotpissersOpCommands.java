package com.memeasaur.potpissersdefault.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.CombatConstants.ATTACK_SPEED_DEFAULT;
import static com.memeasaur.potpissersdefault.Util.Constants.CombatConstants.ATTACK_SPEED_STRING_MAP;

public class PotpissersOpCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            switch (command.getName().toLowerCase()) {
                case "heal" -> {
                    if (args.length != 1) {
                        p.sendMessage("invalid (args)");
                        return true;
                    }
                    Player pArg = Bukkit.getPlayer(args[0]);
                    if (pArg == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    } else {
                        pArg.setHealth(20);
                        pArg.setFoodLevel(20);
                        pArg.sendMessage("healed");
                        p.sendMessage("healed " + pArg.getName());
                        return true;
                    }
                }
                case "feed" -> {}
                case "freeze" -> {
                    if (args.length != 1) {
                        p.sendMessage("?");
                        return true;
                    }
                    Player pArg = Bukkit.getPlayer(args[0]);
                    if (pArg == null) {
                        p.sendMessage("?");
                        return true;
                    }
                    playerDataMap.get(pArg.getUniqueId()).frozen = true;
                    pArg.sendMessage("you've been frozen");
                }
                case "unfreeze" -> {
                    if (args.length != 1) {
                        p.sendMessage("?");
                        return true;
                    }
                    Player pArg = Bukkit.getPlayer(args[0]);
                    if (pArg == null) {
                        p.sendMessage("?");
                        return true;
                    }
                    playerDataMap.get(pArg.getUniqueId()).frozen = false;
                    pArg.sendMessage("you've been unfrozen");
                }
                case "setdefaultattackspeed" -> {
                    if (args.length != 1) {
                        p.sendMessage("?");
                        return true;
                    }
                    if (!ATTACK_SPEED_STRING_MAP.containsKey(args[0])) {
                        p.sendMessage("?");
                        return true;
                    }
                    ATTACK_SPEED_DEFAULT = ATTACK_SPEED_STRING_MAP.get(args[0]);
                    p.sendMessage("done");
                    return true;
                }
            }
        }
        return true;
    }
}

