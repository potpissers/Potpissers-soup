package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.Classes.LocationCoordinate;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;
import java.util.List;

import static com.memeasaur.potpissersdefault.PotpissersDefault.claims;
import static com.memeasaur.potpissersdefault.PotpissersDefault.claimsBlockMap;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.*;
import static com.memeasaur.potpissersdefault.Util.Methods.ClaimUtils.handleClaimBlockTask;

public class ClaimListeners implements Listener {
    private final PotpissersDefault plugin;
    public ClaimListeners(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            Location location = e.getEntity().getLocation();
            if (MOBLESS_CLAIMS.contains(claims.getWorld(location.getWorld().getName()).getOrDefault(new ClaimCoordinate(location), WILDERNESS_CLAIM)))
                e.setCancelled(true);
        }
    }
    @EventHandler
    void onBlockExplosion(BlockExplodeEvent e) {
        handleClaimExplosion(e.blockList());
    }
    @EventHandler
    void onEntityExplosion(EntityExplodeEvent e) {
        handleClaimExplosion(e.blockList());
    }
    @EventHandler
    void onBlockBurn(BlockBurnEvent e) {
        Block block = e.getBlock();
        if (claims.getWorld(block.getWorld().getName()).containsKey(new ClaimCoordinate(block))) {
            LocationCoordinate blockCoordinate = new LocationCoordinate(block);
            if (claimsBlockMap.containsKey(blockCoordinate))
                claimsBlockMap.get(blockCoordinate)[0] = CLAIM_BLOCK_TIMER;
            else if (EVENT_BREAKABLE_BLOCKS.contains(block.getType()))
                handleClaimBlockTask(block, block.getType(), plugin);
            else
                e.setCancelled(true);
        }
    }
    @EventHandler
    void onFireMove(BlockIgniteEvent e) {
        Block block = e.getBlock();
        if (e.getCause().equals(BlockIgniteEvent.IgniteCause.SPREAD)) {
            if (claims.getWorld(block.getWorld().getName()).containsKey(new ClaimCoordinate(block))
                    && !claimsBlockMap.containsKey(new LocationCoordinate(block)))
                e.setCancelled(true);
        }
    }
    @EventHandler
    void onMobChangeBlock(EntityChangeBlockEvent e) {
        Block block = e.getBlock();
        if (claims.getWorld(block.getWorld().getName()).containsKey(new ClaimCoordinate(block))) {
            LocationCoordinate blockCoordinate = new LocationCoordinate(block);
            if (claimsBlockMap.containsKey(blockCoordinate))
                claimsBlockMap.get(blockCoordinate)[0] = CLAIM_BLOCK_TIMER;
            else if (EVENT_BREAKABLE_BLOCKS.contains(block.getType()))
                handleClaimBlockTask(block, block.getType(), plugin);
            else
                e.setCancelled(true);
        }
    }

    void handleClaimExplosion(List<Block> blockList) {
        Iterator<Block> blockIterator = blockList.iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (claims.getWorld(block.getWorld().getName()).containsKey(new ClaimCoordinate(block))) {
                LocationCoordinate blockCoordinate = new LocationCoordinate(block);
                if (claimsBlockMap.containsKey(blockCoordinate))
                    claimsBlockMap.get(blockCoordinate)[0] = CLAIM_BLOCK_TIMER;
                else if (EVENT_BREAKABLE_BLOCKS.contains(block.getType()))
                    handleClaimBlockTask(block, block.getType(), plugin);
                else // this drops items when it probably shouldn't
                    blockIterator.remove();
            }
        }
    }
}
