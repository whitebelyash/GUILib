package ru.whbex.guilib.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class GUIInstance {
    private final GUIManager guiManager;
    private final InventoryView view;
    private final Inventory inv;
    private final GUI gui;
    private final Player player;

    public GUIInstance(GUIManager guiManager, InventoryView view, Inventory inv, GUI gui, Player player) {
        this.guiManager = guiManager;
        this.view = view;
        this.inv = inv;
        this.gui = gui;
        this.player = player;
    }

    public GUI getGui() {
        return gui;
    }

    public InventoryView getView() {
        return view;
    }

    public Inventory getInventory() {
        return inv;
    }

    public void setButton(int pos, Button button, GUIContext ctx){
        if(!guiManager.isHoldingGUI(player))
            return;
        gui.getButtons().put(pos, button);
        view.setItem(pos, button.getIconProvider().getIcon(ctx));
    }
    public void updateButton(int pos, GUIContext ctx){
        if(!guiManager.isHoldingGUI(player))
            return;
        Button button = gui.getButton(pos);
        view.setItem(pos, button.getIconProvider().getIcon(ctx));
    }
}
