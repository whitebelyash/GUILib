package ru.whbex.guilib.gui.icon;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import ru.whbex.guilib.gui.GUIContext;
import ru.whbex.guilib.util.ItemUtils;

import java.util.*;

public class StaticIconProvider implements IconProvider {

    private StaticIconProvider(){}
    private ItemStack item;


    private Builder getBuilder(){
        return new Builder();
    }
    public static Builder builder(){
        return new StaticIconProvider().getBuilder();
    }

    @Override
    public ItemStack getIcon(GUIContext ctx) {
        return item;
    }

    @Override
    public boolean requireContext(){
        return false;
    }

    public class Builder {
        private String name = "Default icon name";
        private int count = 1;
        private List<String> lore = new ArrayList<>();
        private Material material = Material.STONE;
        private boolean ench = false;
        private boolean hideTags = false;
        private final StaticIconProvider inst = StaticIconProvider.this;
        private Builder(){}

        public Builder name(String n){
            this.name = n;
            return this;
        }
        public Builder lore(List<String> lore, boolean append){
            if(append){
                this.lore.addAll(lore);
            }
            else {
                this.lore = lore;
            }
            return this;
        }
        public Builder lore(boolean append, String... lore){
            if(append)
                this.lore.addAll(Arrays.asList(lore));
            else
                this.lore = new ArrayList<>(Arrays.asList(lore));
            return this;
        }
        public Builder appendLore(String l){
            this.lore.add(l);
            return this;
        }
        public Builder count(int c){
            this.count = c;
            return this;
        }
        public Builder material(Material mat){
            this.material = mat;
            return this;
        }
        public Builder hideTags(){
            hideTags = !hideTags;
            return this;
        }
        public Builder enchant(){
            ench = !ench;
            return this;
        }
        public StaticIconProvider build(){
            Map<Enchantment, Integer> ench = this.ench ? Collections.singletonMap(Enchantment.MENDING, 1) : null;
            inst.item = ItemUtils.createItem(name, lore, ench, count, material, hideTags);
            return inst;
        }
    }


}
