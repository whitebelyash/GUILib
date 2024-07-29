package ru.whbex.lib.gui.click;

import org.bukkit.entity.Player;
import ru.whbex.lib.gui.GUIContext;

@FunctionalInterface
public interface ClickCallback {
    void call(Player player, GUIContext ctx);

}
