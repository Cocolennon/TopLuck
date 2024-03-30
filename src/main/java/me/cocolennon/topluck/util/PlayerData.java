package me.cocolennon.topluck.util;

import me.cocolennon.topluck.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

    public FileConfiguration getPlayerData(OfflinePlayer player) {
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

    public void addPlayerData(OfflinePlayer player, @Nullable String data) {
        UUID uuid = player.getUniqueId();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("TopLuck").getDataFolder(), File.separator + "playerdata");
        File f = new File(userdata, File.separator + uuid + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);

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

    public Inventory getPlayerInventory(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("TopLuck").getDataFolder(), File.separator + "playerdata");
        File f = new File(userdata, File.separator + uuid + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
        List<ItemStack> playerItems = (List<ItemStack>) playerData.getList("inventory");
        if(playerItems == null) return null;
        Inventory playerInventory = Bukkit.createInventory(null, 36, player.getName() + "'s offline inventory");
        for(ItemStack item : playerItems) {
            playerInventory.addItem(item);
        }
        return playerInventory;
    }

    public Inventory getPlayerEnderChest(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("TopLuck").getDataFolder(), File.separator + "playerdata");
        File f = new File(userdata, File.separator + uuid + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
        List<ItemStack> playerItems = (List<ItemStack>) playerData.getList("enderchest");
        if(playerItems == null) return null;
        Inventory playerEnderChest = Bukkit.createInventory(null, 27, player.getName() + "'s offline enderchest");
        for(ItemStack item : playerItems) {
            playerEnderChest.addItem(item);
        }
        return playerEnderChest;
    }

    public void savePlayerInventory(Player player) {
        UUID uuid = player.getUniqueId();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("TopLuck").getDataFolder(), File.separator + "playerdata");
        File f = new File(userdata, File.separator + uuid + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(f);
        ArrayList<ItemStack> invItemArray = new ArrayList<>();
        ArrayList<ItemStack> ecItemArray = new ArrayList<>();
        for(ItemStack item : player.getInventory().getContents()) {
            if(item == null) continue;
            invItemArray.add(item);
        }
        for(ItemStack item : player.getEnderChest().getContents()) {
            if(item == null) continue;
            ecItemArray.add(item);
        }
        playerData.set("inventory", invItemArray);
        playerData.set("enderchest", ecItemArray);
        try {
            playerData.save(f);
        }catch(IOException exception){
            exception.printStackTrace();
        }
    }

    public boolean hasData(Player player) {
        UUID uuid = player.getUniqueId();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("TopLuck").getDataFolder(), File.separator + "playerdata");
        File f = new File(userdata, File.separator + uuid + ".yml");
        return f.exists();
    }

    public boolean hasData(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("TopLuck").getDataFolder(), File.separator + "playerdata");
        File f = new File(userdata, File.separator + uuid + ".yml");
        return f.exists();
    }

    public List<File> getAllDataFiles() {
        File userdata = new File(Bukkit.getServer().getPluginManager().getPlugin("TopLuck").getDataFolder(), File.separator + "playerdata");
        return Arrays.stream(userdata.listFiles()).toList();
    }

    public static PlayerData getInstance() {
        return instance;
    }
}
