package com.memeasaur.potpissersdefault.Commands.Warps;

import com.memeasaur.potpissersdefault.Classes.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.memeasaur.potpissersdefault.PotpissersDefault.playerDataMap;
import static com.memeasaur.potpissersdefault.PotpissersDefault.publicWarps;
import static com.memeasaur.potpissersdefault.Util.Methods.TabCompleteUtils.getOnlinePlayerList;

public class WarpsTabExecutor implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            switch (command.getName().toLowerCase()) {
                case "warp" -> {
                    if (strings.length == 1)
                        return List.of(publicWarps.keySet().toArray(new String[0]));
                }
                case "tpaccept" -> {
                    if (strings.length == 1) {
                        PlayerData data = playerDataMap.get(p.getUniqueId());
                        return List.of(data.tpaRequests.stream()
                                .map(uuid -> (Bukkit.getOfflinePlayer(uuid).getName()))
                                .toArray(String[]::new));
                    }
                }
                case "tphereaccept" -> {
                    if (strings.length == 1) {
                        PlayerData data = playerDataMap.get(p.getUniqueId());
                        return List.of(data.tpaHereRequests.stream()
                                .map(uuid -> (Bukkit.getOfflinePlayer(uuid).getName()))
                                .toArray(String[]::new));
                    }
                }
                case "tpa, tpahere" -> {
                    if (strings.length == 1) {
                        return getOnlinePlayerList();
                    }
                }
            }
        }
        return List.of();
    }
}
