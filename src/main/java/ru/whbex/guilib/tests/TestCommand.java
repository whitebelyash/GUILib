package ru.whbex.guilib.tests;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.whbex.guilib.GUILib;
import ru.whbex.guilib.gui.*;
import ru.whbex.guilib.gui.icon.DynamicIconProvider;
import ru.whbex.guilib.gui.icon.IconProvider;
import ru.whbex.guilib.gui.icon.StaticIconProvider;
import ru.whbex.guilib.util.ItemUtils;
import ru.whbex.guilib.util.PatternParser;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage("Open test gui");
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("you must be player to run this command!");
            return true;
        }
        Player p = (Player) commandSender;
        GUIManager gm = GUILib.getInstance().getGUIManager();
        if(strings.length < 1){
            commandSender.sendMessage("/guitesting simple|pattern|dynamic");
            return true;
        }
        String type = strings[0];
        try {
            switch (type) {
                case "simple":
                    showTest(gm, p);
                    break;
                case "pattern":
                    showPattern(gm, p);
                    break;
                case "dynamic":
                    showDynamic(gm, p);
                    break;
                default:
                    commandSender.sendMessage("Unknown gui " + type);
                    return true;
            }
        } catch (Exception e){
            commandSender.sendMessage("FAIL!");
            e.printStackTrace();
        }
        return true;
    }
    private void showTest(GUIManager gm, Player p){
        IconProvider icon = StaticIconProvider.builder().
                name("Hello world!")
                .appendLore("test")
                .count(32)
                .material(Material.ACACIA_DOOR)
                .build();
        IconProvider icon1 = StaticIconProvider.builder()
                .name("Click me")
                .appendLore("Lox")
                .count(1)
                .material(Material.BEDROCK)
                .build();
        Button b = Button.builder()
                .name("Hello world button")
                .icon(icon)
                .addClickHandler(ClickType.LEFT, ((player, context) -> {
                    context.guiManager().logd("Executing click callback");
                    player.sendMessage("Ты лох, потому что нажал на " + context.pos() + " кнопку ХАА!!");
                }))
                .addSoundSuccess(ClickType.LEFT, Sound.BLOCK_AMETHYST_BLOCK_BREAK)
                .build();
        Button b1 = Button.builder()
                .name("Click me")
                .icon(icon1)
                .addSoundSuccess(ClickType.LEFT, Sound.ENTITY_ENDER_DRAGON_DEATH)
                .addClickHandler(ClickType.LEFT, (((player, context) -> {
                    player.sendMessage("Kek");
                    context.guiManager().getGUIInstance(player).updateButton(context.pos(), b, context);
                })))
                .build();
        GUI gui = GUI.builder()
                .name("Test GUI")
                .map(1, b)
                .map(0, b)
                .map(3, b)
                .map(5, b1)
                .build();
        gm.open(p, gui);
        p.sendMessage("Success");
    }
    private void showPattern(GUIManager gm, Player p){
        IconProvider ficon = StaticIconProvider.builder().
                name("Kish")
                .count(64)
                .material(Material.STONE)
                .build();
        IconProvider gicon = StaticIconProvider.builder()
                .name("Джы")
                .material(Material.DIAMOND)
                .build();
        Button f = Button.builder()
                .name("kish")
                .icon(ficon)
                .addClickHandler(ClickType.LEFT, ((player, context) -> player.sendMessage("Cringe")))
                .addClickHandler(ClickType.RIGHT, (((player, context) -> player.sendMessage("Right click bro"))))
                .addSoundSuccess(ClickType.LEFT, Sound.BLOCK_STONE_BREAK)
                .build();
        Button g = Button.builder()
                .name("g")
                .icon(gicon)
                .addClickHandler(ClickType.LEFT, (((player, context) -> player.kickPlayer("Вы были заблокированы навсегда"))))
                .build();
        String[] pattern = {"###f#####", "######g##", "gf#######"};
        p.sendMessage("Show pattern " + String.join(", ", pattern));
        GUI.Builder b = GUI.builder();
        PatternParser.parse(pattern, b);
        b.map('f', f).map('g', g);
        gm.open(p, b.build());
    }
    private void showDynamic(GUIManager gm, Player p){
        final String[] pattern = {"####d####"};
        final String name = "Dynamic icon";
        final List<String> lore = Arrays.asList("shit", "this icon is dynamic");
        Function<GUIContext, ItemStack> func = ctx -> {
            ctx.player().sendMessage("Hello from DynamicIconProvider");
            return ItemUtils.createItem("Haha lol", Material.STONE);
        };
        IconProvider icon = new DynamicIconProvider(func);
        Button b = Button.builder()
                .icon(icon)
                .addClickHandler(ClickType.LEFT, ((player, context) -> player.sendMessage("Click!")))
                .build();
        GUI.Builder gb = GUI.builder();
        PatternParser.parse(pattern, gb);
        gb.map('d', b);
        gm.open(p, gb.build());
    }
}
