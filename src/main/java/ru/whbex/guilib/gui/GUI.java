package ru.whbex.guilib.gui;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class GUI {
    private static final int INV_LINE_SIZE = 9;
    private Function<GUIContext, String> name = ctx -> "Default GUI";
    private Map<Integer, Button> buttons = new HashMap<>();
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
        private Pattern pattern;

        public Builder name(String name){
            GUI.this.name = ctx -> name;
            return this;
        }
        public Builder name(Function<GUIContext, String> name){
            GUI.this.name = name;
            return this;
        }
        public Builder set(int pos, Button b) throws IllegalArgumentException {
            if(pos < 1 || pos > size*9)
                throw new IllegalArgumentException("Invalid position!");
            GUI.this.buttons.put(pos, b);
            return this;
        }
        public Builder map(char c, Button b) throws IllegalArgumentException {
            set(pattern.getCharPos(c), b);
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
        public Builder fromPattern(Pattern pattern){
            this.pattern = pattern;
            return this;
        }
        public GUI build(){
            return GUI.this;
        }
        }
}
