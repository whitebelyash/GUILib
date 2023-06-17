package ru.whbex.guilib.gui.click;

import org.bukkit.entity.Player;
import ru.whbex.guilib.gui.GUIContext;

@FunctionalInterface
public interface ClickCallback {
    void call(Player player, GUIContext ctx);

}
