package ru.whbex.develop.guilib.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.whbex.develop.guilib.GUILib;
import ru.whbex.develop.guilib.examples.GUIMetaExample;
import ru.whbex.develop.guilib.examples.SimpleMenu;
import ru.whbex.lib.gui.GUIContext;
import ru.whbex.lib.gui.util.ExtraUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

// /guilib

public class GuiCommand implements CommandExecutor {
    private static final String RELOAD_PERMISSION = "guilib.reload";
    private final Map<String, Consumer<CommandSender>> commands = new HashMap<>();

    public GuiCommand(){
        commands.put("reload", this::reload);
        commands.put("simple", this::simple);
        commands.put("meta", this::meta);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1 || !commands.containsKey(args[0])){
            sender.sendMessage('/' + command.getName() + " " + String.join("|", commands.keySet()));
            return true;
        }
        commands.get(args[0]).accept(sender);
        return false;
    }
    // this is useless
    private void reload(CommandSender s){
        if(!s.hasPermission(RELOAD_PERMISSION)){
            s.sendMessage(ExtraUtils.color("&cNot enough permissions to run this command"));
            return;
        }
        GUILib.getInstance().reloadConf();
        s.sendMessage(ExtraUtils.color("&aConfiguration reloaded"));
    }

    private void simple(CommandSender s){
        if(!(s instanceof Player)){
            throw new IllegalStateException("Player required");
        }
        GUILib.getInstance().guiManager().open(((Player) s), SimpleMenu.instance().get(), false);
    }
    private void meta(CommandSender s){
        if(!(s instanceof Player)){
            throw new IllegalStateException("Player required");
        }
        GUILib.getInstance().guiManager().open(((Player) s), GUIMetaExample.first, false);
    }
}
