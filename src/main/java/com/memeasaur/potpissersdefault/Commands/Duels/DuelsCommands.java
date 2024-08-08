package com.memeasaur.potpissersdefault.Commands.Duels;

import com.memeasaur.potpissersdefault.Classes.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.UUID;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.SPAWN_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.WILDERNESS_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.CombatConstants.ATTACK_SPEED_DEFAULT;
import static com.memeasaur.potpissersdefault.Util.Constants.DuelsConstants.CPS_STRING_MAP;

public class DuelsCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player p) {
            switch (command.getName().toLowerCase()) {
                case "duel" -> {
                    if (args.length > 3) {
                        p.sendMessage("invalid (args)");
                        return true;
                    }
                    String claim = claims.getWorld(p.getWorld().getName()).getOrDefault(new ClaimCoordinate(p.getLocation()), WILDERNESS_CLAIM);
                    if (!claim.equals(SPAWN_CLAIM)) {
                        p.sendMessage("invalid (spawn)");
                        return true;
                    }
                    if (args.length == 0) {
                        //doQueue or GUI
                        p.sendMessage("placeholder");
                        return true;
                    }
                    Player pArg = Bukkit.getPlayer(args[0]);
                    if (pArg == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    double duelAttackSpeed = ATTACK_SPEED_DEFAULT;
                    String kitName = claim;
                    if (args.length > 1) {
                        if (CPS_STRING_MAP.containsKey(args[1])) {
                            duelAttackSpeed = CPS_STRING_MAP.get(args[1]);
                            if (args.length == 3) {
                                if (defaultKits.containsKey(args[2]))
                                    kitName = args[2];
                                else {
                                    p.sendMessage("invalid (kitName)");
                                    return true;
                                }
                            }
                        } else if (defaultKits.containsKey(args[1])) {
                            kitName = args[1];
                            if (args.length == 3) {
                                if (CPS_STRING_MAP.containsKey(args[2]))
                                    duelAttackSpeed = CPS_STRING_MAP.get(args[2]);
                                else {
                                    p.sendMessage("invalid (cpsName)");
                                    return true;
                                }
                            }
                        } else {
                            p.sendMessage("invalid (args)");
                            return true;
                        }
                    }
                    PlayerData data = playerDataMap.get(p.getUniqueId());
                    PlayerData dataArg = playerDataMap.get(pArg.getUniqueId());
                    Party party = data.partyReference.get();
//                    Party partyArg = dataArg.partyReference.get();
                    if (party != null) {
                        p.sendMessage("invalid (party), use /party duel");
                        return true;
                    }
//                    if (partyArg == null) {
//                        p.sendMessage("invalid (enemy party)");
//                        return true;
//                    }
//                    if (partyArg != null && partyArg.roster.keySet().size() != 1) {
//                        p.sendMessage("invalid (party)");
//                        return true;
//                    }
                    UUID uuidArg = pArg.getUniqueId();
                    if (data.duelRequests.containsKey(uuidArg) && dataArg.isQueued) {
                        if (args.length == 1) {
                            DuelData dd = data.duelRequests.get(uuidArg);
                            return doDuel(p, pArg, data, dataArg, dd.kitName, dd.attackSpeed);
                        }
                    }
                    dataArg.duelRequests.put(p.getUniqueId(), new DuelData(p.getUniqueId(), kitName, duelAttackSpeed));
                    data.isQueued = true;
                    p.sendMessage("duel request sent to " + args[0]);
                    pArg.sendMessage((Component.text(p.getName()).append(Component.text(" has sent you a duel request. type /accept (").append(Component.text(p.getName()).append(Component.text(") or click this message to accept")).clickEvent(ClickEvent.runCommand("/accept " + p.getName()))))));
                }
                case "accept" -> {
                    if (args.length > 1) {
                        p.sendMessage("invalid (args)");
                        return true;
                    }
                    PlayerData data = playerDataMap.get(p.getUniqueId());
                    if (args.length == 0) {
                        boolean hasRequests = false;
                        StringBuilder stringBuilder = new StringBuilder().append("requests: ");
                        for (UUID uuid : data.duelRequests.keySet()) {
                            Player pArg = Bukkit.getPlayer(uuid);
                            if (pArg == null) {
                                data.duelRequests.remove(uuid);
                                break;
                            }
                            stringBuilder.append(pArg.getName()).append(" ");
                            hasRequests = true;
                        }
                        if (hasRequests) p.sendMessage(stringBuilder.toString());
                        else p.sendMessage(stringBuilder.append(" none").toString());
                        return true;
                    }
                    Player pArg = Bukkit.getPlayer(args[0]);
                    if (pArg == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    if (!data.duelRequests.containsKey(pArg.getUniqueId())) {
                        p.sendMessage("invalid (request)");
                        return true;
                    }
                    PlayerData dataArg = playerDataMap.get(pArg.getUniqueId());
                    if (!dataArg.isQueued) {
                        p.sendMessage("invalid (player no longer queued)");
                        return true;
                    }
                    DuelData dd = data.duelRequests.get(pArg.getUniqueId());
                    return doDuel(p, pArg, data, dataArg, dd.kitName, dd.attackSpeed);
                }
                case "anon" -> {
                    if (args.length > 2) {
                        p.sendMessage("invalid (args)");
                        return true;
                    }
                    String claim = claims.getWorld(p.getWorld().getName()).getOrDefault(new ClaimCoordinate(p.getLocation()), WILDERNESS_CLAIM);
                    if (!claim.equals(SPAWN_CLAIM)) {
                        p.sendMessage("invalid (spawn)"); // doSpawnWarp
                        return true;
                    }
                    if (args.length == 0) {
                        if (!duelQueue.isEmpty()) {
                            DuelData dd = null;
                            for (UUID uuid : duelQueue.keySet()) {
                                if (Bukkit.getPlayer(uuid) != null)
                                    dd = duelQueue.get(uuid);
                                else duelQueue.remove(uuid);
                            }
                            if (dd == null) {
                                UUID uuid = p.getUniqueId();
                                duelQueue.putLast(uuid, new DuelData(uuid, claim, ATTACK_SPEED_DEFAULT));
                                p.sendMessage("duel queued with kit " + claim);
                                return true;
                            } else // make duelQueue remove logouts etc
                                doDuel(p, Bukkit.getPlayer(dd.playerUuid), playerDataMap.get(p.getUniqueId()), playerDataMap.get(dd.playerUuid), dd.kitName, dd.attackSpeed);
                        } else {
                            UUID uuid = p.getUniqueId();
                            duelQueue.putLast(uuid, new DuelData(uuid, claim, ATTACK_SPEED_DEFAULT));
                            p.sendMessage("duel queued with kit " + claim);
                        }
                        return true;
                    }
                    String kitName = claim;
                    double attackSpeed = ATTACK_SPEED_DEFAULT;
                    if (defaultKits.containsKey(args[0])) {
                        kitName = args[0];
                        if (args.length == 2) {
                            if (CPS_STRING_MAP.containsKey(args[1])) {
                                attackSpeed = CPS_STRING_MAP.get(args[1]);
                            } else {
                                p.sendMessage("invalid (cpsString)");
                                return true;
                            }
                        }
                    }
                    else if (CPS_STRING_MAP.containsKey(args[0])) {
                        attackSpeed = CPS_STRING_MAP.get(args[0]);
                        if (args.length == 2) {
                            if (defaultKits.containsKey(args[1]))
                                kitName = args[1];
                            else {
                                p.sendMessage("invalid (kitName)");
                                return true;
                            }
                        }
                    }
                    if (!duelQueue.isEmpty()) {
                        for (UUID uuid : duelQueue.keySet()) {
                            Player pArg = Bukkit.getPlayer(uuid);
                            if (pArg == null) {
                                duelQueue.remove(uuid);
                                break;
                            } else {
                                DuelData dd = duelQueue.get(uuid);
                                if (dd.kitName.equals(kitName) && dd.attackSpeed == attackSpeed) {
                                    doDuel(p, pArg, playerDataMap.get(p.getUniqueId()), playerDataMap.get(pArg.getUniqueId()), kitName, attackSpeed);
                                    return true;
                                }
                            }
                        }
                    }
                    else {
                        duelQueue.putLast(p.getUniqueId(), new DuelData(p.getUniqueId(), kitName, attackSpeed));
                        p.sendMessage(attackSpeed + " attackSpeed duel queued with kit " + args[0]);
                        return true;
                    }
                }
                case "leavequeue" -> {
                    playerDataMap.get(p.getUniqueId()).isQueued = false;
                    duelQueue.remove(p.getUniqueId());
                    p.sendMessage("dequeued");
                    return true;
                }
            }
        }
        return true;
    }// V holy puke V, maybe just find/make a custom map that takes this into account
    boolean doDuel(Player p, Player pArg, PlayerData data, PlayerData dataArg, String kit, double attackSpeed) {
        Arena arena = arenas.get((String)arenas.keySet().toArray()[new Random().nextInt(arenas.size())]);
        p.setHealth(20);
        p.clearActivePotionEffects();
        p.setFoodLevel(19);
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(attackSpeed);
        pArg.setHealth(20);
        pArg.clearActivePotionEffects();
        pArg.setFoodLevel(19);
        pArg.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(attackSpeed);
        data.isQueued = false;
        dataArg.isQueued = false;
        if (new Random().nextBoolean()) {
            p.teleport(arena.warp1.toLocation());
            pArg.teleport(arena.warp2.toLocation());
        }
        else {
            pArg.teleport(arena.warp1.toLocation());
            p.teleport(arena.warp2.toLocation());
        }
        p.getInventory().setContents(defaultKits.get(kit));
        pArg.getInventory().setContents(defaultKits.get(kit));
        p.sendMessage("duel started with " + pArg.getName());
        pArg.sendMessage("duel started with " + p.getName());
        data.duelRequests.clear();
        duelQueue.remove(p.getUniqueId());
        data.isDueling = pArg.getUniqueId();
        dataArg.duelRequests.clear();
        duelQueue.remove(pArg.getUniqueId());
        dataArg.isDueling = p.getUniqueId();
        return true; // reset on death
    }
}
