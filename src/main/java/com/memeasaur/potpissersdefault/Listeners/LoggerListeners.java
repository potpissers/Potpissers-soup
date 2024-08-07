package com.memeasaur.potpissersdefault.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Piglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;

import static com.memeasaur.potpissersdefault.PotpissersDefault.loggerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.LoggerConstants.KEY_PIGLIN_LOGGER;

public class LoggerListeners implements Listener {
    @EventHandler
    void onEntityLoad(EntitiesLoadEvent e) {
        for (Entity entity : e.getEntities()) {
            if (entity instanceof Piglin piglin && piglin.getPersistentDataContainer().has(KEY_PIGLIN_LOGGER) && !loggerDataMap.containsKey(piglin)) {
                piglin.remove();
            }
        }
    }
}
