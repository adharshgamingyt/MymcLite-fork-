package com.adharsh.mymcLite.Events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class LeaveEvent implements Listener {

    private final Plugin plugin;
    public LeaveEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        // Leave Message
        String leaveMessage = plugin.getConfig().getString("leave-message");
        if (leaveMessage != null && !leaveMessage.isBlank()) {
            leaveMessage = PlaceholderAPI.setPlaceholders(player, leaveMessage);
            leaveMessage = ChatColor.translateAlternateColorCodes('&', leaveMessage);
            e.setQuitMessage(leaveMessage);
        }
    }
}
