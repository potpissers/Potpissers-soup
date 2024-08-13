package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import static com.memeasaur.potpissersdefault.PotpissersDefault.grappleHooks;
import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.CombatConstants.GRAPPLE_CD;
import static com.memeasaur.potpissersdefault.Util.Constants.ItemConstants.GRAPPLE_KEY;
import static com.memeasaur.potpissersdefault.Util.Constants.ScoreboardConstants.*;
import static com.memeasaur.potpissersdefault.Util.Methods.TimerUtils.doOrUpdateScoreboardTimer;

public class PlayerFishListener implements Listener {
    private final PotpissersDefault plugin;
    public PlayerFishListener(PotpissersDefault plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onFish(PlayerFishEvent e) {
        Player p = e.getPlayer();
        if (!e.getState().equals(org.bukkit.event.player.PlayerFishEvent.State.FISHING)) {
            if (p.getInventory().getItem(e.getHand()).getItemMeta().getPersistentDataContainer().has(GRAPPLE_KEY)) {
                FishHook hook = e.getHook();
                PlayerData data = playerDataMap.get(p.getUniqueId());
                if (hook.pullHookedEntity()) {
                    BoundingBox pBB = p.getBoundingBox();
                    p.setVelocity(p.getVelocity().add(e.getCaught().getBoundingBox().getCenter().subtract(new Vector(pBB.getCenterX(), pBB.getMinY(), pBB.getCenterZ())).multiply(0.25F)));
                    p.getWorld().playSound(p, Sound.ENTITY_ZOMBIE_INFECT, 1F, 1F);
                    return;
                }
                float radius = 0.1255F; // .25x.25x.25 -> .26x.26x.26 for ceilings/walls
                World world = hook.getWorld();
                Block block;
                Block blockChangeCheck = null;
                for (double x = -radius; x <= radius; x += radius)
                    for (double y = -radius; y <= radius; y += radius)
                        for (double z = -radius; z <= radius; z += radius) {
                            block = world.getBlockAt(hook.getBoundingBox().getCenter().toLocation(world).add(x, y, z));
                            if (blockChangeCheck == null || block.getType() != blockChangeCheck.getType()) {
                                if (block.isSolid()) {
                                    BoundingBox pBB = p.getBoundingBox();
                                    BoundingBox hookBB = hook.getBoundingBox();
                                    if (!p.isSneaking())
                                        p.setVelocity(p.getVelocity().add(new Vector(hookBB.getCenterX(), hookBB.getMaxY(), hookBB.getCenterZ()).subtract(new Vector(pBB.getCenterX(), pBB.getMinY(), pBB.getCenterZ())).multiply(0.25F)));
                                    else
                                        p.setVelocity(p.getVelocity().add(new Vector(hookBB.getCenterX(), hookBB.getMaxY(), hookBB.getCenterZ()).subtract(new Vector(pBB.getCenterX(), pBB.getMinY() - 0.35F, pBB.getCenterZ())).multiply(0.25F))); // sneaking = lower center of gravity

                                    ItemStack is = p.getInventory().getItem(e.getHand());
                                    Damageable im = (Damageable) is.getItemMeta();
                                    if (hook.isOnGround())
                                        im.setDamage(im.hasDamage() ? im.getDamage() + 3 : 3);
                                    else
                                        im.setDamage(im.hasDamage() ? im.getDamage() + 5 : 5);
                                    is.setItemMeta(im);

                                    world.playSound(p, Sound.ENTITY_ZOMBIE_INFECT, 1F, 1F);
                                    return;
                                }
                            }
                            blockChangeCheck = block;
                        }
            }
        } else {
            PersistentDataContainer pdc = p.getInventory().getItem(e.getHand()).getItemMeta().getPersistentDataContainer();
            if (pdc.has(GRAPPLE_KEY)) { // make hashset to accompany this
                grappleHooks.add(e.getHook());
                PlayerData data = playerDataMap.get(p.getUniqueId());
                if (data.movementCd[0] != 0) { // || (data.combatTag[0] == 0 && data.cooldowns.movementCdBuffer[0] > 0)
                    e.setCancelled(true);
                    p.sendMessage("movement cd active");
                    return;
                }
            }
        }
    }
}
