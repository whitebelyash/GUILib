package ru.whbex.guilib.tests;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.whbex.guilib.GUILib;
import ru.whbex.guilib.gui.GUI;
import ru.whbex.guilib.gui.GUIInstance;

public class GuiInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length < 1){
            commandSender.sendMessage("Incorrect usage!");
            return true;
        }
        String n = strings[0];
        Player p = Bukkit.getPlayer(n);
        if(p == null){
            commandSender.sendMessage("Player " + n + " not found!");
            return true;
        }
        GUIInstance inv = GUILib.getInstance().getGUIManager().getGUIInstance(p);
        if(inv == null){
            commandSender.sendMessage("Player isn't in GUI");
            return true;
        }
        GUI gui = inv.getGui();
        String guiName = gui.getName();
        String msg = "--- " + n + "'s GUI ---" + '\n' +
                "- Name: " + guiName + '\n' +
                "- Size: " + gui.getSize() + '\n' +
                "- Buttons count: " + gui.getButtons().values().size() + '\n';
        commandSender.sendMessage(msg);
        return true;
    }
}
