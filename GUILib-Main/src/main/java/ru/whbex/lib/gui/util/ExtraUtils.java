package ru.whbex.lib.gui.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;
import ru.whbex.lib.gui.GUIContext;

public class ExtraUtils {
    public static final char DEFAULT_COLOR_CHAR = '&';
    public static String color(String input){
        return ChatColor.translateAlternateColorCodes(DEFAULT_COLOR_CHAR, input);
    }
    // TODO: Think about this
    public static int arrayCharCount(char req, String... strings){
        int ret = 0;
        for(String s : strings){
            ret += s.chars().filter(c -> c == req).count();
        }
        return ret;
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
