package me.supposedly_sam.mymcLite.Utils;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Responses {

    public static void sendNotAuthorized(CommandSender target) {
        String notAuthorizedMessage = ChatColor.translateAlternateColorCodes('&', "&c✘ You are not authorized to perform that.");
        target.sendMessage(notAuthorizedMessage);
    }

    public static void sendCommandUsage(CommandSender target, String command) {
        String commandUsageMessage = ChatColor.translateAlternateColorCodes('&', "&c✘ Usage: " + command);
        target.sendMessage(commandUsageMessage);
    }
}
