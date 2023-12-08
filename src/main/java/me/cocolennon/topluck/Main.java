package me.cocolennon.topluck;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Plugin enabled!");
    }

    public static Main getInstance() { return instance; }
}
