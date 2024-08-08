package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.Classes.LocationCoordinate;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.*;
import static com.memeasaur.potpissersdefault.Util.Methods.ClaimUtils.handleClaimBlockTask;

public class BlockBreakListener implements Listener {
    private final PotpissersDefault plugin;
    public BlockBreakListener(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        String blockClaim = claims.getWorld(block.getWorld().getName()).getOrDefault(new ClaimCoordinate(block), WILDERNESS_CLAIM);
        if (!blockClaim.equals(WILDERNESS_CLAIM) && !p.getGameMode().equals(GameMode.CREATIVE)) {
            LocationCoordinate blockCoordinate = new LocationCoordinate(block);
            if (!claimsBlockMap.containsKey(blockCoordinate)) {
                if (EVENT_BREAKABLE_BLOCKS.contains(block.getType())) {
                    e.setDropItems(false); // explosion still drops :/
                    handleClaimBlockTask(block, block.getType(), plugin);
                }
                else
                    e.setCancelled(true);
            }
            else
                claimsBlockMap.get(blockCoordinate)[0] = CLAIM_BLOCK_TIMER;
        }
    }
}
