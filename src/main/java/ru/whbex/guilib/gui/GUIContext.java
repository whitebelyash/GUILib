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

    public void replaceLater(Button button, long time){
        if(type() == ContextType.OPEN)
            return;
        Bukkit.getScheduler().runTaskLater(guiManager.getPlugin(),
                () -> {
                    if(!guiManager().isHoldingSameGUI(player, gi))
                        return;
                    gi.setButton(slot, button, this);
                }, time);
    }
    /**
     * Temporarily replaces clicked button
     * @param button replace button
     * @param time how long it will be on screen in ticks
     */
    public void replaceTemp(Button button, long time){
        Button old = gui.getButton(slot);
        gi.setButton(slot, button, this);
        replaceLater(old, time);

    }
}
