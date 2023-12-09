package me.cocolennon.topluck.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerHead {
    private final static PlayerHead instance = new PlayerHead();

    public final ItemStack returnHead(Player p) {
        ItemStack head = getHead(p);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("§a" + p.getName());
        headMeta.setLocalizedName("playerHead");
        head.setItemMeta(headMeta);
        return head;
    }

    private ItemStack getHead(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        assert skull != null;
        skull.setDisplayName(player.getName());
        skull.setOwningPlayer(player);
        item.setItemMeta(skull);
        return item;
    }

    public static PlayerHead getInstance() {
        return instance;
    }
}
