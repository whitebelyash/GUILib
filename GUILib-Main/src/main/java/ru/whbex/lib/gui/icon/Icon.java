package ru.whbex.lib.gui.icon;

import org.bukkit.inventory.ItemStack;
import ru.whbex.lib.gui.GUIContext;


public interface Icon {
    ItemStack getIcon(GUIContext ctx);
    boolean requireContext();

}
