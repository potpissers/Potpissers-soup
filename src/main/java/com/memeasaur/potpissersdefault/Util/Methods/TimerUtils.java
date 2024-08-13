package com.memeasaur.potpissersdefault.Util.Methods;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;

import static com.memeasaur.potpissersdefault.Util.Constants.ScoreboardConstants.SCOREBOARD_STRING;
import static com.memeasaur.potpissersdefault.Util.Methods.CombatUtils.doCombatTag;

public class TimerUtils {
    public static void handleTimerCancel(PlayerData data, Player p) {
        if (data.logoutTask != null && !data.logoutTask.isCancelled()) {
            data.logoutTask.cancel();
            p.sendActionBar(Component.text(""));
        }
        // Claims start
        if (!data.teleportTasks.isEmpty()) {
            for (BukkitTask task : data.teleportTasks)
                task.cancel();
            data.teleportTimers.clear();
            data.teleportTasks.clear();
        }
        if (data.tpaReadyTask != null && !data.tpaReadyTask.isCancelled())
            data.tpaReadyTask.cancel();
        if (data.isTpaReady) data.isTpaReady = false;
        // Claims end
    }
    // Scoreboard timers start
    public static void doOrUpdateScoreboardTimer(@Nullable Player p, @Nullable Scoreboard scoreboard, String team, int[] timer, @Nullable int[] buffer, int score, int cooldown, Plugin plugin) {
        timer[0] += cooldown;
        if (timer[0] > cooldown)
            return;
        Objective o = scoreboard.getObjective(SCOREBOARD_STRING);
        Team t = scoreboard.getTeam(team);
        //if (cooldown > 3600) {}
        if (timer[0] > 60)
            t.suffix(Component.text((timer[0] / 60) + "m" + (timer[0] % 60) + "s"));
        else
            t.suffix(Component.text(timer[0]));
        o.getScore(team).setScore(score);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (timer[0] > 0)
                    timer[0]--;
                if (buffer != null && buffer[0] > 0)
                    buffer[0]--;
                else if (p != null && timer[0] > cooldown)
                    doCombatTag(p, plugin);
                if (timer[0] > 60)
                    t.suffix(Component.text(((timer[0] / 60) + "m" + timer[0] % 60 + "s")));
                else
                    t.suffix(Component.text(timer[0]));
                if (timer[0] == 0) {
                    o.getScore(team).resetScore();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
    // Scoreboard timers end
}
