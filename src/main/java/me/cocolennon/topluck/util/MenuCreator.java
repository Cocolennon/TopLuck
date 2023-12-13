package me.cocolennon.topluck.util;

import me.cocolennon.topluck.Main;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MenuCreator {
    private final static MenuCreator instance = new MenuCreator();

    public List<Inventory> getPages() {
        List<Inventory> topLuckPages = new LinkedList<>();
        List<Player> onlinePlayers = new LinkedList<>(Bukkit.getOnlinePlayers());

        int playerCount = 0;
        int inventorySlots = 18;

        for(int pageNumber = 0; pageNumber <= getPagesCount(); pageNumber++) {
            Inventory newPage;
            if(Main.getInstance().getConfig().getBoolean("hide-plugin-name")) {
                newPage = Bukkit.createInventory(null, 27, "§c§lHidden Name §f- §d§lCoco Lennon"); }
            else { newPage = Bukkit.createInventory(null, 27, "§c§lTop Luck §f- §d§lCoco Lennon"); }
            List<Player> playersInCurrentPage = new LinkedList<>();
            if(pageNumber == getPagesCount()) inventorySlots = onlinePlayers.size() - playerCount;
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
            if(getPagesCount() > 0){
                if(pageNumber == 0){
                    newPage.setItem(23, MenuItems.getInstance().getItem(Material.ARROW, "§6§lNext Page", String.valueOf((pageNumber + 1))));
                }else if(pageNumber == getPagesCount()){
                    newPage.setItem(21, MenuItems.getInstance().getItem(Material.ARROW, "§6§lPrevious Page", String.valueOf(pageNumber - 1)));
                }else{
                    newPage.setItem(23, MenuItems.getInstance().getItem(Material.ARROW, "§6§lNext Page", String.valueOf(pageNumber + 1)));
                    newPage.setItem(21, MenuItems.getInstance().getItem(Material.ARROW, "§6§lPrevious Page", String.valueOf(pageNumber - 1)));
                }
            }

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

    private int getPagesCount() {
        return Bukkit.getOnlinePlayers().size() / 18;
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

    public static MenuCreator getInstance() {
        return instance;
    }
}
