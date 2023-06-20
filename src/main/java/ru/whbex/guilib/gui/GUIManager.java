package ru.whbex.guilib.gui;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import ru.whbex.guilib.GUILib;
import ru.whbex.guilib.gui.click.ClickCallback;
import ru.whbex.guilib.gui.click.ClickHandler;
import ru.whbex.guilib.gui.click.ClickSound;
import ru.whbex.guilib.util.ExtraUtils;

import java.util.HashMap;
import java.util.Map;

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
            boolean result = handleClick(((Player) e.getWhoClicked()), e.getClick(), e.getSlot(), e.getClickedInventory());
            e.setCancelled(result);

        }
    }
    private final boolean debug;
    private final Plugin plugin;
    private final Map<Player, GUIInstance> guiHolders = new HashMap<>();
    private final Map<Integer, ClickCallback> sharedHandlers = new HashMap<>();
    public GUIManager(Plugin plugin, boolean debug){
        this.plugin = plugin;
        this.debug = debug;
        Bukkit.getPluginManager().registerEvents(new GUIListener(), plugin);


    }
    public boolean isHoldingGUI(Player player){
        return guiHolders.containsKey(player) && getGUIInstance(player).getView() == player.getOpenInventory();
    }
    public boolean isHoldingSameGUI(Player player, GUIInstance gi){
        return isHoldingGUI(player) && getGUIInstance(player) == gi && getGUIInstance(player).getGui() == gi.getGui();

    }
    public GUIInstance getGUIInstance(Player player){
        return guiHolders.get(player);
    }
    public void bindClick(int pos, ClickCallback handler){
        this.sharedHandlers.put(pos, handler);
    }
    public boolean isClickShared(int pos){
        return this.sharedHandlers.containsKey(pos);
    }

    public void open(Player player, GUI gui) {
        logd("Open GUI " + gui.getName() + " (p: " + player.getName() + ")");
        if (gui.getSize() < 1 || gui.getSize() > 6) {
            GUILib.LOGGER.info("Invalid gui size " + gui.getSize());
            return;
        }
        boolean use_gi = GUILib.getInstance().config().REUSE_GI && isHoldingGUI(player) && getGUIInstance(player).getGui().getSize() == gui.getSize();
        Inventory bukkitInv = use_gi ?
                getGUIInstance(player).getInventory() :
                Bukkit.createInventory(player, gui.getInvSize(), gui.getName());
        GUIInstance inv = use_gi ?
                getGUIInstance(player) :
                new GUIInstance(this, player.openInventory(bukkitInv), bukkitInv, gui, player);
        if (use_gi) {
            logd("Reusing old GUIInstance");
            bukkitInv.clear();
            inv.setGUI(gui);
            inv.getView().setTitle(gui.getName());
        }
        inv.updateAll();
        guiHolders.put(player, inv);
    }
    public void close(Player player){
        logd("Closing GUI for " + player.getName());
        if(isHoldingGUI(player) && player.getOpenInventory() == getGUIInstance(player).getView()){
            player.closeInventory();
            guiHolders.remove(player);
        }
    }

    private boolean handleClick(Player player, ClickType clickType, int pos, Inventory inv){
        logd(String.format("Handling click by %s with type %s at position %d", player.getName(), clickType, pos));
        if(!isHoldingGUI(player))
            return false;
        GUIInstance gi = getGUIInstance(player);
        GUI gui = getGUIInstance(player).getGui();
        GUIContext ctx = new GUIContext(this, gui, gi, pos, clickType, player, GUIContext.ContextType.CLICK);
        if(sharedHandlers.containsKey(pos)){
            sharedHandlers.get(pos).call(player, ctx);
            return true;
        }
        if(inv != null && inv != gi.getInventory())
            return true;
        if(pos > gui.getInvSize()) {
            logd("Invalid click position!");
            return true;
        }
        if(gi.getButton(pos) == null) {
            return true;
        }
        ClickHandler handler  = gi.getButton(pos).getClickHandler(clickType);
        if(handler == null){
            logd("Handler not set! GUI: " + gui.getName() + ", pos: " + pos + ", type: " + clickType);
            return true;
        }
        // Callback is null - do nothing
        if(handler.callback() == null)
            return true;
        Runnable task = () -> {
            logd("Running async (other thread): " + !Bukkit.isPrimaryThread());
            logd("Running async (ClickHandler async): " + handler.async());
            handler.callback().call(player, ctx);
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
        return true;

    }
    private void handleClose(Player player, InventoryView view){
        logd("Handling close (p: " + player.getName() + ")");
        if(guiHolders.containsKey(player)){
            if(view == null || guiHolders.get(player).getView() == view)
                guiHolders.remove(player);
        }
    }
    Plugin getPlugin(){
        return plugin;
    }
    // TODO: Remove
    public final void logd(String msg){
        if(debug)
            GUILib.LOGGER.info("DBG(" + this.plugin.getName() + "): " + msg);
    }


}
