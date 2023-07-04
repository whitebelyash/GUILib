package ru.whbex.guilib.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.whbex.guilib.GUILib;
import ru.whbex.guilib.gui.Button;
import ru.whbex.guilib.gui.icon.IconProvider;
import ru.whbex.guilib.gui.icon.StaticIconProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemUtils {
    public static ItemStack createItem(String name,
                                       @Nullable List<String> lore,
                                       @Nullable Map<Enchantment, Integer> enchantments,
                                       int count,
                                       Material material,
                                       boolean hideTags
                                       ){
        ItemStack is = new ItemStack(material);
        if(enchantments != null){
            for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()){
                is.addUnsafeEnchantment(entry.getKey(), entry.getValue());
            }
        }
        is.setAmount(count);
        ItemMeta im = is.getItemMeta();
        if(im == null){
            GUILib.LOGGER.warning("Item meta is null (Material: " + material + ")");
            return is;
        }
        im.setDisplayName(ExtraUtils.color(name));
        if(hideTags)
            im.addItemFlags(ItemFlag.values());
        if(lore != null) im.setLore(lore.stream().map(ExtraUtils::color).collect(Collectors.toList()));
        is.setItemMeta(im);
        return is;
    }
    public static ItemStack createItem(String name, Material material){
        return createItem(name, null, null, 1, material, true);
    }
    public static Button errBarrier(String msg, String... lore){
        IconProvider barrier = StaticIconProvider.builder()
                .material(Material.BARRIER)
                .name("&c" + msg)
                .lore(false, lore)
                .build();
        return Button.builder(barrier).build();
    }
    public static Button commandDispatcher(IconProvider icon, boolean close, String... args){
        if(args.length < 1)
            throw new IllegalArgumentException("Args not provided");
        String cmd = args[0];
        if(Bukkit.getPluginCommand(cmd) == null)
            return Button.builder(icon)
                    .addClickHandler(((player, ctx) -> {
                        ctx.guiInstance().replaceTemp(errBarrier(Bukkit.spigot().getConfig().getString("messages.unknown-command",
                                "Unknown command!")), ctx.pos(), ExtraUtils.asTicks(2), ctx);
                    }))
                    .build();
        return Button.builder(icon)
                .addClickHandler(((player, ctx) -> {
                    Bukkit.dispatchCommand(player, String.join(" ", args));
                    if(close)
                        ctx.guiManager().close(player);
                    }))
                .build();
    }
}
