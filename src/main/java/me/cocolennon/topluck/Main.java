package me.cocolennon.topluck;

import me.cocolennon.topluck.commands.TopLuckCommand;
import me.cocolennon.topluck.listeners.InventoryClick;
import me.cocolennon.topluck.listeners.PlayerBrokeBlock;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

public class Main extends JavaPlugin {
    private static Main instance;
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        instance = this;
        setUpConfig();
        registerCommands();
        registerListeners();
        getLogger().info("Plugin enabled!");
    }

    private void setUpConfig(){
        List<String> blocks = new LinkedList<>();
        blocks.add("ANCIENT_DEBRIS");
        blocks.add("DIAMOND_ORE");
        blocks.add("DEEPSLATE_DIAMOND_ORE");
        config.addDefault("blocks-to-check", blocks);
        config.options().setHeader(List.of("blocks-to-check: The blocks which will be checked when the menu is opened"));
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void registerCommands() {
        getCommand("topluck").setExecutor(new TopLuckCommand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerBrokeBlock(), instance);
        getServer().getPluginManager().registerEvents(new InventoryClick(), instance);
    }

    public static Main getInstance() { return instance; }
}
