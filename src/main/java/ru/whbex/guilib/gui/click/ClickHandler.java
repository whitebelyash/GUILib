package ru.whbex.guilib.gui.click;

import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;

public class ClickHandler {
    private final ClickType clickType;
    private final ClickCallback callback;
    private final ClickSound snd;
    public ClickHandler(ClickType ctype, ClickCallback callback, ClickSound sound){
        this.clickType = ctype;
        this.callback = callback;
        this.snd = sound;
    }
    public ClickHandler(ClickType ctype, ClickCallback callback){
        this(ctype, callback, null);
    }
    public ClickHandler(ClickCallback callback){
        this(ClickType.LEFT, callback);
    }

    public ClickCallback callback() {
        return callback;
    }
    public ClickSound sound(){
        return snd;
    }
}
