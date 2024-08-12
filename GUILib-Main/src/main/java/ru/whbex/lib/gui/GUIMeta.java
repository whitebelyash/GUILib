package ru.whbex.lib.gui;


import java.util.HashMap;
import java.util.Map;

/**
 * GUI Meta. Used for storing key-object pairs for GUIInstance.
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
    public boolean hasObject(Object object){
        return meta.containsValue(object);
    }

    /**
     * Check object type stored in meta
     * @param key object key
     * @param clazz object type
     * @return is specified object instance of provided type
     */
    public boolean checkType(String key, Class<?> clazz){
        return hasKey(key) && clazz.isInstance(meta.get(key));
    }

    public void wipe(){
        meta.clear();
    }
}
