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
        <groupId>ru.whbex</groupId>
        <artifactId>GUILib</artifactId>
        <version>enter-guilib-version-here</version>
    </dependency>
```

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
                .map(4, b) // Map button 'b' to slot 4
                .size(1) // Inventory size (in lines)
                .build();
            gm.open(player, gui); // Open GUI for player
    }
}
```
[More...](src/main/java/ru/whbex/guilib/gui/examples)

