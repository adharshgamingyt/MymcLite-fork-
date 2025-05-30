package com.adharsh.mymcLite.Commands;

import com.adharsh.mymcLite.Utils.HelperFunctions;
import com.adharsh.mymcLite.Utils.Inventories;
import com.adharsh.mymcLite.Utils.Responses;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class GameSelector implements CommandExecutor {
    private final Plugin plugin;
    public GameSelector(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!plugin.getConfig().getBoolean("game-selector.enabled")) {
            sender.sendMessage(ChatColor.RED + "This command has been disabled.");
            return true;
        }

        if (!(sender instanceof Player p)) {
            Responses.sendNonPlayerExecution(sender);
            return true;
        }

        if (args.length == 0) {
            showGameSelector(p);
        } else if (args.length == 1) {
            if (!p.hasPermission("mymclite.gselector.others")) {
                Responses.sendNotAuthorized(p);
                return true;
            }

            String targetName = args[0];
            if (!HelperFunctions.isPlayerOnline(p, targetName)) {
                return true;
            }

            Player target = Bukkit.getPlayerExact(targetName);
            showGameSelector(target);
        }
        return true;
    }

    private void showGameSelector(Player player) {
        Inventories inventories = new Inventories(plugin);
        Inventory gameSelectorInventory = inventories.getGameSelectorInventory(player);

        if (gameSelectorInventory == null) {
            player.sendMessage(ChatColor.RED + "Could not display game selector.");
            return;
        }

        player.openInventory(gameSelectorInventory);
    }
}
