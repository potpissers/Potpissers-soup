package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.LoggerData;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import org.bukkit.Material;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static com.memeasaur.potpissersdefault.Util.Methods.LoggerUtils.doAppleConsumeLogger;
import static com.memeasaur.potpissersdefault.PotpissersDefault.loggerDataMap;

public class EntityDamageListener implements Listener {
    private final PotpissersDefault plugin;
    public EntityDamageListener(PotpissersDefault plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onDamageCubecore(EntityDamageEvent e) {
        switch (e.getEntity().getType()) {
            case PIGLIN -> {
                Piglin piglin = (Piglin) e.getEntity();
                LoggerData piglinData = loggerDataMap.getOrDefault(piglin, null);
                if (piglinData != null) {
                    if (!piglinData.eating) {
                        double health = piglin.getHealth() + piglin.getAbsorptionAmount();
                        if (health <= 16) {
                            for (ItemStack is : piglinData.playerInventory) {
                                if (is != null && is.getType().equals(Material.GOLDEN_APPLE)) {
                                    doAppleConsumeLogger(piglin, is, piglinData, plugin);
                                    break;
                                }
                            }
                            if (health <= 13) {
                                for (ItemStack is : piglinData.playerInventory) {
                                    if (is != null && is.getType().equals(Material.ENCHANTED_GOLDEN_APPLE)) {
                                        doAppleConsumeLogger(piglin, is, piglinData, plugin);
                                        break;
                                    }
                                }
                                for (ItemStack is : piglinData.playerInventory) {
                                    if (is != null && is.getType().equals(Material.SPLASH_POTION) // do regen, speed, weak_healing etc
                                            && ((PotionMeta) is.getItemMeta()).getBasePotionType().equals(PotionType.STRONG_HEALING))
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                ThrownPotion potion = piglin.launchProjectile(ThrownPotion.class);
                                                potion.setItem(is);
                                                is.setAmount(is.getAmount() - 1);
                                                potion.setVelocity(new Vector(0, -1, 0));
                                            }
                                        }.runTaskLater(plugin, 10L);
                                }
                            }// OFFHAND OPPLE PROBLEMATIC!!!! like totem was
                        }
                    }
                }
            }
        }
    }
}
