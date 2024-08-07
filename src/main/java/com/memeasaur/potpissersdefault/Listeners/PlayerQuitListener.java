package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.LoggerData;
import com.memeasaur.potpissersdefault.Classes.PlayerData;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

import static com.memeasaur.potpissersdefault.PotpissersDefault.loggerDataMap;
import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.LoggerConstants.KEY_PIGLIN_LOGGER;

public class PlayerQuitListener implements Listener {
    private final PotpissersDefault plugin; // if cd doesn't expire when piglin logger alive, full cd when login
    public PlayerQuitListener(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @EventHandler // IF NOT TAGGED LOGGER LASTS 5S
    void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PlayerData data = playerDataMap.get(p.getUniqueId());
        if (p.getGameMode().equals(GameMode.SURVIVAL)) { // ADVENTURE DOESN't spawn logger
            Location location = p.getLocation();
            data.logger = (Piglin) location.getWorld().spawnEntity(location, EntityType.PIGLIN);
            Piglin piglinLogger = data.logger;

            getPlayerEntity(piglinLogger, p);

            piglinLogger.setAdult();
            piglinLogger.setImmuneToZombification(true);

            piglinLogger.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            piglinLogger.setHealth(p.getHealth());

            if (piglinLogger.getFallDistance() != 0) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (piglinLogger.isValid()) { // doesn't handle getting hit before hitting the ground perfectly
                            if (piglinLogger.getFallDistance() == 0) {
                                piglinLogger.setAI(false);
                                piglinLogger.setDancing(true);
                            }
                        } else cancel();
                    }
                }.runTaskTimer(plugin, 1L, 1L);
            } else {
                piglinLogger.setAI(false);
                piglinLogger.setDancing(true);
            }

            PlayerInventory pi = p.getInventory();
            loggerDataMap.put(piglinLogger, new LoggerData(p.getUniqueId(), pi.getContents(), p.getExp()));
            pi.clear();
            p.clearActivePotionEffects();
        }
    }
    public static void getPlayerWeapon(PlayerInventory pi, EntityEquipment ee) {
        ItemStack weapon = pi.getItemInMainHand();
        double highestWeaponDamage = 0;
        double iteratorWeaponDamage = 0;
        for (int i = 0; i <= 8; i++) {
            ItemStack is = pi.getItem(i);
            if (is != null) {
                ItemMeta im = is.getItemMeta();
                if (im != null && im.hasAttributeModifiers()) {
                    Collection<AttributeModifier> damageModifiers = im.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE);
                    if (damageModifiers != null)
                        for (AttributeModifier modifier : damageModifiers)
                            iteratorWeaponDamage += modifier.getAmount();
                    if (iteratorWeaponDamage > highestWeaponDamage) {
                        weapon = is;
                        highestWeaponDamage = iteratorWeaponDamage;
                    }
                }
            }
        }
        ee.setItemInMainHand(weapon);
    }
    public static void getPlayerEntity(LivingEntity entity, Player player) {
        entity.setCanPickupItems(false);
        entity.setCustomNameVisible(true);
        entity.customName(Component.text(player.getName()));
        entity.setKiller(player.getKiller()); // unsure if this works
        entity.setFireTicks(player.getFireTicks());
        entity.setArrowsInBody(player.getArrowsInBody());
        entity.setFreezeTicks(player.getFreezeTicks());
        entity.setVelocity(player.getVelocity());
        entity.setBeeStingersInBody(player.getBeeStingersInBody());
        entity.setBodyYaw(player.getBodyYaw());
        entity.setNoDamageTicks(player.getNoDamageTicks());
        entity.setPersistent(true);
        entity.setAbsorptionAmount(player.getAbsorptionAmount());
        entity.setFrictionState(player.getFrictionState());
        entity.setAbsorptionAmount(player.getAbsorptionAmount());
        entity.getPersistentDataContainer().set(KEY_PIGLIN_LOGGER, PersistentDataType.BOOLEAN, Boolean.TRUE);
        entity.addPotionEffects(player.getActivePotionEffects());
        entity.setFallDistance(player.getFallDistance());

        PlayerInventory pi = player.getInventory();
        EntityEquipment ee = entity.getEquipment();
        if (ee != null) {
            ee.setHelmet(pi.getHelmet());
            ee.setChestplate(pi.getChestplate());
            ee.setLeggings(pi.getLeggings());
            ee.setBoots(pi.getBoots());
        }
        // SIMULATE/TRACK DURABILITY LOSS
        getPlayerWeapon(pi, ee);
    }
}
