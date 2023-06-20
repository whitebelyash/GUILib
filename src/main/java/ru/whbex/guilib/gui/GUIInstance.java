package ru.whbex.guilib.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import ru.whbex.guilib.util.ExtraUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * GUI Container. Represents currently open gui
 */
public class GUIInstance {
    private final GUIManager guiManager;
    private final Map<Integer, Button> buttons = new ConcurrentHashMap<>();
    private final InventoryView view;
    private final Inventory inv;
    private GUI gui;
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

    void setGUI(GUI gui) {
        this.gui = gui;
    }

    /**
     * Set button
     * @param pos button position
     * @param button button to set
     * @param ctx gui context
     */

    public void setButton(int pos, Button button, GUIContext ctx){
        if(!guiManager.isHoldingGUI(player))
            return;
        buttons.put(pos, button);
        updateButton(pos, ctx);
    }

    /**
     * Remove button
     * @param pos button position
     */
    public void removeButton(int pos){
        if(!guiManager.isHoldingGUI(player))
            return;
        buttons.remove(pos);
        inv.setItem(pos, null);
    }

    /**
     * Update button
     * @param pos button position
     * @param ctx gui context
     */
    public void updateButton(int pos, GUIContext ctx){
        Button button = getButton(pos);
        Supplier<ItemStack> is = () -> button.getIconProvider().getIcon(ctx);
        if(button.async()){
            inv.setItem(pos, button.getPlaceholder().getIcon(ctx));
            Bukkit.getScheduler().runTaskAsynchronously(guiManager.getPlugin(), () -> {
                this.setIcon(is.get(), pos);
            });
        } else inv.setItem(pos, is.get());
    }
    public Button getButton(int pos){
        return buttons.get(pos) == null ? gui.getButton(pos) : buttons.get(pos);
    }

    /**
     * Update all buttons in GUI
     */
    public void updateAll(){
        gui.getButtons().forEach((pos, button) -> {
            GUIContext ctx = button.getIconProvider().requireContext() ?
                    new GUIContext(guiManager, gui, this, pos, null, player, GUIContext.ContextType.OPEN) :
                    null;
            this.updateButton(pos, ctx);
        });
    }

    /**
     * Replace button later
     * @param button replacement button
     * @param pos button position
     * @param time when it'll be replaced in ticks
     * @param ctx gui context
     */
    public void replaceLater(Button button, int pos, long time, GUIContext ctx){
        Bukkit.getScheduler().runTaskLater(guiManager.getPlugin(),
                () -> {
                    if(!guiManager.isHoldingSameGUI(player, this))
                        return;
                    this.setButton(pos, button, ctx);
                }, time);
    }
    /**
     * Temporarily replace button
     * @param button temp button
     * @param pos position
     * @param time how long it will be on screen in ticks
     * @param ctx GUI Context
     */
    public void replaceTemp(Button button, int pos, long time, GUIContext ctx){
        Button old = getButton(pos);
        this.setButton(pos, button, ctx);
        replaceLater(old, pos, time, ctx);
    }
    void setIcon(ItemStack item, int pos){
        if(!guiManager.isHoldingSameGUI(player, this))
            return;
        inv.setItem(pos, item);
    }

}
