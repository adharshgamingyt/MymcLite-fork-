package com.adharsh.mymcLite.Commands;

import com.adharsh.mymcLite.Utils.HelperFunctions;
import com.adharsh.mymcLite.Utils.Responses;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

public class Msg implements CommandExecutor {

    private static Plugin plugin;
    public Msg(Plugin plugin) {
        Msg.plugin = plugin;
    }

    public static HashMap<CommandSender, CommandSender> conversation = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) {
            if (!HelperFunctions.isPlayerOnline(sender, args[0])) {
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            String message = HelperFunctions.buildString(Arrays.copyOfRange(args, 1, args.length));
            sendPrivateMsg(sender, target, message);

        } else {
            Responses.sendCommandUsage(sender, "/msg <target> <message>");
        }

        return true;
    }

    public static void sendPrivateMsg(CommandSender sender, CommandSender target, String message) {

        if (target instanceof Player targetPlayer) {
            if (MsgToggle.msgBlockedPlayers.contains(targetPlayer)) {
                sender.sendMessage(ChatColor.RED + "The receiver has turned their messages off");
                return;
            }
        }

        if (sender.hasPermission("mymclite.chat.colours")) {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }

        String senderFormat = plugin.getConfig().getString("msg.sender-format");
        if (senderFormat == null || senderFormat.isBlank()) {
            senderFormat = "&f[You] &b-> &f%receiver%: &7%message%";
        }
        senderFormat = HelperFunctions.parseMsgPlaceholders(senderFormat, sender, target, message);

        String receiverFormat = plugin.getConfig().getString("msg.receiver-format");
        if (receiverFormat == null || receiverFormat.isBlank()) {
            receiverFormat = "&f%sender% &b-> &f[You]: &7%message%";
        }
        receiverFormat = HelperFunctions.parseMsgPlaceholders(receiverFormat, sender, target, message);

        conversation.put(sender, target);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', senderFormat));

        conversation.put(target, sender);
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', receiverFormat));
        if (target instanceof Player targetPlayer) {
            targetPlayer.playSound(targetPlayer, Sound.BLOCK_NOTE_BLOCK_PLING, 40, 2);
        }
    }

}
