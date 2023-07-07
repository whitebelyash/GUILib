package ru.whbex.guilib.gui.examples;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import ru.whbex.guilib.gui.Button;
import ru.whbex.guilib.gui.GUI;
import ru.whbex.guilib.gui.GUIManager;
import ru.whbex.guilib.gui.icon.DynamicIconProvider;
import ru.whbex.guilib.gui.icon.IconProvider;
import ru.whbex.guilib.gui.icon.StaticIconProvider;
import ru.whbex.guilib.util.misc.ListGUI;
import ru.whbex.guilib.util.pager.PagerException;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * A simple player chooser menu. Shows player nickname on click
 */
public class PlayerChooser extends ListGUI<Player> {

    public PlayerChooser(GUIManager guiManager, boolean alwaysShowNavPane) {
        super(guiManager, alwaysShowNavPane);
    }

    @Override
    public IconProvider getEntryIcon(Player viewer, Player entry, int pos) {
        return StaticIconProvider.builder()
                .name("&f" + entry.getName())
                .build();
    }

    @Override
    public Button.Builder parseEntryButton(Player viewer, Player entry, int pos, Button.Builder button) {
        return button.addClickHandler(ClickType.LEFT, ((player, ctx) -> player.sendMessage("Clicked on " + entry.getName())), false);
    }

    @Override
    public String getTitle(Player viewer) {
        return "PlayerChooser example";
    }
    public GUI get(Player p, int page) throws PagerException {
        return get(p, new ArrayList<>(Bukkit.getOnlinePlayers()), page);
    }
}
