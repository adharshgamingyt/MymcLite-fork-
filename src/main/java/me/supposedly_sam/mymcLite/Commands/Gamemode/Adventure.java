package me.supposedly_sam.mymcLite.Commands.Gamemode;

import me.supposedly_sam.mymcLite.Utils.HelperFunctions;
import me.supposedly_sam.mymcLite.Utils.Responses;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Adventure implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (!p.hasPermission("mymclite.adventure")) {
                Responses.sendNotAuthorized(p);
                return true;
            }
            if (args.length == 0) {
                changeModeToAdventure(p);
            } else if (args.length == 1) {
                if (!p.hasPermission("mymclite.adventure.others")) {
                    Responses.sendNotAuthorized(p);
                    return true;
                }

                if (!HelperFunctions.isPlayerOnline(p, args[0])) {
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[0]);
                changeModeToAdventure(target);
                p.sendMessage(ChatColor.AQUA + target.getName() + ChatColor.GRAY + "'s Game-mode changed to " + ChatColor.GREEN + "adventure");
            } else {
                Responses.sendCommandUsage(p, "/adventure [player]");
            }
        } else if (sender instanceof ConsoleCommandSender c){
            if (!HelperFunctions.isPlayerOnline(c, args[0])) {
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            changeModeToAdventure(target);
            c.sendMessage(ChatColor.AQUA + target.getName() + ChatColor.GRAY + "'s Game-mode changed to " + ChatColor.GREEN + "adventure");
        }
        return true;
    }

    private void changeModeToAdventure(Player target) {
        target.setGameMode(GameMode.ADVENTURE);
        target.sendMessage(ChatColor.GRAY + "Game-mode changed to " + ChatColor.GREEN + "adventure");
    }
}
