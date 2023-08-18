package ru.whbex.guilib.gui;

import ru.whbex.guilib.GUILib;
import ru.whbex.guilib.util.ExtraUtils;
import ru.whbex.guilib.util.PatternUtils;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;


public class Pattern {
    public static final char EMPTY_CHAR = ' ';
    private final Map<Character, Set<Integer>> charMap;
    private final int size;

    public Pattern(Map<Character, Set<Integer>> charMap, int size){
        this.charMap = charMap;
        this.size = size;
    }

    public Set<Integer> getCharPos(char c) throws IllegalArgumentException {
        if (!charMap.containsKey(c))
            throw new IllegalArgumentException("Character " + c + " not found!");
        return charMap.get(c);
    }
    public boolean contains(char c){
        return charMap.containsKey(c);
    }
    public boolean hasMultiplePositions(char c){
        return contains(c) && charMap.get(c).size() > 1;
    }
    public int getSize(){
        return size;
    }

    public static Pattern fromStringArray(String... input){
        String[] structure = PatternUtils.validatePattern(input);
        Map<Character, Set<Integer>> out = new LinkedHashMap<>();
        int pointer = 0;
        for(String l : structure){
            for(char c : l.toCharArray()){
                if(c == EMPTY_CHAR){
                    pointer++;
                    continue;
                }
                Set<Integer> positions = out.containsKey(c) ? out.get(c) : new LinkedHashSet<>();
                positions.add(pointer);
                out.put(c, positions);
                pointer++;
            }
        }
        int slotSize = ExtraUtils.stringLengthArray(structure);
        int size = slotSize % 9 == 0 ? slotSize / 9 : (slotSize / 9) + 1;
        GUILib.LOGGER.info("slotSize: " + slotSize + ", size: " + size);
        return new Pattern(out, size);
    }
    public static Pattern fromString(char separator, String pattern){
        return fromStringArray(pattern.split(String.valueOf(separator)));
    }


}
