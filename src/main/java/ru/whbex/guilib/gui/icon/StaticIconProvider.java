package ru.whbex.guilib.gui.icon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.whbex.guilib.gui.GUIContext;

import java.util.ArrayList;
import java.util.List;

public class StaticIconProvider implements IconProvider {
    private String name = "Default icon name";
    private int count = 1;
    private List<String> lore = new ArrayList<>();
    private Material material = Material.STONE;
    private StaticIconProvider(){}


    private Builder getBuilder(){
        return new Builder();
    }
    public static Builder builder(){
        return new StaticIconProvider().getBuilder();
    }

    @Override
    public ItemStack getIcon(GUIContext ctx) {
        ItemStack is = new ItemStack(material, count);
        ItemMeta im = Bukkit.getItemFactory().getItemMeta(material);
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f" + name));
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public class Builder {
        private final StaticIconProvider inst = StaticIconProvider.this;
        private Builder(){}

        public Builder name(String n){
            inst.name = n;
            return this;
        }
        public Builder lore(List<String> lore, boolean append){
            if(append){
                inst.lore.addAll(lore);
            }
            else {
                inst.lore = lore;
            }
            return this;
        }
        public Builder appendLore(String l){
            inst.lore.add(l);
            return this;
        }
        public Builder count(int c){
            inst.count = c;
            return this;
        }
        public Builder material(Material mat){
            inst.material = mat;
            return this;
        }
        public StaticIconProvider build(){
            return inst;
        }
    }


}
