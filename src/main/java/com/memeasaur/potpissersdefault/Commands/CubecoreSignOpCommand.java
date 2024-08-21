package com.memeasaur.potpissersdefault.Commands;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import static com.memeasaur.potpissersdefault.Util.Constants.ClaimsConstants.KEY_CUBECORE_CHEST;
import static com.memeasaur.potpissersdefault.Util.Constants.PotpissersConstants.KEY_CUBECORE_SIGN;

public class CubecoreSignOpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player p && command.getName().toLowerCase().equals("cubecoresign")) {
            if (strings.length == 0) {
                p.sendMessage("not args");
                return true;
            }
            Block block = p.getTargetBlockExact(4);
            if (block == null) {
                p.sendMessage("not block");
                return true;
            } else if (!(block.getState() instanceof Sign)) {
                p.sendMessage("not sign");
                return true;
            }
            Sign sign = (Sign) block.getState();
            StringBuilder stringBuilder = new StringBuilder().append("");
            for (String arg : strings) {
                stringBuilder.append(arg);
                stringBuilder.append(" ");
            }
            sign.getSide(Side.FRONT).line(0, Component.text("click here"));
            sign.getSide(Side.FRONT).line(1, Component.text("or type"));
            sign.getSide(Side.FRONT).line(2, Component.text("/" + stringBuilder));
            sign.getSide(Side.BACK).line(0, Component.text("click here"));
            sign.getSide(Side.BACK).line(1, Component.text("or type"));
            sign.getSide(Side.BACK).line(2, Component.text("/" + stringBuilder));
            sign.getPersistentDataContainer().set(KEY_CUBECORE_SIGN, PersistentDataType.STRING, stringBuilder.toString());
            sign.update();
            p.sendMessage("done");
            return true;
        }
        return false;
    }
}
