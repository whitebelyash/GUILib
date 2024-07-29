package ru.whbex.lib.gui;


import java.util.HashMap;
import java.util.Map;

/**
 * GUI Meta.
 */
public class GUIMeta {
    private final Map<String, Object> meta = new HashMap<>();

    public void addObject(String key, Object obj){
        meta.put(key, obj);
    }
    public Object getObject(String key){
        return meta.get(key);
    }
    public void removeObject(String key){
        meta.remove(key);
    }
    public Object removeObjectAndGet(String key){
        return meta.remove(key);
    }
    public boolean hasKey(String key){
        return meta.containsKey(key);
    }
}
