package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.Classes.LocationCoordinate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

import static com.memeasaur.potpissersdefault.PotpissersDefault.claims;
import static com.memeasaur.potpissersdefault.PotpissersDefault.claimsBlockMap;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.EVENT_BREAKABLE_BLOCKS;

public class BlockDamageListener implements Listener {
    @EventHandler
    void onBlockDamage(BlockDamageEvent e) {
        Block block = e.getBlock();
        if (claims.getWorld(block.getWorld().getName()).containsKey(new ClaimCoordinate(block))) {
            if (!EVENT_BREAKABLE_BLOCKS.contains(block.getType()))
                e.setCancelled(true);
            else {
                if (block.getType().equals(Material.SHULKER_BOX)) // if owner, no instant break
                    e.setInstaBreak(true);
            }
        }
    }
}
