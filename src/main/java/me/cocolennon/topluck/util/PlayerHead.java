package me.cocolennon.topluck.util;

import me.cocolennon.topluck.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PlayerHead {
    private final static PlayerHead instance = new PlayerHead();

    public final ItemStack returnHead(Player p) {
        ItemStack head = getHead(p);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("§a" + p.getName());
        PersistentDataContainer pdc = headMeta.getPersistentDataContainer();
        NamespacedKey buttonAction = new NamespacedKey(Main.getInstance(), "buttonAction");
        pdc.set(buttonAction, PersistentDataType.STRING, "playerHead_" + p.getName());
        head.setItemMeta(headMeta);
        return head;
    }

    public final ItemStack returnHead(OfflinePlayer p, UUID uuid) {
        ItemStack head = getHead(p);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("§a" + p.getName());
        PersistentDataContainer pdc = headMeta.getPersistentDataContainer();
        NamespacedKey buttonAction = new NamespacedKey(Main.getInstance(), "buttonAction");
        pdc.set(buttonAction, PersistentDataType.STRING, "playerHead_" + uuid);
        head.setItemMeta(headMeta);
        return head;
    }

    private ItemStack getHead(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        assert skull != null;
        skull.setDisplayName(player.getName());
        if(Main.getInstance().getConfig().getBoolean("use-player-heads-in-menu")) skull.setOwningPlayer(player);
        item.setItemMeta(skull);
        return item;
    }

    private ItemStack getHead(OfflinePlayer player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        assert skull != null;
        skull.setDisplayName(player.getName());
        if(Main.getInstance().getConfig().getBoolean("use-player-heads-in-menu")) skull.setOwningPlayer(player);
        item.setItemMeta(skull);
        return item;
    }

    public static PlayerHead getInstance() {
        return instance;
    }
}
