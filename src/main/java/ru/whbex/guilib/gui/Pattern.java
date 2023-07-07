package ru.whbex.guilib.gui;

import ru.whbex.guilib.util.PatternUtils;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;


public class Pattern {
    public static final char EMPTY_CHAR = '#';
    private final Map<Character, Set<Integer>> charMap;

    public Pattern(Map<Character, Set<Integer>> charMap){
        this.charMap = charMap;
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
        return charMap.values().size();
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
        return new Pattern(out);
    }


}
