package me.cocolennon.topluck.listeners;

import me.cocolennon.topluck.Main;
import me.cocolennon.topluck.util.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.function.Supplier;

public class PlayerBrokeBlock implements Listener {
    private final Main mainInstance = Main.getInstance();

    @EventHandler
    public void onBlockBroken(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(block.hasMetadata("player-placed") && block.getMetadata("player-placed").get(0).asBoolean()) return;
        FileConfiguration config = mainInstance.getConfig();

        List<String> blocksToCheck = config.getStringList("blocks-to-check");

        if(blocksToCheck.contains(block.getType().name())) {
            PlayerData.getInstance().addPlayerData(player, block.getType().name());
        }else{PlayerData.getInstance().addPlayerData(player, null);}
    }
}
