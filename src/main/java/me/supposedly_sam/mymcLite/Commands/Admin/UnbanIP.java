package me.supposedly_sam.mymcLite.Commands.Admin;

import me.supposedly_sam.mymcLite.Utils.Responses;
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

public class UnbanIP implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p && !p.hasPermission("mymclite.unban-ip")) {
            Responses.sendNotAuthorized(p);
            return true;
        }

        if (args.length == 1) {
            String targetIP = args[0];

            BanList banList = Bukkit.getBanList(BanList.Type.IP);

            if (!banList.isBanned(targetIP)) {
                sender.sendMessage(ChatColor.RED + "Player is not banned");
                return true;
            }

            banList.pardon(targetIP);
            sender.sendMessage(ChatColor.AQUA + targetIP + ChatColor.GREEN + " has been unbanned");
        } else {
            Responses.sendCommandUsage(sender, "/unbanip <ip>");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            BanList banList = Bukkit.getBanList(BanList.Type.IP );
            Set<BanEntry> banEntries = banList.getBanEntries();

            return banEntries.stream()
                    .map(BanEntry::getBanTarget)
                    .filter(obj -> obj instanceof String)
                    .map(obj -> (String) obj)
                    .filter(ip -> ip.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
