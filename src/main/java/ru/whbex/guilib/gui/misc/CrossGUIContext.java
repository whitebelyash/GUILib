package ru.whbex.guilib.gui.misc;


import ru.whbex.guilib.gui.GUIInstance;

import java.util.UUID;

/**
    How to Use:
    1. Extend this class
    2. Add your methods, fields, etc
    3. create cross context instance e.g. before opening new GUI
    3. call GUIInstance#setCrossContext() anywhere you want
    4. ????
    5. TBD
 */
public abstract class CrossGUIContext {
    protected final GUIInstance guiInstance;
    protected final UUID guiId;
    public CrossGUIContext(GUIInstance gi){
        this.guiInstance = gi;
        this.guiId = gi.getId();
    }

    public UUID getBoundId() {
        return guiId;
    }
}
