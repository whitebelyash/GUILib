package ru.whbex.guilib.gui.icon;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.whbex.guilib.gui.GUIContext;
import ru.whbex.guilib.util.ItemUtils;

import java.util.ArrayList;
import java.util.List;
// TODO: Implement enchantments
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
        public StaticIconProvider build(){
            inst.item = ItemUtils.createItem(name, lore, null, count, material);
            return inst;
        }
    }


}
