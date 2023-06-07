package ru.whbex.guilib.gui;

import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import ru.whbex.guilib.gui.icon.IconProvider;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Button {

    private String name = "Button";
    private IconProvider icon;
    private final Map<ClickType, ClickHandler> clickHandlers = new HashMap<>();
    private final Map<ClickType, Sound> soundSuccessMap = new HashMap<>();
    private final Map<ClickType, Sound> soundFailMap = new HashMap<>();
    private Button(){}

    public String getName() {
        return name;
    }

    public IconProvider getIcon() {
        return icon;
    }

    public ClickHandler getClickHandler(ClickType type) {
        return clickHandlers.get(type);
    }

    public Sound getSound(ClickType type, boolean success) {
        if(success)
            return soundSuccessMap.get(type);
        else
            return soundFailMap.get(type);
    }
    private Builder getBuilder(){
        return new Builder();
    }
    public static Builder builder(){
        return new Button().getBuilder();
    }
    public class Builder {
        private final Button inst = Button.this;

        private Builder(){}

        public Builder name(String n){
            inst.name = n;
            return this;
        }
        public Builder icon(IconProvider ip){
            inst.icon = ip;
            return this;
        }
        public Builder addClickHandler(ClickType type, ClickHandler handler){
            inst.clickHandlers.put(type, handler);
            return this;
        }
        public Builder addSoundFail(ClickType type, Sound sound){
            soundFailMap.put(type, sound);
            return this;
        }
        public Builder addSoundSuccess(ClickType type, Sound sound){
            soundSuccessMap.put(type, sound);
            return this;
        }
        public Button build(){
            return inst;
        }
    }
}
