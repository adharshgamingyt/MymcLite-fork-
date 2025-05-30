package com.adharsh.mymcLite.Commands.Admin;

import com.adharsh.mymcLite.Utils.HelperFunctions;
import com.adharsh.mymcLite.Utils.Responses;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Kick implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p && !p.hasPermission("mymclite.kick")) {
            Responses.sendNotAuthorized(p);
            return true;
        }

        if (args.length >= 1) {
            String targetName = args[0];
            String reason = HelperFunctions.getKickReasonFormatted(args);

            if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*")) {
                for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (!onlinePlayer.hasPermission("mymclite.kick.bypass-kickall")){
                        onlinePlayer.kickPlayer(reason);
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "Kicked all players from the server");
                return true;
            }

            if (!HelperFunctions.isPlayerOnline(sender, targetName)){
                return true;
            }

            Player target = Bukkit.getPlayerExact(targetName);
            target.kickPlayer(reason);

            List<String> kickBroadcast = new ArrayList<>();
            kickBroadcast.add(" ");
            kickBroadcast.add("&b" + target.getName() + " &chas been kicked from the server");
            kickBroadcast.add("&cReason: &f" + reason);
            kickBroadcast.add(" ");

            HelperFunctions.batchBroadcast(kickBroadcast);

        } else {
            Responses.sendCommandUsage(sender, "/kick <player> [reason]");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            String current = args[0].toLowerCase();
            List<String> playerList = HelperFunctions.getPlayerList(current);
            playerList.addFirst("*");
            playerList.addFirst("all");
            return playerList;
        } else if (args.length == 2) {
            return List.of("[reason]");
        }
        return List.of();
    }
}
