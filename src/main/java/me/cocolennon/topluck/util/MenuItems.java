package me.cocolennon.topluck.util;

import me.cocolennon.topluck.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class MenuItems {
    private final static MenuItems instance = new MenuItems();

    public ItemStack getItem(Material material, String newName, String localizedName){
        ItemStack it = new ItemStack(material, 1);
        ItemMeta itM = it.getItemMeta();
        assert itM != null;
        if(newName != null) itM.setDisplayName(newName);
        PersistentDataContainer pdc = itM.getPersistentDataContainer();
        NamespacedKey buttonAction = new NamespacedKey(Main.getInstance(), "buttonAction");
        if(localizedName != null) pdc.set(buttonAction, PersistentDataType.STRING, localizedName);
        it.setItemMeta(itM);
        return it;
    }

    public ItemStack getItem(Material material, String newName, String localizedName, ArrayList<String> itemlore){
        ItemStack it = new ItemStack(material, 1);
        ItemMeta itM = it.getItemMeta();
        assert itM != null;
        if(newName != null) itM.setDisplayName(newName);
        PersistentDataContainer pdc = itM.getPersistentDataContainer();
        NamespacedKey buttonAction = new NamespacedKey(Main.getInstance(), "buttonAction");
        if(localizedName != null) pdc.set(buttonAction, PersistentDataType.STRING, localizedName);
        if(itemlore != null) itM.setLore(itemlore);
        it.setItemMeta(itM);
        return it;
    }

    public void fillEmpty(Inventory inv, int start, ItemStack item){
        for(int i = start; i < inv.getSize(); i++){
            if(inv.getItem(i) == null) inv.setItem(i, item);
        }
    }

    public static MenuItems getInstance() {
        return instance;
    }
}
