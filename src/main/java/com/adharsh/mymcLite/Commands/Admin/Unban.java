package com.adharsh.mymcLite.Commands.Admin;

import com.adharsh.mymcLite.Utils.HelperFunctions;
import com.adharsh.mymcLite.Utils.Responses;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Unban implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p && !p.hasPermission("mymclite.unban")) {
            Responses.sendNotAuthorized(p);
            return true;
        }

        if (args.length == 1) {
            String targetName = args[0];
            OfflinePlayer target = HelperFunctions.getOfflinePlayerObject(targetName);
            if (target == null) {
                Responses.sendPlayerNotExists(sender);
                return true;
            }

            BanList banList = Bukkit.getBanList(BanList.Type.PROFILE);

            if (!banList.isBanned(target.getPlayerProfile())) {
                sender.sendMessage(ChatColor.RED + "Player is not banned");
                return true;
            }

            banList.pardon(target.getPlayerProfile());
            sender.sendMessage(ChatColor.AQUA + target.getName() + ChatColor.GREEN + " has been unbanned");
        } else {
            Responses.sendCommandUsage(sender, "/unban <player>");
        }

        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            BanList banList = Bukkit.getBanList(BanList.Type.PROFILE);
            Set<BanEntry> banEntries = banList.getBanEntries();

            return banEntries.stream()
                    .map(entry -> (PlayerProfile) entry.getBanTarget())
                    .map(PlayerProfile::getName)
                    .filter(name -> name != null && name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
