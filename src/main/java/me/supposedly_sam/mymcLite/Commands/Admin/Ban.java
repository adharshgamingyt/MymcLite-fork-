package me.supposedly_sam.mymcLite.Commands.Admin;

import me.supposedly_sam.mymcLite.Models.BanMessage;
import me.supposedly_sam.mymcLite.Utils.HelperFunctions;
import me.supposedly_sam.mymcLite.Utils.Responses;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class Ban implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p && !p.hasPermission("mymclite.ban")) {
            Responses.sendNotAuthorized(p);
            return true;
        }

        if (args.length == 0) {
            Responses.sendCommandUsage(sender, "/ban <player> [duration] [reason]");
            return true;
        }

        String targetName = args[0];
        OfflinePlayer target = HelperFunctions.getOfflinePlayerObject(targetName);
        if (target == null) {
            Responses.sendPlayerNotExists(sender);
            return true;
        }

        banPlayer(sender, target, args);
        return true;
    }

    private void banPlayer(CommandSender sender, OfflinePlayer target, String[] args) {
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

        BanList banList = Bukkit.getBanList(BanList.Type.PROFILE);
        banList.addBan(target.getPlayerProfile(), banMessage.banText, expirationDate, source);
        if (target.isOnline()) {
            Player onlinePlayer = target.getPlayer();
            if (onlinePlayer != null) {
                onlinePlayer.kickPlayer(banMessage.banText);
            }
        }

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
