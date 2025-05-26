package me.supposedly_sam.mymcLite.Commands;

import me.supposedly_sam.mymcLite.Utils.Responses;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class MsgToggle implements CommandExecutor {

    public static ArrayList<Player> msgBlockedPlayers = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (args.length > 0) {
                Responses.sendCommandUsage(p, "/msgtoggle");
                return true;
            }

            if (msgBlockedPlayers.contains(p)) {
                msgBlockedPlayers.remove(p);
                p.sendMessage(ChatColor.GREEN + "Msg toggled on. " + ChatColor.GRAY +  "You will now receive messages");
            } else {
                msgBlockedPlayers.add(p);
                p.sendMessage(ChatColor.RED + "Msg toggled off. " + ChatColor.GRAY + "You will not receive any messages");
            }
        } else {
            Responses.sendNonPlayerExecution(sender);
        }
        return true;
    }
}
