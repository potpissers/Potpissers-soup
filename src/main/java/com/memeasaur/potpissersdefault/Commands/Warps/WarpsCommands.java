package com.memeasaur.potpissersdefault.Commands.Warps;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.Classes.PlayerData;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.WildcardType;
import java.util.HashSet;
import java.util.UUID;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.SPAWN_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.WILDERNESS_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.LoggerConstants.SAFE_LOGOUT_TIMER;
import static com.memeasaur.potpissersdefault.Util.Constants.LoggerConstants.TAG_LOGOUT_TIMER;
import static com.memeasaur.potpissersdefault.Util.Constants.ScoreboardConstants.*;
import static com.memeasaur.potpissersdefault.Util.Methods.WarpUtils.doTeleport;

public class WarpsCommands implements CommandExecutor {
    private final PotpissersDefault plugin;
    public WarpsCommands(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            switch (command.getName().toLowerCase()) {
                case "spawn" -> {
                    if (strings.length != 0 ) {
                        p.sendMessage("invalid usage");
                        return true;
                    }
                    Location location = Bukkit.getServer().getWorld("world").getSpawnLocation();
                    PlayerData data = playerDataMap.get(p.getUniqueId());
                    doTeleport(p, data, location, plugin);
                    data.combatTag[0] = 0;
                }
                case "warp" -> {
                    if (strings.length == 0) {
                        StringBuilder stringBuilder = new StringBuilder().append("warps: ");
                        for (String string : publicWarps.keySet()) {
                            stringBuilder.append(string);
                            stringBuilder.append(" ");
                        }
                        p.sendMessage(stringBuilder.toString());
                        return true;
                    }
                    if (strings.length != 1) {
                        p.sendMessage("invalid (strings)");
                        return true;
                    }
                    if (!publicWarps.containsKey(strings[0])) {
                        p.sendMessage("invalid (strings)");
                        return true;
                    }
                    doTeleport(p, playerDataMap.get(p.getUniqueId()), publicWarps.get(strings[0]).toLocation(), plugin);
                    return true;
                }
                case "tpa" -> {
                    if (strings.length == 0) {
                        p.sendMessage("warps to player arg. usage: /tpa (player)");
                        return true;
                    }
                    Player pArg = Bukkit.getPlayer(strings[0]);
                    if (strings.length != 1 || pArg == null) {
                        p.sendMessage("invalid (strings)");
                        return true;
                    }
                    PlayerData data = playerDataMap.get(p.getUniqueId());
                    PlayerData dataArg = playerDataMap.get(pArg.getUniqueId());
                    dataArg.tpaRequests.add(data.uuid);
                    doTpaTimer(p, data, pArg, dataArg, data.tpaRequests);
                    p.sendMessage("tpa request sent");
                    pArg.sendMessage("tpa requested by " + p.getName() + ". /tpaccept (name) to accept");
                    return true;
                }
                case "tpahere" -> {
                    if (strings.length == 0) {
                        p.sendMessage("warps to player arg. usage: /tpahere (player)");
                        return true;
                    }
                    Player pArg = Bukkit.getPlayer(strings[0]);
                    if (strings.length != 1 || pArg == null) {
                        p.sendMessage("invalid (strings)");
                        return true;
                    }
                    PlayerData data = playerDataMap.get(p.getUniqueId());
                    PlayerData dataArg = playerDataMap.get(pArg.getUniqueId());
                    dataArg.tpaRequests.add(data.uuid);
                    doTpaTimer(p, data, pArg, dataArg, data.tpaHereRequests);
                    p.sendMessage("tpahere request sent");
                    pArg.sendMessage("tpahere requested by " + p.getName() + ". /tphereaccept (name) to accept");
                    return true;
                }
                case "tpaccept" -> {
                    return handleTpAccept(strings, p, "tpaccept", playerDataMap.get(p.getUniqueId()).tpaRequests, "tpa requests", true);
                }
                case "tpahereaccept" -> {
                    return handleTpAccept(strings, p, "tphere", playerDataMap.get(p.getUniqueId()).tpaHereRequests, "tphere requests", false);
                }
                case "tpdeny" -> {}
                case "stuck" -> {}
            }
        }
        return false;
    }
    void doTpaHereTimer(Player p, PlayerData data, Player pArg, HashSet<UUID> requests) {
        int timer = claims.getWorld(p.getWorld().getName()).getOrDefault(new ClaimCoordinate(p.getLocation()), WILDERNESS_CLAIM).equals(SPAWN_CLAIM)
                ? 0 : data.combatTag[0] != 0
                ? TAG_LOGOUT_TIMER : SAFE_LOGOUT_TIMER;

        Scoreboard scoreboard = p.getScoreboard();
        Team t = scoreboard.getTeam(SCOREBOARD_TELEPORT);
        t.suffix(Component.text(timer + "s"));
        Score score = scoreboard.getObjective(SCOREBOARD_STRING).getScore(SCOREBOARD_TELEPORT);
        score.setScore(SCORE_TELEPORT);

        data.tpaReadyTask = new BukkitRunnable() {
            int i = timer;
            @Override
            public void run() {
                i--;
                if (i == 0) {
                    if (playerDataMap.get(pArg.getUniqueId()).isTpaReady) {
                        p.teleport(pArg); // make teleport remove tpaReady
                        p.sendActionBar(Component.text("teleport success"));
                        pArg.sendActionBar(Component.text("teleport success"));
                        requests.remove(p.getUniqueId());
                    }
                    else {
                        p.sendActionBar(Component.text("tpa ready, don't move"));
                        data.isTpaReady = true;
                    }
                    score.resetScore();
                    cancel();
                }
                else {
                    t.suffix(Component.text(i));
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
    void doTpaTimer(Player p, PlayerData data, Player pArg, PlayerData dataArg, HashSet<UUID> requests) {
        int timer = claims.getWorld(p.getWorld().getName()).getOrDefault(new ClaimCoordinate(p.getLocation()), WILDERNESS_CLAIM).equals(SPAWN_CLAIM)
                ? 0 : data.combatTag[0] != 0
                ? TAG_LOGOUT_TIMER : SAFE_LOGOUT_TIMER;

        Scoreboard scoreboard = p.getScoreboard();
        Team t = scoreboard.getTeam(SCOREBOARD_TELEPORT);
        t.suffix(Component.text(timer + "s"));
        Score score = scoreboard.getObjective(SCOREBOARD_STRING).getScore(SCOREBOARD_TELEPORT);
        score.setScore(SCORE_TELEPORT);

        data.tpaReadyTask = new BukkitRunnable() {
            int i = timer;
            @Override
            public void run() {
                i--;
                if (i == 0) {
                    if (dataArg.isTpaReady) {
                        p.teleport(pArg);
                        p.sendActionBar(Component.text("teleport success"));
                        pArg.sendActionBar(Component.text("teleport success"));
                        requests.remove(p.getUniqueId());
                    }
                    else t.sendActionBar(Component.text(i + ", arg not tpa ready, don't move")); // show other person's timer here
                    data.isTpaReady = true;// add using this for other teleports + changing this timer to a different teleport timer
                    score.resetScore();
                    cancel();
                    return;
                }
                else t.suffix(Component.text(i + "s"));
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
    boolean handleTpAccept(String[] strings, Player p, String argName, HashSet<UUID> requests, String requestsName, boolean isTpa) {
        if (strings.length > 1) {
            p.sendMessage("invalid strings: usage /" + argName + " (name)");
            return true;
        }
        PlayerData data = playerDataMap.get(p.getUniqueId());
        if (strings.length == 0) {
            StringBuilder stringBuilder;
            stringBuilder = new StringBuilder().append(requestsName).append("s: ");
            boolean hasRequests = false;
            for (UUID u : requests) {
                stringBuilder.append(Bukkit.getOfflinePlayer(u).getName()).append(" ");
                hasRequests = true;
            }
            if (hasRequests) p.sendMessage(stringBuilder.toString());
            else p.sendMessage(stringBuilder + " none");
            return true;
        }
        Player pArg = Bukkit.getPlayer(strings[0]);
        if (pArg == null) {
            p.sendMessage("invalid (player)");
            return true;
        }
        if (!requests.contains(pArg.getUniqueId())) {
            p.sendMessage("invalid (request)");
            return true;
        }
        if (isTpa) doTpaTimer(p, data, pArg, playerDataMap.get(pArg.getUniqueId()), data.tpaRequests);
        else doTpaHereTimer(p, data, pArg, data.tpaHereRequests);
        return true;
    }
}
