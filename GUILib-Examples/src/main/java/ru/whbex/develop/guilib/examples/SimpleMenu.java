package ru.whbex.develop.guilib.examples;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.whbex.lib.gui.*;
import ru.whbex.lib.gui.click.ClickCallback;
import ru.whbex.lib.gui.icon.DynamicIconProvider;
import ru.whbex.lib.gui.icon.IconProvider;
import ru.whbex.lib.gui.icon.StaticIconProvider;
import ru.whbex.lib.gui.icon.StubIconProvider;
import ru.whbex.lib.gui.util.ExtraUtils;
import ru.whbex.lib.gui.util.ItemUtils;

/**
 * Simple menu. Just a pattern showcase
 */
public class SimpleMenu {
    private static final Pattern PATTERN = Pattern.fromStringArray(
            "#####a###",
            "####b####",
            "#c#d#e#f#");
    private static SimpleMenu inst;
    private static final IconProvider A_ICON = StaticIconProvider.builder()
            .name("A Button")
            .count(3)
            .material(Material.STONE)
            .build();
    private static final IconProvider B_ICON = StaticIconProvider.builder()
            .name("B Button")
            .count(2)
            .material(Material.REDSTONE)
            .build();
    private static final IconProvider C_ICON = new DynamicIconProvider((ctx -> ItemUtils.createItem("C Button", Material.BARRIER)));
    private static final IconProvider D_ICON = StaticIconProvider.builder()
            .name("D Button")
            .enchant()
            .hideTags()
            .count(64)
            .material(Material.END_STONE)
            .build();
    private static final IconProvider E_ICON = new StubIconProvider();
    private static final IconProvider F_ICON = new IconProvider() {
        @Override
        public ItemStack getIcon(GUIContext ctx) {
            return ItemUtils.createItem("F Button", Material.BEDROCK);
        }

        @Override
        public boolean requireContext() {
            return false;
        }
    };
    private static final ClickCallback CLICK_CALLBACK = (player, ctx) ->
            ctx.guiInstance().replaceTemp(ItemUtils.errBarrier("Click!"), ctx.pos(), ExtraUtils.asTicks(3), ctx);
    private final GUI gui;
    public SimpleMenu(){
        gui = GUI.builder().fromPattern(PATTERN)
            .map('a', Button.builder(A_ICON).addClickHandler(CLICK_CALLBACK).build())
            .map('b', Button.builder(B_ICON).addClickHandler(CLICK_CALLBACK).build())
            .map('c', Button.builder(C_ICON).addClickHandler(CLICK_CALLBACK).build())
            .map('d', Button.builder(D_ICON).addClickHandler(CLICK_CALLBACK).build())
            .map('e', Button.builder(E_ICON).addClickHandler(CLICK_CALLBACK).build())
            .map('f', Button.builder(F_ICON).addClickHandler(CLICK_CALLBACK).build())
            .name("SimpleMenu Example")
                .build();
    }
    public static SimpleMenu instance(){
        if(inst == null)
            inst = new SimpleMenu();
        return inst;
    }
    public GUI get(){
        return gui;
    }
}
