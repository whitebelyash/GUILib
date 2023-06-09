package ru.whbex.guilib.gui.icon;

import org.bukkit.inventory.ItemStack;
import ru.whbex.guilib.gui.GUIContext;


public interface IconProvider {
    ItemStack getIcon(GUIContext ctx);
    boolean isEmpty();

}
