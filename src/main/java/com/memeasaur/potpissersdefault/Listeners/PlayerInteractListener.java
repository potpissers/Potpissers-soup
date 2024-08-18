package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.KEY_CUBECORE_CHEST;
import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.SPAWN_CLAIM;
import static com.memeasaur.potpissersdefault.Util.Constants.ItemConstants.GRAPPLE_KEY;

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
            // Grapple start
            ItemStack is = e.getItem();
            if (is != null)
                switch (is.getType()) {
                    case FISHING_ROD -> {
                        PlayerData data = playerDataMap.get(p.getUniqueId());
                        if (is.getItemMeta().getPersistentDataContainer().has(GRAPPLE_KEY)) {
                            if (data.movementCd[0] > 0) { // && data.combatTag[0] > 0 && data.cooldowns.movementCdBuffer[0] == 0)
                                e.setCancelled(true);
                                p.sendMessage("grapple cd active");
                                return;
                            }
                        }
                    }
                    // Movement cooldown start
                    case BOW -> {
                        PlayerData data = playerDataMap.get(p.getUniqueId());
                        if (is.getEnchantmentLevel(Enchantment.PUNCH) > 0 && data.movementCd[0] != 0) {
                            e.setCancelled(true);
                            p.sendActionBar(Component.text("movement cd active"));
                            return;
                        }
                    }
                    // Movement cooldown end
                }
            // Grapple end
        }
    }
}
