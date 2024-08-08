package com.memeasaur.potpissersdefault.Commands;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.LoggerConstants.SAFE_LOGOUT_TIMER;
import static com.memeasaur.potpissersdefault.Util.Constants.LoggerConstants.TAG_LOGOUT_TIMER;

public class LogoutCommand implements CommandExecutor {
    private final PotpissersDefault plugin;
    public LogoutCommand(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p && command.getName().equalsIgnoreCase("logout")) {
            if (strings.length != 0) {
                p.sendMessage("invalid usage");
                return true;
            }
            else doLogout(p, playerDataMap.get(p.getUniqueId()), plugin);
        }
        return false;
    }
    void doLogout(Player p, PlayerData data, Plugin plugin) {
        data.logoutTimer[0] = data.combatTag[0] == 0 ? SAFE_LOGOUT_TIMER : TAG_LOGOUT_TIMER;
        p.sendActionBar(Component.text("Logout in " + data.logoutTimer[0]));
        data.logoutTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (data.logoutTimer[0] > 0)
                    data.logoutTimer[0]--;
                if (data.logoutTimer[0] == 0) {
                    data.canSafeLog = true;
                    p.kick();
                    data.canSafeLog = false;
                    p.sendActionBar(Component.text(""));
                    cancel();
                }
                else
                    p.sendActionBar(Component.text("Logout in " + data.logoutTimer[0]));
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
}
