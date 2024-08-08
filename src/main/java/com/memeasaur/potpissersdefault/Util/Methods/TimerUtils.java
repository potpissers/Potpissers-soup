package com.memeasaur.potpissersdefault.Util.Methods;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

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
}
