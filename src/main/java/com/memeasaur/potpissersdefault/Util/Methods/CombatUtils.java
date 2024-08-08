package com.memeasaur.potpissersdefault.Util.Methods;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import static com.memeasaur.potpissersdefault.PotpissersDefault.currentlyTaggedPlayers;
import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.CombatConstants.COMBAT_TAG;
import static com.memeasaur.potpissersdefault.Util.Constants.ScoreboardConstants.*;

public class CombatUtils {
    public static void doCombatTag(Player p, Plugin plugin) {
        PlayerData data = playerDataMap.get(p.getUniqueId());
        if (data.combatTag[0] > 0)
            data.combatTag[0] = COMBAT_TAG;
        else {
            currentlyTaggedPlayers.add(data.uuid);
            data.combatTag[0] = COMBAT_TAG;
            Scoreboard s = p.getScoreboard();
            Objective o = s.getObjective(SCOREBOARD_STRING);
            Team t = s.getTeam(SCOREBOARD_COMBAT);
            t.suffix(Component.text(COMBAT_TAG));
            o.getScore(SCOREBOARD_COMBAT).setScore(SCORE_COMBAT);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (data.combatTag[0] > 0)
                        data.combatTag[0]--;
                    if (data.combatTag[0] >= 60)
                        t.suffix(Component.text((data.combatTag[0] / 60) + "m" + data.combatTag[0] % 60 + "s"));
                    else
                        t.suffix(Component.text(data.combatTag[0]));
                    if (data.combatTag[0] == 0) {
                        // timers check
                        o.getScore(SCOREBOARD_COMBAT).resetScore();
                        currentlyTaggedPlayers.remove(data.uuid);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 20L, 20L);
        }
    }
}
