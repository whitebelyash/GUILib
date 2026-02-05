package ru.whbex.develop.guilib.examples;

import org.bukkit.Material;
import org.checkerframework.checker.units.qual.A;
import ru.whbex.lib.gui.Button;
import ru.whbex.lib.gui.GUI;
import ru.whbex.lib.gui.icon.DynamicIconProvider;
import ru.whbex.lib.gui.icon.IconProvider;
import ru.whbex.lib.gui.icon.StaticIconProvider;
import ru.whbex.lib.gui.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


/* GUI Meta test example */

/* GUI can also be created by extending base GUI class. Let's test that here */
public class GUIMetaExample {
    private static final String META_OBJ = "metaTest";
    private static final IconProvider a = new DynamicIconProvider(ctx -> {
        final boolean exist = ctx.guiInstance().getMeta().hasKey("metaexample");
        String name = exist ? "Meta exists!" : "Meta not found :(";
        List<String> lore = new ArrayList<>();
        if(exist && !ctx.guiInstance().getMeta().checkType("metaexample", String.class)){
            ctx.guiManager().logd("!!! Invalid meta object type !!!");
            lore.add("Failed to get meta string obj");
            return ItemUtils.createItem(name, lore, null, 1, Material.BARRIER, true);
        }
        if(exist) lore.add((String) ctx.guiInstance().getMeta().getObject("metaexample"));
        return ItemUtils.createItem(name, lore, null,64, Material.BEDROCK, true);
    });

    /* Icons */
    private static final IconProvider metaWipe = new StaticIconProvider("Wipe meta objects", null, 1, Material.BARRIER, false, false);
    private static final IconProvider next = new StaticIconProvider("next", null, 1, Material.PAPER, true, true);
    private static final IconProvider prev = new StaticIconProvider("prev", null, 1, Material.PAPER, true, true);

    /* Buttons */
    private static final Button metaWipeButton = Button.builder(metaWipe)
            .addClickHandler(((player, ctx) -> {
                String res = ctx.guiInstance().getMeta().wipe() ? "Removed all objs successfully" : "Map is empty";
                ctx.guiManager().logd(res);
                ctx.player().sendMessage(res);
            })).build();

    public static final GUIMetaFirst first = new GUIMetaFirst();
    public static final GUIMetaSecond second = new GUIMetaSecond();


    public static class GUIMetaFirst extends GUI {
        public GUIMetaFirst(){
            super(builder -> {
                builder.set(8, metaWipeButton);
                builder.set(0, Button.builder(a).addClickHandler(((player, ctx) -> {
                    ctx.guiManager().logd("Check next gui for something");
                })).build());
                builder.set(1, Button.builder(next).addClickHandler(((player, ctx) -> ctx.guiManager().open(player, second, true))).build());
            });
        }
    }
    public static class GUIMetaSecond extends GUI {

        public GUIMetaSecond() {
            super(builder -> {
                builder.set(8, metaWipeButton);
                builder.set(0, Button.builder(a).addClickHandler(((player, ctx) -> {
                    ctx.guiInstance().getMeta().addObject("metaexample", "Meta set from " + ctx.gui().getName(ctx));
                })).build());
                builder.set(1, Button.builder(prev).addClickHandler(((player, ctx) -> ctx.guiManager().open(player, first, true)))
                        .build());
            });
        }
    }
}
