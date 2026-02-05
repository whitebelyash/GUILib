package ru.whbex.lib.gui.click;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ClickSound {
    public static final float DEF_VOLUME = 1;
    public static final float DEF_PITCH= 1;
    public static void playSound(Player player, ClickSound sound){
        player.playSound(player.getLocation(), sound.getSound(), sound.getVolume(), sound.getPitch());

    }
    private final Sound sound;
    private final float volume;
    private final float pitch;

    public ClickSound(Sound sound, float volume, float pitch){
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }
    public ClickSound(Sound snd){
        this(snd, DEF_VOLUME, DEF_PITCH);
    }
    public ClickSound(){
        this(null);
    }

    public Sound getSound(){
        return sound;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }
}
