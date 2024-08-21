package com.memeasaur.potpissersdefault.Listeners;

import com.memeasaur.potpissersdefault.Classes.Party;
import com.memeasaur.potpissersdefault.Classes.PlayerData;
import com.memeasaur.potpissersdefault.PotpissersDefault;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.CombatConstants.ATTACK_SPEED_DEFAULT;

public class PlayerDeathListener implements Listener {
    private final PotpissersDefault plugin;
    public PlayerDeathListener(PotpissersDefault plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getPlayer();
        PlayerData data = playerDataMap.get(p.getUniqueId());
        data.combatTag[0] = 0;
        // Lightning start
        p.getWorld().strikeLightningEffect(p.getLocation());
        // Lightning end
        // Dueling start
        Party party = data.partyReference.get();
        if (party != null && party.isDueling != null) {
            party.isAliveDueling.remove(p.getUniqueId());
            p.setRespawnLocation(p.getLocation());
            p.setGameMode(GameMode.SPECTATOR);
            if (party.isAliveDueling.isEmpty()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location spawn = Bukkit.getWorld("world").getSpawnLocation();
                        Component loserMsg = Component.text("Winner: " + party.isDueling.name).appendNewline().append(Component.text("Loser: " + party.name));
                        for (UUID u : party.roster.keySet()) {
                            Player player = Bukkit.getPlayer(u);
                            if (player != null) {
                                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(ATTACK_SPEED_DEFAULT);
                                player.setGameMode(GameMode.SURVIVAL);
                                player.setRespawnLocation(spawn);
                                player.teleport(spawn); // check if player is alive somehow, so I can let respawn screens stay where they are
                                playerDataMap.get(player.getUniqueId()).combatTag[0] = 0;
                                player.sendMessage(loserMsg);
                            }
                        }
                        Component winnerMsg = Component.text("Winner: " + party.isDueling.name).appendNewline().append(Component.text("Loser: " + party.name));
                        for (UUID u : party.isDueling.roster.keySet()) {
                            Player player = Bukkit.getPlayer(u);
                            if (player != null) {
                                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(ATTACK_SPEED_DEFAULT);
                                player.setGameMode(GameMode.SURVIVAL);
                                player.setRespawnLocation(spawn);
                                player.teleport(spawn);
                                playerDataMap.get(player.getUniqueId()).combatTag[0] = 0;
                                player.sendMessage(winnerMsg);
                            }
                        }
                        Party partyArg = party.isDueling;
                        party.isDueling = null;
                        partyArg.isDueling = null;
                        party.isAliveDueling.clear();
                        partyArg.isAliveDueling.clear();
                    }
                }.runTaskLater(plugin, 60L);
            }
        }
        else if (data.isDueling != null) {
            List<ItemStack> drops = e.getDrops();
            p.setRespawnLocation(p.getLocation());
            p.setGameMode(GameMode.SPECTATOR);
            PlayerData dataArg = playerDataMap.get(data.isDueling);
            dataArg.isDueling = null;
            dataArg.combatTag[0] = 0;
            Player pWinner = Bukkit.getPlayer(data.isDueling);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (pWinner != null) {
                        Location spawn = Bukkit.getWorld("world").getSpawnLocation();
                        pWinner.teleport(spawn);
                        pWinner.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(ATTACK_SPEED_DEFAULT);
                        p.teleport(spawn);
                        p.setRespawnLocation(spawn);
                        p.setGameMode(GameMode.SURVIVAL);
                        // drops entities, arrows ,pots ,etc
                    }
                }
            }.runTaskLater(plugin, 60L);
            data.isDueling = null;
            p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(ATTACK_SPEED_DEFAULT);
        }
        else
            p.setRespawnLocation(Bukkit.getWorld("world").getSpawnLocation());
        // Dueling end
    }
}
