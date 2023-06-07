package ru.whbex.guilib.gui.icon;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.whbex.guilib.gui.GUIContext;

import java.util.List;

public interface IconProvider {
    ItemStack getIcon(GUIContext ctx);
    boolean isEmpty();

}
