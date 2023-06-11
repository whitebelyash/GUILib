package ru.whbex.guilib.gui;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.whbex.guilib.GUILib;

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
    private final Map<Integer, ClickHandler> sharedHandlers = new HashMap<>();
    public final Sound defaultSound;
    public GUIManager(Plugin plugin, Sound defaultSound, boolean debug){
        this.plugin = plugin;
        this.debug = debug;
        this.defaultSound = defaultSound;
        Bukkit.getPluginManager().registerEvents(new GUIListener(), plugin);


    }
    public Sound getDefaultSound(){
        return defaultSound;
    }
    public boolean isHoldingGUI(Player player){
        return guiHolders.containsKey(player) && getGUIInstance(player).getView() == player.getOpenInventory();
    }
    public GUIInstance getGUIInstance(Player player){
        return guiHolders.get(player);
    }
    public void bindClick(int pos, ClickHandler handler){
        this.sharedHandlers.put(pos, handler);
    }
    public boolean isClickShared(int pos){
        return this.sharedHandlers.containsKey(pos);
    }

    public void open(Player player, GUI gui){
        if (gui.getSize() < 1 || gui.getSize() > 5){
            GUILib.LOGGER.info("Invalid gui size " + gui.getSize());
            return;
        }
        Inventory bukkitInv = Bukkit.createInventory(player, gui.getInvSize(), gui.getName());
        GUIInstance inv = new GUIInstance(this, player.openInventory(bukkitInv), bukkitInv, gui, player);
        for(Map.Entry<Integer, Button> e : gui.getButtons().entrySet()){
            ItemStack is = e.getValue().getIconProvider().isEmpty() ?
                    e.getValue().getIconProvider().getIcon(null) :
                    e.getValue().getIconProvider().getIcon(new GUIContext(this, gui, inv, e.getKey(), null, player, GUIContext.ContextType.OPEN));
            if(is == null){
                GUILib.LOGGER.warning("Null item stack detected at " + e.getKey());
                continue;
            }
            bukkitInv.setItem(e.getKey(), is);
        }
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
            sharedHandlers.get(pos).handle(player, ctx);
            return true;
        }
        if(inv != null && inv != gi.getInventory())
            return true;
        if(pos > gui.getInvSize()) {
            logd("Invalid click position!");
            return true;
        }
        if(gui.getButton(pos) == null){
            logd("Unknown button at " + pos);
            return true;
        }
        ClickHandler handler = gui.getButton(pos).getClickHandler(clickType);
        if(handler == null){
            logd("Handler not set! GUI: " + gui.getName() + ", pos: " + pos + ", type: " + clickType);
            return true;
        }
        handler.handle(player, ctx);
        boolean result = ctx.clickResult();
        logd("Click result: " + result);
        Sound snd = gui.getButton(pos).getSound(clickType, result);
        if(snd == null)
            snd = defaultSound;
        if(snd != null)
            player.playSound(player.getLocation(), snd, 1L, 1L);
        return true;

    }
    private void handleClose(Player player, InventoryView view){
        logd("Handling close (p: " + player.getName() + ")");
        if(guiHolders.containsKey(player)){
            if(view == null || guiHolders.get(player).getView() == view)
                guiHolders.remove(player);
        }
    }
    // TODO: Remove
    public final void logd(String msg){
        if(debug)
            GUILib.LOGGER.info("DBG(" + this.plugin.getName() + "): " + msg);

    }


}
