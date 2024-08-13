package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.PotpissersDefault;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import static com.memeasaur.potpissersdefault.PotpissersDefault.grappleHooks;
import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;

public class ProjectileHitListener implements Listener {
    private final PotpissersDefault plugin;
    public ProjectileHitListener(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player p) {
            switch (e.getEntity().getType()) {
                case FISHING_BOBBER -> {
                    FishHook fh = (FishHook) e.getEntity();
                    if (grappleHooks.contains(fh) && e.getHitBlockFace() != null)
                        fh.setNoPhysics(true);
                    // if rogue go through teammate thanks kayla
                    if (e.getHitEntity() instanceof Player p1 && playerDataMap.get(p1.getUniqueId()).combatTag[0] != 0) {
                        p1.hideEntity(plugin, fh);
                        p1.sendActionBar(Component.text("hiding fish hook"));
                    }
                }
            }
        }
    }
}
