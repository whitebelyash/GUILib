package ru.whbex.lib.gui.icon;

import org.bukkit.inventory.ItemStack;
import ru.whbex.lib.gui.GUIContext;


public interface IconProvider {
    ItemStack getIcon(GUIContext ctx);
    boolean requireContext();

}
