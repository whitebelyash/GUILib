package ru.whbex.guilib.gui;

import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import ru.whbex.guilib.gui.click.ClickCallback;
import ru.whbex.guilib.gui.click.ClickHandler;
import ru.whbex.guilib.gui.click.ClickSound;
import ru.whbex.guilib.gui.icon.IconProvider;
import ru.whbex.guilib.util.ExtraUtils;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Button. Contains icon and ClickHandler
 */
public class Button {

    private IconProvider icon;
    private IconProvider placeholder;
    private long throttle = ExtraUtils.asTicks(3);
    private final Map<ClickType, ClickHandler> clickHandlers = new EnumMap<>(ClickType.class);
    private boolean async = false;
    private Button(IconProvider iconProvider){
        this.icon = iconProvider;
    }


    public IconProvider getIconProvider() {
        return icon;
    }
    public boolean async(){
        return async;
    }
    public boolean hasPlaceholder(){
        return placeholder != null;
    }
    public IconProvider getPlaceholder(){
        return placeholder;
    }

    public ClickHandler getClickHandler(ClickType type) {
        return clickHandlers.get(type);
    }
    public long getThrottle(){
        return throttle;
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
        public Builder addClickHandler(ClickType type, ClickCallback callback, boolean async){
            inst.clickHandlers.put(type, new ClickHandler(type, callback, null, async));
            return this;
        }
        public Builder addClickHandler(ClickType type, ClickCallback callback){
            return this.addClickHandler(type, callback, false);
        }
        public Builder addClickHandler(ClickCallback callback){
            return this.addClickHandler(ClickType.LEFT, callback, false);
        }
        public Builder addSoundFail(ClickType type, Sound sound){
            getClickHandler(type).sound().setFail(sound);
            return this;
        }
        public Builder addSoundSuccess(ClickType type, Sound sound){
            getClickHandler(type).sound().setSuccess(sound);
            return this;
        }
        public Builder async(){
            inst.async = !inst.async;
            return this;
        }
        public Builder placeholder(IconProvider iconProvider){
            inst.placeholder = iconProvider;
            return this;
        }
        public Builder throttle(long t){
            if(t < 0)
                throw new IllegalArgumentException("Throttle must be a positive number or 0");
            inst.throttle = t;
            return this;
        }
        public Button build(){
            return inst;
        }
    }
}
