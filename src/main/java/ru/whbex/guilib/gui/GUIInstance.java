package ru.whbex.guilib.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import ru.whbex.guilib.gui.misc.CrossGUIContext;
import ru.whbex.guilib.util.ExtraUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * GUI Instance. Represents currently opened gui
 */
public class GUIInstance {
    private final GUIManager guiManager;
    private final Map<Integer, Button> buttons = new HashMap<>();
    private final List<Integer> throttleList = new ArrayList<>();

    // ListGUI page
    private int page;
    private final InventoryView view;
    private final Inventory inv;
    private final List<BukkitTask> tasks;
    private final GUIContext ctx;
    private CrossGUIContext cgctx;
    private GUI gui;
    private final Player player;

    public GUIInstance(GUIManager guiManager, GUI gui, Player player) {
        this.guiManager = guiManager;

        this.gui = gui;
        this.player = player;
        this.tasks = new ArrayList<>();
        this.ctx = new GUIContext(guiManager, gui, this, 0, null, player, GUIContext.ContextType.OPEN);
        this.inv = Bukkit.createInventory(player, gui.getInvSize(), gui.getName(ctx));
        this.view = player.openInventory(this.inv);
        this.updateAll();
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
     * Set button at specified position
     * @param pos Button position
     * @param button Button to set
     * @param ctx GUI Context
     * @throws IllegalArgumentException if position is invalid
     */

    public void setButton(int pos, Button button, GUIContext ctx){
        if(pos > gui.getInvSize())
            throw new IllegalArgumentException("Invalid position!");
        buttons.put(pos, button);
        updateButton(pos, ctx);
    }

    /**
     * Remove button at specified position.
     * @param pos button position
     * @throws IllegalArgumentException if position is invalid
     */
    public void removeButton(int pos){
        if(pos > gui.getInvSize())
            throw new IllegalArgumentException("Invalid position!");
        buttons.remove(pos);
        inv.setItem(pos, null);
    }

    /**
     * Is GUIInstance or GUI contains button at the specified position
     * @param pos Button position
     * @return boolean
     * @throws IllegalArgumentException if position is invalid
     */
    public boolean containsButton(int pos) throws IllegalArgumentException {
        if(pos > gui.getInvSize())
            throw new IllegalArgumentException("Invalid position!");
        return buttons.containsKey(pos) || gui.getButton(pos) != null;
    }

    /**
     * Update button (recreate ItemStack) at specified position.
     * @param pos button position
     * @param ctx gui context
     * @throws IllegalArgumentException if position is invalid
     */
    public void updateButton(int pos, GUIContext ctx) throws IllegalArgumentException {
        Button button = getButton(pos);
        if(button == null)
            return;
        Supplier<ItemStack> is = () -> button.getIconProvider().getIcon(ctx);
        if(button.async()){
            inv.setItem(pos, button.getPlaceholder().getIcon(ctx));
            Bukkit.getScheduler().runTaskAsynchronously(guiManager.getPlugin(), () -> {
                this.setIcon(is.get(), pos);
            });
        } else inv.setItem(pos, is.get());
    }

    /**
     * Get button at specified position. Returns Button from GUI, if it isn't changed, otherwise - from GUIInstance
     * @param pos Button position
     * @return Button
     * @throws IllegalArgumentException if position is invalid
     */
    public Button getButton(int pos) throws IllegalArgumentException {
        if(gui.getInvSize() < pos)
            throw new IllegalArgumentException("Invalid position!");
        return buttons.get(pos) == null ? gui.getButton(pos) : buttons.get(pos);
    }

    /**
     * Update all buttons in GUI. Skips modified buttons in GUIInstance
     */
    public void updateAll(){
        GUIContext ctx = new GUIContext(guiManager, gui, this, 0, null, player, GUIContext.ContextType.OPEN);
        gui.getButtons().forEach((pos, button) -> {
            if(button == null)
                return;
            if (button.getIconProvider().requireContext()) {
                ctx.setSlot(pos);
                this.updateButton(pos, ctx);
            } else {
                this.updateButton(pos, null);
            }
        });
    }

    /**
     * Replace button later at specified position
     * @param button Replacement button
     * @param pos Button position
     * @param time Replace delay
     * @param ctx GUI Context
     */
    public void replaceLater(Button button, int pos, long time, GUIContext ctx){
        BukkitTask t = Bukkit.getScheduler().runTaskLater(guiManager.getPlugin(),
                () -> {
                    if(!guiManager.isHoldingSameGUI(player, this))
                        return;
                    this.setButton(pos, button, ctx);
                }, time);
        addTask(t);
    }

    /**
     * Temporarily replace button at specified position
     * @param button Temporary button
     * @param pos Position
     * @param time How long it will be on screen in ticks
     * @param ctx GUI Context
     */
    public void replaceTemp(Button button, int pos, long time, GUIContext ctx){
        Button old = getButton(pos);
        this.setButton(pos, button, ctx);
        replaceLater(old, pos, time, ctx);
    }
    void setIcon(ItemStack item, int pos){
        inv.setItem(pos, item);
    }
    void addTask(BukkitTask task){
        this.tasks.add(task);
    }
    void removeTask(BukkitTask task){
        this.tasks.remove(task);
        task.cancel();
    }
    void cancelAllTasks(){
        tasks.forEach(BukkitTask::cancel);
    }
    void destroy(){
        this.view.close();
        cancelAllTasks();
    }

    GUIContext getContext(){
        return ctx;
    }
    void reuse(GUI gui) throws IllegalArgumentException {
        if(this.gui.getInvSize() != gui.getInvSize()){
            throw new IllegalArgumentException("Cannot reuse GUIInstance: size is different");
        }
        cancelAllTasks();
        this.gui = gui;
        this.view.setTitle(gui.getName(ctx));
        this.inv.clear();
        buttons.clear();
        throttleList.clear();
        updateAll();
    }
    // Button throttle
    // TODO: Support click types

    /**
     * Adds throttle for specified position.
     * @param pos position
     * @param time time (in seconds)
     */

    public void addThrottle(int pos, long time){
        if(throttleList.contains(pos))
            return;
        throttleList.add(pos);
        BukkitTask t = Bukkit.getScheduler().runTaskLater(guiManager.getPlugin(), () -> throttleList.remove(pos), ExtraUtils.asTicks(time));
    }

    /**
     * Checks if specified position is throttled
     * @param pos position
     * @return is position throttled
     */
    public boolean isThrottled(int pos){
        return throttleList.contains(pos);
    }

    /**
     * Removes throttle at specified position
     * @param pos position
     */
    public void removeThrottle(int pos){
        if(throttleList.contains(pos))
            throttleList.remove(pos);
    }
    public CrossGUIContext getCrossContext(){
        return cgctx;
    }
    public void setCrossContext(CrossGUIContext cgctx){
        this.cgctx = cgctx;
    }
    public boolean hasCrossContext(){
        return cgctx != null;
    }





}
