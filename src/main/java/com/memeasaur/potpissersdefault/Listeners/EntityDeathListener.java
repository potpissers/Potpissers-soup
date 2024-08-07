package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.LoggerData;
import com.memeasaur.potpissersdefault.Classes.LoggerUpdate;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static com.memeasaur.potpissersdefault.PotpissersDefault.*;
import static com.memeasaur.potpissersdefault.Util.Constants.LoggerConstants.KEY_PIGLIN_LOGGER;
import static com.memeasaur.potpissersdefault.Util.Methods.LoggerUtils.doTotemLogger;

public class EntityDeathListener implements Listener {
    @EventHandler
    void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Piglin piglin && piglin.getPersistentDataContainer().has(KEY_PIGLIN_LOGGER)) {
            e.getDrops().clear();
            e.setDroppedExp(0);
            if (loggerDataMap.containsKey(piglin)) {
                LoggerData piglinData = loggerDataMap.get(piglin);
                for (ItemStack is : piglinData.playerInventory)
                    if (is != null && is.getType().equals(Material.TOTEM_OF_UNDYING)) {
                        doTotemLogger(piglin);
                        e.setCancelled(true); // cancel eat?
                        e.setReviveHealth(1);
                        is.setAmount(is.getAmount() - 1);
                        return;
                    }
                loggerUpdateMap.put(piglinData.u, new LoggerUpdate(piglin.getLocation(), 0, new ItemStack[0]));
                e.setDroppedExp((int) piglinData.exp);
                Collections.addAll(e.getDrops(), piglinData.playerInventory);
                loggerDataMap.remove(piglin);
                Bukkit.broadcast(Component.text(piglin.getName() + " has been slain"));
            }
        }
    }

}
