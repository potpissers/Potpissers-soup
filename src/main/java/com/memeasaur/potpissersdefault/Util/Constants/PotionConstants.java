package com.memeasaur.potpissersdefault.Util.Constants;

import org.bukkit.potion.PotionType;

import java.util.HashSet;
import java.util.List;

import static org.bukkit.potion.PotionType.*;

public class PotionConstants {
    public static final HashSet<PotionType> NOT_NERFED_SPLASH_EFFECTS = new HashSet<>(List.of(PotionType.POISON, PotionType.LONG_POISON, PotionType.STRONG_POISON, PotionType.SLOWNESS, PotionType.LONG_SLOW_FALLING, PotionType.STRONG_SLOWNESS, PotionType.WEAKNESS, PotionType.LONG_WEAKNESS, PotionType.SLOW_FALLING, PotionType.LONG_SLOW_FALLING, PotionType.HEALING, PotionType.STRONG_HEALING, PotionType.HARMING, PotionType.STRONG_HARMING));
    // Combat tag start
    public static HashSet<PotionType> DEBUFF_EFFECTS = new HashSet<>(List.of(HARMING, STRONG_HARMING, LONG_POISON, LONG_SLOW_FALLING, LONG_WEAKNESS, POISON, SLOW_FALLING, STRONG_POISON, WEAKNESS, LONG_WEAKNESS, STRONG_SLOWNESS, LONG_SLOWNESS, SLOWNESS));
    // Combat tag end
}
