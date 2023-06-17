package ru.whbex.guilib.util;

import org.bukkit.ChatColor;

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
}
