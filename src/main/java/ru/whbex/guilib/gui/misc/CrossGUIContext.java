package ru.whbex.guilib.gui.misc;


import ru.whbex.guilib.gui.GUIInstance;

public abstract class CrossGUIContext {
    protected final GUIInstance guiInstance;
    public CrossGUIContext(GUIInstance gi){
        this.guiInstance = gi;
    }

}
