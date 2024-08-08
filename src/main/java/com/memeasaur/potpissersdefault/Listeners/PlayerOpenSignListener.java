package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import io.papermc.paper.event.player.PlayerOpenSignEvent;
import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static com.memeasaur.potpissersdefault.PotpissersDefault.claims;

public class PlayerOpenSignListener implements Listener {
    @EventHandler
    void onSignInteract(PlayerOpenSignEvent e) {
        Player p = e.getPlayer();
        Sign sign = e.getSign();
        if (claims.getWorld(sign.getWorld().getName()).containsKey(new ClaimCoordinate(sign.getLocation())) && !p.getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
            return;
        }
    }
}
