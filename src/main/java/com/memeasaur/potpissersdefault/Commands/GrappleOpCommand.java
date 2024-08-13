package com.memeasaur.potpissersdefault.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.memeasaur.potpissersdefault.Util.Methods.ItemUtils.doGrapple;

public class GrappleOpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p) {
            switch (command.getName().toLowerCase()) {
                case "getgrapple" -> {
                    if (strings.length != 0) {
                        p.sendMessage("?");
                        return true;
                    }
                    else {
                        p.getInventory().addItem(doGrapple());
                        p.sendMessage("done");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
