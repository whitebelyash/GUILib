package ru.whbex.develop.guilib;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.whbex.develop.guilib.cmd.GuiCommand;
import ru.whbex.lib.gui.GUIManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public final class GUILib extends JavaPlugin {
    public static final boolean DEBUG = true;
    public static Logger LOGGER;
    private static GUILib instance;
    private final List<BukkitTask> tasks = new ArrayList<>();
    private GLConfig conf;

    @Override
    public void onEnable() {
        LOGGER = this.getLogger();
        instance = this;
        String startup = DEBUG ? "Startup (Debug enabled)" : "Startup!";
        getLogger().info(startup);
        this.saveDefaultConfig();
        conf = new GLConfig(this.getConfig());
        this.getCommand("glreload").setExecutor(new GuiCommand());

    }

    @Override
    public void onDisable() {
        tasks.forEach(BukkitTask::cancel);


    }

    public static GUILib getInstance() {
        return instance;
    }
    // TODO: rework this
    // bukkit api doesn't tell us, is the config reloaded successfully or i just didn't find anything about it.. nevermind
    public void reloadConf() {
        this.reloadConfig();
    }
    public GLConfig config(){
        return conf;
    }
}
