package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static com.memeasaur.potpissersdefault.PotpissersDefault.loggerDataMap;
import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.CombatConstants.*;

public class EntityDamageByEntityListener implements Listener {
    private final PotpissersDefault plugin;
    public EntityDamageByEntityListener(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    void onDamagedCubecore(EntityDamageByEntityEvent e) {
        if (e.getDamageSource().getCausingEntity() instanceof Player p) {
            if (e.getEntity() instanceof Player p1) {
                PlayerData data = playerDataMap.get(p.getUniqueId());
                PlayerData data1 = playerDataMap.get(p1.getUniqueId());
                if (data.frozen || data1.frozen) {
                    e.setCancelled(true);
                    p.sendMessage("cancelled (frozen)");
                    return;
                }
                switch (e.getCause()) {
                    case ENTITY_ATTACK -> {
                        AttributeInstance attackSpeedInstance = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
                        if (attackSpeedInstance.getBaseValue() != ATTACK_SPEED_DEFAULT) {
                            attackSpeedInstance.setBaseValue(ATTACK_SPEED_DEFAULT);
                            p.sendActionBar(Component.text("attack speed: " + ATTACK_SPEED_DEFAULT));
                        }
                        if (ATTACK_SPEED_DEFAULT != ATTACK_SPEED_VANILLA && p.getAttackCooldown() != 1) {
                            e.setCancelled(true);
                            p.sendActionBar(Component.text("attack cancelled (cooldown)"));
                            p.resetCooldown();
                            return;
                        }
                        if (p.isSprinting()) new BukkitRunnable() {
                            @Override
                            public void run() {
                                p.setSprinting(true);
                            }
                        }.runTaskLater(plugin, 1L);
                    }
                    // Explosions start
                    case ENTITY_EXPLOSION, BLOCK_EXPLOSION -> {
                        p1.setHealth(Math.max(p1.getHealth() - e.getDamage() / 2.25, 0));
                        e.setDamage(0);
                    }
                    // Explosions end
                }
            }
            else {
                if (e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)
                        && ATTACK_SPEED_DEFAULT != ATTACK_SPEED_VANILLA) {
                    if (p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue() != ATTACK_SPEED_REVERTED_VANILLA) {
                        p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(ATTACK_SPEED_REVERTED_VANILLA);
                        p.sendActionBar(Component.text("attack speed: vanilla"));
                    }
                }
            }
        }
        if (e.getEntity() instanceof Piglin piglin && loggerDataMap.containsKey(piglin)) {
            piglin.setTarget((LivingEntity)e.getDamageSource().getCausingEntity());
            piglin.setAI(true);
        }
    }
}
