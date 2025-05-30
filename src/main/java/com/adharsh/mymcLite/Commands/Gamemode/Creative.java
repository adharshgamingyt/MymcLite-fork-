package com.adharsh.mymcLite.Commands.Gamemode;

import com.adharsh.mymcLite.Utils.HelperFunctions;
import com.adharsh.mymcLite.Utils.Responses;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Creative implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (!p.hasPermission("mymclite.creative")) {
                Responses.sendNotAuthorized(p);
                return true;
            }
            if (args.length == 0) {
                changeModeToCreative(p);
            } else if (args.length == 1) {
                if (!p.hasPermission("mymclite.creative.others")) {
                    Responses.sendNotAuthorized(p);
                    return true;
                }

                if (!HelperFunctions.isPlayerOnline(p, args[0])) {
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[0]);
                changeModeToCreative(target);
                p.sendMessage(ChatColor.AQUA + target.getName() + ChatColor.GRAY + "'s Game-mode changed to " + ChatColor.GREEN + "creative");
            } else {
                Responses.sendCommandUsage(p, "/creative [player]");
            }
        } else if (sender instanceof ConsoleCommandSender c){
            if (!HelperFunctions.isPlayerOnline(c, args[0])) {
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            changeModeToCreative(target);
            c.sendMessage(ChatColor.AQUA + target.getName() + ChatColor.GRAY + "'s Game-mode changed to " + ChatColor.GREEN + "creative");
        }
        return true;
    }

    private void changeModeToCreative(Player target) {
        target.setGameMode(GameMode.CREATIVE);
        target.sendMessage(ChatColor.GRAY + "Game-mode changed to " + ChatColor.GREEN + "creative");
    }
}
