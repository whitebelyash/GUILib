package ru.whbex.guilib.gui.click;

import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;

public class ClickHandler {
    private final ClickType clickType;
    private final ClickCallback callback;
    private ClickSound snd;
    private final boolean async;
    public ClickHandler(ClickType ctype, ClickCallback callback, ClickSound sound, boolean async){
        this.clickType = ctype;
        this.callback = callback;
        this.snd = sound;
        this.async = async;
    }
    public ClickHandler(ClickType ctype, ClickCallback callback){
        this(ctype, callback, null, false);
    }
    public ClickHandler(ClickCallback callback){
        this(ClickType.LEFT, callback, null,  false);
    }

    public ClickCallback callback() {
        return callback;
    }
    public ClickSound sound(){
        if(snd == null)
            snd = new ClickSound();
        return snd;
    }
    public boolean async(){
        return async;
    }
}
