package ru.whbex.guilib.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
@FunctionalInterface
public interface ClickHandler {
    void handle(Player player, GUIContext context);

}
