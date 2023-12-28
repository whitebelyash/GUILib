package ru.whbex.guilib.gui;

import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.whbex.guilib.Constants;
import ru.whbex.guilib.gui.click.ClickCallback;
import ru.whbex.guilib.gui.click.ClickHandler;
import ru.whbex.guilib.gui.click.ClickSound;
import ru.whbex.guilib.gui.icon.DynamicIconProvider;
import ru.whbex.guilib.gui.icon.IconProvider;
import ru.whbex.guilib.gui.icon.StaticIconProvider;
import ru.whbex.guilib.util.ExtraUtils;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Button. Contains icon and ClickHandler
 */
// TODO: Rework async !!! current system is fcking cringe lol
public class Button {

    private IconProvider icon;
    private IconProvider placeholder;
    private long throttle = ExtraUtils.asTicks(3);
    private ClickHandler handler;
    private boolean async = false;
    private Button(IconProvider iconProvider){
        this.icon = iconProvider;
    }

    public static Button fromItem(ItemStack i, ClickHandler h){
        return Button.builder(new DynamicIconProvider(ctx -> i)).addClickHandler(h).build();
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

    public ClickHandler getClickHandler() {
        return handler;
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
        public Builder addClickHandler(ClickHandler handler){
            inst.handler = handler;
            return this;
        }
        public Builder addClickHandler(ClickType type, ClickCallback callback, boolean async, int throttle){
            inst.handler = new  ClickHandler(null, async, throttle).addCallback(type, callback);
            return this;
        }
        public Builder addClickHandler(ClickType type, ClickCallback callback){
            return this.addClickHandler(type, callback, false, Constants.DEFAULT_CLICK_THROTTLE);
        }
        public Builder addClickHandler(ClickCallback callback){
            return this.addClickHandler(ClickType.LEFT, callback, false, Constants.DEFAULT_CLICK_THROTTLE);
        }
        public Builder addSoundFail(ClickType type, Sound sound){
            getClickHandler().sound().setFail(sound);
            return this;
        }
        public Builder addSoundSuccess(ClickType type, Sound sound){
            getClickHandler().sound().setSuccess(sound);
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
        public Button build(){
            return inst;
        }
    }
}
