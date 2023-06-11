package ru.whbex.guilib;

import org.bukkit.configuration.Configuration;

public class GLConfig {
    public final boolean REUSE_GI;

    public GLConfig(Configuration conf){
        this.REUSE_GI = conf.getBoolean("reuse-gi", true);
    }
}
