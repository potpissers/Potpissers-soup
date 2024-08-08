package com.memeasaur.potpissersdefault.Commands.Duels;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.memeasaur.potpissersdefault.PotpissersDefault.defaultKits;
import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.Util.Constants.DuelsConstants.CPS_STRING_MAP;
import static com.memeasaur.potpissersdefault.Util.Methods.TabCompleteUtils.getOnlinePlayerList;

public class DuelsTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            switch (command.getName().toLowerCase()) {
                case "duel" -> {
                    switch (strings.length) {
                        case 1 -> {
                            return getOnlinePlayerList();
                        }
                        case 2 -> {
                            return Stream.concat(defaultKits.keySet().stream(), CPS_STRING_MAP.keySet().stream()).collect(Collectors.toList());
                        }
                        case 3 -> {
                            if (defaultKits.containsKey(strings[1]))
                                return List.of(CPS_STRING_MAP.keySet().toArray(new String[0]));
                            if (CPS_STRING_MAP.containsKey(strings[1]))
                                return List.of(defaultKits.keySet().toArray(new String[0]));
                        }
                    }
                }
                case "accept" -> {
                    if (strings.length == 1) {
                        PlayerData data = playerDataMap.get(p.getUniqueId());
                        if (data.partyReference.get() == null)
                            return List.of(data.duelRequests.keySet().stream()
                                    .map(Bukkit::getPlayer)
                                    .filter(Objects::nonNull)
                                    .map(Player::getName)
                                    .toArray(String[]::new));
//                        else {
//                            Party party = data.partyReference.get();
//                            if (party.leader == p.getUniqueId())
//                                return List.of(data.partyReference.get().partyDuelRequests.keySet().stream()
//                                        .map(party -> (Bukkit.getOfflinePlayer(party.leader).getName()))
//                                        .toArray(String[]::new));
//                        }
                    }
                }
                case "anon" -> {}
            }
        }
        return List.of();
    }
}
