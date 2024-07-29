package ru.whbex.develop.guilib.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.whbex.develop.guilib.GUILib;
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
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1 || !commands.containsKey(args[0])){
            sender.sendMessage('/' + command.getName() + " " + String.join("|", commands.keySet()));
        }
        commands.get(args[0]).accept(sender);
        return false;
    }

    private void reload(CommandSender s){
        if(!s.hasPermission(RELOAD_PERMISSION)){
            s.sendMessage(ExtraUtils.color("&cNot enough permissions to run this command"));
            return;
        }
        GUILib.getInstance().reloadConf();
        s.sendMessage(ExtraUtils.color("&aConfiguration reloaded"));
    }
}
