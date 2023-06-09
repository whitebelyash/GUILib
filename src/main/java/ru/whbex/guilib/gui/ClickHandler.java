package ru.whbex.guilib.gui;

import org.bukkit.entity.Player;
@FunctionalInterface
public interface ClickHandler {
    void handle(Player player, GUIContext context);

}
