package com.memeasaur.potpissersdefault.Util.Methods;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class TimerUtils {
    public static void handleTimerCancel(PlayerData data, Player p) {
        if (data.logoutTask != null && !data.logoutTask.isCancelled()) {
            data.logoutTask.cancel();
            p.sendActionBar(Component.text(""));
        }
    }
}
