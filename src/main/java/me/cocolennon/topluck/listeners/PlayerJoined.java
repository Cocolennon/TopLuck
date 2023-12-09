package me.cocolennon.topluck.listeners;

import me.cocolennon.topluck.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoined implements Listener {
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!player.isOp()) return;
        if(Main.getInstance().getUsingOldVersion()) {
            player.sendMessage("§a[Top Luck] §dYou are using an older version of Top Luck, please update to version " + Main.getInstance().getLatestVersion());
        }
    }
}
