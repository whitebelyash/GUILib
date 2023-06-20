package ru.whbex.guilib.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;


public class GUIContext {
    public enum ContextType {
        OPEN,
        CLICK
    }
    private final GUIManager guiManager;
    private final GUI gui;
    private final GUIInstance gi;
    private boolean result = true;
    private final int slot;
    private final ClickType clickType;
    private final Player player;
    private final ContextType ctxType;

    public GUIContext(GUIManager guiManager, GUI gui, GUIInstance gi, int slot, ClickType clickType, Player player, ContextType ctxType) {
        this.guiManager = guiManager;
        this.gui = gui;
        this.gi = gi;
        this.slot = slot;
        this.clickType = clickType;
        this.player = player;
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
        return player;
    }
    public ContextType type() {
        return ctxType;
    }


    // TODO: Better way to get async info
    /**
     * Is GUIContext running asynchronously
     * @return boolean
     */
    public boolean async(){
        return Bukkit.isPrimaryThread();
    }
}
