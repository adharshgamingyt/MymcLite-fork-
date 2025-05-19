package me.supposedly_sam.mymcLite.Tasks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class AutoAnnouncer {

    private final Plugin plugin;
    private BukkitTask announcerTask;

    public AutoAnnouncer(Plugin plugin) {
        this.plugin = plugin;
   }

    public void start() {
        if (!plugin.getConfig().getBoolean("auto-announcer.enabled")) {
            return;
        }

        int rawFrequency = plugin.getConfig().getInt("auto-announcer.frequency");
        int frequency = rawFrequency == 0 ? 60 : rawFrequency;

        final int[] nextMessage = {0};

        announcerTask = new BukkitRunnable() {
            @Override
            public void run() {
                String rawHeader = plugin.getConfig().getString("auto-announcer.message-header");
                String header = rawHeader == null ? "&7-------------" : rawHeader;

                String rawFooter = plugin.getConfig().getString("auto-announcer.message-footer");
                String footer = rawFooter == null ? "&7-------------" : rawFooter;

                List<String> messages = plugin.getConfig().getStringList("auto-announcer.messages");
                if (messages.isEmpty()) return;

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    List<String> announcement = new ArrayList<>();
                    announcement.addFirst(header);
                    announcement.add(" ");

                    String message = messages.get(nextMessage[0]);
                    String formattedMessage = PlaceholderAPI.setPlaceholders(onlinePlayer, message);
                    announcement.add(formattedMessage);

                    announcement.add(" ");
                    announcement.addLast(footer);

                    for (String line : announcement) {
                        onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
                    }
                }

                nextMessage[0]++;
                if (nextMessage[0] >= messages.size()) {
                    nextMessage[0] = 0;
                }
            }
        }.runTaskTimerAsynchronously(plugin, frequency * 20L, frequency * 20L);
    }

    public void stop() {
        if (announcerTask != null && !announcerTask.isCancelled()) {
            announcerTask.cancel();
        }
    }
}
