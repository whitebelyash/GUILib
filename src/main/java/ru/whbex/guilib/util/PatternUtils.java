package ru.whbex.guilib.util;

import ru.whbex.guilib.GUILib;
import ru.whbex.guilib.gui.Button;
import ru.whbex.guilib.gui.Pattern;

import java.util.Map;

public class PatternUtils {
    public static String[] validatePattern(String[] pattern){
        for(int p = 0; p < pattern.length; p++){
            pattern[p] = validatePatternLine(pattern[p]);
        }
        return pattern;
    }
    public static String validatePatternLine(String input){
        StringBuilder output = new StringBuilder(input);
        int outputLen = output.length();
        if(outputLen < 9){
            for(int c = 0; c < 9 - outputLen; c++){
                output.append(Pattern.EMPTY_CHAR);
            }
        } if(outputLen > 9){
            output.setLength(9);
        }
        return output.toString();
    }
}
