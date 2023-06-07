package ru.whbex.guilib.gui;

public class GUIContext {
    private final GUIManager guiManager;
    private final GUI gui;
    private final GUIInstance gi;
    private boolean result = true;
    private final int slot;

    public GUIContext(GUIManager guiManager, GUI gui, GUIInstance gi, int slot) {
        this.guiManager = guiManager;
        this.gui = gui;
        this.gi = gi;
        this.slot = slot;
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
}
