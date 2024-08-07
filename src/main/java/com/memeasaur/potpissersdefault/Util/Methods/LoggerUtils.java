package com.memeasaur.potpissersdefault.Util.Methods;

import com.memeasaur.potpissersdefault.Classes.LoggerData;
import org.bukkit.*;
import org.bukkit.entity.Piglin;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static com.memeasaur.potpissersdefault.Util.Constants.LoggerConstants.*;

public class LoggerUtils {
    public static void doAppleConsumeLogger(Piglin piglin, ItemStack is, LoggerData piglinData, Plugin plugin) {
        EntityEquipment ee = piglin.getEquipment();
        ItemStack weapon = ee.getItemInMainHand();
        ee.setItemInMainHand(is);
        piglinData.eating = true;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (piglin.isValid()) {
                    if (is.getType().equals(Material.GOLDEN_APPLE))
                        piglin.addPotionEffects(GAPPLE_EFFECT);
                    else
                        piglin.addPotionEffects(OPPLE_EFFECT);
                    is.setAmount(is.getAmount() - 1);
                    ee.setItemInMainHand(weapon);
                    piglinData.eating = false;
                }
            }
        }.runTaskLater(plugin, CONSUME_TIME);
    }
    public static void doTotemLogger(Piglin piglin) {
        World world = piglin.getWorld(); // find vanilla particles + location
        world.spawnParticle(Particle.TOTEM_OF_UNDYING, piglin.getLocation(), 1);
        world.playSound(piglin, Sound.ITEM_TOTEM_USE, 1.0f, 1.0f);
        piglin.clearActivePotionEffects();
        piglin.addPotionEffects(TOTEM_EFFECT);
    }
}
