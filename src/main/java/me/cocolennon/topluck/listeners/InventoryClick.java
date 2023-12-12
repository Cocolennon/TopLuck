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
import java.util.Objects;

public class InventoryClick implements Listener {
    @EventHandler
    public void InventoryClicked(InventoryClickEvent event) {
        String error;
        String pluginName;
        if(Main.getInstance().getConfig().getBoolean("hide-plugin-name")) {
            error = "§a[Hidden Name] §cYou can't do that!"; pluginName = "Hidden Name"; }
        else { error = "§a[Top Luck] §cYou can't do that!"; pluginName = "Top Luck"; }
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if(inv.getType() == InventoryType.PLAYER && !player.hasPermission("topluck.invsee.move")) event.setCancelled(true);
        if(inv.getItem(0) == null) return;
        if(current == null) return;
        if(!current.hasItemMeta()) return;
        if(!getItemMeta(current).hasLocalizedName()) return;

        event.setCancelled(true);
        String currentDName = getItemMeta(current).getDisplayName();
        String currentLName = getItemMeta(current).getLocalizedName();

        if(StringUtils.isNumeric(currentLName)) {
            if(currentDName.equals("§6§lNext Page") || currentDName.equals("§6§lPrevious Page")){
                player.openInventory(MenuCreator.getInstance().getPages().get(Integer.parseInt(currentLName)));
            }
        }else if(currentLName.startsWith("playerHead") && inv.getItem(0).getItemMeta().getLocalizedName().startsWith("playerHead")){
            Player target = Bukkit.getPlayer(currentLName.replace("playerHead_", ""));
            player.openInventory(MenuCreator.getInstance().getOptionsMenu(target));
        }else if(inv.getItem(0).getItemMeta().getLocalizedName().equals("filler")){
            if(currentLName.startsWith("invsee_")) {
                if(!player.hasPermission("topluck.invsee")) {
                    player.sendMessage(error);
                    return;
                }
                Player target = Bukkit.getPlayer(currentLName.replace("invsee_", ""));
                player.openInventory(target.getInventory());
            }else if(currentLName.startsWith("warn_")) {
                String newLName = currentLName.replace("warn_", "");
                if(newLName.startsWith("pb_")) {
                    if(!player.hasPermission("topluck.warn.public")) {
                        player.sendMessage(error);
                        return;
                    }
                    Player target = Bukkit.getPlayer(newLName.replace("pb_", ""));
                    PlayerData.getInstance().addPlayerData(target, "warns");
                    Bukkit.broadcastMessage(String.format("§a[" + pluginName + "] §c" + Main.getInstance().getConfig().getString("warn-message-public"), target.getName(), player.getName()));
                    player.sendMessage("§a[" + pluginName + "] §dYou have publicly warned " + target.getName() + "!");
                }else if(newLName.startsWith("pv_")) {
                    if(!player.hasPermission("topluck.warn.private")) {
                        player.sendMessage(error);
                        return;
                    }
                    Player target = Bukkit.getPlayer(newLName.replace("pv_", ""));
                    PlayerData.getInstance().addPlayerData(target, "warns");
                    target.sendMessage(String.format("§a[" + pluginName + "] §c" + Main.getInstance().getConfig().getString("warn-message-private"), player.getName()));
                    player.sendMessage("§a[" + pluginName + "] §dYou have privately warned " + target.getName() + "!");
                }
            }
        }
    }

    @Nonnull
    public ItemMeta getItemMeta(ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta());
    }
}
