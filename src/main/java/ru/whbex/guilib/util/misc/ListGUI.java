package ru.whbex.guilib.util.misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.whbex.guilib.gui.*;
import ru.whbex.guilib.gui.icon.IconProvider;
import ru.whbex.guilib.gui.icon.StaticIconProvider;
import ru.whbex.guilib.util.ExtraUtils;
import ru.whbex.guilib.util.pager.Pager;
import ru.whbex.guilib.util.PatternUtils;
import ru.whbex.guilib.util.pager.PagerException;

import java.util.List;

public abstract class ListGUI<T> {
    public static final char LIST_ENTRY_CHAR = 'l';
    public static final IconProvider NEXT_ICON = StaticIconProvider.builder().name("&aСледующая страница").build();
    public static final IconProvider PREV_ICON = StaticIconProvider.builder().name("&cПредыдущая страница").build();
    public static final IconProvider UNKNOWN_PAGE_ICON = StaticIconProvider.builder().name("Неизвестная страница").material(Material.BARRIER).build();

    public static final String LIST_TITLE = "#%d";
    public static final String[] DEFAULT_PATTERN = {"lllllllll", "lllllllll", "lllllllll", "lllllllll", "lllllllll", "p#######n"};
    private static final Pattern PATTERN = Pattern.fromStringArray(DEFAULT_PATTERN);
    private final GUI.Builder gui;
    protected final int pageSize;
    protected final GUIManager gm;
    protected final boolean showPane;

    // Buttons
    public ListGUI(GUIManager guiManager, boolean alwaysShowNavPane){
        this.gui = GUI.builder()
                .fromPattern(PATTERN);
        pageSize = ExtraUtils.arrayCharCount(LIST_ENTRY_CHAR, DEFAULT_PATTERN);
        this.gm = guiManager;
        this.showPane = alwaysShowNavPane;
    }
    public abstract IconProvider getEntryIcon(Player viewer, T entry, int pos);
    public abstract Button.Builder parseEntryButton(Player viewer, T entry, int pos, Button.Builder button);
    public abstract String getTitle(Player viewer);
    public GUI get(Player viewer, List<T> list, int page) throws PagerException {
        Pager<T> pager = new Pager<>(list, pageSize);
        List<T> entries = pager.getPage(page);
        // TODO: Rework this
        entries.forEach(e -> {
            int index = entries.indexOf(e);
            Button.Builder b = Button.builder(getEntryIcon(viewer, e, index));
            parseEntryButton(viewer, e, index, b);
            addEntry(b);
        });
        gui.name(getTitle(viewer) + " " + String.format(LIST_TITLE, page));
        addNavPane(page, pager.getPageCount());
        return gui.build();
    }
    private void addEntry(Button.Builder button){
        gui.mapOnce(LIST_ENTRY_CHAR, button.build());
    }
    protected void addNavPane(int page, int pageCount){

    }





}
