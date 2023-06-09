package ru.whbex.guilib;

import org.bukkit.plugin.java.JavaPlugin;
import ru.whbex.guilib.gui.GUIManager;

import java.util.logging.Logger;

public final class GUILib extends JavaPlugin {
    public static Logger LOGGER;
    private static GUILib instance;

    @Override
    public void onEnable() {
        LOGGER = this.getLogger();
        instance = this;
        getLogger().info("Startup!");

    }

    @Override
    public void onDisable() {

    }

    public static GUILib getInstance() {
        return instance;
    }
}
