package me.cocolennon.topluck.listeners;

import me.cocolennon.topluck.util.MenuCreator;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.Objects;

public class InventoryClick implements Listener {
    @EventHandler
    public void InventoryClicked(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

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
        }
    }

    @Nonnull
    public ItemMeta getItemMeta(ItemStack item) {
        return Objects.requireNonNull(item.getItemMeta());
    }
}
