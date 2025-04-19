package me.supposedly_sam.mymcLite.Events;

import me.supposedly_sam.mymcLite.Commands.Build;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.UUID;

public class BuildEvents implements Listener {

    private Plugin plugin;
    public BuildEvents(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (!isAllowedToBuild(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!isAllowedToBuild(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!isAllowedToBuild(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if (!isAllowedToBuild(p)) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent e){
        Player p = e.getPlayer();
        if (!isAllowedToBuild(p)) e.setCancelled(true);
    }


    private boolean isAllowedToBuild(Player p) {
        if (plugin.getConfig().getBoolean("build.enabled")){
            return true;
        }
        ArrayList<UUID> buildAllowedPlayers = Build.getBuildAllowedPlayers();
        if (!buildAllowedPlayers.contains(p.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "You are not allowed to do that.");
            return false;
        }
        return true;
    }
}
