package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import io.papermc.paper.event.player.PlayerOpenSignEvent;
import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

import static com.memeasaur.potpissersdefault.PotpissersDefault.claims;
import static com.memeasaur.potpissersdefault.Util.Constants.PotpissersConstants.KEY_CUBECORE_SIGN;

public class PlayerOpenSignListener implements Listener {
    @EventHandler
    void onSignInteract(PlayerOpenSignEvent e) {
        Player p = e.getPlayer();
        Sign sign = e.getSign();
        // Cubecore sign start
        if (sign.getPersistentDataContainer().has(KEY_CUBECORE_SIGN)) {
            e.setCancelled(true);
            p.performCommand(sign.getPersistentDataContainer().get(KEY_CUBECORE_SIGN, PersistentDataType.STRING));
            return;
        }
        // Cubecore sign end
        if (claims.getWorld(sign.getWorld().getName()).containsKey(new ClaimCoordinate(sign.getLocation())) && !p.getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
            return;
        }
    }
}
