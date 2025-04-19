package me.supposedly_sam.mymcLite.Events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.IllegalFormatException;

public class Chat implements Listener {

    private final Plugin plugin;
    public Chat(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (!plugin.getConfig().getBoolean("chat.enabled")) {
            return;
        }

        Player p = e.getPlayer();
        String message = e.getMessage();

        // Replacing '%' with '/'
        if (message.contains("%")) {
            message = message.replace("%", "/");
            p.sendMessage(ChatColor.GRAY + "• '%' is an invalid character, and has been replaced with '/'");
        }

        // Allowing color codes for permed players
        if (p.hasPermission("mymclite.chat.colours")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        String chatFormat = plugin.getConfig().getString("chat.chat-format");
        if (chatFormat == null || chatFormat.isEmpty()) {
            plugin.getLogger().warning("Config.yml | 'chat.chat-format' is missing or empty!");
            e.setFormat(p.getName() + ": " + message);
            return;
        }

        chatFormat = PlaceholderAPI.setPlaceholders(p, chatFormat)
                .replace("%author%", p.getName())
                .replace("%message%", message);

        try {
            e.setFormat(ChatColor.translateAlternateColorCodes('&', chatFormat));
        } catch (IllegalFormatException | NullPointerException ex) {
            plugin.getLogger().warning("Chat format error, reverting to default format. " + e.getMessage());
            e.setFormat(p.getName() + ": " + message);
        }
    }
}
