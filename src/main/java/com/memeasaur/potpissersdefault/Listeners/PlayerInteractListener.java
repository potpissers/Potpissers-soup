package com.memeasaur.potpissersdefault.Listeners;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.KEY_CUBECORE_CHEST;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.SPAWN_CLAIM;

public class PlayerInteractListener implements Listener {
    @EventHandler
    void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction().isRightClick()) {
            Player p = e.getPlayer();
            Block block = e.getClickedBlock();
            if (block != null && e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && block.getState() instanceof Chest chest && chest.getPersistentDataContainer().has(KEY_CUBECORE_CHEST) && !playerDataMap.get(p.getUniqueId()).currentClaim.equals(SPAWN_CLAIM)) {
                e.setCancelled(true);
                return;
            }
        }
    }
}
