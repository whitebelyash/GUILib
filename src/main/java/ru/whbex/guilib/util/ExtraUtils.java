package ru.whbex.guilib.util;

import org.bukkit.ChatColor;

public class ExtraUtils {
    public static final char DEFAULT_COLOR_CHAR = '&';
    public static String color(String input){
        return ChatColor.translateAlternateColorCodes(DEFAULT_COLOR_CHAR, input);

    }
}
