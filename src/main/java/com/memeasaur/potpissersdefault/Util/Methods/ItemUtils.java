package com.memeasaur.potpissersdefault.Util.Methods;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Random;

import static com.memeasaur.potpissersdefault.Util.Constants.ItemConstants.GRAPPLE_KEY;
import static com.memeasaur.potpissersdefault.Util.Constants.ItemConstants.GRAPPLE_LORE;

public class ItemUtils {
    public static ItemStack doGrapple() {
        ItemStack grapple = new ItemStack(Material.FISHING_ROD);
        ItemMeta im = grapple.getItemMeta();
        im.lore(List.of(Component.text(GRAPPLE_LORE.get(new Random().nextInt(GRAPPLE_LORE.size())))));
        im.displayName(Component.text("Grapple").decoration(TextDecoration.ITALIC, false));
        im.setRarity(ItemRarity.RARE);
        im.getPersistentDataContainer().set(GRAPPLE_KEY, PersistentDataType.BOOLEAN, Boolean.TRUE);
        grapple.setItemMeta(im);
        return grapple;
    }
}
