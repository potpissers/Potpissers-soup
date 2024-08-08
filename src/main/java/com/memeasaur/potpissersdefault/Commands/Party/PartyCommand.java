package com.memeasaur.potpissersdefault.Commands.Party;

import com.memeasaur.potpissersdefault.Classes.*;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.SPAWN_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.WILDERNESS_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.CombatConstants.ATTACK_SPEED_DEFAULT;
import static com.memeasaur.potpissersdefault.Util.Constants.DuelsConstants.CPS_STRING_MAP;
import static com.memeasaur.potpissersdefault.Util.Constants.PartyConstants.*;
import static com.memeasaur.potpissersdefault.Util.Methods.PartyUtils.*;
import static com.memeasaur.potpissersdefault.Util.Methods.WarpUtils.doTeleport;

public class PartyCommand implements CommandExecutor {
    private final PotpissersDefault plugin;
    public PartyCommand(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p && command.getName().equalsIgnoreCase("party")) {
            PlayerData data = playerDataMap.get(p.getUniqueId());
            if (args.length == 0) { // GUI? or just f who
                Party party = data.partyReference.get();
                if (party != null) {
                    p.sendMessage(party.leader.toString());
                    p.sendMessage(party.roster.toString());
                    p.sendMessage(party.invites.toString());
                    p.sendMessage(party.allies.toString());
                    p.sendMessage(party.allianceRequests.toString());
                    return true;
                }
                else p.sendMessage(PARTY_USAGE);
                return true;
            }
            switch (args[0]) {
                case "create" -> {
                    if (args.length != 1) {
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party != null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    doPartyCreate(p, data);
                    return true;
                }
                case "disband", "delete" -> {
                    if (args.length != 1) {
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    if (!party.leader.equals(data.uuid)) {
                        p.sendMessage("invalid (permissions)");
                        return true;
                    }
                    doPartyDisband(p, party); // || officers?
                    return true;
                }
                case "invite", "inv" -> {
                    if (args.length < 2) { // add multiple invites at once ability + GUI
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null)
                        party = doPartyCreate(p, playerDataMap.get(p.getUniqueId()));
                    else if (party.roster.get(data.uuid).equals(MEMBER_STRING)) {
                        p.sendMessage("invalid (permissions)"); // doRosterMsg wants to invite meme
                        return true;
                    }
                    OfflinePlayer oPArg = Bukkit.getOfflinePlayer(args[1]);
                    if (oPArg.getName() == null) { // offlinePlayer is nonnull
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    UUID uArg = oPArg.getUniqueId();
                    if (party.roster.containsKey(uArg)) {
                        p.sendMessage("invalid (roster contains)");
                        return true;
                    }
                    party.invites.add(uArg);
                    Player pArg = oPArg.getPlayer();
                    if (pArg != null)
                        oPArg.getPlayer().sendMessage((Component.text(p.getName()).append(Component.text(" has invited you to their party. click here or type /party accept ").append(Component.text(p.getName())).append(Component.text(" to join"))).clickEvent(ClickEvent.runCommand("/party accept " + p.getName()))));
                    doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has invited ")).append(Component.text(oPArg.getName())));
                    return true; // maybe swap oPArg color depending on team relationship
                }
                case "uninvite", "revoke", "disinvite", "deinvite" -> {
                    if (args.length < 2) { // add multiple unInvite functionality
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    if (party.roster.get(data.uuid).equals(MEMBER_STRING)) {
                        p.sendMessage("invalid (permissions)");
                        return true;
                    }
                    OfflinePlayer oPArg = Bukkit.getOfflinePlayer(args[1]);
                    if (oPArg.getName() == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    UUID uArg = oPArg.getUniqueId();
                    if (!party.invites.contains(uArg)) {
                        p.sendMessage("invalid (no invite)");
                        return true;
                    }
                    else party.invites.remove(uArg);
                    Player pArg = Bukkit.getPlayer(uArg);
                    if (pArg != null)
                        pArg.sendMessage(Component.text(p.getName()).append(Component.text(" has uninvited you")));
                    doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has uninvited ").append(Component.text(oPArg.getName()))));
                    return true;
                }
                case "kick" -> {
                    if (args.length == 1) {
                        p.sendMessage(PARTY_USAGE);
                        //GUI
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    OfflinePlayer oPArg = Bukkit.getOfflinePlayer(args[1]);
                    if (oPArg.getName() == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    UUID uArg = oPArg.getUniqueId();
                    if (!party.roster.containsKey(uArg)) {
                        p.sendMessage("invalid (roster)");
                        return true;
                    }
                    if (PARTY_PERMS_MAP.get(party.roster.get(p.getUniqueId())) < PARTY_PERMS_MAP.get(party.roster.get(uArg)) && p != oPArg.getPlayer()) {
                        p.sendMessage("invalid (permissions)");
                        return true;
                    }
                    party.roster.remove(uArg);
                    Player pArg = Bukkit.getPlayer(uArg);
                    if (pArg != null) {
                        pArg.sendMessage(Component.text(p.getName()).append(Component.text(" has kicked you")));
                        playerDataMap.get(uArg).partyReference = NULL_PARTY;
                        doRosterMsg(party, Component.text(pArg.getName()).append(Component.text(" has been kicked by ").append(Component.text(p.getName()))));
                    }
                    return true;
                }
                case "accept", "join" -> {
                    if (args.length == 1) {
                        //GUI
                        return true;
                    }
                    if (args.length > 2) {
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    OfflinePlayer opArg = Bukkit.getOfflinePlayer(args[1]);
                    if (opArg.getName() == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    Party party = playerDataMap.get(opArg.getUniqueId()).partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    if (!party.invites.contains(data.uuid)) {
                        p.sendMessage("invalid (need invite)");
                        // request
                        // doRosterMsg
                        return true;
                    }
                    data.partyReference = party.selfWeakReference;
                    party.roster.put(data.uuid, MEMBER_STRING);
                    party.invites.remove(data.uuid);
                    doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has joined the party")));
                    return true;
                }
                case "leave", "quit" -> {
                    if (args.length != 1) {
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    if (party.leader.equals(data.uuid)) {
                        p.sendMessage("invalid (leader), use disband. or switch leader first");
                        return true;
                    }
//                    if (party.roster.get(data.uuid).equals(OFFICER_STRING)) {
//                        doRosterMsg(party, Component.text(p.getName(), OFFICERCHAT).append(doItalic(" has demoted themselves from officer", TEAMCHAT)));
//                    } // maybe just swap this for OFFICERCHAT name meme in kick message
                    party.roster.remove(data.uuid);
                    data.partyReference = NULL_PARTY;
                    doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has left the party")));
                    p.sendMessage("you have left the party");
                }
                case "officer" -> {
                    if (args.length == 1) {
                        //GUI
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    OfflinePlayer opArg = Bukkit.getOfflinePlayer(args[1]);
                    if (opArg.getName() == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    if (!party.roster.get(data.uuid).equals(COLEADER_STRING)) {
                        p.sendMessage("invalid (permissions)");
                        return true;
                    }
                    UUID uArg = opArg.getUniqueId();
                    if (!party.roster.containsKey(uArg)) { //doInvite? has invited you to be an officer meme
                        p.sendMessage("invalid roster");
                        return true;
                    }
                    if (party.roster.get(uArg).equals(OFFICER_STRING)) {
                        p.sendMessage("invalid (promoted)");
                        return true;
                    }
                    party.roster.put(uArg, OFFICER_STRING);
                    doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has promoted ")).append(Component.text(opArg.getName()).append(Component.text(" to officer"))));
                    return true;
                }
                case "demote" -> {
                    if (args.length == 1) {
                        //GUI
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    OfflinePlayer opArg = Bukkit.getOfflinePlayer(args[1]);
                    if (opArg.getName() == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    UUID uArg = opArg.getUniqueId();
                    if (party.roster.get(uArg).equals(MEMBER_STRING)) {
                        p.sendMessage("invalid (memberRank)");
                        return true;
                    }
                    if (!party.roster.get(data.uuid).equals(COLEADER_STRING) && p != opArg.getPlayer()) {
                        p.sendMessage("invalid (permissions)");
                        return true;
                    }
                    party.roster.put(uArg, MEMBER_STRING);
                    doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has demoted ")).append(Component.text(opArg.getName()).append(Component.text(" from officer"))));
                    return true;
                }
                case "ally", "truce", "friend" -> { // works for player names and both team objects
                    if (args.length == 1) { // add multi ally etc
                        //GUI
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    OfflinePlayer oPArg = Bukkit.getOfflinePlayer(args[1]);
                    if (oPArg.getName() == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    Party partyArg = playerDataMap.get(oPArg.getUniqueId()).partyReference.get();
                    if (partyArg == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null)
                        party = doPartyCreate(p, playerDataMap.get(p.getUniqueId()));
                    else if (party.roster.get(p.getUniqueId()).equals(MEMBER_STRING)) {
                        p.sendMessage("invalid (permissions)");
                        return true;
                    }
                    if (party == partyArg) {
                        p.sendMessage("invalid (teammates)");
                        return true;
                    }
                    if (party.allies.contains(partyArg)) {
                        p.sendMessage("invalid (allied)");
                        return true;
                    }
                    if (partyArg.allianceRequests.contains(party)) {
                        p.sendMessage("invalid (request pending)");
                        return true;
                    }
                    if (party.allianceRequests.contains(partyArg)) {
                        party.allies.add(partyArg);
                        partyArg.allies.add(party);
                        party.allianceRequests.remove(partyArg);
                        doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has accepted an alliance with ").append(Component.text(partyArg.name))));
                        doRosterMsg(partyArg, Component.text("alliance accepted with ").append(Component.text(party.name)));
                    } // handle enemy meme GL
                    else {
                        partyArg.allianceRequests.add(party);
                        doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has suggested an alliance with ").append(Component.text(oPArg.getName()))));
                        doRosterMsg(partyArg, (Component.text(party.name).append(Component.text(" has suggested an alliance"))).clickEvent(ClickEvent.runCommand("/party ally " + p.getName())));// neutral chat check for relationship status possibly + neutralchat OFFICER
                    }
                    return true;
                }
                case "enemy" -> {
                    if (args.length == 1) {
                        //GUI
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    OfflinePlayer oPArg = Bukkit.getOfflinePlayer(args[1]);
                    if (oPArg.getName() == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    Party partyArg = playerDataMap.get(oPArg.getUniqueId()).partyReference.get();
                    if (partyArg == null) {
                        p.sendMessage("invalid (partyArg)");
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null)
                        party = doPartyCreate(p, playerDataMap.get(p.getUniqueId()));
                    else if (party.roster.get(p.getUniqueId()).equals(MEMBER_STRING)) {
                        p.sendMessage("invalid (permissions)");
                        return true;
                    }
                    if (party == partyArg) {
                        p.sendMessage("invalid (teammates)");
                        return true;
                    }
                    if (party.enemies.contains(partyArg)) {
                        p.sendMessage("invalid (enemied)");
                        return true;
                    }
                    if (party.allies.contains(partyArg))
                        doUnAlly(party, partyArg, p, oPArg.getName());
                    party.enemies.add(partyArg);
                    doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has marked ").append(Component.text(partyArg.name).append(Component.text(" as an enemy")))));
                    doRosterMsg(partyArg, Component.text(party.name).append(Component.text(" has marked your party as an enemy")));
                }
                case "neutral" -> {
                    if (args.length == 1) {
                        //GUI
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    if (party.roster.get(p.getUniqueId()).equals(MEMBER_STRING)) {
                        p.sendMessage("invalid (permissions)"); // request rosterMsg
                        return true;
                    }
                    OfflinePlayer oPArg = Bukkit.getOfflinePlayer(args[1]);
                    if (oPArg.getName() == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    Party partyArg = playerDataMap.get(oPArg.getUniqueId()).partyReference.get();
                    if (partyArg == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    if (party.allies.contains(partyArg)) {
                        doUnAlly(party, partyArg, p, oPArg.getName());
                        return true;
                    }
                    if (party.enemies.contains(partyArg)) {
                        party.enemies.remove(partyArg);
                        doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has un-marked ").append(Component.text(partyArg.name).append(Component.text(" as an enemy")))));
                        doRosterMsg(partyArg, Component.text(party.name).append(Component.text(" has un-marked your party as an enemy")));
                        return true;
                    }
                    else p.sendMessage("invalid (neutral)");
                    return true;
                }
                case "unally", "unfriend", "untruce" -> {
                    if (args.length == 1) {
                        //GUI
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    OfflinePlayer oPArg = Bukkit.getOfflinePlayer(args[1]);
                    if (oPArg.getName() == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    Party partyArg = playerDataMap.get(oPArg.getUniqueId()).partyReference.get();
                    if (partyArg == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    if (!party.allies.contains(partyArg)) {
                        p.sendMessage("invalid (alliance)");
                        return true;
                    }
                    if (party.roster.get(p.getUniqueId()).equals(MEMBER_STRING)) {
                        p.sendMessage("invalid (permissions)"); // doRosterMsg
                        return true;
                    }
                    doNeutral(party, partyArg, true,
                            Component.text(p.getName()).append(UN_ALLY_MSG.append(Component.text(partyArg.name))),
                            UN_ALLY_MSG_ARG.append(Component.text(party.name)));
                }
                case "unenemy" -> {
                    if (args.length == 1) {
                        p.sendMessage(PARTY_USAGE);
                        //GUI
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    OfflinePlayer oPArg = Bukkit.getOfflinePlayer(args[1]);
                    if (oPArg.getName() == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    Party partyArg = playerDataMap.get(oPArg.getUniqueId()).partyReference.get();
                    if (partyArg == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    if (!party.enemies.contains(partyArg)) {
                        p.sendMessage("invalid (neutral)");
                        return true;
                    }
                    if (party.roster.get(p.getUniqueId()).equals(MEMBER_STRING)) {
                        p.sendMessage("invalid (permissions)"); // doRosterMsg
                        return true;
                    }
                    doNeutral(party, partyArg, false,
                            Component.text(p.getName()).append(UN_ENEMY_MSG).append(Component.text(partyArg.name).append(UN_ENEMY_MSG_1)),
                            Component.text(party.name).append(UN_ENEMY_MSG_ARG));
                }
                case "focus", "target" -> {
                    if (args.length == 1) {
                        p.sendMessage(PARTY_USAGE);
                        //gui
                        return true;
                    }
                    if (args.length > 2) {
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null)
                        party = doPartyCreate(p, playerDataMap.get(p.getUniqueId()));
                    else if (party.roster.get(p.getUniqueId()).equals(MEMBER_STRING)) {
                        p.sendMessage("invalid (permissions)");
                        return true;
                    }
                    OfflinePlayer opArg = Bukkit.getOfflinePlayer(args[1]);
                    Party partyArg = playerDataMap.get(opArg.getUniqueId()).partyReference.get();
                    if (partyArg == null) {
                        p.sendMessage("invalid (partyArg)");
                        return true;
                    }
                    if (party == partyArg) {
                        p.sendMessage("invalid (teammates)");
                        return true;
                    }
                    party.focus = partyArg;
                    doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has focused ").append(Component.text(partyArg.name))));
                    doRosterMsg(partyArg, Component.text(party.name).append(Component.text(" has focused your party")));
                }
                case "unfocus" -> {}
                case "apply", "request" -> {} // include request message
                case "decline", "deny" -> {} // include case for both factions denying player and player denying faction, include deny message
                case "who" -> {} // works for both party and faction?
                case "list" -> { // lists both party and faction? excludes partied members from factions
                }
                case "invites" -> {}
                case "chat", "c" -> {}
                case "leader", "transfer" -> {
                    if (args.length == 1) {
                        p.sendMessage(PARTY_USAGE);
                        //GUI
                        return true;
                    }
                    if (args.length != 2) {
                        p.sendMessage(PARTY_USAGE);
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    OfflinePlayer opArg = Bukkit.getOfflinePlayer(args[1]);
                    if (opArg.getName() == null) {
                        p.sendMessage("invalid (player)");
                        return true;
                    }
                    if (!party.leader.equals(data.uuid)) {
                        p.sendMessage("invalid (permissions)");
                        return true;
                    }
                    UUID uArg = opArg.getUniqueId();
                    if (!party.roster.containsKey(uArg)) {
                        p.sendMessage("invalid (roster)"); // invite as leader case
                        return true;
                    }
                    party.doSetLeader(uArg);
                    p.sendMessage("leadership transferred");
                    return true;
                }
                case "coleader" -> {}
                case "rally" -> {
                    Party party = data.partyReference.get();
                    if (party != null) {
                        party.rally = p.getLocation();
                        if (party.rallyTimer != null)
                            party.rallyTimer.cancel();
                        //p.sendBlockChanges();
                        party.rallyTimer = new BukkitRunnable() {
                            @Override
                            public void run() {
                                party.rally = null;
                            }
                        }.runTaskLater(plugin, RALLY_TIMER_TICK);
                        doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has placed your rally at ").append(Component.text(party.rally.getBlockX() + ", " + party.rally.getBlockY() + ", " + party.rally.getBlockZ()))));
                        // officer vs member coloring
                    }
                    else {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                }
                case "warp", "tp", "teleport" -> {
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    if (party.warp == null) {
                        p.sendMessage("invalid (warp)");
                        return true;
                    }
                    if (claims.getWorld(p.getWorld().getName()).getOrDefault(new ClaimCoordinate(p.getLocation()), WILDERNESS_CLAIM).equals(SPAWN_CLAIM)) {
                        p.teleport(party.warp);
                        p.sendMessage("teleport successful");
                        return true;
                    }
                    else doTeleport(p, data, party.warp, plugin);
                    return true;
                }
                case "warpset", "setwarp", "settp", "tpset", "teleportset", "setteleport", "sethome" -> { //sethome ?
                    if (data.combatTag[0] != 0) {
                        p.sendMessage("invalid (combat)");
                        return true;
                    } //ADD TIMER!
                    Party party = data.partyReference.get();
                    if (party == null) {
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    if (party.warp != null) {
                        party.warpTask.cancel();
                    }
                    party.warp = p.getLocation();
                    party.warpTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            party.warp = null;
                            doRosterMsg(party, Component.text("party warp has expired"));
                        }
                    }.runTaskLater(plugin, 600L);
                    doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has updated your warp to ").append(Component.text(party.warp.getBlockX() + ", " + party.warp.getBlockY() + ", " + party.warp.getBlockZ()).append(Component.text(", click here or type /p warp to teleport to it. warps last 30 seconds")))).clickEvent(ClickEvent.runCommand("/p warp")));
                }
                case "ff", "friendlyfire" -> {}
                case "duel" -> {
                    if (args.length > 4) {
                        p.sendMessage("invalid (args)");
                        return true;
                    }
                    Party party = data.partyReference.get();
                    if (party == null) { // create party
                        p.sendMessage("invalid (party)");
                        return true;
                    }
                    String claim = claims.getWorld(p.getWorld().getName()).getOrDefault(new ClaimCoordinate(p.getLocation()), WILDERNESS_CLAIM);
                    if (!claim.equals(SPAWN_CLAIM)) {
                        p.sendMessage("invalid (spawn)");
                        return true;
                    }
                    if (args.length == 1) {
                        //doQueue or GUI
                        p.sendMessage("placeholder");
                        return true;
                    }
                    Player pArg = Bukkit.getPlayer(args[1]);
                    if (pArg == null) {
                        p.sendMessage("invalid (player)"); // add support for teamName
                        return true;
                    }
                    PlayerData dataArg = playerDataMap.get(pArg.getUniqueId());
                    Party partyArg = dataArg.partyReference.get();
                    if (partyArg == null) {
                        p.sendMessage("invalid (partyArg)");
                        return true;
                    }
                    double duelAttackSpeed = ATTACK_SPEED_DEFAULT;
                    String kitName = claim;
                    if (args.length > 2) {
                        if (CPS_STRING_MAP.containsKey(args[2])) {
                            duelAttackSpeed = CPS_STRING_MAP.get(args[2]);
                            if (args.length == 4) {
                                if (defaultKits.containsKey(args[3]))
                                    kitName = args[3];
                                else {
                                    p.sendMessage("invalid (kitName)");
                                    return true;
                                }
                            }
                        } else if (defaultKits.containsKey(args[2])) {
                            kitName = args[2];
                            if (args.length == 4) {
                                if (CPS_STRING_MAP.containsKey(args[3]))
                                    duelAttackSpeed = CPS_STRING_MAP.get(args[3]);
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
                    StringBuilder stringBuilder = new StringBuilder().append("duel cancelled. out of spawn: ");
                    boolean cancelled = false;
                    for (UUID u : party.roster.keySet()) {
                        Player player = Bukkit.getPlayer(u);
                        if (player != null && !claims.getWorld(p.getWorld().getName()).getOrDefault(new ClaimCoordinate(player.getLocation()), WILDERNESS_CLAIM).equals(SPAWN_CLAIM)) {
                            stringBuilder.append(player.getName()).append(" ");
                            cancelled = true;
                        }
                    }
                    if (cancelled) {
                        p.sendMessage(stringBuilder.toString());
                        return true;
                    }
                    if (party.partyDuelRequests.containsKey(partyArg)) {
                        DuelData dd = (party.partyDuelRequests.get(partyArg));
                        if (kitName.equals(dd.kitName) && duelAttackSpeed == dd.attackSpeed)
                            return doTeamfight(party, partyArg, dd.kitName, duelAttackSpeed);
                        else {
                            p.sendMessage("dfsdfdsfsd");
                            return true;
                        }
                    }
                    partyArg.partyDuelRequests.put(party, new DuelData(party, kitName, duelAttackSpeed));
                    for (UUID u : partyArg.roster.keySet()) {
                        Player player = Bukkit.getPlayer(u);
                        if (player != null)
                            player.sendMessage(party.name + " has sent your party a duel request");
                    }
                    for (UUID u : party.roster.keySet()) {
                        Player player = Bukkit.getPlayer(u);
                        if (player != null)
                            player.sendMessage(p.getName() + " has sent " + partyArg.name + " a duel request");
                    }
                    party.isQueued = true;
                }
            }
        }
        return true;
    }
    void doPartyDisband(Player disbander, Party party) {
        for (UUID u : party.roster.keySet()) {
            playerDataMap.get(u).partyReference = NULL_PARTY;
            Player p = Bukkit.getPlayer(u);
            if (p != null) {
                p.sendMessage(Component.text("party disbanded by ").append(Component.text(disbander.getName())));
            }
        }
        teams.remove(party);
    }// Player p, String pArgName
    void doUnAlly(Party party, Party partyArg, Player p, String pArgName) {
        party.allies.remove(partyArg);
        doRosterMsg(party, Component.text(p.getName()).append(Component.text(" has un-allied " + partyArg.name + " (" + pArgName + ")")));
        partyArg.allies.remove(party);
        doRosterMsg(partyArg, Component.text(party.name).append(Component.text(" has un-allied your party")));
    }
    boolean doTeamfight(Party party, Party partyArg, String kit, double attackSpeed) {
        Arena arena = arenas.get((String)arenas.keySet().toArray()[new Random().nextInt(arenas.size())]);
        if (new Random().nextBoolean()) {
            handlePartyFight(party, kit, arena.warp1.toLocation(), partyArg, attackSpeed);
            handlePartyFight(partyArg, kit, arena.warp2.toLocation(), party, attackSpeed);
        }
        else {
            handlePartyFight(partyArg, kit, arena.warp1.toLocation(), party, attackSpeed);
            handlePartyFight(party, kit, arena.warp2.toLocation(), partyArg, attackSpeed);
        }
        return true;
    }
    void handlePartyFight(Party party, String kit, Location location, Party partyArg, double attackSpeed) {
        party.isAliveDueling.clear();
        for (UUID u : party.roster.keySet()) {
            Player player = Bukkit.getPlayer(u);
            if (player != null) {
                party.isAliveDueling.add(u);
                player.setHealth(20);
                player.setFoodLevel(19);
                player.getInventory().setContents(defaultKits.get(kit));
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(attackSpeed);
                player.teleport(location);
                player.sendMessage("duel started with " + partyArg.name);
            }
        }
        teamfightQueue.remove(party);
        party.partyDuelRequests.clear();
        party.isQueued = false;
        party.isDueling = partyArg;
//        for (Party party1 : party.partyDuelRequests.keySet()) {
//            party1.leader
//        }
    }
}
