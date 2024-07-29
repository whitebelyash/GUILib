package ru.whbex.lib.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.UUID;

public class GUIContext {
    public enum ContextType {
        OPEN,
        CLICK
    }
    private final GUIManager guiManager;
    private final GUI gui;
    private final GUIInstance gi;
    private boolean result = true;
    private int slot;
    private ClickType clickType;
    private final UUID uuid;
    private ContextType ctxType;

    public GUIContext(GUIManager guiManager, GUI gui, GUIInstance gi, int slot, ClickType clickType, UUID playerId, ContextType ctxType) {
        this.guiManager = guiManager;
        this.gui = gui;
        this.gi = gi;
        this.slot = slot;
        this.clickType = clickType;
        this.uuid = playerId;
        this.ctxType = ctxType;
    }

    public GUIManager guiManager() {
        return guiManager;
    }

    public GUI gui() {
        return gui;
    }
    public GUIInstance guiInstance(){
        return gi;
    }
    public void setClickResult(boolean result){
        this.result = result;
    }
    public boolean clickResult(){
        return this.result;
    }
    public int pos(){
        return slot;
    }
    public ClickType clickType(){
        return clickType;
    }
    public Player player(){
        return Bukkit.getPlayer(uuid);
    }
    public UUID getPlayerId() {
        return uuid;
    }
    public ContextType type() {
        return ctxType;
    }
    public void setSlot(int s){
        this.slot = s;
    }
    public void setContextType(ContextType ct){
        this.ctxType = ct;
    }
    public void setClickType(ClickType ct){
        this.clickType = ct;
    }
    void reset(){
        slot = 0;
        ctxType = null;
        clickType = null;
    }
}
