package ru.whbex.guilib.gui;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class GUI {
    private static final int INV_LINE_SIZE = 9;
    private Function<GUIContext, String> name = ctx -> "Default GUI";
    private Map<Integer, Button> buttons = new ConcurrentHashMap<>();
    private Map<Character, List<Integer>> charPos = new HashMap<>();
    // in columns
    private int size = 1;
    private GUI(){}

    public String getName() {
        return name.apply(null);
    }
    public String getName(GUIContext ctx){
        return name.apply(ctx);
    }
    public Function<GUIContext, String> getNameFunc(){
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
        private Iterator<Integer> mapIter;

        public Builder name(String name){
            GUI.this.name = ctx -> name;
            return this;
        }
        public Builder name(Function<GUIContext, String> name){
            GUI.this.name = name;
            return this;
        }
        public Builder map(int pos, Button button) throws IllegalArgumentException{
            if(button == null)
                return this;
            if(pos < 0)
                throw new IllegalArgumentException("Button position must be a positive number!");
            if (pos > size*INV_LINE_SIZE)
                throw new IllegalArgumentException("Button position is bigger than GUI size!");
            buttons.put(pos, button);
            return this;
        }
        public Builder map(char patternChar, Button button) throws IllegalArgumentException {
            if(button == null)
                return this;
            if(!charPos.containsKey(patternChar))
                throw new IllegalArgumentException("Unknown pattern character " + patternChar);
            List<Integer> positions = charPos.getOrDefault(patternChar, new ArrayList<>());
            positions.forEach(pos -> buttons.put(pos, button));
            return this;
        }
        public Builder mapOnce(char patternChar, Button button){
            if(button == null)
                return this;
            if(!charPos.containsKey(patternChar)){
                throw new IllegalArgumentException("Unknown pattern character " + patternChar);
            }
            if(charPos.get(patternChar).isEmpty()) {
                map(patternChar, button);
                return this;
            }
            if(mapIter == null){
                mapIter = charPos.get(patternChar).iterator();
                buttons.put(charPos.get(patternChar).get(0), button);
            }
            if(mapIter.hasNext()){
                buttons.put(mapIter.next(), button);
            }
            return this;
        }
        public Builder charToPos(int pos, char c) throws IllegalArgumentException {
            if(pos < 0)
                throw new IllegalArgumentException("Char position must be a positive number!");
            if(pos > size*INV_LINE_SIZE)
                throw new IllegalArgumentException("Button position is bigger than GUI Size");
            List<Integer> positions = charPos.getOrDefault(c, new ArrayList<>());
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
