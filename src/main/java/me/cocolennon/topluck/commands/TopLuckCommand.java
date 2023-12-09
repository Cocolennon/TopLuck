package me.cocolennon.topluck.commands;

import me.cocolennon.topluck.Main;
import me.cocolennon.topluck.util.MenuCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TopLuckCommand implements TabExecutor {
    private static final List<String> autoComplete = Arrays.asList("info", "menu", "reload");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;

        if(args.length == 0) return openMenu(sender);
        return switch (args[0]) {
            case "menu" -> openMenu(sender);
            case "info" -> sendInfo(sender);
            case "reload" -> reloadConfig(sender);
            default -> openMenu(sender);
        };
    }

    @Nullable @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) return null;
        if(args.length == 1) return autoComplete;
        return null;
    }

    private boolean openMenu(CommandSender sender) {
        if(!sender.hasPermission("topluck.open")) {
            sender.sendMessage("§a[Top Luck] §cYou can't do that!");
            return false;
        }
        List<Inventory> inventories = new LinkedList<>(MenuCreator.getInstance().getPages());
        Player player = (Player) sender;
        player.openInventory(inventories.get(0));
        return true;
    }

    private boolean sendInfo(CommandSender sender) {
        if (!sender.hasPermission("topluck.info")) {
            sender.sendMessage("§a[Top Luck] §cYou can't do that!");
            return false;
        }
        List<String> info = new LinkedList<>();
        info.add("§c§l=========================");
        info.add("§a§lTop Luck " + Main.getInstance().getVersion());
        if(Main.getInstance().getUsingOldVersion()){
            info.add("§aAn update is available!");
        }else{
            info.add("§aYou're using the latest version");
        }
        info.add("§aMade with §c❤ §aby §d§lCoco Lennon");
        info.add("§c§l=========================");

        info.forEach(sender::sendMessage);
        return true;
    }

    private boolean reloadConfig(CommandSender sender) {
        if(!sender.hasPermission("topluck.reload")) {
            sender.sendMessage("§a[Top Luck] §cYou can't do that!");
            return false;
        }

        Main.getInstance().reloadConfig();
        sender.sendMessage("§a[Top Luck] §dConfiguration reloaded!");
        return true;
    }
}
