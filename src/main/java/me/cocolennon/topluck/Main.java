package me.cocolennon.topluck;

import me.cocolennon.topluck.commands.TopLuckCommand;
import me.cocolennon.topluck.listeners.InventoryClick;
import me.cocolennon.topluck.listeners.PlayerBrokeBlock;
import me.cocolennon.topluck.listeners.PlayerJoined;
import me.cocolennon.topluck.listeners.PlayerPlaceBlock;
import me.cocolennon.topluck.util.TopLuckPlaceholders;
import me.cocolennon.topluck.util.UpdateChecker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

public class Main extends JavaPlugin {
    private static Main instance;
    FileConfiguration config = getConfig();
    private String version;
    private String latestVersion;
    private boolean usingOldVersion = false;

    @Override
    public void onEnable() {
        instance = this;
        checkVersion();
        setUpConfig();
        registerCommands();
        registerListeners();
        new TopLuckPlaceholders().register();
        getLogger().info("Plugin enabled!");
    }

    public void checkVersion() {
        new UpdateChecker(this, 113886).getVersion(cVersion -> {
            version = this.getDescription().getVersion();
            latestVersion = cVersion;
            if (!getVersion().equals(cVersion)) {
                getLogger().info("You are using an older version of Top Luck, please update to version " + cVersion);
                usingOldVersion = true;
            }
        });
    }

    private void setUpConfig(){
        List<String> blocks = new LinkedList<>();
        blocks.add("ANCIENT_DEBRIS");
        blocks.add("DIAMOND_ORE");
        blocks.add("DEEPSLATE_DIAMOND_ORE");
        config.addDefault("blocks-to-check", blocks);
        config.options().header("blocks-to-check: The blocks which will be checked when the menu is opened");
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void registerCommands() {
        getCommand("topluck").setExecutor(new TopLuckCommand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerBrokeBlock(), instance);
        getServer().getPluginManager().registerEvents(new InventoryClick(), instance);
        getServer().getPluginManager().registerEvents(new PlayerJoined(), instance);
        getServer().getPluginManager().registerEvents(new PlayerPlaceBlock(), instance);
    }

    public String getVersion() { return version; }
    public String getLatestVersion(){ return latestVersion; }
    public boolean getUsingOldVersion() { return usingOldVersion; }
    public static Main getInstance() { return instance; }
}
