package com.adharsh.mymcLite.Commands;

import com.adharsh.mymcLite.Utils.Responses;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Reload implements CommandExecutor {

    private final Plugin plugin;
    public Reload(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player p && !p.hasPermission("mymclite.reload")) {
            Responses.sendNotAuthorized(p);
            return true;
        }

        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Reloaded config.yml");
        return true;
    }
}
