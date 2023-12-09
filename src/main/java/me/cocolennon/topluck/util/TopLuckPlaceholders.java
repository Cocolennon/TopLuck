package me.cocolennon.topluck.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.cocolennon.topluck.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TopLuckPlaceholders extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "topluck";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Coco Lennon";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.1";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        FileConfiguration config = Main.getInstance().getConfig();
        FileConfiguration playerData = PlayerData.getInstance().getPlayerData(player);

        if(params.equalsIgnoreCase("total_blocks_mined")) {
            return String.valueOf(playerData.getInt("totalBlocksMined"));
        }

        if(params.equalsIgnoreCase("total_ore_mined")) {
            int totalOreMined = 0;
            for(String block : config.getStringList("blocks-to-check")) {
                int data = playerData.getInt(block.toLowerCase());
                totalOreMined += data;
            }
            return String.valueOf(totalOreMined);
        }

        if(params.equalsIgnoreCase("total_ore_mined_percentage")) {
            int totalOreMined = 0;
            int totalBlocksMined = playerData.getInt("totalBlocksMined");
            for(String block : config.getStringList("blocks-to-check")) {
                int data = playerData.getInt(block.toLowerCase());
                totalOreMined += data;
            }
            double unrounded = (double) (totalOreMined*100)/totalBlocksMined;
            double rounded = Math.round(unrounded*1000)/1000D;
            return rounded + "%";
        }

        if(config.getStringList("blocks-to-check").contains(params.toUpperCase())) {
            return String.valueOf(playerData.getInt(params.toLowerCase()));
        }

        for(String block : config.getStringList("blocks-to-check")) {
            if(params.equalsIgnoreCase(block + "_percentage")) {
                int data = playerData.getInt(params.replace("_percentage", "").toLowerCase());
                int total = playerData.getInt("totalBlocksMined");
                double unrounded = (double) (data*100)/total;
                double rounded = Math.round(unrounded*1000)/1000D;
                return rounded + "%";
            }
        }

        return null;
    }
}
