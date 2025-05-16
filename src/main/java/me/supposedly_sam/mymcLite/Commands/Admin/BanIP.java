package me.supposedly_sam.mymcLite.Commands.Admin;

import me.supposedly_sam.mymcLite.Models.BanMessage;
import me.supposedly_sam.mymcLite.Utils.HelperFunctions;
import me.supposedly_sam.mymcLite.Utils.Responses;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BanIP implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p && !p.hasPermission("mymclite.ban-ip")) {
            Responses.sendNotAuthorized(p);
            return true;
        }

        if (args.length == 0) {
            Responses.sendCommandUsage(sender, "/ban-ip <player> [duration] [reason]");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Offline players cannot be IP-Banned.");
            return true;
        }

        if (target.getAddress() == null) {
            sender.sendMessage(ChatColor.RED + "Could not retrieve IP address of the player.");
            return true;
        }

        banPlayerIP(sender, target, args);
        return true;
    }

    private void banPlayerIP(CommandSender sender, Player target, String[] args) {
        long duration;
        if (args.length <= 1) {
            duration = 0;
        } else {
            try{
                duration = HelperFunctions.parseDuration(args[1]);
            }catch (IllegalArgumentException e){
                sender.sendMessage(ChatColor.RED + "Invalid duration format! Use 'p', 'd', 'h', 'm', 's' for permanent, days, hours, minutes and seconds");
                return;
            }

        }

        String durationAlias = HelperFunctions.getDurationAlias(duration);
        BanMessage banMessage = HelperFunctions.getBanReasonFormatted(args, durationAlias);
        Instant expirationDate = duration == 0 ? null : new Date(System.currentTimeMillis() + (duration * 1000)).toInstant();
        String source = sender.getName().isEmpty() ? "Console" : sender.getName();

        BanList<InetAddress> banList = Bukkit.getBanList(BanList.Type.IP);
        banList.addBan(target.getAddress().getAddress(), banMessage.banText, expirationDate, source);
        target.kickPlayer(banMessage.banText);

        HelperFunctions.banBroadcast(target.getName(), banMessage.durationText, banMessage.reasonText);
    }



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            String current = args[0].toLowerCase();
            return HelperFunctions.getPlayerList(current);
        } else if (args.length == 2) {
            return List.of("p", "d", "h", "m", "s");
        } else if (args.length == 3) {
            return List.of("[reason]");
        }
        return List.of();
    }
}
