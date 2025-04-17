package me.supposedly_sam.mymcLite.Events;

import me.supposedly_sam.mymcLite.Commands.Build;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

public class BuildEvents implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        ArrayList<Player> buildAllowedPlayers = Build.getBuildAllowedPlayers();
        Player p = e.getPlayer();
        if (!buildAllowedPlayers.contains(p)) {
            p.sendMessage(ChatColor.RED + "You are not allowed to build here.");
//            e.setBuild();
            return;
        }
    }
}
