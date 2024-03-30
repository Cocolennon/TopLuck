package me.cocolennon.topluck.util;

import me.cocolennon.topluck.Main;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public class MenuCreator {
    private final static MenuCreator instance = new MenuCreator();
    private final String topluckColor = Main.getInstance().getConfig().getString("plugin-name-color");

    public List<Inventory> getPagesOnline() {
        List<Inventory> topLuckPages = new LinkedList<>();
        List<Player> onlinePlayers = new LinkedList<>(Bukkit.getOnlinePlayers());

        int playerCount = 0;
        int inventorySlots = 18;

        for(int pageNumber = 0; pageNumber <= getPagesCount(true, 0); pageNumber++) {
            Inventory newPage;
            if(Main.getInstance().getConfig().getBoolean("hide-plugin-name")) {
                newPage = Bukkit.createInventory(null, 27, topluckColor + "§lHidden Name §f- §d§lCoco Lennon"); }
            else { newPage = Bukkit.createInventory(null, 27, topluckColor + "§lTop Luck §f- §d§lCoco Lennon"); }
            List<Player> playersInCurrentPage = new LinkedList<>();
            if(pageNumber == getPagesCount(true, 0)) inventorySlots = onlinePlayers.size() - playerCount;
            for(int i = 0; i < inventorySlots; i++){
                playersInCurrentPage.add(onlinePlayers.get(playerCount));
                playerCount++;
            }
            int menuSlot = 0;
            for (Player player : playersInCurrentPage) {
                ItemStack playerHead = getPlayerHeadWithStats(player);
                newPage.setItem(menuSlot, playerHead);
                menuSlot++;
            }
            if(getPagesCount(true, 0) > 0){
                if(pageNumber == 0){
                    newPage.setItem(23, MenuItems.getInstance().getItem(Material.ARROW, "§6§lNext Page", String.valueOf((pageNumber + 1))));
                }else if(pageNumber == getPagesCount(true, 0)){
                    newPage.setItem(21, MenuItems.getInstance().getItem(Material.ARROW, "§6§lPrevious Page", String.valueOf(pageNumber - 1)));
                }else{
                    newPage.setItem(23, MenuItems.getInstance().getItem(Material.ARROW, "§6§lNext Page", String.valueOf(pageNumber + 1)));
                    newPage.setItem(21, MenuItems.getInstance().getItem(Material.ARROW, "§6§lPrevious Page", String.valueOf(pageNumber - 1)));
                }
            }
            newPage.setItem(18, MenuItems.getInstance().getItem(Material.RED_STAINED_GLASS_PANE, "§c§lOffline Players", "offlinePlayers"));
            MenuItems.getInstance().fillEmpty(newPage, 18, MenuItems.getInstance().getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "filler"));
            topLuckPages.add(newPage);
        }
        return topLuckPages;
    }

    public List<Inventory> getPagesOffline() {
        List<Inventory> topLuckPages = new LinkedList<>();
        List<OfflinePlayer> offlinePlayers = new LinkedList<>();

        for(File dataFile : PlayerData.getInstance().getAllDataFiles()) {
            if(dataFile.getName().contains(".yml")) {
                UUID uuid = UUID.fromString(dataFile.getName().replace(".yml", ""));
                OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);
                if (!off.isOnline()) {
                    offlinePlayers.add(off);
                }
            }
        }

        int playerCount = 0;
        int inventorySlots = 18;

        for(int pageNumber = 0; pageNumber <= getPagesCount(false, offlinePlayers.size()); pageNumber++) {
            Inventory newPage;
            if(Main.getInstance().getConfig().getBoolean("hide-plugin-name")) {
                newPage = Bukkit.createInventory(null, 27, topluckColor + "§lHidden Name §f- §d§lCoco Lennon"); }
            else { newPage = Bukkit.createInventory(null, 27, topluckColor + "§lTop Luck §f- §d§lCoco Lennon"); }
            List<OfflinePlayer> playersInCurrentPage = new LinkedList<>();
            if(pageNumber == getPagesCount(false, offlinePlayers.size())) inventorySlots = offlinePlayers.size() - playerCount;
            for(int i = 0; i < inventorySlots; i++){
                playersInCurrentPage.add(offlinePlayers.get(playerCount));
                playerCount++;
            }
            int menuSlot = 0;
            for (OfflinePlayer player : playersInCurrentPage) {
                ItemStack playerHead = getPlayerHeadWithStats(player, player.getUniqueId());
                newPage.setItem(menuSlot, playerHead);
                menuSlot++;
            }
            if(getPagesCount(false, offlinePlayers.size()) > 0){
                if(pageNumber == 0){
                    newPage.setItem(23, MenuItems.getInstance().getItem(Material.ARROW, "§6§lNext Page", String.valueOf((pageNumber + 1))));
                }else if(pageNumber == getPagesCount(false, offlinePlayers.size())){
                    newPage.setItem(21, MenuItems.getInstance().getItem(Material.ARROW, "§6§lPrevious Page", String.valueOf(pageNumber - 1)));
                }else{
                    newPage.setItem(23, MenuItems.getInstance().getItem(Material.ARROW, "§6§lNext Page", String.valueOf(pageNumber + 1)));
                    newPage.setItem(21, MenuItems.getInstance().getItem(Material.ARROW, "§6§lPrevious Page", String.valueOf(pageNumber - 1)));
                }
            }
            newPage.setItem(18, MenuItems.getInstance().getItem(Material.RED_STAINED_GLASS_PANE, "§c§lGo Back", "goBack"));
            MenuItems.getInstance().fillEmpty(newPage, 18, MenuItems.getInstance().getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "filler"));
            topLuckPages.add(newPage);
        }
        return topLuckPages;
    }

    public Inventory getOptionsMenu(Player target) {
        Inventory optionsMenu;
        if(Main.getInstance().getConfig().getBoolean("hide-plugin-name")) {
            optionsMenu = Bukkit.createInventory(null, 27, "§a§l" + target.getName() + "§f - §c§lHidden Name"); }
        else { optionsMenu = Bukkit.createInventory(null, 27, "§a§l" + target.getName() + "§f - §c§lTop Luck"); }
        optionsMenu.setItem(9, getPlayerHeadWithStats(target));
        optionsMenu.setItem(11, MenuItems.getInstance().getItem(Material.CHEST, "§c§lInvSee", "invsee_" + target.getName()));
        optionsMenu.setItem(12, MenuItems.getInstance().getItem(Material.ENDER_CHEST, "§c§lInvSee (Ender Chest)", "ecsee_" + target.getName()));
        optionsMenu.setItem(14, MenuItems.getInstance().getItem(Material.TORCH, "§c§lWarn Publicly", "warn_pb_" + target.getName()));
        optionsMenu.setItem(16, MenuItems.getInstance().getItem(Material.REDSTONE_TORCH, "§c§lWarn Privately", "warn_pv_" + target.getName()));
        MenuItems.getInstance().fillEmpty(optionsMenu, 0, MenuItems.getInstance().getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "filler"));
        return optionsMenu;
    }

    public Inventory getOptionsMenu(OfflinePlayer target) {
        Inventory optionsMenu;
        if(Main.getInstance().getConfig().getBoolean("hide-plugin-name")) {
            optionsMenu = Bukkit.createInventory(null, 27, "§a§l" + target.getName() + "§f - §c§lHidden Name"); }
        else { optionsMenu = Bukkit.createInventory(null, 27, "§a§l" + target.getName() + "§f - §c§lTop Luck"); }
        optionsMenu.setItem(9, getPlayerHeadWithStats(target, target.getUniqueId()));
        optionsMenu.setItem(11, MenuItems.getInstance().getItem(Material.CHEST, "§c§lInvSee", "invsee_off_" + target.getUniqueId()));
        optionsMenu.setItem(12, MenuItems.getInstance().getItem(Material.ENDER_CHEST, "§c§lInvSee (Ender Chest)", "ecsee_off_" + target.getUniqueId()));
        optionsMenu.setItem(14, MenuItems.getInstance().getItem(Material.TORCH, "§c§lWarn Publicly", "warn_pb_off_" + target.getUniqueId()));
        optionsMenu.setItem(16, MenuItems.getInstance().getItem(Material.REDSTONE_TORCH, "§c§lWarn Privately", "warn_pv_off_" + target.getUniqueId()));
        MenuItems.getInstance().fillEmpty(optionsMenu, 0, MenuItems.getInstance().getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "filler"));
        return optionsMenu;
    }

    private int getPagesCount(boolean isOnline, int listSize) {
        if(isOnline) { return Bukkit.getOnlinePlayers().size() / 18; }
        else { return listSize / 18; }
    }

    private ItemStack getPlayerHeadWithStats(Player player) {
        ItemStack playerHead = PlayerHead.getInstance().returnHead(player);
        ItemMeta headMeta = playerHead.getItemMeta();
        FileConfiguration config = Main.getInstance().getConfig();
        FileConfiguration playerData = PlayerData.getInstance().getPlayerData(player);
        ArrayList<String> itemlore = new ArrayList<>();
        int warns = playerData.getInt("warns");
        itemlore.add("§1Warns: §c" + warns);
        int total = playerData.getInt("totalBlocksMined");
        itemlore.add("§1Total Blocks Mined: §c" + total);
        int totalOreMined = 0;
        for(String block : config.getStringList("blocks-to-check")) {
            String blockName = WordUtils.capitalize(block.toLowerCase().replace("_", " "));
            int data = playerData.getInt(block.toLowerCase());
            totalOreMined += data;
            double unrounded = (double) (data*100)/total;
            double rounded = Math.round(unrounded*1000)/1000D;
            itemlore.add("§1" + blockName + "§f: §c" + data + " (" + rounded + "%)");
        }
        double unrounded = (double) (totalOreMined*100)/total;
        double rounded = Math.round(unrounded*1000)/1000D;
        itemlore.add(1, "§1Total Ore Mined: §c" + totalOreMined + " (" + rounded + "%)");
        headMeta.setLore(itemlore);
        playerHead.setItemMeta(headMeta);
        return playerHead;
    }

    private ItemStack getPlayerHeadWithStats(OfflinePlayer player, UUID uuid) {
        ItemStack playerHead = PlayerHead.getInstance().returnHead(player, uuid);
        ItemMeta headMeta = playerHead.getItemMeta();
        FileConfiguration config = Main.getInstance().getConfig();
        FileConfiguration playerData = PlayerData.getInstance().getPlayerData(player);
        ArrayList<String> itemlore = new ArrayList<>();
        int warns = playerData.getInt("warns");
        itemlore.add("§1Warns: §c" + warns);
        int total = playerData.getInt("totalBlocksMined");
        itemlore.add("§1Total Blocks Mined: §c" + total);
        int totalOreMined = 0;
        for(String block : config.getStringList("blocks-to-check")) {
            String blockName = WordUtils.capitalize(block.toLowerCase().replace("_", " "));
            int data = playerData.getInt(block.toLowerCase());
            totalOreMined += data;
            double unrounded = (double) (data*100)/total;
            double rounded = Math.round(unrounded*1000)/1000D;
            itemlore.add("§1" + blockName + "§f: §c" + data + " (" + rounded + "%)");
        }
        double unrounded = (double) (totalOreMined*100)/total;
        double rounded = Math.round(unrounded*1000)/1000D;
        itemlore.add(1, "§1Total Ore Mined: §c" + totalOreMined + " (" + rounded + "%)");
        headMeta.setLore(itemlore);
        playerHead.setItemMeta(headMeta);
        return playerHead;
    }

    public static MenuCreator getInstance() {
        return instance;
    }
}
