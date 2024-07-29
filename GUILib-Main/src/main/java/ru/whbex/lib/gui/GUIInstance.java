package ru.whbex.lib.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import ru.whbex.lib.gui.misc.CrossObject;

import java.util.*;
import java.util.function.Supplier;

/**
 * GUI Instance. Represents currently opened gui
 */
public class GUIInstance {
    private final GUIManager guiManager;
    private final Map<Integer, Button> buttons = new HashMap<>();
    private final List<Integer> throttleList = new ArrayList<>();

    private InventoryView view;
    private Inventory inv;
    private final List<BukkitTask> tasks;
    private final GUIContext ctx;
  //  private CrossObject cgctx;
    private GUI gui;
    private final UUID uuid;
    private UUID guiId; // not final cos of gui instance reuse

    /**
     * GUIInstance constructor.
     * @param guiManager GUIManager instance
     * @param gui GUI
     * @param uuid Player UUID. Can be offline, just don't call open() then
     */

    public GUIInstance(GUIManager guiManager, GUI gui, UUID uuid) {
        this.guiManager = guiManager;
        this.guiId = UUID.randomUUID();
        this.gui = gui;
        this.uuid = uuid;
        this.tasks = new ArrayList<>();
        this.ctx = new GUIContext(guiManager, gui, this, 0, null, uuid, GUIContext.ContextType.OPEN);
        guiManager.logd("Created GUIInstance for"  + uuid);
    }
    public GUIInstance(GUIManager guiManager, GUI gui, Player player){
        this(guiManager, gui, player.getUniqueId());
        open();
    }

    //
    // === GUIInstance control
    //

    // Now we can have GUIInstance bound to offline player
    // but open() will throw NPE if player isn't online
    void open(){
        Player player;
        if((player = Bukkit.getPlayer(uuid)) == null)
            throw new NullPointerException("Player " + uuid + " is null!");
        this.inv = Bukkit.createInventory(player, gui.getInvSize(), gui.getName(ctx));
        this.view = player.openInventory(this.inv);
        this.updateAll();
    }

    public void destroy(){
        this.view.close();
        cancelAllTasks();
    }

    void reuse(GUI gui) throws IllegalArgumentException {
        if(this.gui.getInvSize() != gui.getInvSize()){
            throw new IllegalArgumentException("Cannot reuse GUIInstance: size is different");
        }
        cancelAllTasks();
        this.gui = gui;
        this.guiId = UUID.randomUUID();
        this.view.setTitle(gui.getName(ctx)); // not supported on bugrock with geyser
        this.inv.clear();
        buttons.clear();
        throttleList.clear();
    }

    //
    // ===  Common Getters ===
    //

    public GUI getGui() {
        return gui;
    }

    // TODO: make private/default
    public InventoryView getView() {
        return view;
    }

    public Inventory getInventory() {
        return inv;
    }


    GUIContext getContext(){
        return ctx;
    }


    /**
     * Get GUIInstance ID
     * @return id
     */
    public UUID getId(){
        return guiId;
    }

    /**
     * Get GUIInstance holder UUID
     * @return holder's UUID
     */

    public UUID getHolder(){
        return this.uuid;
    }
    /*
    void setGUI(GUI gui) {
        this.gui = gui;
    }

     */

    //
    // === Button management ===
    //

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
     * Set button at specified position. Doesn't change icon in inventory, call updateButton() afterward
     * @param pos Button position
     * @param button Button to set
     * @param ctx GUI Context
     * @throws IllegalArgumentException if position is invalid
     */
    public void setButton(int pos, Button button, GUIContext ctx){
        if(pos > gui.getInvSize())
            throw new IllegalArgumentException("Invalid position!");
        buttons.put(pos, button);
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
        /* if(button.async()){
            inv.setItem(pos, button.getPlaceholder().getIcon(ctx));
            Bukkit.getScheduler().runTaskAsynchronously(guiManager.getPlugin(), () -> {
                this.setIcon(is.get(), pos);
            });
        } else
            */
        inv.setItem(pos, is.get());
    }

    /**
     * Update all buttons in GUI.
     */
    public void updateAll(){
        GUIContext ctx = new GUIContext(guiManager, gui, this, 0, null, uuid, GUIContext.ContextType.OPEN);
        gui.getButtons().forEach((pos, button) -> {
            if(button == null)
                return;
            if(buttons.containsKey(pos))
                button = buttons.get(pos);
            if (button.getIconProvider().requireContext()) {
                ctx.setSlot(pos);
                this.updateButton(pos, ctx);
            } else {
                this.updateButton(pos, null);
            }
        });
    }





    //
    // === Task Management ===
    //

    // TODO: Make public and add docs
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





    //
    // === GUI Cooldown Management ===
    //


    // Button throttle
    // TODO: Support click types

    /**
     * Adds throttle for specified position.
     * @param pos position
     * @param time time (in seconds)
     */

    // TODO: fix
    public void addThrottle(int pos, long time){
        /* if(throttleList.contains(pos))
            return;
        throttleList.add(pos);
        BukkitTask t = Bukkit.getScheduler().runTaskLater(guiManager.getPlugin(), () -> throttleList.remove(pos), ExtraUtils.asTicks(time));
         */
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

    //
    // === Utilities ===
    //

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
                    if(!guiManager.isHoldingSameGUI(uuid, this))
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

    //
    // === GUIMeta Management ===
    //

    /*
    public CrossObject getCrossObject(){
        contextVerify();
        return cgctx;
    }
    public void setCrossObject(CrossObject cgctx){
        guiManager.logd(String.format("Cross context set: %s", cgctx));
        this.cgctx = cgctx;
    }
    public boolean hasCrossObject(){
        return cgctx != null;
    }
    public boolean isOriginalCrossObject(){
        return hasCrossObject() && cgctx.getBoundId() != guiId;
    }
    // experimenting
    // TODO: remove !!!
    private void contextVerify(){
        if(hasCrossObject() && cgctx.getBoundId() == guiId){
            // do nothing, everything is good
        }
        else {
            // !!!!!!!!!!!!!!!!!!!!!1 warning !!!!!!!!!!!!!!!!!!11111111111111111
            guiManager.logd("!!! CrossContext bound id changed !!!");
        }
    }
     */





}
