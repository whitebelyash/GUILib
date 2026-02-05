package ru.whbex.lib.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import ru.whbex.lib.gui.util.Constants;
import ru.whbex.lib.gui.click.ClickCallback;
import ru.whbex.lib.gui.click.ClickHandler;
import ru.whbex.lib.gui.icon.DynamicIcon;
import ru.whbex.lib.gui.icon.Icon;
import ru.whbex.lib.gui.util.ExtraUtils;

/**
 * Button. Contains icon and ClickHandler
 */
// TODO: Rework async !!! current system is fcking cringe lol
public class Button {

    private Icon icon;
    private long throttle = ExtraUtils.asTicks(3);
    private ClickHandler handler;
    private Button(Icon icon){
        this.icon = icon;
    }

    public static Button fromItem(ItemStack i, ClickHandler h){
        return Button.builder(new DynamicIcon(ctx -> i)).addClickHandler(h).build();
    }


    public Icon getIconProvider() {
        return icon;
    }

    public ClickHandler getClickHandler() {
        return handler;
    }


    private Builder getBuilder(){
        return new Builder();
    }
    public static Builder builder(Icon icon){
        return new Button(icon).getBuilder();
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
            return this.addClickHandler(Constants.DEFAULT_CLICK_TYPE, callback, false, Constants.DEFAULT_CLICK_THROTTLE);
        }
        public Button build(){
            return inst;
        }
    }
}
