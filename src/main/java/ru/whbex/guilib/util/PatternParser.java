package ru.whbex.guilib.util;

import ru.whbex.guilib.gui.GUI;

public class PatternParser {
    public static final char EMPTY = '#';
    public static GUI.Builder parse(String[] pattern, GUI.Builder gui){
        if(pattern == null)
            throw new IllegalArgumentException("Pattern is null!");
        int size = pattern.length;
        gui.size(size);
        int pos = 0;
        for(String line : validatePattern(pattern)){
            for(char c : line.toCharArray()){
                gui.charToPos(pos, c);
                if(c == EMPTY){
                    gui.map(c, null);
                }
                pos++;
            }
        }
        return gui;
    }
    public static String[] validatePattern(String[] pattern){
        for(int p = 0; p < pattern.length; p++){
            pattern[p] = validatePatternLine(pattern[p]);
        }
        return pattern;
    }
    public static String validatePatternLine(String input){
        StringBuilder output = new StringBuilder(input);
        if(output.length() < 9){
            for(int c = 0; c < 9 - output.length(); c++){
                output.append(EMPTY);
            }
        } if(output.length() > 9){
            output.setLength(9);
        }
        return output.toString();
    }
}
