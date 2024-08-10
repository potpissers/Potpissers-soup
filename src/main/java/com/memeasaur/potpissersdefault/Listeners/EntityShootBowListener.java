package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.PotpissersDefault;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityShootBowListener implements Listener {
    private final PotpissersDefault plugin;
    public EntityShootBowListener(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    void onShootBowCubecore(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player p && e.getProjectile() instanceof AbstractArrow aa) {
            if (e.getForce() < 0.4) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!aa.isValid() || aa.isInBlock())
                            cancel();
                        else if (aa.getBoundingBox().overlaps(p.getBoundingBox())) {
                            aa.hitEntity(p);
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 3L, 1L);
            }
//            if (aa instanceof Arrow arrow
//                    && (!isLowForce || debuffEffects.contains(arrow.getBasePotionType()))) {
//                handleTippedArrow(arrow, data, p, e, plugin);
//            }
//            handleKnockbackArrow(aa, data, e, p, plugin);
        }
    }
}
