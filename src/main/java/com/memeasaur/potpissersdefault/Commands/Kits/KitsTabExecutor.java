package com.memeasaur.potpissersdefault.Commands.Kits;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.memeasaur.potpissersdefault.PotpissersDefault.defaultKits;

public class KitsTabExecutor implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            switch (command.getName().toLowerCase()) {
                case "loaddefault", "anon" -> {
                    if (strings.length == 1)
                        return List.of(defaultKits.keySet().toArray(new String[0]));
                }
            }
        }
        return List.of();
    }
}
