package com.memeasaur.potpissersdefault.Util.Methods;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.Classes.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

import static com.memeasaur.potpissersdefault.PotpissersDefault.claims;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.SPAWN_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.WILDERNESS_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.LoggerConstants.SAFE_LOGOUT_TIMER;
import static com.memeasaur.potpissersdefault.Util.Constants.LoggerConstants.TAG_LOGOUT_TIMER;

public class WarpUtils {
    public static void doTeleport(Player p, PlayerData data, Location location, Plugin plugin) {
        if (claims.getWorld(p.getWorld().getName()).getOrDefault(new ClaimCoordinate(p.getLocation()), WILDERNESS_CLAIM).equals(SPAWN_CLAIM))
            p.teleport(location);
        else {
            AtomicInteger timer = new AtomicInteger(data.combatTag[0] != 0 ? TAG_LOGOUT_TIMER : SAFE_LOGOUT_TIMER);
            p.sendActionBar(Component.text("Teleport in " + timer.get()));
            if (!data.teleportTimers.containsKey(location)) {
                data.teleportTimers.put(location, timer);
                data.teleportTasks.add(new BukkitRunnable() {
                    @Override
                    public void run() {
                        timer.getAndDecrement();
                        if (timer.get() == 0) {
                            p.teleport(location);
                            if (claims.getWorld(location.getWorld().getName()).getOrDefault(new ClaimCoordinate(location), WILDERNESS_CLAIM).equals(SPAWN_CLAIM))
                                data.combatTag[0] = 0;
                            p.sendActionBar(Component.text(""));
                            cancel();
                        } else {
                            p.sendActionBar(Component.text("Teleport in " + timer.get()));
                        }
                    }
                }.runTaskTimer(plugin, 20L, 20L));
            }
            else p.sendMessage("cancelled (already teleporting)");
        }
    }
}
