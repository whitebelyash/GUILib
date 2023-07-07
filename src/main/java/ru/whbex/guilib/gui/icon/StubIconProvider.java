package ru.whbex.guilib.gui.icon;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.whbex.guilib.gui.GUIContext;
import ru.whbex.guilib.util.ItemUtils;

/**
 * Stub icon provider. Returns a hardcoded ItemStack
 */
public class StubIconProvider implements IconProvider {
    public static final ItemStack STUB = ItemUtils.createItem("", Material.GRAY_STAINED_GLASS_PANE);

    @Override
    public ItemStack getIcon(GUIContext ctx) {
        return STUB;
    }

    @Override
    public boolean requireContext() {
        return false;
    }
}
