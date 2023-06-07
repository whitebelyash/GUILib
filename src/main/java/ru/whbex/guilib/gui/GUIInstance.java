package ru.whbex.guilib.gui;

import org.bukkit.inventory.InventoryView;

public class GUIInstance {
    private final InventoryView view;
    private final GUI gui;

    public GUIInstance(InventoryView view, GUI gui) {
        this.view = view;
        this.gui = gui;
    }

    public GUI getGui() {
        return gui;
    }

    public InventoryView getView() {
        return view;
    }
    public void updateButton(int pos, Button button, GUIContext ctx){
        gui.getButtons().put(pos, button);
        view.setItem(pos, button.getIcon().getIcon(ctx));
    }
}
