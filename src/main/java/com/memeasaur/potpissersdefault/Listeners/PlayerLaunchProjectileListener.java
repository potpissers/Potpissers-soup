package com.memeasaur.potpissersdefault.Listeners;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerLaunchProjectileListener implements Listener {
    private final PotpissersDefault plugin;
    public PlayerLaunchProjectileListener(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    void onProjectileLaunchCubecore(PlayerLaunchProjectileEvent e) {
        switch (e.getProjectile().getType()) {
            case POTION -> {
                Player p = e.getPlayer();
                ThrownPotion tp = (ThrownPotion) e.getProjectile();
                if (p.getPitch() < 35F) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!tp.isValid()) {
                                cancel();
                                return;
                            }
                            if (tp.getBoundingBox().overlaps(p.getBoundingBox())) {
                                Location location = p.getLocation().add(p.getLocation().getDirection().normalize().multiply(1.25).add(p.getVelocity()));
                                location.setY(tp.getY());
                                tp.teleport(location);
                                tp.hitEntity(p);
                                cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 3L, 1L);
                }
            }
        }
    }
}