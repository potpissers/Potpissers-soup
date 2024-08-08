package com.memeasaur.potpissersdefault.Util.Methods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class TabCompleteUtils {
    public static List<String> getOnlinePlayerList() {
        return List.of(Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toArray(String[]::new));
    }
}
