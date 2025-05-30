package com.adharsh.mymcLite.Events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat implements Listener {

    private final Plugin plugin;
    public Chat(Plugin plugin) {
        this.plugin = plugin;
    }

    private HashMap<Player, Long> chatCooldown = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (!plugin.getConfig().getBoolean("chat.enabled")) {
            return;
        }

        Player p = e.getPlayer();
        String message = e.getMessage();

        int cooldown = plugin.getConfig().getInt("chat.chat-cooldown");
        if (cooldown > 1) {
            if (chatCooldown.containsKey(p) && !p.hasPermission("mymclite.chat.bypass-cooldown")) {
                long secondsLeft = (((chatCooldown.get(p)) / 1000) + cooldown) - (System.currentTimeMillis() / 1000);
                if(secondsLeft > 0){

                    e.setCancelled(true);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You must wait &c" + secondsLeft + " second(s) &7before sending another message"));
                    return;

                }
            }
        }

        chatCooldown.put(p, System.currentTimeMillis());

        // URL checker
        Pattern urlPattern = Pattern.compile("((http|https)://\\S+)");
        Matcher urlMatcher = urlPattern.matcher(message);

        if(urlMatcher.find() && !p.hasPermission("mymclite.chat.links")){
            e.setCancelled(true);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You &ccannot &7send links in global chat."));
            return;
        }

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

        chatFormat = ChatColor.translateAlternateColorCodes('&', chatFormat);

        chatFormat = PlaceholderAPI.setPlaceholders(p, chatFormat)
                .replace("%author%", p.getName())
                .replace("%message%", message);

        try {
            e.setFormat(chatFormat);
        } catch (IllegalFormatException | NullPointerException ex) {
            plugin.getLogger().warning("Chat format error, reverting to default format. " + e.getMessage());
            e.setFormat(p.getName() + ": " + message);
        }
    }
}
