package ru.whbex.guilib.gui;
import java.util.*;
import java.util.function.Supplier;

public class GUI {
    private static final int INV_LINE_SIZE = 9;
    private Supplier<String> name = () -> "Default GUI";
    private Map<Integer, Button> buttons = new HashMap<>();
    private Map<Character, Set<Integer>> charPos = new HashMap<>();
    // in columns
    private int size = 1;
    private GUI(){}

    public String getName() {
        return name.get();
    }
    public Supplier<String> getNameSupplier(){
        return name;
    }

    public int getSize() {
        return size;
    }
    public int getInvSize(){
        return size*INV_LINE_SIZE;
    }

    Map<Integer, Button> getButtons() {
        return buttons;
    }
    public Button getButton(int pos) {
        return buttons.get(pos);
    }
    void setButton(int pos, Button button){
        if(pos < 0 || pos > getInvSize())
            throw new IllegalArgumentException("Size is invalid!");
        buttons.put(pos, button);
    }


    private Builder getBuilder(){
        return new Builder();
    }
    public static Builder builder(){
        return new GUI().getBuilder();
    }


    public class Builder {

        public Builder name(String name){
            GUI.this.name = () -> name;
            return this;
        }
        public Builder name(Supplier<String> name){
            GUI.this.name = name;
            return this;
        }
        public Builder map(int pos, Button button) throws IllegalArgumentException{
            if(pos < 0)
                throw new IllegalArgumentException("Button position must be a positive number!");
            if (pos > size*INV_LINE_SIZE)
                throw new IllegalArgumentException("Button position is bigger than GUI size!");
            buttons.put(pos, button);
            return this;
        }
        public Builder map(char patternChar, Button button) throws IllegalArgumentException {
            if(!charPos.containsKey(patternChar))
                throw new IllegalArgumentException("Unknown pattern character " + patternChar);
            Set<Integer> positions = charPos.getOrDefault(patternChar, new HashSet<>());
            positions.forEach(pos -> buttons.put(pos, button));
            return this;
        }
        public Builder charToPos(int pos, char c) throws IllegalArgumentException {
            if(pos < 0)
                throw new IllegalArgumentException("Char position must be a positive number!");
            if(pos > size*INV_LINE_SIZE)
                throw new IllegalArgumentException("Button position is bigger than GUI Size");
            Set<Integer> positions = charPos.getOrDefault(c, new HashSet<>());
            positions.add(pos);
            GUI.this.charPos.put(c, positions);
            return this;
        }
        public Builder size(int s) throws IllegalArgumentException {
            if(s < 1)
                throw new IllegalArgumentException("GUI Size must be a positive number!");
            if(s > 6)
                throw new IllegalArgumentException("Line count is too big!");
            GUI.this.size = s;
            return this;
        }
        public GUI build(){

            return GUI.this;
        }
        }
}
