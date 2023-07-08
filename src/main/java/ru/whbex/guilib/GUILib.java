package ru.whbex.guilib;

import org.bukkit.plugin.java.JavaPlugin;
import ru.whbex.guilib.cmd.GuiCommand;
import ru.whbex.guilib.gui.GUIManager;

import java.util.logging.Logger;

public final class GUILib extends JavaPlugin {
    public static Logger LOGGER;
    private static GUILib instance;
    private GLConfig conf;

    @Override
    public void onEnable() {
        LOGGER = this.getLogger();
        instance = this;
        getLogger().info("Startup!");
        this.saveDefaultConfig();
        reloadConf();
        this.getCommand("glreload").setExecutor(new GuiCommand());

    }

    @Override
    public void onDisable() {

    }

    public static GUILib getInstance() {
        return instance;
    }
    public void reloadConf(){
        this.conf = new GLConfig(this.getConfig());
    }
    public GLConfig config(){
        return conf;
    }
}
