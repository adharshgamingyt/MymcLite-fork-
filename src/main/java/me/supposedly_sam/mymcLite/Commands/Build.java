package me.supposedly_sam.mymcLite.Commands;

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
import java.util.UUID;

public class Build implements CommandExecutor {

    private static ArrayList<UUID> buildAllowedPlayers = new ArrayList<>();

    public static ArrayList<UUID> getBuildAllowedPlayers() {
        return buildAllowedPlayers;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (!p.hasPermission("mymclite.build")) {
                Responses.sendNotAuthorized(p);
                return true;
            }

            buildMethod(p, args);
        } else {
            if (args.length != 1) {
                Responses.sendCommandUsage(sender, "/build <player>");
                return true;
            }

            buildMethod(sender, args);
        }
        return true;
    }

    private void buildMethod(CommandSender sender, String[] args) {
        if (args.length > 1) {
            Responses.sendCommandUsage(sender, "/build [player]");
            return;
        }

        if (args.length == 0) {
            Player p = (Player) sender;
            if (!buildAllowedPlayers.contains(p.getUniqueId())) {
                buildAllowedPlayers.add(p.getUniqueId());
                p.sendMessage(ChatColor.GREEN + "Build mode enabled!");
            } else {
                buildAllowedPlayers.remove(p.getUniqueId());
                p.sendMessage(ChatColor.RED + "Build mode disabled!");
            }
        } else {
            String targetName = args[0];
            if (!HelperFunctions.isPlayerOnline(sender, targetName)) {
                return;
            }

            Player target = Bukkit.getPlayerExact(targetName);
            if (!buildAllowedPlayers.contains(target.getUniqueId())) {
                buildAllowedPlayers.add(target.getUniqueId());
                target.sendMessage(ChatColor.GREEN + "Build mode enabled by a staff");
            } else {
                buildAllowedPlayers.remove(target.getUniqueId());
                target.sendMessage(ChatColor.RED + "Build mode disabled by a staff");
            }
        }
    }
}
