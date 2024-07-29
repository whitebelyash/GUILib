package ru.whbex.lib.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;

import ru.whbex.lib.gui.click.ClickHandler;
import ru.whbex.lib.gui.click.ClickSound;
import ru.whbex.lib.gui.misc.CrossObject;
import ru.whbex.lib.gui.util.Constants;
import ru.whbex.lib.gui.util.ExtraUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GUI Manager. Manages currently open GUI Instances, registers events, handles clicking/opening/closing GUI and other things
 */
public class GUIManager {
    private class GUIListener implements Listener {
        @EventHandler(priority = EventPriority.HIGH)
        public void on(InventoryCloseEvent e){
            if(!(e.getPlayer() instanceof Player))
                return;
            handleClose((Player) e.getPlayer(), e.getView());
        }
        @EventHandler(priority = EventPriority.HIGH)
        public void on(PlayerQuitEvent e){
            handleClose(e.getPlayer(), e.getPlayer().getOpenInventory());
        }
        @EventHandler(priority = EventPriority.HIGH)
        public void on(InventoryClickEvent e){
            if(!(e.getWhoClicked() instanceof Player))
                return;
            Boolean result = handleClick(((Player) e.getWhoClicked()), e.getClick(), e.getSlot(), e.getClickedInventory());
            if(result != null)
                e.setCancelled(result);
        }
    }



    private final Plugin plugin;
    private final Logger logger;
    private final Map<Player, GUIInstance> guiHolders = new HashMap<>();
    private final Map<Integer, ClickHandler> sharedHandlers = new HashMap<>();
    private final Map<Player, Button> throttled = new HashMap<>();
    public GUIManager(Plugin plugin, boolean debug){
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        Bukkit.getPluginManager().registerEvents(new GUIListener(), plugin);
        logd("Using GUILib on " + plugin.getName()); // yes yes


    }

    /* Settings */

    private boolean reuseGI = false;
    public GUIManager setReuseGI(boolean val){
        this.reuseGI = val; return this;
    }

    /* Checks */

    /**
     * Is player holding GUI
     * @param player Player to check
     * @return boolean
     */
    public boolean isHoldingGUI(Player player){
        return guiHolders.containsKey(player) && getGUIInstance(player).getView() == player.getOpenInventory();
    }
    public boolean isHoldingGUI(UUID playerId){
        return Bukkit.getPlayer(playerId) != null && isHoldingGUI(Bukkit.getPlayer(playerId));
    }

    /**
     * Is player holding same GUI, i.e. not reused
     * @param player Player to check
     * @param gi GUI Instance
     * @return boolean
     */
    public boolean isHoldingSameGUI(Player player, GUIInstance gi){
        return isHoldingGUI(player) && getGUIInstance(player) == gi && getGUIInstance(player).getGui() == gi.getGui();
    }
    public boolean isHoldingSameGUI(UUID playerId, GUIInstance gi){
        return Bukkit.getPlayer(playerId) != null && isHoldingSameGUI(Bukkit.getPlayer(playerId), gi);
    }

    /**
     * Returns GUI Instance or null if player isn't holding GUI
     * @param player GUI Holder
     * @return GUI Instance
     */
    public GUIInstance getGUIInstance(Player player){
        return guiHolders.get(player);
    }
    public GUIInstance getGUIInstance(UUID playerId){
        return Bukkit.getPlayer(playerId) == null ? null : getGUIInstance(Bukkit.getPlayer(playerId));
    }

    /**
     * Binds ClickHandler to specified position. Has higher priority than GUI Buttons
     * @param pos Position to bind
     * @param handler ClickHandler
     */
    public void bindClick(int pos, ClickHandler handler){
        this.sharedHandlers.put(pos, handler);
    }
    // TODO: properly document this
    /**
     * Is click at position shared
     * @param pos Position
     * @return boolean
     */
    public boolean isClickShared(int pos){
        return this.sharedHandlers.containsKey(pos);
    }

    /**
     * Removes shared click handler if it exists, otherwise does nothing
     * @param pos position
     */
    public void unbindClick(int pos){
        this.sharedHandlers.remove(pos);
    }

    /**
     * Open GUI for player
     * @param player GUI Holder
     * @param gui GUI
     * @return GUI Instance
     */


    public GUIInstance open(Player player, GUI gui, boolean preserveCrossContext) {
        logd("Open GUI " + gui.getName() + " (p: " + player.getName() + ")");
        if (gui.getSize() < 1 || gui.getSize() > 6) {
            // Inaccessible.
            // :skull:
            log(Level.WARNING, "Got invalid GUI");
            return null;
        }
       // CrossObject cgctx = isHoldingGUI(player) ? getGUIInstance(player).getCrossObject() : null;
        // GUI Instance reuse
        boolean use_gi = reuseGI && isHoldingGUI(player) && getGUIInstance(player).getGui().getSize() == gui.getSize();
        GUIInstance gi = use_gi ?
                getGUIInstance(player) :
                new GUIInstance(this, gui, player.getUniqueId());
        if (use_gi) {
            logd("Reusing old GUIInstance");
            gi.reuse(gui);
        }
        else {
            if(getGUIInstance(player) != null)
                getGUIInstance(player).destroy();
        }
        if(preserveCrossContext)
         //   gi.setCrossObject(cgctx);
        guiHolders.put(player, gi);
        return gi;
    }

    /**
     * Close GUI. If player isn't holding GUI, does nothing
     * @param player GUI Holder
     */
    public void close(Player player){
        if(isHoldingGUI(player) && player.getOpenInventory() == getGUIInstance(player).getView()){
            logd("Closing GUI for " + player.getName());
            getGUIInstance(player).destroy();
            guiHolders.remove(player);
        }
    }

    /**
     * Handle GUI Click
     * @param player GUI Holder
     * @param clickType Click type
     * @param pos Click position
     * @param inv Inventory
     * @return Is click cancelled or not. If null - skip Event#setCancelled
     */
    // TODO: Rework click handle result

    private Boolean handleClick(Player player, ClickType clickType, int pos, Inventory inv){
        if(!isHoldingGUI(player))
            return null;
        logd(String.format("Handling click by %s with type %s at position %d", player.getName(), clickType, pos));
        GUIInstance gi = getGUIInstance(player);
        GUI gui = gi.getGui();
        GUIContext ctx = gi.getContext();
        ExtraUtils.updateContext(ctx, pos, clickType, GUIContext.ContextType.CLICK);
        if(sharedHandlers.containsKey(pos)){
            runClickTask(sharedHandlers.get(pos), clickType, player, ctx);
            return true;
        }
        if(gi.isThrottled(pos))
            return true;
        if(inv != null && inv != gi.getInventory())
            return true;
        if(pos > gui.getInvSize()) {
            logd("Invalid click position!");
            return true;
        }
        if(gi.getButton(pos) == null) {
            return true;
        }
        Button b = gi.getButton(pos);

        ClickHandler handler  = b.getClickHandler();
      /*  if(handler == null){
            logd("Handler not set! GUI: " + gui.getName() + ", pos: " + pos + ", type: " + clickType);
            return true;
        }
       */
        // Callback is null - do nothing
        if(handler.callback(clickType) == null)
            return true;
        runClickTask(handler, clickType, player, ctx);
        logd("Throttle: " + handler.throttle());
        if(handler.throttle() > 0){
            gi.addThrottle(pos, handler.throttle());
        }
        return true;

    }

    // click task
    // TODO: rework async
    private void runClickTask(ClickHandler handler, ClickType type, Player player, GUIContext ctx){
        Runnable task = () -> {
           // logd("Running async (other thread): " + !Bukkit.isPrimaryThread());
           // logd("Running async (ClickHandler async): " + handler.async());

            handler.callback(type).call(player, ctx);
            boolean result = ctx.clickResult();
            logd("Click result: " + result);
            if(handler.sound() != null){
                logd("Playing sound " + handler.sound().getSound(result));
                ClickSound.playSound(player, handler.sound(), result);
            }
        };
        if(handler.async())
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        else
            Bukkit.getScheduler().runTask(plugin, task);
    }

    /**
     * Handle GUI close. If player isn't holding GUI do nothing
     * @param player GUI Holder
     * @param view Inventory view
     */
    private void handleClose(Player player, InventoryView view){
        if(guiHolders.containsKey(player)){
            if(view == null || guiHolders.get(player).getView() == view)
                guiHolders.remove(player);
        }
    }
    /**
     * Shutdown task
     */
    public void onShutdown(){
        logd("Shutting down");
        this.guiHolders.values().forEach(GUIInstance::destroy);
    }
    Plugin getPlugin(){
        return plugin;
    }



    public final void log(Level level, String msg){
        logger.log(level, msg);
    }
    // TODO: Remove
    public final void logd(String msg){
        if(Constants.DEBUG_ENABLED)
            logger.log(Level.INFO, "GUILib debug: " + msg);
    }
}
