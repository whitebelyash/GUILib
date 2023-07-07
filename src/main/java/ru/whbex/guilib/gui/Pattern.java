package ru.whbex.guilib.gui;

import ru.whbex.guilib.util.PatternUtils;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;


public class Pattern {
    public static final char EMPTY_CHAR = '#';
    private final Map<Character, Integer> charMap;

    public Pattern(Map<Character, Integer> charMap){
        this.charMap = charMap;
    }

    public int getCharPos(char c) throws IllegalArgumentException {
        if(!charMap.containsKey(c))
            throw new IllegalArgumentException("Character " + c + " not found!");
        return charMap.get(c);
    }
    public boolean isEmpty(int pos){

    }
    public int getSize(){
        return charMap.values().size();
    }

    public static Pattern fromStringArray(String... input){
        String[] structure = PatternUtils.validatePattern(input);
        Map<Character, Integer> out = new HashMap<>();
        for()
    }


}
