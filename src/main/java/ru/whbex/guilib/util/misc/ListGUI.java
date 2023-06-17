package ru.whbex.guilib.util.misc;

import org.bukkit.event.inventory.ClickType;
import ru.whbex.guilib.gui.*;
import ru.whbex.guilib.gui.click.ClickCallback;
import ru.whbex.guilib.gui.icon.IconProvider;
import ru.whbex.guilib.gui.icon.StaticIconProvider;
import ru.whbex.guilib.util.ExtraUtils;
import ru.whbex.guilib.util.pager.Pager;
import ru.whbex.guilib.util.PatternParser;
import ru.whbex.guilib.util.pager.PagerException;

import java.util.List;

@Pattern({"lllllllll", "lllllllll", "lllllllll", "lllllllll", "lllllllll", "p#######n"})
public abstract class ListGUI<T> {
    public static final char LIST_ENTRY_CHAR = 'l';
    public static final IconProvider NEXT_ICON = StaticIconProvider.builder().name("&aСледующая страница").build();
    public static final IconProvider PREV_ICON = StaticIconProvider.builder().name("&cПредыдущая страница").build();
    public static final IconProvider UNKNOWN_PAGE_ICON = StaticIconProvider.builder().name("Неизвестная страница").build();

    public static final String LIST_TITLE = "#%d";
    private final GUI.Builder gui;
    protected final int pageSize;
    protected final GUIManager gm;
    protected final boolean showPane;

    // Buttons
    public ListGUI(GUIManager guiManager, boolean alwaysShowNavPane){
        GUI.Builder gui = GUI.builder();
        Pattern p = this.getClass().getAnnotation(Pattern.class);
        if(p == null){
            throw new IllegalStateException("Pattern is not set for this class!");
        }
        pageSize = ExtraUtils.arrayCharCount(LIST_ENTRY_CHAR, p.value());
        PatternParser.parse(p.value(), gui);
        this.gui = gui;
        this.gm = guiManager;
        this.showPane = alwaysShowNavPane;
    }
    public abstract IconProvider getEntryIcon(T entry, int pos);
    public abstract Button.Builder parseEntryButton(T entry, int pos, Button.Builder button);
    public abstract String getTitle();
    public GUI get(List<T> list, int page) throws PagerException {
        Pager<T> pager = new Pager<>(list, pageSize);
        List<T> entries = pager.getPage(page);
        // TODO: Rework this
        entries.forEach(e -> {
            int index = entries.indexOf(e);
            Button.Builder b = Button.builder(getEntryIcon(e, index));
            parseEntryButton(e, index, b);
            addEntry(b);
        });
        gui.name(getTitle() + " " + String.format(LIST_TITLE, page));
        addNavPane(page, pager.getPageCount());
        return gui.build();
    }
    private void addEntry(Button.Builder button){
        gui.mapOnce(LIST_ENTRY_CHAR, button.build());
    }
    protected void addNavPane(int page, int pageCount){
        Button next;
        Button prev;
    }
    public void addClickAction(T entry, ClickType ctype, ClickCallback handler){

    }





}
