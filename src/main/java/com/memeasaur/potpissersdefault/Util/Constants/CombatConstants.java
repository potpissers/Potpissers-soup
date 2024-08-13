package com.memeasaur.potpissersdefault.Util.Constants;

import java.util.HashMap;
import java.util.Map;

public class CombatConstants {
    public static final double ATTACK_SPEED_VANILLA = 4F;
    public static final double ATTACK_SPEED_REVERTED_VANILLA = 1.5999999046325684;
    public static final double ATTACK_SPEED_7CPS = 6.25F;
    public static final double ATTACK_SPEED_12CPS = 9.25F;
    public static final double ATTACK_SPEED_UNCAPPED = 16777216;
    public static double ATTACK_SPEED_DEFAULT = ATTACK_SPEED_7CPS; // make command
    public static final HashMap<String, Double> ATTACK_SPEED_STRING_MAP = new HashMap<>(Map.of("vanilla", ATTACK_SPEED_VANILLA, "revertedVanilla", ATTACK_SPEED_REVERTED_VANILLA, "7cps", ATTACK_SPEED_7CPS, "12cps", ATTACK_SPEED_12CPS, "uncapped", ATTACK_SPEED_UNCAPPED));

    // Combat tag start
    public static final int COMBAT_TAG = 120; // make command
    // Combat tag end
    // Grapple start
    public static final int GRAPPLE_CD = 12;
    public static final int KNOCKBACK_CD = 4;
    // Grapple end
}
