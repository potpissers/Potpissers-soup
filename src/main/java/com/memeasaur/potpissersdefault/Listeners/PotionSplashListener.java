package com.memeasaur.potpissersdefault.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionType;

import static com.memeasaur.potpissersdefault.Util.Constants.PotionConstants.NOT_NERFED_SPLASH_EFFECTS;

public class PotionSplashListener implements Listener {
    @EventHandler
    void onSplashPotion (PotionSplashEvent e) {
        if (e.getPotion().getShooter() instanceof Player p) {
            PotionType potionType = e.getPotion().getPotionMeta().getBasePotionType();
            if (!NOT_NERFED_SPLASH_EFFECTS.contains(potionType)) {
                for (Entity entity : e.getAffectedEntities()) {
                    if (entity instanceof Player p1) {
                        e.setIntensity(p1, e.getIntensity(p1) * .85);
                    }
                }
            }
        }
    }
}
