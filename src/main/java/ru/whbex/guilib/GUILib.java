package ru.whbex.guilib;

import org.bukkit.plugin.java.JavaPlugin;
import ru.whbex.guilib.gui.GUIManager;
import ru.whbex.guilib.tests.GuiInfoCommand;
import ru.whbex.guilib.tests.TestCommand;

import java.util.logging.Logger;

public final class GUILib extends JavaPlugin {
    public static Logger LOGGER;
    private static GUILib instance;
    private GUIManager gm;

    @Override
    public void onEnable() {
        LOGGER = this.getLogger();
        instance = this;
        getLogger().info("Startup!");
        gm = new GUIManager(this, null, true);
        gm.bindClick(-999, ((player, context) -> context.guiManager().close(player)));
        this.getCommand("guitesting").setExecutor(new TestCommand());
        this.getCommand("guiinfo").setExecutor(new GuiInfoCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static GUILib getInstance() {
        return instance;
    }

    public GUIManager getGUIManager() {
        return gm;
    }
}
