package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;

public class PlayerMoveListener implements Listener {
    @EventHandler
    void onPlayerMoveCubecore(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = playerDataMap.get(p.getUniqueId());
        if (data.frozen) {
            e.setCancelled(true);
            return;
        }
    }
}
