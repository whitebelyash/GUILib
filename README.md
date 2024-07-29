# GUILib (WIP)
Library for inventory-based GUI (Bukkit).
Still work-in-progress.

## Installation
1. Install to local repo
```shell
   $ mvn install
```
2. Add as dependency
```xml
    <dependency>
        <groupId>ru.whbex.lib</groupId>
        <artifactId>gui</artifactId>
        <version>0.3-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
```

## TODO
- [ ] Finish ListGUI
- [ ] Refactor click sounds
- [ ] Add hooks support (for e.g. translating strings(lore,name) in StaticIconProvider)
- [ ] Abstract library from Bukkit API
- [ ] Use packets (NMS?) for GUI (will break api)

## Usage example
```java
public class GUIExample {
    private void showSimple(Player player, GUIManager gm){
            // Create simple icon
            // Color codes (&) are supported in both lore & name
            IconProvider icon = StaticIconProvider.builder()
                .name("Simple icon") // item name
                .appendLore("&fDoes nothing :)") // item lore (will have &5 color)
                .material(Material.BEDROCK) // item material
                .count(2) // item amount
                .build();
            // Create button
            Button b = Button.builder(icon)
                .addClickHandler(ClickType.LEFT, ((player, ctx) -> {
                    player.sendMessage("Really, it does nothing...");
                    // Callback code here
                }), false) // click callback
                // sound on success (ctx.clickResult() == true)
                .addSoundSuccess(ClickType.LEFT, Sound.ENTITY_GHAST_SCREAM)
                .build();
            // Create GUI
            GUI gui = GUI.builder()
                .name("Simple") // GUI (Inventory) title
                .set(4, b) // Set button 'b' at slot 4
                .size(1) // Inventory size (in lines)
                .build();
            gm.open(player, gui); // Open GUI for player
    }
}
```
[More...](GUILib-Examples/src/main/java/ru/whbex/develop/guilib/examples)

