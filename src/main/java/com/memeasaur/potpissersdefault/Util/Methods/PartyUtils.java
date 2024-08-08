package com.memeasaur.potpissersdefault.Util.Methods;

import com.memeasaur.potpissersdefault.Classes.Party;
import com.memeasaur.potpissersdefault.Classes.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.memeasaur.potpissersdefault.PotpissersDefault.teams;

public class PartyUtils {
    public static void doRosterMsg(Party party, Component msg) {
        for (UUID u : party.roster.keySet()) {
            Player p = Bukkit.getPlayer(u);
            if (p != null)
                p.sendMessage(msg);
        }
    }
    public static Party doPartyCreate(Player p, PlayerData data) {
        Party party = new Party(data.uuid);
        data.partyReference = party.selfWeakReference;
        teams.add(party);
        p.sendMessage(Component.text("party created"));
        return party;
    }
    public static void doNeutral(Party party, Party partyArg, boolean isAlly, Component msg, Component msgArg) {
        if (!isAlly) {
            party.enemies.remove(partyArg);
            doRosterMsg(party, msg);
            doRosterMsg(partyArg, msgArg);
        }
        else {
            party.allies.remove(partyArg);
            partyArg.allies.remove(party);
            doRosterMsg(party, msg);
            doRosterMsg(partyArg, msgArg);
        }
    }
}
