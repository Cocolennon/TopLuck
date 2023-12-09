package me.cocolennon.topluck.listeners;

import me.cocolennon.topluck.Main;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class PlayerPlaceBlock implements Listener {
    @EventHandler
    public void BlockPlaced(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockState blockState = block.getState();
        blockState.setMetadata("player-placed", new FixedMetadataValue(Main.getInstance(), true));
        //block.setMetadata("player-placed", new FixedMetadataValue(Main.getInstance(), true));
    }
}
