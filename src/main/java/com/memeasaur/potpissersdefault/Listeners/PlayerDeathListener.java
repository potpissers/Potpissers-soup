package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;

public class PlayerDeathListener implements Listener {
    @EventHandler
    void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        PlayerData data = playerDataMap.get(p.getUniqueId());
        data.combatTag[0] = 0;
    }
}
