package me.cocolennon.topluck.listeners;

import me.cocolennon.topluck.Main;
import me.cocolennon.topluck.util.MenuCreator;
import me.cocolennon.topluck.util.PlayerData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class InventoryClick implements Listener {
    private final String topluckColor = Main.getInstance().getConfig().getString("plugin-name-color");
    private final String successColor = Main.getInstance().getConfig().getString("messages-success-color");
    private final String errorColor = Main.getInstance().getConfig().getString("messages-error-color");

    @EventHandler
    public void InventoryClicked(InventoryClickEvent event) {
        String error;
        String pluginName;
        if(Main.getInstance().getConfig().getBoolean("hide-plugin-name")) {
            error = topluckColor + "[Hidden Name]" + errorColor + "You can't do that!"; pluginName = "Hidden Name"; }
        else { error = topluckColor + "[Top Luck]" + errorColor + "You can't do that!"; pluginName = "Top Luck"; }
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if(!player.hasPermission("topluck.invsee.move")) {
            Player holder = (Player) inv.getHolder();
            if(inv.getType() == InventoryType.PLAYER || inv.getType() == InventoryType.ENDER_CHEST) {
                if(holder.getUniqueId() != player.getUniqueId()) event.setCancelled(true);
            }
        }
        if(inv.getItem(0) == null) return;
        if(current == null) return;
        if(!current.hasItemMeta()) return;
        if(!getItemMeta(current).hasLocalizedName()) return;

        event.setCancelled(true);
        String currentDName = getItemMeta(current).getDisplayName();
        String currentLName = getItemMeta(current).getLocalizedName();

        if(StringUtils.isNumeric(currentLName)) {
            if(currentDName.equals("§6§lNext Page") || currentDName.equals("§6§lPrevious Page")){
                if(inv.getItem(18).getItemMeta().getDisplayName().equals("§c§lGo Back")) {
                    player.openInventory(MenuCreator.getInstance().getPagesOffline().get(Integer.parseInt(currentLName)));
                }else {
                    player.openInventory(MenuCreator.getInstance().getPagesOnline().get(Integer.parseInt(currentLName)));
                }
            }
        }else if(currentLName.startsWith("playerHead") && inv.getItem(0).getItemMeta().getLocalizedName().startsWith("playerHead")){
            Player target = Bukkit.getPlayer(currentLName.replace("playerHead_", ""));
            player.openInventory(MenuCreator.getInstance().getOptionsMenu(target));
        }else if(currentLName.equals("offlinePlayers")){
            List<Inventory> inventories = new LinkedList<>(MenuCreator.getInstance().getPagesOffline());
            player.openInventory(inventories.get(0));
        }else if(currentLName.equals("goBack")){
            List<Inventory> inventories = new LinkedList<>(MenuCreator.getInstance().getPagesOnline());
            player.openInventory(inventories.get(0));
        }else if(inv.getItem(0).getItemMeta().getLocalizedName().equals("filler")){
            if(currentLName.startsWith("invsee_")) {
                if(!player.hasPermission("topluck.invsee")) {
                    player.sendMessage(error);
                    return;
                }
                Player target = Bukkit.getPlayer(currentLName.replace("invsee_", ""));
                player.openInventory(target.getInventory());
            }else if(currentLName.startsWith("ecsee_")){
                if(!player.hasPermission("topluck.invsee")) {
                    player.sendMessage(error);
                    return;
                }
                Player target = Bukkit.getPlayer(currentLName.replace("ecsee_", ""));
                player.openInventory(target.getEnderChest());
            }else if(currentLName.startsWith("warn_")) {
                String newLName = currentLName.replace("warn_", "");
                if(newLName.startsWith("pb_")) {
                    if(!player.hasPermission("topluck.warn.public")) {
                        player.sendMessage(error);
                        return;
                    }
                    Player target = Bukkit.getPlayer(newLName.replace("pb_", ""));
                    PlayerData.getInstance().addPlayerData(target, "warns");
                    Bukkit.broadcastMessage(String.format(topluckColor + "[" + pluginName + "] " + errorColor + Main.getInstance().getConfig().getString("warn-message-public"), target.getName(), player.getName()));
                    player.sendMessage(topluckColor + "[" + pluginName + "] " + successColor + "You have publicly warned " + target.getName() + "!");
                }else if(newLName.startsWith("pv_")) {
                    if(!player.hasPermission("topluck.warn.private")) {
                        player.sendMessage(error);
                        return;
                    }
                    Player target = Bukkit.getPlayer(newLName.replace("pv_", ""));
                    PlayerData.getInstance().addPlayerData(target, "warns");
                    target.sendMessage(String.format(topluckColor + "[" + pluginName + "] " + errorColor + Main.getInstance().getConfig().getString("warn-message-private"), player.getName()));
                    player.sendMessage(topluckColor + "[" + pluginName + "] " + successColor +"§dYou have privately warned " + target.getName() + "!");
                }
            }
        }
    }

    @Nonnull
    public ItemMeta getItemMeta(ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta());
    }
}
