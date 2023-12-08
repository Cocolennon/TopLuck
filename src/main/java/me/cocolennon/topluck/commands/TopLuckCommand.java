package me.cocolennon.topluck.commands;

import me.cocolennon.topluck.util.MenuCreator;
import me.cocolennon.topluck.util.MenuItems;
import me.cocolennon.topluck.util.PlayerHead;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class TopLuckCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) return false;
        if(!sender.hasPermission("topluck.open")) {
            sender.sendMessage("§c[Top Luck] You can't do that!");
            return false;
        }
        List<Inventory> inventories = new LinkedList<>(MenuCreator.getInstance().getPages());
        player.openInventory(inventories.get(0));
        return true;
    }
}
