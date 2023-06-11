package ru.whbex.guilib.gui.icon;

import org.bukkit.inventory.ItemStack;
import ru.whbex.guilib.gui.GUIContext;

import java.util.function.Function;

public class DynamicIconProvider implements IconProvider {
    public DynamicIconProvider(Function<GUIContext, ItemStack> supplier){
        this.itemSupplier = supplier;
    }
    private final Function<GUIContext, ItemStack> itemSupplier;
    @Override
    public ItemStack getIcon(GUIContext ctx) {
        return itemSupplier.apply(ctx);
    }

    @Override
    public boolean requireContext(){
        return true;
    }
}
