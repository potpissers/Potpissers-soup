package com.memeasaur.potpissersdefault.Util.Constants;

import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class LoggerConstants {
    public static final int CONSUME_TIME = 32; // verify this
    public static final List<PotionEffect> GAPPLE_EFFECT = List.of(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1), new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
    public static final List<PotionEffect> OPPLE_EFFECT = List.of(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 3), new PotionEffect(PotionEffectType.REGENERATION, 400, 1), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0), new PotionEffect(PotionEffectType.RESISTANCE, 6000, 0));
    public static final List<PotionEffect> TOTEM_EFFECT = List.of(new PotionEffect(PotionEffectType.ABSORPTION, 100, 2), new PotionEffect(PotionEffectType.REGENERATION, 900, 2), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 1));
    public static final NamespacedKey KEY_PIGLIN_LOGGER = new NamespacedKey("piglinlogger", "piglinlogger");
    // Logout + tag start
    public static final int SAFE_LOGOUT_TIMER = 8;
    public static final int TAG_LOGOUT_TIMER = 30;
    // Logout + tag end
}
