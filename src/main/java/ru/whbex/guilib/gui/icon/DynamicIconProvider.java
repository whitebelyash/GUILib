package ru.whbex.guilib.gui.icon;

import org.bukkit.inventory.ItemStack;
import ru.whbex.guilib.gui.GUIContext;

public class DynamicIconProvider implements IconProvider {
    @Override
    public ItemStack getIcon(GUIContext ctx) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
