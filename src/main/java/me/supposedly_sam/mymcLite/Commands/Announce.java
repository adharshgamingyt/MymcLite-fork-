package me.supposedly_sam.mymcLite.Commands;

import me.supposedly_sam.mymcLite.Events.Chat;
import me.supposedly_sam.mymcLite.Utils.HelperFunctions;
import me.supposedly_sam.mymcLite.Utils.Responses;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Announce implements CommandExecutor {
    private final Plugin plugin;
    public Announce(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p && !p.hasPermission("mymclite.announce")) {
            Responses.sendNotAuthorized(p);
            return true;
        }

        if (args.length < 1) {
            Responses.sendCommandUsage(sender, "/announce <message>");
        }

        String message = HelperFunctions.buildString(args);

        List<String> announceMessage = new ArrayList<>();
        announceMessage.add(" ");
        announceMessage.add(plugin.getConfig().getString("announce.header"));
        announceMessage.add(" ");
        announceMessage.add("&e" + message);
        announceMessage.add(" ");

        HelperFunctions.batchBroadcast(announceMessage);

        String title = plugin.getConfig().getString("announce.title");
        if (title == null || title.isBlank()) {
            title = " ";
        }
        String subtitle = plugin.getConfig().getString("announce.subtitle");
        if (subtitle == null || subtitle.isBlank()) {
            subtitle = " ";
        }

        title = ChatColor.translateAlternateColorCodes('&', title);
        subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
        int stay = plugin.getConfig().getInt("announce.stay");
        if (stay < 1) {
            stay = 5;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendTitle(title, subtitle, 10, stay * 20, 20);
        }

        return true;
    }
}
