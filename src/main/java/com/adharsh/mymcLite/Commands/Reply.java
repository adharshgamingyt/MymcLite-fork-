package com.adharsh.mymcLite.Commands;

import com.adharsh.mymcLite.Utils.HelperFunctions;
import com.adharsh.mymcLite.Utils.Responses;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Reply implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            Responses.sendCommandUsage(sender, "/r <message>");
            return true;
        }

        if (Msg.conversation.get(sender) == null) {
            sender.sendMessage(ChatColor.GRAY + "No current conversations");
            return true;
        }

        CommandSender target = Msg.conversation.get(sender);

        if (!HelperFunctions.isPlayerOnline(sender, target.getName())) {
            return true;
        }

        String message = HelperFunctions.buildString(Arrays.copyOfRange(args, 0, args.length));
        Msg.sendPrivateMsg(sender, target, message);

        return true;
    }
}
