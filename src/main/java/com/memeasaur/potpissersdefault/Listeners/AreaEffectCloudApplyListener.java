package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.PotpissersDefault;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;

import static com.memeasaur.potpissersdefault.Util.Constants.PotionConstants.DEBUFF_EFFECTS;
import static com.memeasaur.potpissersdefault.Util.Methods.CombatUtils.doCombatTag;

public class AreaEffectCloudApplyListener implements Listener {
    private final PotpissersDefault plugin;
    public AreaEffectCloudApplyListener(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    void onLingeringTick(AreaEffectCloudApplyEvent e) {
        if (e.getEntity().getSource() instanceof ThrownPotion tp && tp.getShooter() instanceof Player p) {
            if (DEBUFF_EFFECTS.contains(tp.getPotionMeta().getBasePotionType())) {
                for (LivingEntity entity : e.getAffectedEntities())
                    if (entity instanceof Player p1) {
                        doCombatTag(p, plugin);
                    }
            }
        }
    }
}
