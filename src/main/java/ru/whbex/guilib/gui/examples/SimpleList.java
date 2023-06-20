package ru.whbex.guilib.gui.examples;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.whbex.guilib.gui.Button;
import ru.whbex.guilib.gui.GUI;
import ru.whbex.guilib.gui.GUIManager;
import ru.whbex.guilib.gui.icon.IconProvider;
import ru.whbex.guilib.gui.icon.StaticIconProvider;
import ru.whbex.guilib.util.misc.ListGUI;

import java.util.Collections;

public class SimpleList extends ListGUI<String> {

    public SimpleList(GUIManager guiManager) {
        super(guiManager,  true);
    }

    @Override
    public IconProvider getEntryIcon(Player viewer, String entry, int pos) {
        return StaticIconProvider.builder()
                .name("&f" + entry)
                .material(Material.STONE)
                .appendLore(String.valueOf(pos))
                .build();
    }

    @Override
    public Button.Builder parseEntryButton(Player viewer, String entry, int pos, Button.Builder button) {
        return button;
    }

    @Override
    public String getTitle(Player viewer) {
        return "Simple list example";
    }
}
