package me.supposedly_sam.mymcLite.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelperFunctions {
    public static boolean isPlayerOnline(Player p, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            p.sendMessage(ChatColor.RED + "Player is not online");
            return false;
        }
        return true;
    }
}
