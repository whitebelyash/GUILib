package ru.whbex.lib.gui.click;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ClickSound {
    public static final float DEF_VOLUME = 1;
    public static final float DEF_PITCH= 1;
    public static void playSound(Player player, ClickSound sound, boolean success){
        player.playSound(player.getLocation(), sound.getSound(success), sound.getVolume(), sound.getPitch());

    }
    private Sound soundSuccess;
    private Sound soundFail;
    private final float volume;
    private final float pitch;

    public ClickSound(Sound sndSuccess, Sound sndFail, float volume, float pitch){
        this.soundFail = sndFail;
        this.soundSuccess = sndSuccess;
        this.volume = volume;
        this.pitch = pitch;
    }
    public ClickSound(Sound sndSuccess, Sound sndFail){
        this(sndSuccess, sndFail, DEF_VOLUME, DEF_PITCH);
    }
    public ClickSound(Sound snd){
        this(snd, snd, DEF_VOLUME, DEF_PITCH);
    }
    public ClickSound(){
        this(null, null);
    }

    public Sound getSound(boolean success){
        return success ? soundSuccess : soundFail;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }
    public void setFail(Sound snd){
        this.soundFail = snd;
    }
    public void setSuccess(Sound snd){
        this.soundSuccess = snd;
    }
}
