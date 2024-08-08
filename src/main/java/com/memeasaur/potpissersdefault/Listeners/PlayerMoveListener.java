package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Methods.TimerUtils.handleTimerCancel;

public class PlayerMoveListener implements Listener {
    @EventHandler
    void onPlayerMoveCubecore(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = playerDataMap.get(p.getUniqueId());
        if (data.frozen) {
            e.setCancelled(true);
            return;
        }
        // Logout start
        if (p.isSwimming()) {
            if (data.combatTag[0] != 0)
                p.setPose(Pose.STANDING);
            else
                p.setVelocity(p.getLocation().getDirection().multiply(0.07875F));
        }
        else if (p.isGliding() && data.combatTag[0] != 0)
            p.setPose(Pose.STANDING);
        if (e.hasChangedBlock()) {
            handleTimerCancel(data, p);
        }
        // Logout end
    }
}
