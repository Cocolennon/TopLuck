package me.cocolennon.topluck.util;

import me.cocolennon.topluck.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PlayerData {
    private final static PlayerData instance = new PlayerData();
    private final Main mainInstance = Main.getInstance();
    FileConfiguration config = mainInstance.getConfig();

    public FileConfiguration getPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("TopLuck").getDataFolder(), File.separator + "playerdata");
        File f = new File(userdata, File.separator + uuid + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

        if(!f.exists()) {
            try {
                playerData.set("totalBlocksMined", 0);
                List<String> blocksToCheck = config.getStringList("blocks-to-check");
                for(String block : blocksToCheck) {
                    playerData.set(block.toLowerCase(), 0);
                }
                playerData.set("warns", 0);
                playerData.save(f);
            }catch(IOException exception){
                exception.printStackTrace();
            }
        }

        return playerData;
    }

    public void addPlayerData(Player player, @Nullable String data) {
        UUID uuid = player.getUniqueId();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("TopLuck").getDataFolder(), File.separator + "playerdata");
        File f = new File(userdata, File.separator + uuid + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

        if(!f.exists()) {
            try {
                playerData.set("totalBlocksMined", 0);
                List<String> blocksToCheck = config.getStringList("blocks-to-check");
                for(String block : blocksToCheck) {
                    playerData.set(block.toLowerCase(), 0);
                }
                playerData.set("warns", 0);
                playerData.save(f);
            }catch(IOException exception){
                exception.printStackTrace();
            }
        }

        int totalBlocksMined = playerData.getInt("totalBlocksMined");
        playerData.set("totalBlocksMined", totalBlocksMined+1);

        if(data != null) {
            int dataValue = playerData.getInt(data.toLowerCase());
            playerData.set(data.toLowerCase(), dataValue + 1);
        }

        try {
            playerData.save(f);
        }catch(IOException exception){
            exception.printStackTrace();
        }
    }

    public static PlayerData getInstance() {
        return instance;
    }
}
