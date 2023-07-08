package ru.whbex.guilib.gui;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * GUI Container. Contains buttons and their positions
 */
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

    /**
     * Get GUI Size in lines
     * @return size
     */

    public int getSize() {
        return size;
    }

    /**
     * Get GUI Size in slots
     * @return slot amount
     */
    public int getInvSize(){
        return size*INV_LINE_SIZE;
    }

    Map<Integer, Button> getButtons() {
        return buttons;
    }
    public Button getButton(int pos) {
        return buttons.get(pos);
    }
    // Maybe remove this... GUI is designed to be immutable, actually
    // Use GUIInstance if u want to change GUI
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
        private Map<Character, Iterator<Integer>> posIteratorMap;

        /**
         * Sets GUI Name (Title) without context
         * @param name Title
         */

        public Builder name(String name){
            GUI.this.name = ctx -> name;
            return this;
        }

        /**
         * Sets GUI Name (Title) with context
         * @param name Name Function
         */
        public Builder name(Function<GUIContext, String> name){
            GUI.this.name = name;
            return this;
        }

        /**
         * Sets button to slot
         * @param pos button slot
         * @param b button
         * @throws IllegalArgumentException if position is invalid
         */
        public Builder set(int pos, Button b) throws IllegalArgumentException {
            if(pos < 0 || pos > size*9)
                throw new IllegalArgumentException("Invalid position!");
            GUI.this.buttons.put(pos, b);
            return this;
        }

        /**
         * Maps button to specified character positions. fromPattern() is required for this
         * @param c Character
         * @param b Button
         * @throws IllegalArgumentException if pattern doesn't contain this character
         * @throws NullPointerException if pattern is unset
         */
        public Builder map(char c, Button b) throws IllegalArgumentException,NullPointerException {
            if(pattern == null)
                throw new NullPointerException("Pattern is null!");
            pattern.getCharPos(c).forEach(p -> set(p, b));
            return this;
        }

        /**
         * Maps button to a character position and creates an iterator.
         * Next calls to this method with the same character will set another button to the next position of same char until the iterator end is reached.
         * Does nothing if iterator end is reached.
         * fromPattern() is required for this
         * @param c Character
         * @param b Button to set
         * @throws IllegalArgumentException If character has only one position
         * @throws NullPointerException If pattern is unset
         */
        public Builder mapOnce(char c, Button b) throws IllegalArgumentException, NullPointerException {
            if(pattern == null)
                throw new NullPointerException("Pattern is null!");
            if(!pattern.hasMultiplePositions(c))
                throw new IllegalArgumentException("Character has only one position!");
            if(posIteratorMap == null || !posIteratorMap.containsKey(c))
                posIteratorMap = Collections.singletonMap(c, pattern.getCharPos(c).iterator());
            Iterator<Integer> posIterator = posIteratorMap.get(c);
            if(!posIterator.hasNext())
                return this;
            int pos = posIterator.next();
            set(pos, b);
            return this;
        }

        /**
         * Map remaining positions in iterator to button
         * @param b Button to set
         * @throws NullPointerException If iterator is null
         */
        public Builder mapRemaining(Button b) throws NullPointerException {
            if(posIteratorMap == null)
                throw new NullPointerException("Position iterator is null!");
            posIteratorMap.values().forEach(i -> i.forEachRemaining(v -> set(v, b)));
            return this;
        }

        /**
         * Applies inventory size in lines
         * @param s line amount
         * @throws IllegalArgumentException if size is invalid
         * @throws IllegalStateException if size is already set by pattern
         */
        public Builder size(int s) throws IllegalArgumentException, IllegalStateException {
            if(s < 0)
                throw new IllegalArgumentException("GUI Size must be a positive number!");
            if(s > 6)
                throw new IllegalArgumentException("Size is too big!");
            if(pattern != null && size > 0)
                throw new IllegalStateException("Size is already set by pattern!");
            GUI.this.size = s;
            return this;
        }

        /**
         * Use pattern for GUI building
         * @param pattern Pattern
         */
        public Builder fromPattern(Pattern pattern){
            if(pattern == null)
                throw new NullPointerException("Pattern is null!");
            size(pattern.getSize());
            this.pattern = pattern;
            return this;
        }
        public GUI build(){
            return GUI.this;
        }
        }
}
