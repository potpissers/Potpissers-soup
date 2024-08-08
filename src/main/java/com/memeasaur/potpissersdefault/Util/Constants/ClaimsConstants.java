package com.memeasaur.potpissersdefault.Util.Constants;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashSet;
import java.util.List;

import static org.bukkit.Material.*;
import static org.bukkit.Material.COBWEB;

public class ClaimsConstants {
    public static final String WILDERNESS_CLAIM = "wilderness";
    public static final String SPAWN_CLAIM = "spawn";
    public static final NamespacedKey KEY_CUBECORE_CHEST = new NamespacedKey("cubecorechest", "cubecorechest");

    public static final HashSet<String> MOBLESS_CLAIMS = new HashSet<>(List.of(SPAWN_CLAIM));

    public static final HashSet<Material> EVENT_BREAKABLE_BLOCKS = new HashSet<>(List.of(COBWEB, SHORT_GRASS, TALL_GRASS, ROSE_BUSH, TRIPWIRE, FIRE, SOUL_FIRE, TWISTING_VINES, TWISTING_VINES_PLANT, Material.TNT, RAIL, POWERED_RAIL, DETECTOR_RAIL, ACTIVATOR_RAIL, LADDER, VINE, SCAFFOLDING, CAKE)); // BONE MEAL
    public static final HashSet<Material> EVENT_PLACEABLE_BLOCKS = new HashSet<>(List.of(FIRE, SOUL_FIRE, TWISTING_VINES, TWISTING_VINES_PLANT, SHULKER_BOX, SCAFFOLDING, CAKE)); // WATER, LAVA - add placing block removes water/lava
    public static final HashSet<Material> EVENT_NON_COMBAT_PLACEABLE_BLOCKS = new HashSet<>(List.of(COBWEB, ROSE_BUSH, LADDER, VINE, RAIL, POWERED_RAIL, DETECTOR_RAIL, ACTIVATOR_RAIL, TRIPWIRE));// water and lava, maybe no spread

    public static final int CLAIM_BLOCK_TIMER = 300;

    public static final HashSet<Material> CLAIM_UNCLICKABLE_BLOCKS = new HashSet(List.of(CHEST, TRAPPED_CHEST, BARREL, BREWING_STAND, HOPPER, FURNACE, BLAST_FURNACE, DISPENSER, DROPPER, // move this downstream for factions, only chests are needed
            STONE_BUTTON, POLISHED_BLACKSTONE_BUTTON, LEVER, COMPARATOR, OBSERVER, REPEATER,
            ACACIA_DOOR, BAMBOO_DOOR, BIRCH_DOOR, CHERRY_DOOR, CRIMSON_DOOR, DARK_OAK_DOOR, JUNGLE_DOOR, OAK_DOOR, SPRUCE_DOOR, WARPED_DOOR, MANGROVE_DOOR,
            DARK_OAK_TRAPDOOR, ACACIA_TRAPDOOR, BAMBOO_TRAPDOOR, OAK_TRAPDOOR, CHERRY_TRAPDOOR, BIRCH_TRAPDOOR, MANGROVE_TRAPDOOR, JUNGLE_TRAPDOOR, SPRUCE_TRAPDOOR, CRIMSON_TRAPDOOR, WARPED_TRAPDOOR,
            DARK_OAK_BUTTON, ACACIA_BUTTON, BAMBOO_BUTTON, OAK_BUTTON, CHERRY_BUTTON, BIRCH_BUTTON, MANGROVE_BUTTON, JUNGLE_BUTTON, SPRUCE_BUTTON, CRIMSON_BUTTON, WARPED_BUTTON,
            DARK_OAK_FENCE_GATE, ACACIA_FENCE_GATE, BAMBOO_FENCE_GATE, OAK_FENCE_GATE, CHERRY_FENCE_GATE, BIRCH_FENCE_GATE, MANGROVE_FENCE_GATE, JUNGLE_FENCE_GATE, SPRUCE_FENCE_GATE, CRIMSON_FENCE_GATE, WARPED_FENCE_GATE));
}
