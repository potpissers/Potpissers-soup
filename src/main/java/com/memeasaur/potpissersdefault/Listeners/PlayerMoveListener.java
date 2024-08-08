package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.ClaimCoordinate;
import com.memeasaur.potpissersdefault.Classes.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Methods.TimerUtils.handleTimerCancel;
import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.PotpissersDefault.worldBorderRadius;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.SPAWN_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.WILDERNESS_CLAIM;

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
            // Claims start
            Location location = e.getTo();
            String toClaim = claims.getWorld(p.getWorld().getName()).getOrDefault(new ClaimCoordinate(location), WILDERNESS_CLAIM);
            if (!toClaim.equals(data.currentClaim)) {
                switch (toClaim) {
                    case SPAWN_CLAIM -> {
                        if (data.combatTag[0] != 0) {
                            e.setCancelled(true);
                            p.sendMessage("movement cancelled: combat tagged");
                            return;
                        }
                        else {
                            p.setCollidable(false);
                        }
                    }
                    case WILDERNESS_CLAIM -> {
                        if (!p.getGameMode().equals(GameMode.CREATIVE)) {
                            if (Math.abs(location.getX()) > worldBorderRadius || Math.abs(location.getZ()) > worldBorderRadius) {
                                e.setCancelled(true);
                                p.sendMessage("cancelled (out of bounds)");
                                return;
                            }
                            else {
                                p.setCollidable(true);
                            }
                        }
                    }
                }
                if (data.currentClaim.equals(SPAWN_CLAIM) && (data.isQueued || (data.partyReference.get() != null && data.partyReference.get().isQueued))) {
                    e.setCancelled(true);
                    p.sendMessage("cancelled (currently queued)");
                    return;
                }
                p.sendMessage(toClaim + " claim entered");
                data.currentClaim = toClaim;
            }
            // Claims end
            handleTimerCancel(data, p);
        }
        // Logout end
    }
}
