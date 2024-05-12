package me.cocolennon.topluck.listeners;

import me.cocolennon.topluck.Main;
import me.cocolennon.topluck.util.MenuCreator;
import me.cocolennon.topluck.util.PlayerData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
            try {
                Player holder = (Player) inv.getHolder();
                if(inv.getType() == InventoryType.PLAYER || inv.getType() == InventoryType.ENDER_CHEST) {
                    if(holder == null) return;
                    if(holder.getUniqueId() != player.getUniqueId()) event.setCancelled(true);
                }
            }catch(ClassCastException err) {
                // do nothing lol!
            }
        }
        if(inv.getItem(0) == null) return;
        if(current == null) return;
        if(!current.hasItemMeta()) return;
        NamespacedKey buttonAction = new NamespacedKey(Main.getInstance(), "buttonAction");
        PersistentDataContainer pdc = getItemMeta(current).getPersistentDataContainer();
        if(!pdc.has(buttonAction)) return;

        event.setCancelled(true);
        String currentDName = getItemMeta(current).getDisplayName();
        String currentLName = pdc.get(buttonAction, PersistentDataType.STRING);

        if(StringUtils.isNumeric(currentLName)) {
            if(currentDName.equals("§6§lNext Page") || currentDName.equals("§6§lPrevious Page")){
                if(inv.getItem(18).getItemMeta().getDisplayName().equals("§c§lGo Back")) {
                    player.openInventory(MenuCreator.getInstance().getPagesOffline().get(Integer.parseInt(currentLName)));
                }else {
                    player.openInventory(MenuCreator.getInstance().getPagesOnline().get(Integer.parseInt(currentLName)));
                }
            }
        }else if(currentLName.startsWith("playerHead") && inv.getItem(0).getItemMeta().getPersistentDataContainer().get(buttonAction, PersistentDataType.STRING).startsWith("playerHead")){
            Player target = Bukkit.getPlayer(currentLName.replace("playerHead_", ""));
            if(target == null) {
                OfflinePlayer offTarget = Bukkit.getOfflinePlayer(UUID.fromString(currentLName.replace("playerHead_", "")));
                player.openInventory(MenuCreator.getInstance().getOptionsMenu(offTarget));
            }else player.openInventory(MenuCreator.getInstance().getOptionsMenu(target));
        }else if(currentLName.equals("offlinePlayers")){
            List<Inventory> inventories = new LinkedList<>(MenuCreator.getInstance().getPagesOffline());
            player.openInventory(inventories.get(0));
        }else if(currentLName.equals("goBack")){
            List<Inventory> inventories = new LinkedList<>(MenuCreator.getInstance().getPagesOnline());
            player.openInventory(inventories.get(0));
        }else if(inv.getItem(0).getItemMeta().getPersistentDataContainer().get(buttonAction, PersistentDataType.STRING).equals("filler")){
            if(currentLName.startsWith("invsee_")) {
                if(!player.hasPermission("topluck.invsee")) {
                    player.sendMessage(error);
                    return;
                }
                String playerName = currentLName.replace("invsee_", "");
                if(playerName.startsWith("off_")) {
                    OfflinePlayer offTarget = Bukkit.getOfflinePlayer(UUID.fromString(playerName.replace("off_", "")));
                    Inventory inventory = PlayerData.getInstance().getPlayerInventory(offTarget);
                    if(inventory == null) {
                        player.sendMessage(topluckColor + "[" + pluginName + "] " + errorColor + "This player's inventory is not cached!");
                        return;
                    }
                    player.openInventory(inventory);
                }else{
                    Player target = Bukkit.getPlayer(playerName);
                    player.openInventory(target.getInventory());
                }
            }else if(currentLName.startsWith("ecsee_")){
                if(!player.hasPermission("topluck.invsee")) {
                    player.sendMessage(error);
                    return;
                }
                String playerName = currentLName.replace("ecsee_", "");
                if(playerName.startsWith("off_")) {
                    OfflinePlayer offTarget = Bukkit.getOfflinePlayer(UUID.fromString(playerName.replace("off_", "")));
                    Inventory enderChest = PlayerData.getInstance().getPlayerEnderChest(offTarget);
                    if(enderChest == null) {
                        player.sendMessage(topluckColor + "[" + pluginName + "] " + errorColor + "This player's enderchest is not cached!");
                        return;
                    }
                    player.openInventory(enderChest);
                }else {
                    Player target = Bukkit.getPlayer(playerName);
                    player.openInventory(target.getEnderChest());
                }
            }else if(currentLName.startsWith("warn_")) {
                String newLName = currentLName.replace("warn_", "");
                if(newLName.startsWith("pb_")) {
                    if(!player.hasPermission("topluck.warn.public")) {
                        player.sendMessage(error);
                        return;
                    }
                    String playerName = newLName.replace("pb_", "");
                    if(playerName.startsWith("off_")) {
                        OfflinePlayer offTarget = Bukkit.getOfflinePlayer(UUID.fromString(playerName.replace("off_", "")));
                        PlayerData.getInstance().addPlayerData(offTarget, "warns");
                        Bukkit.broadcastMessage(String.format(topluckColor + "[" + pluginName + "] " + errorColor + Main.getInstance().getConfig().getString("warn-message-public"), offTarget.getName(), player.getName()));
                        player.sendMessage(topluckColor + "[" + pluginName + "] " + successColor + "You have publicly warned " + offTarget.getName() + "!");
                    }else{
                        Player target = Bukkit.getPlayer(playerName);
                        PlayerData.getInstance().addPlayerData(target, "warns");
                        Bukkit.broadcastMessage(String.format(topluckColor + "[" + pluginName + "] " + errorColor + Main.getInstance().getConfig().getString("warn-message-public"), target.getName(), player.getName()));
                        player.sendMessage(topluckColor + "[" + pluginName + "] " + successColor + "You have publicly warned " + target.getName() + "!");
                    }
                }else if(newLName.startsWith("pv_")) {
                    if(!player.hasPermission("topluck.warn.private")) {
                        player.sendMessage(error);
                        return;
                    }
                    String playerName = newLName.replace("pv_", "");
                    if(playerName.startsWith("off_")) {
                        OfflinePlayer offTarget = Bukkit.getOfflinePlayer(UUID.fromString(playerName.replace("off_", "")));
                        PlayerData.getInstance().addPlayerData(offTarget, "warns");
                        player.sendMessage(topluckColor + "[" + pluginName + "] " + successColor +"§dYou have privately warned " + offTarget.getName() + "!");
                    }else{
                        Player target = Bukkit.getPlayer(newLName.replace("pv_", ""));
                        PlayerData.getInstance().addPlayerData(target, "warns");
                        target.sendMessage(String.format(topluckColor + "[" + pluginName + "] " + errorColor + Main.getInstance().getConfig().getString("warn-message-private"), player.getName()));
                        player.sendMessage(topluckColor + "[" + pluginName + "] " + successColor +"§dYou have privately warned " + target.getName() + "!");
                    }
                }
            }
        }
    }

    @Nonnull
    public ItemMeta getItemMeta(ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta());
    }
}
