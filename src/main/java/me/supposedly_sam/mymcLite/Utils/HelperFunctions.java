package me.supposedly_sam.mymcLite.Utils;

import me.supposedly_sam.mymcLite.Events.Chat;
import me.supposedly_sam.mymcLite.Models.BanMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelperFunctions {
    public static boolean isPlayerOnline(CommandSender sender, String targetName) {
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player is not online");
            return false;
        }
        return true;
    }

    public static void batchBroadcast(List<String> messages) {
        for (String msg : messages) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    public static void banBroadcast(String targetName, String durationText, String reasonText) {
        List<String> banBroadcast = new ArrayList<>();
        banBroadcast.add(" ");
        banBroadcast.add("&b" + targetName + " &chas been banned from the server");
        banBroadcast.add(durationText);
        banBroadcast.add(reasonText);
        banBroadcast.add(" ");

        batchBroadcast(banBroadcast);
    }

    public static List<String> getPlayerList(String currentWord) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(currentWord))
                .collect(Collectors.toList());
    }

    public static OfflinePlayer getOfflinePlayerObject(String targetName) {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName().equals(targetName)) {
                return offlinePlayer;
            }
        }
        return null;
    }

    /* Ban/Ban-ip Helper Functions */
    public static String getDurationAlias(long duration) {
        String durationAlias;
        if (duration == 0) {
            durationAlias = "Permanent";
        } else {
            durationAlias = formatDuration(duration);
        }

        return durationAlias;
    }

    public static long parseDuration(String durationStr) {
        if (durationStr.equalsIgnoreCase("p")) {
            return 0;
        }

        char unit = durationStr.charAt(durationStr.length() - 1);
        long duration = Long.parseLong(durationStr.substring(0, durationStr.length() - 1));

        return switch (unit) {
            case 'd' -> duration * 86400;
            case 'h' -> duration * 3600;
            case 'm' -> duration * 60;
            case 's' -> duration;
            default -> throw new IllegalArgumentException("Invalid duration format");
        };
    }

    private static String formatDuration(long seconds) {
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append(" day").append(days > 1 ? "s" : "").append(", ");
        if (hours > 0) sb.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(", ");
        if (minutes > 0) sb.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(", ");
        if (seconds > 0) sb.append(seconds).append(" second").append(seconds > 1 ? "s" : "").append(", ");

        if (sb.length() >= 2) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public static BanMessage getBanReasonFormatted(String[] args, String durationAlias) {
        String reason = "&fBanned by staff";
        if (args.length > 2) {
            StringBuilder builder = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                builder.append(args[i]);
                builder.append(" ");
            }

            reason = builder.toString();
            reason = reason.stripTrailing();
        }

        String durationText = "\n&cDuration: &f" + durationAlias;
        String reasonText = "\n&cFor: &f" + reason;
        String banText = "&c[Ban Hammer] &7You have been banned from the server" + durationText + reasonText;
        banText = ChatColor.translateAlternateColorCodes('&', banText);

        return new BanMessage(banText, durationText, reasonText);
    }

    public static String getKickReasonFormatted(String[] args) {
        String reason = "&fKicked by staff";
        if (args.length > 1) {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                builder.append(args[i]);
                builder.append(" ");
            }

            reason = builder.toString();
            reason = reason.stripTrailing();
        }

        return ChatColor.translateAlternateColorCodes('&', reason);
    }
}
