package me.cocolennon.topluck.listeners;

import me.cocolennon.topluck.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoined implements Listener {
    private final String topluckColor = Main.getInstance().getConfig().getString("plugin-name-color");
    private final String successColor = Main.getInstance().getConfig().getString("messages-success-color");

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        Main.getInstance().checkVersion();
        Player player = event.getPlayer();
        if(!player.isOp()) return;
        if(Main.getInstance().getUsingOldVersion()) {
            player.sendMessage(topluckColor + "[Top Luck]" + successColor + "You are using an older version of Top Luck, please update to version " + Main.getInstance().getLatestVersion());
        }
    }
}
