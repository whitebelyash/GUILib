package ru.whbex.guilib.gui.click;

import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import ru.whbex.guilib.Constants;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * ClickHandler. Nothing more
 */
public class ClickHandler {
    private final EnumMap<ClickType, ClickCallback> callbackMap = new EnumMap<>(ClickType.class);
    private ClickSound snd;
    private final boolean async;
    private final int throttle;
    public ClickHandler(ClickSound sound, boolean async, int throttle){
        this.snd = sound;
        this.async = async;
        this.throttle = throttle;
    }
    public ClickHandler(){
        this(null, false, Constants.DEFAULT_CLICK_THROTTLE);
    }

    public ClickCallback callback(ClickType ctype) {
        return callbackMap.getOrDefault(ctype, callbackMap.get(Constants.DEFAULT_CLICK_TYPE));
    }
    public ClickSound sound(){
        if(snd == null)
            snd = new ClickSound();
        return snd;
    }
    public ClickHandler addCallback(ClickType ctype, ClickCallback cb){
        callbackMap.put(ctype, cb);
        return this;
    }
    public ClickHandler addCallback(ClickCallback cb){
        callbackMap.put(Constants.DEFAULT_CLICK_TYPE, cb);
        return this;
    }
    public boolean async(){
        return async;
    }
    public int throttle(){
        return throttle;
    }
}
