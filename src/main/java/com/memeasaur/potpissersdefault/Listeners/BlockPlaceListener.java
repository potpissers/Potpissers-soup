package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.Classes.LocationCoordinate;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.*;
import static com.memeasaur.potpissersdefault.Util.Methods.ClaimUtils.handleClaimBlockTask;

public class BlockPlaceListener implements Listener {
    private final PotpissersDefault plugin;
    public BlockPlaceListener(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlockPlaced();
        String blockClaim = claims.getWorld(block.getWorld().getName()).getOrDefault(new ClaimCoordinate(block), WILDERNESS_CLAIM);
        if (!blockClaim.equals(WILDERNESS_CLAIM) && !p.getGameMode().equals(GameMode.CREATIVE)) {
            Material blockType = block.getType();
            if (blockClaim.equals(SPAWN_CLAIM)) {
                if (!blockType.equals(Material.SHULKER_BOX)) {
                    e.setCancelled(true);
                    return;
                }
                else
                    handleClaimBlockTask(block, Material.AIR, plugin);
            } else {
                if (!EVENT_PLACEABLE_BLOCKS.contains(blockType) &&
                        (!EVENT_NON_COMBAT_PLACEABLE_BLOCKS.contains(blockType) || playerDataMap.get(p.getUniqueId()).combatTag[0] != 0)) {
                    e.setCancelled(true);
                    return;
                }
                else if (claimsBlockMap.containsKey(new LocationCoordinate(block)))
                    claimsBlockMap.get(new LocationCoordinate(block))[0] = CLAIM_BLOCK_TIMER;
                else
                    handleClaimBlockTask(block, Material.AIR, plugin);
            }
        }

    }
}
