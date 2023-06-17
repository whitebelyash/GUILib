package ru.whbex.guilib.gui;

import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import ru.whbex.guilib.gui.click.ClickCallback;
import ru.whbex.guilib.gui.click.ClickHandler;
import ru.whbex.guilib.gui.click.ClickSound;
import ru.whbex.guilib.gui.icon.IconProvider;

import java.util.HashMap;
import java.util.Map;

public class Button {

    private IconProvider icon;
    private final Map<ClickType, ClickHandler> clickHandlers = new HashMap<>();
    private final Map<ClickType, Sound> soundSuccessMap = new HashMap<>();
    private final Map<ClickType, Sound> soundFailMap = new HashMap<>();
    private Button(IconProvider iconProvider){
        this.icon = iconProvider;
    }


    public IconProvider getIconProvider() {
        return icon;
    }

    public ClickHandler getClickHandler(ClickType type) {
        return clickHandlers.get(type);
    }


    private Builder getBuilder(){
        return new Builder();
    }
    public static Builder builder(IconProvider iconProvider){
        return new Button(iconProvider).getBuilder();
    }
    public class Builder {
        private final Button inst = Button.this;

        private Builder(){}
        public Builder addClickHandler(ClickType type, ClickHandler handler){
            inst.clickHandlers.put(type, handler);
            return this;
        }
        public Builder addClickHandler(ClickType type, ClickCallback callback){
            inst.clickHandlers.put(type, new ClickHandler(type, callback));
            return this;
        }
        public Builder addSoundFail(ClickType type, Sound sound){
            getClickHandler(type).sound().setFail(sound);
            return this;
        }
        public Builder addSoundSuccess(ClickType type, Sound sound){
            getClickHandler(type).sound().setSuccess(sound);
            return this;
        }
        public Button build(){
            return inst;
        }
    }
}
