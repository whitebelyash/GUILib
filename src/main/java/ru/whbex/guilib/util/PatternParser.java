package ru.whbex.guilib.util;

import org.bukkit.Sound;
import ru.whbex.guilib.gui.Button;
import ru.whbex.guilib.gui.GUI;
import ru.whbex.guilib.gui.Pattern;
import ru.whbex.guilib.gui.icon.EmptyIconProvider;

import java.util.Arrays;

public class PatternParser {
    public static final char EMPTY = '#';
    private static final Button EMPTY_BUTTON = Button.builder().icon(new EmptyIconProvider()).build();

    public static GUI.Builder parse(String[] pattern, GUI.Builder gui){
        if(pattern == null)
            throw new IllegalArgumentException("Pattern is null!");
        int size = pattern.length;
        gui.size(size);
        int pos = 0;
        for(String line : pattern){
            if(line.length() != 9)
                throw new IllegalArgumentException("Invalid pattern line: " + line);
            for(char c : line.toCharArray()){
                gui.charToPos(pos, c);
                if(c == EMPTY){
                    gui.map(c, EMPTY_BUTTON);
                }
                pos++;
            }
        }
        return gui;

    }
}
