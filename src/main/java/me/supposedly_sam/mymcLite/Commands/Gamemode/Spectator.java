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

public class Spectator implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (!p.hasPermission("mymclite.spectator")) {
                Responses.sendNotAuthorized(p);
                return true;
            }
            if (args.length == 0) {
                changeModeToSpectator(p);
            } else if (args.length == 1) {
                if (!p.hasPermission("mymclite.spectator.others")) {
                    Responses.sendNotAuthorized(p);
                    return true;
                }

                if (!HelperFunctions.isPlayerOnline(p, args[0])) {
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[0]);
                changeModeToSpectator(target);
                p.sendMessage(ChatColor.AQUA + target.getName() + ChatColor.GRAY + "'s Game-mode changed to " + ChatColor.GREEN + "spectator");
            } else {
                Responses.sendCommandUsage(p, "/spectator [player]");
            }
        } else if (sender instanceof ConsoleCommandSender c){
            if (!HelperFunctions.isPlayerOnline(c, args[0])) {
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            changeModeToSpectator(target);
            c.sendMessage(ChatColor.AQUA + target.getName() + ChatColor.GRAY + "'s Game-mode changed to " + ChatColor.GREEN + "spectator");
        }
        return true;
    }

    private void changeModeToSpectator(Player target) {
        target.setGameMode(GameMode.SPECTATOR);
        target.sendMessage(ChatColor.GRAY + "Game-mode changed to " + ChatColor.GREEN + "spectator");
    }
}
