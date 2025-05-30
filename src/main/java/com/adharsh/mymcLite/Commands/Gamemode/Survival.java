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

public class Survival implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (!p.hasPermission("mymclite.survival")) {
                Responses.sendNotAuthorized(p);
                return true;
            }
            if (args.length == 0) {
                changeModeToSurvival(p);
            } else if (args.length == 1) {
                if (!p.hasPermission("mymclite.survival.others")) {
                    Responses.sendNotAuthorized(p);
                    return true;
                }

                if (!HelperFunctions.isPlayerOnline(p, args[0])) {
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[0]);
                changeModeToSurvival(target);
                p.sendMessage(ChatColor.AQUA + target.getName() + ChatColor.GRAY + "'s Game-mode changed to " + ChatColor.GREEN + "survival");
            } else {
                Responses.sendCommandUsage(p, "/survival [player]");
            }
        } else if (sender instanceof ConsoleCommandSender c){
            if (!HelperFunctions.isPlayerOnline(c, args[0])) {
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            changeModeToSurvival(target);
            c.sendMessage(ChatColor.AQUA + target.getName() + ChatColor.GRAY + "'s Game-mode changed to " + ChatColor.GREEN + "survival");
        }
        return true;
    }

    private void changeModeToSurvival(Player target) {
        target.setGameMode(GameMode.SURVIVAL);
        target.sendMessage(ChatColor.GRAY + "Game-mode changed to " + ChatColor.GREEN + "survival");
    }
}
