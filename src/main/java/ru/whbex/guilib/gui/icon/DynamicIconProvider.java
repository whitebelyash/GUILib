package ru.whbex.guilib.gui.icon;

import org.bukkit.inventory.ItemStack;
import ru.whbex.guilib.gui.GUIContext;

import java.util.function.Supplier;

public class DynamicIconProvider implements IconProvider {
    public DynamicIconProvider(Supplier<ItemStack> supplier){
        this.itemSupplier = supplier;
    }
    private final Supplier<ItemStack> itemSupplier;
    @Override
    public ItemStack getIcon(GUIContext ctx) {
        return itemSupplier.get();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
