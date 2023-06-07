package ru.whbex.guilib.gui.icon;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.whbex.guilib.gui.GUIContext;

public class EmptyIconProvider implements IconProvider {
    public static final Material MATERIAL = Material.AIR;

    @Override
    public ItemStack getIcon(GUIContext ctx) {
        return new ItemStack(MATERIAL);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
