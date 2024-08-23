package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.Classes.PlayerData;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static com.memeasaur.potpissersdefault.PotpissersDefault.loggerDataMap;
import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.CombatConstants.*;
import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.CombatConstants.KNOCKBACK_CD;
import static com.memeasaur.potpissersdefault.Util.Constants.ScoreboardConstants.*;
import static com.memeasaur.potpissersdefault.Util.Methods.CombatUtils.doCombatTag;
import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.SPAWN_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.WILDERNESS_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Methods.TimerUtils.doOrUpdateScoreboardTimer;

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
                // Combat-tag start
                boolean isSelfInflicted = p == p1;
                // Combat-tag end
                switch (e.getCause()) {
                    case ENTITY_ATTACK -> {
                        if (data.isDueling == null && (data.partyReference.get() == null || data.partyReference.get().isDueling == null)) { // Claims
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
                            } // Claims
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
                    // Fishing rod start
                    case PROJECTILE -> {
                        switch (e.getDamager().getType()) {
                            case FISHING_BOBBER -> e.setCancelled(true);
                        }
                    }
                    // Fishing rod end
                }
                // Combat-tag start
                if (!isSelfInflicted) {
                    if (p1.isBlocking()) {
                        doCombatTag(p1, plugin);
                    }
                    if (!e.isCancelled())
                        doCombatTag(p, plugin);
                }
                // Combat-tag end
            }
            else {
                // Claims start
                PlayerData data = playerDataMap.get(p.getUniqueId());
                // Claims end
                if (data.isDueling == null && (data.partyReference.get() == null || data.partyReference.get().isDueling == null)) // Claims
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
