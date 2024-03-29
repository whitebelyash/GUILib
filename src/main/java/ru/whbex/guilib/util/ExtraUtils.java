package ru.whbex.guilib.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;
import ru.whbex.guilib.gui.GUIContext;

public class ExtraUtils {
    public static final char DEFAULT_COLOR_CHAR = '&';
    public static String color(String input){
        return ChatColor.translateAlternateColorCodes(DEFAULT_COLOR_CHAR, input);
    }
    public static int arrayCharCount(char req, String... strings){
        int ret = 0;
        for(String s : strings){
            ret += s.chars().filter(c -> c == req).count();
        }
        return ret;
    }
    public static void runInMain(Plugin plugin, Runnable task){
        if(Bukkit.isPrimaryThread())
            task.run();
        else
            Bukkit.getScheduler().runTask(plugin, task);
    }
    public static long asTicks(long sec){
        return sec*20;
    }
    public static long asSecs(long ticks){
        return ticks/20;
    }
    public static int stringLengthArray(String... array){
        int ret = 0;
        for(String s : array){
            for(char c : s.toCharArray()){
                ret++;
            }
        }
        return ret;
    }
    public static GUIContext updateContext(GUIContext ctx, int pos, ClickType ctype, GUIContext.ContextType ctx_type){
        ctx.setSlot(pos);
        ctx.setClickType(ctype);
        ctx.setContextType(ctx_type);
        return ctx;
    }
}
