package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.LoggerData;
import com.memeasaur.potpissersdefault.Classes.LoggerUpdate;
import com.memeasaur.potpissersdefault.Classes.PlayerData;
import net.kyori.adventure.text.Component;
import io.papermc.paper.scoreboard.numbers.NumberFormat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.CombatConstants.ATTACK_SPEED_DEFAULT;
import static com.memeasaur.potpissersdefault.Util.Constants.ScoreboardConstants.SCOREBOARD_COMBAT;
import static com.memeasaur.potpissersdefault.Util.Constants.ScoreboardConstants.SCOREBOARD_STRING;

public class PlayerJoinListener implements Listener {
    @EventHandler
    void onPlayerJoin(PlayerJoinEvent e) { // make base scoreboard method
        Player p = e.getPlayer();
        if (!isServerReady && !p.isOp())
            p.kick(Component.text("deserialization not finished"));
        UUID u = p.getUniqueId();
        playerDataMap.putIfAbsent(u, new PlayerData(u));
        // Logout + tag start
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        p.setScoreboard(scoreboard);
        Objective o = scoreboard.registerNewObjective(SCOREBOARD_STRING, Criteria.DUMMY, Component.text(SCOREBOARD_STRING));
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.numberFormat(NumberFormat.blank());
        scoreboard.registerNewTeam(SCOREBOARD_COMBAT).addEntry(SCOREBOARD_COMBAT);
        // Logout + tag end
        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(ATTACK_SPEED_DEFAULT);
        PlayerData data = playerDataMap.get(u);
        LoggerData piglinData = loggerDataMap.getOrDefault(data.logger, null);
        if (piglinData != null) {
            Piglin piglin = data.logger;
            p.setHealth(piglin.getHealth());
            p.setAbsorptionAmount(piglin.getAbsorptionAmount());
            p.addPotionEffects(piglin.getActivePotionEffects());
            p.getInventory().setContents(piglinData.playerInventory);
            p.setFireTicks(piglin.getFireTicks());
            p.setFreezeTicks(piglin.getFreezeTicks());
            p.setArrowsInBody(piglin.getArrowsInBody());
            p.setFallDistance(piglin.getFallDistance());
            p.teleport(piglin);
            p.setVelocity(piglin.getVelocity());
//            loggerDataMap.get(piglin).task.cancel();
            loggerDataMap.remove(piglin);
            piglin.remove();
        }
        else {
            LoggerUpdate update = loggerUpdateMap.getOrDefault(u, null);
            if (update != null) {
                p.getInventory().setContents(update.inventory);
                p.teleport(update.location);
                p.setHealth(update.health);
                loggerUpdateMap.remove(u);
            }
        }
    }
}
