package com.memeasaur.potpissersdefault.Listeners;

import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.KEY_CUBECORE_CHEST;

public class InventoryClickListener implements Listener {
    @EventHandler
    void onInventoryClick(InventoryClickEvent e) {
        Inventory clickInv = e.getClickedInventory();
        HumanEntity p = e.getWhoClicked();
        ItemStack is = e.getCurrentItem();
        if (clickInv != null && is != null && !p.getGameMode().isInvulnerable())
            switch (clickInv.getType()) {
                case PLAYER -> {
                    if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)
                            && (e.getInventory().getHolder() instanceof DoubleChest doubleChest
                            && (((Chest) doubleChest.getLeftSide()).getPersistentDataContainer().has(KEY_CUBECORE_CHEST) || ((Chest) doubleChest.getRightSide()).getPersistentDataContainer().has(KEY_CUBECORE_CHEST))
                            || (e.getInventory().getHolder() instanceof Chest chest && chest.getPersistentDataContainer().has(KEY_CUBECORE_CHEST))))
                        e.setCancelled(true);
                }
                case CHEST -> {
                    if (e.getInventory().getHolder() instanceof DoubleChest doubleChest
                            && (((Chest) doubleChest.getLeftSide()).getPersistentDataContainer().has(KEY_CUBECORE_CHEST) || ((Chest) doubleChest.getRightSide()).getPersistentDataContainer().has(KEY_CUBECORE_CHEST))
                            || (e.getInventory().getHolder() instanceof Chest chest && chest.getPersistentDataContainer().has(KEY_CUBECORE_CHEST))) {
                        e.setCancelled(true);
                        p.getInventory().addItem(is);
                    }
                }
            }
    }
}
